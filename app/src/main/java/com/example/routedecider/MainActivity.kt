package com.example.routedecider

import android.os.Bundle

import androidx.appcompat.app.AppCompatActivity

import androidx.lifecycle.lifecycleScope
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.launch
import timber.log.Timber

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Timber.plant(Timber.DebugTree())

        lifecycleScope.launch {
            val routeService = RouteService()
            val origin = Waypoint(
                location = Location(
                    latLng = LatLng(39.49162065869929, -0.3595850997645922)
                )
            )
            val destination = Waypoint(
                location =  Location(
                    latLng = LatLng(39.490883941147786, -0.40469611578464404)
                )
            )
            val travelMode = "DRIVE"
            val routingPreference = "TRAFFIC_AWARE_OPTIMAL"
            val fieldMask = "routes.description,routes.localizedValues.distance,routes.localizedValues.duration"
            val apiKey = getString(R.string.api_key)

            val response = routeService.getRoute(origin, destination, travelMode, routingPreference, apiKey, fieldMask)

//            NotificationHelper.sendNotification(this@MainActivity, route)
        }
    }
}