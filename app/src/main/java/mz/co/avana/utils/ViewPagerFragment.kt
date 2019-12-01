package mz.co.avana.utils

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import com.google.android.gms.location.LocationRequest
import kotlinx.android.synthetic.main.fragment_view_pager.view.*
import kotlinx.android.synthetic.main.search_bar.view.*
import mz.co.avana.R
import mz.co.avana.callbacks.MessageCallback
import mz.co.avana.model.User
import mz.co.avana.presentation.ui.item.LowersFragment
import mz.co.avana.presentation.ui.item.MostViewedFragment
import mz.co.avana.presentation.ui.item.NearFragment
import mz.co.avana.presentation.ui.search.SearchActivity
import mz.co.avana.repository.user.UserRepository
import mz.co.avana.services.FetchAddressTask

class ViewPagerFragment : Fragment(), FetchAddressTask.OnTaskCompleted {
    override fun onTaskCompleted(result: String) {

        if (result != city && !result.isBlank()) {
            Utils.writeSharedPreferences(
                Constants.USER_LOCATION_CITY,
                result, Constants.LOCATION,
                activity!!)
        }
    }

    private var city: String = ""
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_view_pager, container, false)

        setupSearch(view)

        city = Utils.readPreference(Constants.USER_LOCATION_CITY, Constants.LOCATION, activity!!)
        getLocationRequest()
        val user = User()
        user.location = city
        val userRepository = UserRepository(context!!, user, activity!!)
        if (UserRepository.isLogged()) {
            userRepository.setLocation(object : MessageCallback {
                override fun onSuccess(successMessage: String) {
                }
                override fun onError(errorMessage: String) {
                }
            })
        }

        view.materialButtonBack.visibility = View.GONE

        view.search.hint = getString(R.string.what_are_you_look)
        view.search.setOnClickListener {
            startActivity(Intent(context, SearchActivity::class.java))
        }
        setupViewPager(view.viewpager, city)
        return view
    }

    private fun setupViewPager(viewPager: ViewPager, city: String) {
        val adapter = ViewPagerAdapter(childFragmentManager)
        adapter.addFragment(LowersFragment(), getString(R.string.lowers))
        if (city !="" && city.isNotEmpty())
            adapter.addFragment(NearFragment(), city)
        adapter.addFragment(MostViewedFragment(), getString(R.string.hot_deals))
        viewPager.adapter = adapter
    }

    override fun onStart() {
        super.onStart()
        city = Utils.readPreference(Constants.USER_LOCATION_CITY, Constants.LOCATION, activity!!)
    }

    private fun setupSearch(view: View) {
        view.search.isFocusable = false
        view.search.isFocusableInTouchMode = false
        view.search.isClickable = true
        view.search.isCursorVisible = false
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

}
