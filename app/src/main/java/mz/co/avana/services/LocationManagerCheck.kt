package mz.co.avana.services

import android.app.Activity
import android.content.Context
import android.location.LocationManager


class LocationManagerCheck() {
    private var locationManager: LocationManager? = null
    private var locationServiceBoolean: Boolean? = false
    private var providerType = 0
    private var mContext: Activity? = null

    constructor(context: Activity):this(){
        mContext = context
        locationManager = context
            .getSystemService(Context.LOCATION_SERVICE) as LocationManager
        val gpsIsEnabled = locationManager!!
            .isProviderEnabled(LocationManager.GPS_PROVIDER)

        if (gpsIsEnabled) {
            locationServiceBoolean = true
            providerType = 1

//            try {
//                locationManager.requestLocationUpdates(
//                    LocationManager.GPS_PROVIDER, 1000, 10,
//                )
//
//            } catch (ex: Exception) {
//                turnGPSOff()
//            }

        }
    }

    fun isLocationServiceAvailable(): Boolean? {
        return locationServiceBoolean
    }
}