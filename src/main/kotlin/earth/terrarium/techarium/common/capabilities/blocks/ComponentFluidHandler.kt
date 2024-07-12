package earth.terrarium.techarium.common.capabilities.blocks

import earth.terrarium.techarium.common.registries.ModComponents
import earth.terrarium.techarium.common.utils.ComponentSlot
import earth.terrarium.techarium.common.utils.default
import net.neoforged.neoforge.common.MutableDataComponentHolder
import net.neoforged.neoforge.fluids.FluidStack
import net.neoforged.neoforge.fluids.capability.IFluidHandler
import kotlin.reflect.KMutableProperty

class ComponentFluidHandler(
    private val slot: ComponentSlot,
    private val getSet: KMutableProperty<FluidStack>,
    holder: MutableDataComponentHolder,
    private val validator: (FluidStack) -> Boolean
) : IFluidHandler {

    private val data: Map<ComponentSlot, Int> by ModComponents.tankCapacity.default(emptyMap(), holder)

    private val capacity: Int get() = data[slot] ?: 0
    private val amount: Int get() = fluid.amount
    private val remaining: Int get() = capacity - amount

    private var fluid: FluidStack
        get() = getSet.getter.call()
        set(value) = getSet.setter.call(value)

    override fun getTanks() = 1
    override fun getTankCapacity(tank: Int) = capacity
    override fun getFluidInTank(tank: Int) = fluid
    override fun isFluidValid(tank: Int, stack: FluidStack) = validator(stack)

    override fun fill(resource: FluidStack, action: IFluidHandler.FluidAction): Int {
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

    override fun drain(resource: FluidStack, action: IFluidHandler.FluidAction): FluidStack {
        if (resource.isEmpty || !FluidStack.isSameFluidSameComponents(fluid, resource)) return FluidStack.EMPTY
        return drain(resource.amount, action)
    }

    override fun drain(maxDrain: Int, action: IFluidHandler.FluidAction): FluidStack {
        val amount = maxDrain.coerceAtMost(amount)
        if (amount == 0) return FluidStack.EMPTY
        val fluid = fluid.copyWithAmount(amount)
        if (action.execute()) {
            fluid.amount -= amount
        }
        return fluid
    }
}