package org.bezsahara.kittybot.bot.action.flow

import org.bezsahara.kittybot.bot.action.route.RoutingStrategyInt
import org.bezsahara.kittybot.bot.dispatchers.*
import org.bezsahara.kittybot.bot.updates.HandlerContext
import org.bezsahara.kittybot.telegram.classes.core.update.UpdateKind

inline fun <T> TransparentHandlerStore.flowHandler(
    flowIdentityFinder: FlowIdentityFinder,
    updateKinds: Set<UpdateKind<*>>? = null,
    flowIdentityStorage: FlowIdentityStorage<T> = FlowIdentityStorageInMem(),
    builder: FlowHandlerBuilder<T>.() -> Unit
) {
    val fhb = FlowHandlerBuilder(this, flowIdentityStorage, flowIdentityFinder, updateKinds)
    fhb.builder()
    fhb.build()
}

class FlowManager<T>(
    private val id: Int,
    private val flowIdentityStorage: FlowIdentityStorage<T>,
    private val fidAttribute: AttrKey<FlowIdentityData<T>>,
    private val nameToId: Map<String, Int>,
) {
    fun nextSection(handlerContext: HandlerContext, args: T? = null) {
        val identity = handlerContext[fidAttribute]?.identity ?: error("Could not find identity of the user!")
        flowIdentityStorage[identity] = FlowPayload(id + 1, args)
    }

    fun nextSectionWithName(handlerContext: HandlerContext, name: String, args: T? = null) {
        val identity = handlerContext[fidAttribute]?.identity ?: error("Could not find identity of the user!")
        val targetId = nameToId[name] ?: error("Could not find flow section named `$name`!")
        flowIdentityStorage[identity] = FlowPayload(targetId, args)
    }

    fun nextSection(handlerContext: HandlerContext, id: Int, args: T? = null) {
        val identity = handlerContext[fidAttribute]?.identity ?: error("Could not find identity of the user!")
        flowIdentityStorage[identity] = FlowPayload(id, args)
    }

    fun pauseSection(handlerContext: HandlerContext, args: T? = null) {
        val identity = handlerContext[fidAttribute]?.identity ?: error("Could not find identity of the user!")
        flowIdentityStorage[identity] = FlowPayload(id, args)
    }

    fun resetFlow(handlerContext: HandlerContext) {
        val identity = handlerContext[fidAttribute]?.identity ?: error("Could not find identity of the user!")
        flowIdentityStorage[identity] = FlowPayload(0, null)
    }

    fun getFlowData(handlerContext: HandlerContext): FlowIdentityData<T> {
        return handlerContext[fidAttribute] ?: error("Could not find identity of the user!")
    }
}

class FlowHandlerBuilder<T>(
    private val original: HandlerStore,
    val flowIdentityStorage: FlowIdentityStorage<T>,
    private val flowIdentityFinder: FlowIdentityFinder,
    private val ofKinds: Set<UpdateKind<*>>?
) {
    val fidAttribute = original.felineDispatcher.identityScope.attrKeyOf<FlowIdentityData<T>>()

    @PublishedApi
    internal val sections = arrayListOf<FlowSectionStore<T>>()

    @PublishedApi
    internal val nameToId = HashMap<String, Int>()

    inline fun section(block: FlowSectionStore<T>.() -> Unit) {
        addSection(null, block)
    }

    inline fun section(name: String, block: FlowSectionStore<T>.() -> Unit) {
        addSection(name, block)
    }

    fun createSectionStore(name: String?) = FlowSectionStore(sections.size, name, flowIdentityStorage, fidAttribute, nameToId, original)

    inline fun addSection(name: String?, block: FlowSectionStore<T>.() -> Unit) {
        val fss = createSectionStore(name)
        fss.block()
        sections.add(fss)
    }

    fun build() {
        if (sections.isEmpty()) return

        sections.forEachIndexed { index, section ->
            section.handlersStore.firstOrNull()
                ?: error("Section in a flow must contain at least one handler!")

            val name = section.name ?: return@forEachIndexed
            if (name.isBlank()) {
                error("Flow section name cannot be blank!")
            }
            val previous = nameToId.put(name, index)
            if (previous != null) {
                error("Duplicate flow section name detected: `$name`")
            }
        }

        val routingStrategy = RoutingStrategyInt(
            FlowRoutingKeyGenerator(flowIdentityFinder, flowIdentityStorage, fidAttribute, sections.size),
            original,
            ofKinds
        )

        sections.forEachIndexed { index, store ->
            routingStrategy.section(index) {
                store.handlersStore.forEach { addHandler(it) }
            }
            store.handlersStore.clear()
            store.handlersStore.trimToSize()
        }

        routingStrategy.build()
    }
}

class FlowSectionStore<T>(
    id: Int,
    internal val name: String?,
    flowIdentityStorage: FlowIdentityStorage<T>,
    fidAttribute: AttrKey<FlowIdentityData<T>>,
    nameToId: Map<String, Int>,
    val original: HandlerStore,
) : HandlerStore, FlowSupport<T>(
    id,
    flowIdentityStorage,
    fidAttribute,
    nameToId
) {

    internal val handlersStore = arrayListOf<Handler>()

    override fun addHandler(handler: Handler) {
        handlersStore.add(handler.ensureHasIdentity())
    }

    override val felineDispatcher: FelineDispatcher
        get() = original.felineDispatcher
}


open class FlowSupport<T>(
    id: Int,
    flowIdentityStorage: FlowIdentityStorage<T>,
    fidAttribute: AttrKey<FlowIdentityData<T>>,
    nameToId: Map<String, Int>,
) {
    val flowManager = FlowManager(id, flowIdentityStorage, fidAttribute, nameToId)

    fun HandlerContext.nextSection(args: T? = null) {
        flowManager.nextSection(this, args)
    }

    fun HandlerContext.nextSectionWithName(name: String, args: T? = null) {
        flowManager.nextSectionWithName(this, name, args)
    }

    // Sections are numbered from 0 to N. So first section has id of zero
    fun HandlerContext.nextSectionWithId(id: Int, args: T? = null) {
        flowManager.nextSection(this, id, args)
    }

    fun HandlerContext.pauseSection(args: T? = null) {
        flowManager.pauseSection(this, args)
    }

    fun HandlerContext.resetFlow() {
        flowManager.resetFlow(this)
    }

    fun HandlerContext.getFlowArgs(): Any? {
        return flowManager.getFlowData(this).args
    }

    fun HandlerContext.getFlowIdentityData(): FlowIdentityData<T> {
        return flowManager.getFlowData(this)
    }
}