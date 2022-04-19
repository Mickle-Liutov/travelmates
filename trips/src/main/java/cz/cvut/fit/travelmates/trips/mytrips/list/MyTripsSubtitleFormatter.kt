package cz.cvut.fit.travelmates.trips.mytrips.list

import android.content.Context
import cz.cvut.fit.travelmates.trips.R

/**
 * Formatter for my trips subtitle item
 */
class MyTripsSubtitleFormatter(item: MyTripsSubtitle, context: Context) {

    val subtitle = when (item.type) {
        MyTripsSubtitleType.OWNER -> R.string.my_trips_subtitle_owner
        MyTripsSubtitleType.REQUEST -> R.string.my_trips_subtitle_request
        MyTripsSubtitleType.MEMBER -> R.string.my_trips_subtitle_member
    }.let { context.getString(it) }

}