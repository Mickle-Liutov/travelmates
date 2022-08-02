package cz.cvut.fit.travelmates.trips.mytrips

import cz.cvut.fit.travelmates.core.networking.toBody
import cz.cvut.fit.travelmates.mainapi.user.UserService
import cz.cvut.fit.travelmates.trips.TripsRepository
import cz.cvut.fit.travelmates.trips.mytrips.list.MyTrip
import cz.cvut.fit.travelmates.trips.mytrips.list.MyTripsItem
import cz.cvut.fit.travelmates.trips.mytrips.list.MyTripsSubtitle
import cz.cvut.fit.travelmates.trips.mytrips.list.MyTripsSubtitleType

/**
 * Use case for composing a list of my trips
 */
class ComposeMyTripsUseCase(
    private val tripsRepository: TripsRepository,
    private val userService: UserService
) {

    suspend fun invoke(): List<MyTripsItem> {
        val trips = tripsRepository.getMyTrips()
        val user = userService.getUser().toBody()
        //Split received trips into categories
        val ownerTrips = trips.filter { it.owner.email == user.email }
        val requestTrips = trips.filter { it.currentUserRequest != null }
        val memberTrips = trips.filter { it.members.any { it.email == user.email } }
        //Create pairs of subtitles and trips
        val subLists = listOf(
            MyTripsSubtitleType.OWNER to ownerTrips,
            MyTripsSubtitleType.REQUEST to requestTrips,
            MyTripsSubtitleType.MEMBER to memberTrips
        )
        return buildList {
            subLists.forEach { (type, items) ->
                //Add subtitle if section has some items
                if (items.isNotEmpty()) {
                    add(MyTripsSubtitle(type))
                }
                //Add the items
                addAll(items.map {
                    MyTrip(
                        it.description,
                        it.id,
                        it.location,
                        it.owner,
                        it.requirements,
                        it.state,
                        it.suggestedDate,
                        it.title
                    )
                })
            }
        }
    }

}