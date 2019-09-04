package mz.co.avana.presentation.ui.main

import android.content.Context
import android.content.Intent
import android.location.LocationManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import it.sephiroth.android.library.bottomnavigation.BottomNavigation
import kotlinx.android.synthetic.main.activity_main.*
import mz.co.avana.R
import mz.co.avana.presentation.ui.item.ShareItemActivity
import mz.co.avana.presentation.ui.user.UserProfile
import mz.co.avana.services.AlertActiveGPS
import mz.co.avana.utils.Utils.loadFragment
import mz.co.avana.utils.ViewPagerFragment


class HomeActivity : AppCompatActivity(), BottomNavigation.OnMenuItemSelectionListener {
    var manager: LocationManager? = null
    val fl = R.id.frame_layout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initBottomNavigation()
        loadFragment(ViewPagerFragment(), fl, supportFragmentManager)

        manager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
    }

    override fun onMenuItemReselect(itemId: Int, position: Int, fromUser: Boolean) {

    }

    override fun onMenuItemSelect(itemId: Int, position: Int, fromUser: Boolean) {

        when (itemId) {
            R.id.home -> {
                val viewPagerFragment = ViewPagerFragment()
                val bundle = Bundle()
                bundle.putString("item", "Home")
                viewPagerFragment.arguments = bundle
                loadFragment(viewPagerFragment, fl, supportFragmentManager)
            }
            R.id.profile -> {
                val myProfile = UserProfile()
                val bundle = Bundle()
                bundle.putString("item", "Profile")
                myProfile.arguments = bundle
                loadFragment(myProfile, fl, supportFragmentManager)
            }
            R.id.newItem -> {
                startActivity(Intent(this@HomeActivity, ShareItemActivity::class.java))
            }
        }
    }

    private fun initBottomNavigation() {
        bottom_navigation!!.menuItemSelectionListener = this
    }

    override fun onResume() {
        super.onResume()
        if (!manager!!.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            showDialog()
        }
    }


    private fun showDialog() {

        val activeGPS = AlertActiveGPS()
        activeGPS.show(supportFragmentManager, "dialog")
    }
}
