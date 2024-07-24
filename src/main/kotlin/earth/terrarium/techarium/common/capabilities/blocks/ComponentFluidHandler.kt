package earth.terrarium.techarium.common.capabilities.blocks

import earth.terrarium.techarium.common.registries.ModComponents
import earth.terrarium.techarium.common.utils.ComponentSlot
import earth.terrarium.techarium.common.utils.default
import net.neoforged.neoforge.attachment.AttachmentType
import net.neoforged.neoforge.attachment.IAttachmentHolder
import net.neoforged.neoforge.common.MutableDataComponentHolder
import net.neoforged.neoforge.fluids.FluidStack
import net.neoforged.neoforge.fluids.capability.IFluidHandler

typealias FluidValidator = (FluidStack) -> Boolean

open class ComponentFluidHandler protected constructor(
    private val slot: ComponentSlot,
    private val getter: () -> FluidStack,
    private val setter: (FluidStack) -> Unit,
    holder: MutableDataComponentHolder,
    private val validator: FluidValidator = { true }
) : IFluidHandler {

    private val data: Map<ComponentSlot, Int> by ModComponents.tankCapacity.default(emptyMap(), holder)

    private val capacity: Int get() = data[slot] ?: 0
    private val amount: Int get() = fluid.amount
    private val remaining: Int get() = capacity - amount

    private var fluid: FluidStack
        get() = getter()
        set(value) = setter(value)

    override fun getTanks() = 1
    override fun getTankCapacity(tank: Int) = capacity
    override fun getFluidInTank(tank: Int) = fluid
    override fun isFluidValid(tank: Int, stack: FluidStack) = validator(stack)

    override fun fill(resource: FluidStack, action: IFluidHandler.FluidAction): Int {
        if (!slot.canInput()) return 0
        return fillInternal(resource, action)
    }

    fun fillInternal(resource: FluidStack, action: IFluidHandler.FluidAction): Int {
        if (resource.isEmpty || !isFluidValid(0, resource)) return 0
        val amount = resource.amount.coerceAtMost(remaining)
        if (amount == 0) return 0
        if (fluid.isEmpty) {
            if (action.execute()) {
                fluid = resource.copyWithAmount(amount)
            }
            return amount
        }
        if (FluidStack.isSameFluidSameComponents(fluid, resource)) {
            if (action.execute()) {
                fluid.amount += amount
            }
            return amount
        }
        return 0
    }

    fun tryFill(resource: FluidStack): Boolean {
        if (!slot.canInput()) return false
        return tryFillInternal(resource)
    }

    fun tryFillInternal(resource: FluidStack): Boolean {
        if (fillInternal(resource, IFluidHandler.FluidAction.SIMULATE) < resource.amount) {
            return false
        }
        return fillInternal(resource, IFluidHandler.FluidAction.EXECUTE) == resource.amount
    }

    override fun drain(resource: FluidStack, action: IFluidHandler.FluidAction): FluidStack {
        if (!slot.canOutput()) return FluidStack.EMPTY
        if (resource.isEmpty || !FluidStack.isSameFluidSameComponents(fluid, resource)) return FluidStack.EMPTY
        return drainInternal(resource.amount, action)
    }

    override fun drain(maxDrain: Int, action: IFluidHandler.FluidAction): FluidStack {
        if (!slot.canOutput()) return FluidStack.EMPTY
        return drainInternal(maxDrain, action)
    }

    fun drainInternal(maxDrain: Int, action: IFluidHandler.FluidAction): FluidStack {
        if (!slot.canOutput()) return FluidStack.EMPTY
        val amount = maxDrain.coerceAtMost(amount)
        if (amount == 0) return FluidStack.EMPTY
        val fluid = fluid.copyWithAmount(amount)
        if (action.execute()) {
            this.fluid.amount -= amount
        }
        return fluid
    }

    fun tryDrain(maxDrain: Int): Boolean {
        if (!slot.canOutput()) return false
        return tryDrainInternal(maxDrain)
    }

    fun tryDrainInternal(maxDrain: Int): Boolean {
        if (drainInternal(maxDrain, IFluidHandler.FluidAction.SIMULATE).amount < maxDrain) {
            return false
        }
        return drainInternal(maxDrain, IFluidHandler.FluidAction.EXECUTE).amount == maxDrain
    }

    companion object {

        fun create(
            slot: ComponentSlot,
            getter: () -> FluidStack,
            setter: (FluidStack) -> Unit,
            holder: MutableDataComponentHolder,
            validator: FluidValidator = { true }
        ) = ComponentFluidHandler(slot, getter, setter, holder, validator)

        fun <T> create(
            slot: ComponentSlot,
            type: AttachmentType<FluidStack>,
            holder: T,
            validator: FluidValidator = { true }
        )
                where T : MutableDataComponentHolder, T : IAttachmentHolder =
            create(slot, { holder.getData(type) }, { holder.setData(type, it) }, holder, validator)
    }
}