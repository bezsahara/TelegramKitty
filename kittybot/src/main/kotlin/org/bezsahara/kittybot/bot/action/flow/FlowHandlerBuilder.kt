package org.bezsahara.kittybot.bot.action.flow

import org.bezsahara.kittybot.bot.action.route.RoutingStrategyInt
import org.bezsahara.kittybot.bot.dispatchers.*
import org.bezsahara.kittybot.bot.updates.HandlerContext
import org.bezsahara.kittybot.telegram.classes.core.update.UpdateKind

inline fun TransparentHandlerStore.flowHandler(
    flowIdentityFinder: FlowIdentityFinder,
    updateKinds: Set<UpdateKind<*>>? = null,
    flowIdentityStorage: FlowIdentityStorage = FlowIdentityStorageInMem(),
    builder: FlowHandlerBuilder.() -> Unit
) {
    val fhb = FlowHandlerBuilder(this, flowIdentityStorage, flowIdentityFinder, updateKinds)
    fhb.builder()
    fhb.build()
}

class FlowManager(
    private val id: Int,
    private val flowIdentityStorage: FlowIdentityStorage,
    private val fidAttribute: AttrKey<FlowIdentityData>,
    private val nameToId: Map<String, Int>,
) {
    fun nextSection(handlerContext: HandlerContext, args: Any? = null) {
        val identity = handlerContext[fidAttribute]?.identity ?: error("Could not find identity of the user!")
        flowIdentityStorage[identity] = FlowPayload(id + 1, args)
    }

    fun nextSection(handlerContext: HandlerContext, name: String, args: Any? = null) {
        val identity = handlerContext[fidAttribute]?.identity ?: error("Could not find identity of the user!")
        val targetId = nameToId[name] ?: error("Could not find flow section named `$name`!")
        flowIdentityStorage[identity] = FlowPayload(targetId, args)
    }

    fun nextSection(handlerContext: HandlerContext, id: Int, args: Any? = null) {
        val identity = handlerContext[fidAttribute]?.identity ?: error("Could not find identity of the user!")
        flowIdentityStorage[identity] = FlowPayload(id, args)
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

class FlowManagerCommon(
    private val fidAttribute: AttrKey<FlowIdentityData>,
    private val flowIdentityStorage: FlowIdentityStorage
) {
    fun HandlerContext.resetFlow() {
        val identity = get(fidAttribute)?.identity ?: error("Could not find identity of the user!")
        flowIdentityStorage[identity] = FlowPayload(0, null)
    }
}

class FlowHandlerBuilder(
    private val original: HandlerStore,
    val flowIdentityStorage: FlowIdentityStorage,
    private val flowIdentityFinder: FlowIdentityFinder,
    private val ofKinds: Set<UpdateKind<*>>?
) {
    val fidAttribute = original.felineDispatcher.identityScope.attrKeyOf<FlowIdentityData>()

    @PublishedApi
    internal val sections = arrayListOf<FlowSectionStore>()

    @PublishedApi
    internal val nameToId = HashMap<String, Int>()

    private var commonLambda: ((RoutingStrategyInt) -> Unit)? = null

    fun common(handler: FlowManagerCommon.() -> Handler) {
        commonLambda = { it.common(handler.invoke(FlowManagerCommon(fidAttribute, flowIdentityStorage))) }
    }

    fun common(allowedKinds: Set<UpdateKind<*>>? = null, identity: HandlerIdentity? = null, handler: FlowManagerCommon.() -> Handler) {
        commonLambda = { it.common(allowedKinds, identity, handler.invoke(FlowManagerCommon(fidAttribute, flowIdentityStorage))) }
    }

    inline fun section(block: FlowSectionStore.() -> Unit) {
        addSection(null, block)
    }

    inline fun section(name: String, block: FlowSectionStore.() -> Unit) {
        addSection(name, block)
    }

    fun createSectionStore(name: String?) = FlowSectionStore(sections.size, name, flowIdentityStorage, fidAttribute, nameToId, original)

    inline fun addSection(name: String?, block: FlowSectionStore.() -> Unit) {
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

        commonLambda?.invoke(routingStrategy)

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

class FlowSectionStore(
    id: Int,
    internal val name: String?,
    flowIdentityStorage: FlowIdentityStorage,
    fidAttribute: AttrKey<FlowIdentityData>,
    nameToId: Map<String, Int>,
    val original: HandlerStore,
) : HandlerStore, FlowSupport(
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


open class FlowSupport(
    id: Int,
    flowIdentityStorage: FlowIdentityStorage,
    fidAttribute: AttrKey<FlowIdentityData>,
    nameToId: Map<String, Int>,
) {
    val flowManager = FlowManager(id, flowIdentityStorage, fidAttribute, nameToId)

    fun HandlerContext.nextSection(args: Any? = null) {
        flowManager.nextSection(this, args)
    }

    fun HandlerContext.nextSectionWithName(name: String, args: Any? = null) {
        flowManager.nextSection(this, name, args)
    }

    // Sections are numbered from 0 to N. So first section has id of zero
    fun HandlerContext.nextSectionWithId(id: Int, args: Any? = null) {
        flowManager.nextSection(this, id, args)
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
}