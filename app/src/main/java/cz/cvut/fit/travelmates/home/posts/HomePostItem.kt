package cz.cvut.fit.travelmates.home.posts

import cz.cvut.fit.travelmates.mainapi.posts.Post

/**
 * Represents a UI item in a list of posts
 * on the home screen
 */
sealed class HomePostItem {

    abstract fun isSameAs(other: HomePostItem): Boolean
    abstract fun hasSameContentAs(other: HomePostItem): Boolean
    abstract val viewType: Int

    companion object {
        const val TYPE_ITEM = 1
        const val TYPE_ADD = 2
    }
}

/**
 * A post in the list of posts
 */
data class HomePost(val post: Post) : HomePostItem() {
    override fun isSameAs(other: HomePostItem) = other is HomePost && post.id == other.post.id

    override fun hasSameContentAs(other: HomePostItem) = this == other

    override val viewType = TYPE_ITEM
}

/**
 * An add button in the list of posts
 */
object HomeAddPost : HomePostItem() {
    override fun isSameAs(other: HomePostItem) = this == other

    override fun hasSameContentAs(other: HomePostItem) = this == other

    override val viewType = TYPE_ADD
}
