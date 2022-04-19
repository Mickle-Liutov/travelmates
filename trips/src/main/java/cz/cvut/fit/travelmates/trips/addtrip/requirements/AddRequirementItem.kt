package cz.cvut.fit.travelmates.trips.addtrip.requirements

/**
 * List item for requirements list
 */
sealed class AddRequirementItem {

    abstract fun isItemSame(other: AddRequirementItem): Boolean
    abstract fun areContentsSame(other: AddRequirementItem): Boolean
    abstract val itemType: Int

    companion object {
        const val TYPE_ITEM = 1
        const val TYPE_ADD = 2
    }
}

/**
 * Represents existing requirement item in the list
 */
data class RequirementItem(val id: String, val name: String) : AddRequirementItem() {
    override fun isItemSame(other: AddRequirementItem): Boolean {
        return other is RequirementItem && id == other.id
    }

    override fun areContentsSame(other: AddRequirementItem): Boolean {
        return this == other
    }

    override val itemType = TYPE_ITEM
}

/**
 * Represents add new button item in the list
 */
object AddItem : AddRequirementItem() {
    override fun isItemSame(other: AddRequirementItem): Boolean {
        return this == other
    }

    override fun areContentsSame(other: AddRequirementItem): Boolean {
        return this == other
    }

    override val itemType = TYPE_ADD
}

