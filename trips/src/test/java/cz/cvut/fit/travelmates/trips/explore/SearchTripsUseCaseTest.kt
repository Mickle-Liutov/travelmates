package cz.cvut.fit.travelmates.trips.explore

import cz.cvut.fit.travelmates.location.Location
import cz.cvut.fit.travelmates.mainapi.trips.models.*
import org.junit.Before
import org.junit.Test
import java.time.LocalDate

class SearchTripsUseCaseTest {

    private lateinit var searchTripsUseCase: SearchTripsUseCase

    @Before
    fun setup() {
        searchTripsUseCase = SearchTripsUseCase()
    }

    @Test
    fun `title is searched correctly`() {
        val trips = listOf(trip1, trip2)
        val searchTerm = "title1"
        val expected = listOf(trip1)

        val actual = searchTripsUseCase.invoke(trips, searchTerm)

        assert(expected == actual)
    }

    @Test
    fun `requirements are searched correctly`() {
        val trips = listOf(trip1, trip2)
        val searchTerm = "qera"
        val expected = listOf(trip2)

        val actual = searchTripsUseCase.invoke(trips, searchTerm)

        assert(expected == actual)
    }

    @Test
    fun `search is case insensitive`() {
        val trips = listOf(trip1, trip2)
        val searchTerm = "ElTIt1"
        val expected = listOf(trip2)

        val actual = searchTripsUseCase.invoke(trips, searchTerm)

        assert(expected == actual)
    }

    companion object {
        private val trip1 = Trip(
            0,
            "title1",
            "",
            LocalDate.now(),
            TripState.GATHERING,
            Location(1.0, 2.0),
            listOf(
                TripRequirement(0, false, "reqa"),
                TripRequirement(1, false, "reqb"),
                TripRequirement(2, false, "reqc"),
            ),
            TripMember("", "", "", "", emptyList()),
            emptyList(),
            UserType.GUEST,
            emptyList(),
            null,
            emptyList()
        )
        private val trip2 = Trip(
            0,
            "eltit1",
            "",
            LocalDate.now(),
            TripState.GATHERING,
            Location(1.0, 2.0),
            listOf(
                TripRequirement(0, false, "qera"),
                TripRequirement(1, false, "qerb"),
                TripRequirement(2, false, "qerc"),
            ),
            TripMember("", "", "", "", emptyList()),
            emptyList(),
            UserType.GUEST,
            emptyList(),
            null,
            emptyList()
        )
    }
}