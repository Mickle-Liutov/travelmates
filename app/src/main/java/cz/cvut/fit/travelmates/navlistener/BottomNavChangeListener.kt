package cz.cvut.fit.travelmates.navlistener

import android.os.Bundle
import androidx.core.view.isVisible
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import com.google.android.material.bottomnavigation.BottomNavigationView
import cz.cvut.fit.travelmates.R

class BottomNavChangeListener(
    private val bottomNavigationView: BottomNavigationView
) : NavController.OnDestinationChangedListener {

    private val destinationsWithBottomNav = listOf(
        R.id.navigation_my_trips,
        R.id.navigation_profile,
        R.id.navigation_home,
        R.id.navigation_posts_list
    )

    override fun onDestinationChanged(
        controller: NavController,
        destination: NavDestination,
        arguments: Bundle?
    ) {
        bottomNavigationView.isVisible = destination.id in destinationsWithBottomNav
    }
}