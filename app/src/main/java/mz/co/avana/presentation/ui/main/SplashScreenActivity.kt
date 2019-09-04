package mz.co.avana.presentation.ui.main

import android.Manifest
import android.content.Context
import android.content.Intent
import android.location.LocationManager
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import mz.co.avana.R
import mz.co.avana.presentation.ui.user.UserLoginActivity
import mz.co.avana.repository.user.UserRepository
import mz.co.avana.services.AlertActiveGPS
import mz.co.avana.services.AppLocationService
import mz.co.avana.utils.Constants
import mz.co.avana.utils.Utils

class SplashScreenActivity : AppCompatActivity() {
    private var manager: LocationManager? = null
    private var appLocationService: AppLocationService? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)
        manager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
    }

    override fun onStart() {
        super.onStart()

        Dexter.withActivity(this)
            .withPermissions(
                Manifest.permission.CAMERA,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ).withListener(object : MultiplePermissionsListener {
                override fun onPermissionsChecked(report: MultiplePermissionsReport) {
                    if (manager!!.isProviderEnabled(LocationManager.GPS_PROVIDER)) {

                        appLocationService = AppLocationService(this@SplashScreenActivity)
                        val city = appLocationService!!.city()
                        val province = appLocationService!!.location()
                        val location = Utils.readPreference(Constants.USER_LOCATION_CITY, this@SplashScreenActivity)

                        if (location != city) {
                            Utils.writeSharedPreferences(Constants.USER_LOCATION_CITY, city, this@SplashScreenActivity)
                            Utils.writeSharedPreferences(
                                Constants.USER_LOCATION_PROVINCE,
                                province,
                                this@SplashScreenActivity
                            )
                        }
                        handler()
                    } else {
                        showDialog()
                    }
                }

                override fun onPermissionRationaleShouldBeShown(
                    permissions: List<PermissionRequest>,
                    token: PermissionToken
                ) { }
            }).check()
    }

    private fun handler() {
        val handle = Handler()
        handle.postDelayed(this::nextScreen, 3000)
    }

    private fun nextScreen() {
        if (!UserRepository.isLogged()) {
            val intent = Intent(this, UserLoginActivity::class.java)
            startActivity(intent)
            finish()
        } else {
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun showDialog() {
        val ft = supportFragmentManager
        val activeGPS = AlertActiveGPS()
        activeGPS.show(ft, "dialog")
    }
}