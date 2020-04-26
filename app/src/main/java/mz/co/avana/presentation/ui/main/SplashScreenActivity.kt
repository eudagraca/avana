package mz.co.avana.presentation.ui.main

import android.Manifest
import android.Manifest.permission.ACCESS_COARSE_LOCATION
import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.location.Location
import android.os.Bundle
import android.os.Handler
import android.provider.Settings
import android.view.Window
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.*
import com.google.android.material.button.MaterialButton
import com.google.firebase.analytics.FirebaseAnalytics
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import kotlinx.android.synthetic.main.activity_splash_screen.*
import mz.co.avana.R
import mz.co.avana.services.FetchAddressTask
import mz.co.avana.services.LocationManagerCheck
import mz.co.avana.utils.Constants
import mz.co.avana.utils.Utils
import kotlin.system.exitProcess


class SplashScreenActivity : AppCompatActivity(), FetchAddressTask.OnTaskCompleted {
    private var locationManagerCheck: LocationManagerCheck? = null

    //Controller to prevent app crash
    private var controller = 0

    private lateinit var firebaseAnalytics: FirebaseAnalytics

    // Location classes
    private var mTrackingLocation: Boolean = false
    private var mFusedLocationClient: FusedLocationProviderClient? = null
    private var mLocationCallback: LocationCallback? = null
    var location: Location? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)
// Obtain the FirebaseAnalytics instance.
        firebaseAnalytics = FirebaseAnalytics.getInstance(this)
        // Initialize the FusedLocationClient.
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        // Restore the state if the activity is recreated.
        if (savedInstanceState != null) {
            mTrackingLocation = savedInstanceState.getBoolean(
                Constants.LOCATION_KEY
            )
        }

        // Initialize the location callbacks.
        mLocationCallback = object : LocationCallback() {
            /**
             * This is the callback that is triggered when the
             * FusedLocationClient updates location.
             * @param locationResult The result containing the device location.
             */
            override fun onLocationResult(locationResult: LocationResult?) {
                // If tracking is turned on, reverse geocode into an address
                if (mTrackingLocation) {
                    FetchAddressTask(this@SplashScreenActivity, this@SplashScreenActivity)
                        .execute(locationResult!!.lastLocation)
                }
            }
        }

        controller += 1
        locationManagerCheck = LocationManagerCheck(this)
        Dexter.withActivity(this@SplashScreenActivity)
            .withPermissions(
                Manifest.permission.CAMERA,
                ACCESS_FINE_LOCATION,
                ACCESS_COARSE_LOCATION,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ).withListener(object : MultiplePermissionsListener {
                @SuppressLint("MissingPermission")
                override fun onPermissionsChecked(report: MultiplePermissionsReport) {
                    if (report.areAllPermissionsGranted()) {

                        nextScreen()
                        val alert = Dialog(this@SplashScreenActivity)
                        if (locationManagerCheck!!.isLocationServiceAvailable()!!) {
                            alert.dismiss()
                            startTrackingLocation()
                            getLocation()
                        } else {
                            alert.requestWindowFeature(Window.FEATURE_NO_TITLE)
                            alert.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                            alert.window?.setLayout(
                                (resources.displayMetrics.widthPixels * 0.80).toInt(),
                                LinearLayout.LayoutParams.WRAP_CONTENT
                            )
                            alert.setContentView(R.layout.need_active_resource)

                            val no = alert.findViewById<MaterialButton>(R.id.dont_enable_gps)
                            no.setOnClickListener {
                                handler()
                            }

                            val yes = alert.findViewById<MaterialButton>(R.id.enable_gps)
                            yes.setOnClickListener {
                                if (alert.isShowing) {
                                    controller = 0
                                    alert.dismiss()
                                }
                                startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
                            }
                            if (controller == 1) {
                                alert.show()
                            }
                        }
                    } else {
                        exitProcess(0)
                    }
                }

                override fun onPermissionRationaleShouldBeShown(
                    permissions: MutableList<PermissionRequest>?,
                    token: PermissionToken?
                ) {
                    token?.continuePermissionRequest()
                }
            }).withErrorListener { }.check()
    }

    /**
     * Starts tracking the device. Checks for
     * permissions, and requests them if they aren't present. If they are,
     * requests periodic location updates, sets a loading text and starts the
     * animation.
     */
    private fun startTrackingLocation() {
        if (ActivityCompat.checkSelfPermission(this, ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this, arrayOf(ACCESS_FINE_LOCATION, ACCESS_COARSE_LOCATION),
                Constants.LOCATION_PERMISSION
            )
        } else {
            mTrackingLocation = true
            mFusedLocationClient!!.requestLocationUpdates(
                getLocationRequest(),
                mLocationCallback,
                null /* Looper */
            )
            // Set a loading text while you wait for the address to be
            // returned
        }
    }

    /**
     * Sets up the location request.
     *
     * @return The LocationRequest object containing the desired parameters.
     */
    private fun getLocationRequest(): LocationRequest {
        val locationRequest = LocationRequest()
        locationRequest.interval = Constants.UPDATE_INTERVAL
        locationRequest.fastestInterval = Constants.MAX_WAIT_TIME
        locationRequest.priority = LocationRequest.PRIORITY_NO_POWER
        return locationRequest
    }

    /**
     * Saves the last location on configuration change
     */
    override fun onSaveInstanceState(outState: Bundle) {
        outState.putBoolean(Constants.LOCATION_KEY, mTrackingLocation)
        super.onSaveInstanceState(outState)
    }

    override fun onTaskCompleted(result: String) {
        if (mTrackingLocation) {
            val location = Utils.readPreference(
                Constants.USER_LOCATION_CITY,
                Constants.LOCATION,
                this@SplashScreenActivity
            )
            if (result != location && !result.isBlank()) {
                Utils.writeSharedPreferences(
                    Constants.USER_LOCATION_CITY,
                    result, Constants.LOCATION,
                    this@SplashScreenActivity
                )
            }

            val finalLocation = Utils.readPreference(
                Constants.USER_LOCATION_CITY,
                Constants.LOCATION,
                this@SplashScreenActivity
            )
            if (!finalLocation.isBlank()) {
                tv_location.text = finalLocation

            }
            handler()
        }
    }

    override fun onPause() {
        if (mTrackingLocation) {
            mTrackingLocation = false
            stopLocationUpdates()
        }
        controller = 0
        super.onPause()
    }

    private fun stopLocationUpdates() {
        mFusedLocationClient!!.removeLocationUpdates(mLocationCallback)
    }

    private fun handler() {
        val handle = Handler()
        handle.postDelayed(this::nextScreen, 4000)
    }

    override fun onResume() {
        super.onResume()
        if (mTrackingLocation) {
            startTrackingLocation()
        }
    }

    private fun nextScreen() {
//        if (!UserRepository.isLogged()) {
//            val intent = Intent(this, UserLoginActivity::class.java)
//            startActivity(intent)
//            finish()
//        } else {
        val intent = Intent(this, HomeActivity::class.java)
        startActivity(intent)
        finish()
//        }
    }

    private fun getLocation() {
        if (ActivityCompat.checkSelfPermission(this, ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this, arrayOf(ACCESS_FINE_LOCATION),
                Constants.LOCATION_PERMISSION
            )
        } else {
            mFusedLocationClient!!.lastLocation.addOnSuccessListener { location ->
                FetchAddressTask(
                    this@SplashScreenActivity,
                    this@SplashScreenActivity
                ).execute(location)
            }
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        val intent = Intent(Intent.ACTION_MAIN)
        intent.addCategory(Intent.CATEGORY_HOME)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
    }
}