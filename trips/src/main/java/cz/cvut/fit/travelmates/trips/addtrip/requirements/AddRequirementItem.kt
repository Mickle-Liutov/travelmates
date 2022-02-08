package cz.cvut.fit.travelmates.trips.addtrip.requirements

sealed class AddRequirementItem {

    abstract fun isItemSame(other: AddRequirementItem): Boolean
    abstract fun areContentsSame(other: AddRequirementItem): Boolean
    abstract val itemType: Int

    companion object {
        const val TYPE_ITEM = 1
        const val TYPE_ADD = 2
    }
}

data class RequirementItem(val name: String) : AddRequirementItem() {
    override fun isItemSame(other: AddRequirementItem): Boolean {
        return this == other
    }

    override fun areContentsSame(other: AddRequirementItem): Boolean {
        return this == other
    }

    override val itemType = TYPE_ITEM
}

object AddItem : AddRequirementItem() {
    override fun isItemSame(other: AddRequirementItem): Boolean {
        return this == other
    }

    override fun areContentsSame(other: AddRequirementItem): Boolean {
        return this == other
    }

    override val itemType = TYPE_ADD
}

