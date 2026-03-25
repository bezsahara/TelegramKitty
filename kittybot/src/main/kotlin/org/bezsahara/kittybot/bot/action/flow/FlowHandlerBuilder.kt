package org.bezsahara.kittybot.bot.action.flow

import org.bezsahara.kittybot.bot.dispatchers.*
import org.bezsahara.kittybot.bot.updates.HandlerContext

class FlowManager(
    private val id: Int,
    private val flowIdentityStorage: FlowIdentityStorage,
    private val fidAttribute: AttrKey<FlowIdentityData>,
) {
    fun nextSection(handlerContext: HandlerContext, args: Any? = null) {
        val identity = handlerContext[fidAttribute]?.identity ?: error("Could not find identity of the user!")

        flowIdentityStorage[identity] = FlowPayload(id + 1, args)
    }

    fun pauseSection(handlerContext: HandlerContext, args: Any? = null) {
        val identity = handlerContext[fidAttribute]?.identity ?: error("Could not find identity of the user!")

        flowIdentityStorage[identity] = FlowPayload(id, args)
    }

    fun resetFlow(handlerContext: HandlerContext) {
        val identity = handlerContext[fidAttribute]?.identity ?: error("Could not find identity of the user!")
        flowIdentityStorage[identity] = FlowPayload(0, null)
    }

    fun getFlowData(handlerContext: HandlerContext): FlowIdentityData {
        return handlerContext[fidAttribute] ?: error("Could not find identity of the user!")
    }
}

inline fun HandlerStore.flowHandler(
    flowIdentityFinder: FlowIdentityFinder,
    flowIdentityStorage: FlowIdentityStorage = FlowIdentityStorageInMem(),
    builder: FlowHandlerBuilder.() -> Unit
) {
    val fhb = FlowHandlerBuilder(this, flowIdentityStorage, flowIdentityFinder)
    fhb.builder()
    fhb.build()
}

class FlowHandlerBuilder(
    val original: HandlerStore,
    val flowIdentityStorage: FlowIdentityStorage,
    internal val flowIdentityFinder: FlowIdentityFinder,
) {
    val fidAttribute = original.felineDispatcher.identityScope.attrKeyOf<FlowIdentityData>()

    @PublishedApi
    internal val sections = arrayListOf<FlowSectionStore>()

    inline fun section(block: FlowSectionStore.() -> Unit) {
        val fss = FlowSectionStore(sections.size, flowIdentityStorage, fidAttribute, original)
        fss.block()
        sections.add(fss)
    }

    fun build() {
        if (sections.isEmpty()) return

        sections[0].handlersStore.firstOrNull()
            ?: error("Section in a flow must contain at least one handler!")

        val sectionHeaders = Array(sections.size - 1) {
            (sections[it + 1].handlersStore.firstOrNull()
                ?: error("Section in a flow must contain at least one handler!")).identity!!
        }

        val exitHandlerIdentity = sections.last().handlersStore.last().identity!!

        val firstHandler = FlowHandlerBegin(
            sectionHeaders,
            flowIdentityFinder,
            flowIdentityStorage,
            exitHandlerIdentity,
            fidAttribute
        )

        original.addHandler(firstHandler)

        sections.forEachIndexed { index, store ->
            store.handlersStore.forEach { handler ->
                original.addHandler(handler)
            }
            store.handlersStore.clear()
            store.handlersStore.trimToSize()
            if (index != sections.lastIndex) {
                original.addHandler(FlowHandlerSectionEnd(exitHandlerIdentity))
            }
        }
    }
}


class FlowSectionStore(id: Int, flowIdentityStorage: FlowIdentityStorage, fidAttribute: AttrKey<FlowIdentityData>,
    val original: HandlerStore) :
    HandlerStore {
    val flowManager = FlowManager(id, flowIdentityStorage, fidAttribute)
    internal val handlersStore = arrayListOf<Handler>()

    fun HandlerContext.nextSection(args: Any? = null) {
        flowManager.nextSection(this, args)
    }

    fun HandlerContext.pauseSection(args: Any? = null) {
        flowManager.pauseSection(this, args)
    }

    fun HandlerContext.resetFlow() {
        flowManager.resetFlow(this)
    }

    fun HandlerContext.getFlowArgs(): Any? {
        return flowManager.getFlowData(this).args
    }

    fun HandlerContext.getFlowIdentityData(): FlowIdentityData {
        return flowManager.getFlowData(this)
    }

    override fun addHandler(handler: Handler) {
        handlersStore.add(handler.ensureHasIdentity())
    }

    override val felineDispatcher: FelineDispatcher
        get() = original.felineDispatcher
}
