package mz.co.avana.services

import android.annotation.SuppressLint
import android.app.Service
import android.content.Context
import android.content.Intent
import android.location.*
import android.os.Bundle
import android.os.IBinder
import mz.co.avana.R
import java.io.IOException
import java.util.*

class AppLocationService(val context: Context) : Service(), LocationListener {

    protected var locationManager: LocationManager? = null
    internal lateinit var location: Location

    init {
        locationManager = context
                .getSystemService(Context.LOCATION_SERVICE) as LocationManager?
    }

    override fun onLocationChanged(location: Location) {}

    override fun onProviderDisabled(provider: String) {}

    override fun onProviderEnabled(provider: String) {}

    override fun onStatusChanged(provider: String, status: Int, extras: Bundle) {}

    override fun onBind(arg0: Intent): IBinder? {
        return null
    }

    companion object {
        private const val MIN_DISTANCE_FOR_UPDATE: Long = 10
        private const val MIN_TIME_FOR_UPDATE = (1000 * 60 * 2).toLong()
    }

    @SuppressLint("MissingPermission")
    fun getLocation(): Location? {
        try {
            locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager?
            // getting GPS status
            val isGPSEnabled = locationManager!!.isProviderEnabled(LocationManager.GPS_PROVIDER)

            // getting network status
            val isNetworkEnabled = locationManager!!.isProviderEnabled(LocationManager.NETWORK_PROVIDER)

            if (!isGPSEnabled && !isNetworkEnabled) {


            } else {
                if (isNetworkEnabled) {
                    locationManager!!.requestLocationUpdates(
                            LocationManager.NETWORK_PROVIDER,
                            MIN_TIME_FOR_UPDATE, MIN_DISTANCE_FOR_UPDATE.toFloat(), this
                    )

                    if (locationManager != null) {
                        location = locationManager!!.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)!!
                        location.latitude
                        location.longitude
                    }
                }
                // if GPS Enabled get lat/long using GPS Services
                if (isGPSEnabled) {
                    if (location == null) {
                        locationManager!!.requestLocationUpdates(
                                LocationManager.GPS_PROVIDER,
                                MIN_TIME_FOR_UPDATE, MIN_DISTANCE_FOR_UPDATE.toFloat(), this
                        )

                        if (locationManager != null) {
                            location = locationManager!!.getLastKnownLocation(LocationManager.GPS_PROVIDER)!!
                            location.latitude
                            location.longitude
                        }
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return location
    }

    fun location(): String {
        var local = String()
        var province = String()
        val geocode = Geocoder(context, Locale.getDefault())
        val addresses: List<Address>
        try {
            addresses = geocode.getFromLocation(getLocation()!!.latitude, getLocation()!!.longitude, 5)
            if (addresses.isNotEmpty()) {
                for (address in addresses) {
                    if (address.locality != null && address.locality.isNotEmpty()) {
                        local = address.locality
                        province = address.adminArea
                        break
                    }
                }
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return context.getString(R.string.you_are_see_itens_from, local, province)
    }

    fun city(): String {
        var local = String()
        val geocode = Geocoder(context, Locale.getDefault())
        val addresses: List<Address>
        try {
            addresses = geocode.getFromLocation(getLocation()!!.latitude, getLocation()!!.longitude, 5)
            if (addresses.isNotEmpty()) {
                for (address in addresses) {
                    if (address.locality != null && address.locality.isNotEmpty()) {
                        local = address.locality
                        break
                    }
                }
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return local
    }
}