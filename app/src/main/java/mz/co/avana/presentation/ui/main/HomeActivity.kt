package mz.co.avana.presentation.ui.main

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.provider.Settings
import android.view.MenuItem
import android.view.Window
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.button.MaterialButton
import kotlinx.android.synthetic.main.activity_main.*
import mz.co.avana.R
import mz.co.avana.presentation.ui.dialog.CustomDialogAlertLogin
import mz.co.avana.presentation.ui.item.ShareItemActivity
import mz.co.avana.presentation.ui.others.OthersFragment
import mz.co.avana.presentation.ui.user.UserProfile
import mz.co.avana.repository.user.UserRepository
import mz.co.avana.services.LocationManagerCheck
import mz.co.avana.utils.Constants
import mz.co.avana.utils.Utils
import mz.co.avana.utils.Utils.loadFragment
import mz.co.avana.utils.ViewPagerFragment
import mz.co.avana.viewModel.item.ItemViewModel
import mz.co.avana.viewModel.user.UserViewModel
import kotlin.system.exitProcess


class HomeActivity : AppCompatActivity() {

    private var alertLogin: CustomDialogAlertLogin? = null

    private var locationManagerCheck: LocationManagerCheck? = null
    private val fl = R.id.frame_layout
    //Controller to prevent app crash
    private var controller = 0
    private var controllerOnBack = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        locationManagerCheck = LocationManagerCheck(this)
        alertLogin = CustomDialogAlertLogin()


        loadFragment(ViewPagerFragment(), fl, supportFragmentManager)
        bottom_navigation.setOnNavigationItemSelectedListener { item: MenuItem ->
            return@setOnNavigationItemSelectedListener when (item.itemId) {
                R.id.home -> {
                    val viewPagerFragment = ViewPagerFragment()
                    val bundle = Bundle()
                    bundle.putString("fragment", "home")
                    viewPagerFragment.arguments = bundle
                    loadFragment(viewPagerFragment, fl, supportFragmentManager)
                    true
                }
                R.id.profile -> {

                    if (UserRepository.isLogged()) {
                        val myProfile = UserProfile()
                        val bundle = Bundle()
                        bundle.putString("fragment", "profile")
                        myProfile.arguments = bundle
                        loadFragment(myProfile, fl, supportFragmentManager)
                    } else {
                        alertLogin!!.show(supportFragmentManager, "milses")
                    }
                    true
                }
                R.id.newItem -> {
                    if (UserRepository.isLogged()) {
                        startActivity(Intent(this@HomeActivity, ShareItemActivity::class.java))
                    } else {
                        alertLogin!!.show(supportFragmentManager, "milses")
                    }
                    true
                }
                R.id.others -> {
                    val othersFragment = OthersFragment()
                    loadFragment(othersFragment, fl, supportFragmentManager)
                    true
                }
                else -> false
            }
        }
    }

    override fun onResume() {
        super.onResume()
        val fragment = intent.getStringExtra("fragment")
        val viewPagerFragment = ViewPagerFragment()
        val myProfile = UserProfile()
        when (fragment) {
            "home" ->
                loadFragment(viewPagerFragment, fl, supportFragmentManager)
            "profile" ->
                loadFragment(myProfile, fl, supportFragmentManager)
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()

        val intent = Intent(Intent.ACTION_MAIN)
        intent.addCategory(Intent.CATEGORY_HOME)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
    }

    override fun onStart() {
        super.onStart()

        controller += 1
        locationManagerCheck = LocationManagerCheck(this@HomeActivity)

        val alert = Dialog(this@HomeActivity)
        if (locationManagerCheck!!.isLocationServiceAvailable()!!) {
            alert.dismiss()
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
                exitProcess(1)
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
            } else if (alert.isShowing && controller != 1) {
                alert.dismiss()
            }
        }
        if (UserRepository.isLogged()) {
            Utils.writeSharedPreferences(
                Constants.EMAIL,
                UserRepository.email()!!, Constants.EMAIL,
                this@HomeActivity
            )
            val userViewModel = ViewModelProviders.of(this).get(UserViewModel::class.java)
            userViewModel.userLiveData.observe(this, Observer {
                it?.let { user ->
                    Utils.writeSharedPreferences(
                        Constants.USER,
                        "${user.name} ${user.surname}", Constants.USER,
                        this@HomeActivity
                    )

                    Utils.writeSharedPreferences(
                        Constants.NAMES_OF_USER,
                        user.name, Constants.NAMES_OF_USER,
                        this@HomeActivity
                    )

                    Utils.writeSharedPreferences(
                        Constants.SURNAME,
                        "${user.surname}", Constants.SURNAME,
                        this@HomeActivity
                    )

                    Utils.writeSharedPreferences(
                        Constants.POSTS,
                        user.posts.toString(), Constants.POSTS,
                        this@HomeActivity
                    )

                    Utils.writeSharedPreferences(
                        Constants.IMAGE_PROF,
                        user.image.toString(), Constants.IMAGE_PROF,
                        this@HomeActivity
                    )

                }
            })

            val itemViewModel = ViewModelProviders.of(this).get(ItemViewModel::class.java)
            itemViewModel.itemLiveDataList.observe(this, Observer { items ->
                Utils.writeSharedPreferences(
                    Constants.PROMOTION,
                    items.size.toString(),
                    Constants.PROMOTION, this@HomeActivity
                )
            })
            itemViewModel.itemsUser()
            userViewModel.userData()
        }
    }

    override fun onPause() {
        super.onPause()
        controller = 0
        controllerOnBack = false
    }
}
