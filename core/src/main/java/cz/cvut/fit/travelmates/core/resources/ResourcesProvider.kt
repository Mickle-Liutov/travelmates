package cz.cvut.fit.travelmates.core.resources

import android.content.Context
import androidx.annotation.StringRes

class ResourcesProvider(private val context: Context) {

    fun getString(@StringRes stringRes: Int) = context.getString(stringRes)

    fun getString(@StringRes stringRes: Int, vararg args: Any) = context.getString(stringRes, *args)

}