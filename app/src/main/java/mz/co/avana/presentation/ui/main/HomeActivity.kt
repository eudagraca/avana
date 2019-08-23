package mz.co.avana.presentation.ui.main

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import it.sephiroth.android.library.bottomnavigation.BottomNavigation
import kotlinx.android.synthetic.main.activity_main.*
import mz.co.avana.R
import mz.co.avana.presentation.ui.item.ShareItemActivity
import mz.co.avana.presentation.ui.user.UserProfile
import mz.co.avana.utils.ViewPagerFragment


class HomeActivity : AppCompatActivity(), BottomNavigation.OnMenuItemSelectionListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initBottomNavigation()
        loadFragment(ViewPagerFragment())
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
                loadFragment(viewPagerFragment)
            }
            R.id.profile -> {
                val myProfile = UserProfile()
                val bundle = Bundle()
                bundle.putString("item", "Profile")
                myProfile.arguments = bundle
                loadFragment(myProfile)
            }
            R.id.newItem -> {
                startActivity(Intent(this@HomeActivity, ShareItemActivity::class.java))
            }
        }
    }

    private fun initBottomNavigation() {
        bottom_navigation!!.menuItemSelectionListener = this
    }

    private fun loadFragment(fragment: Fragment?) {
        if (fragment != null) {
            supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.frame_layout, fragment)
                    .commit()
        }
    }

}
