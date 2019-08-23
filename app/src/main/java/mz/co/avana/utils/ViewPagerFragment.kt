package mz.co.avana.utils

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import kotlinx.android.synthetic.main.fragment_view_pager.view.*
import mz.co.avana.R
import mz.co.avana.presentation.ui.item.LowersFragment
import mz.co.avana.presentation.ui.item.MostViewedFragment
import mz.co.avana.presentation.ui.item.NearFragment

class ViewPagerFragment : Fragment() {

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_view_pager, container, false)
        setupViewPager(view.viewpager)
        return view
    }

    private fun setupViewPager(viewPager: ViewPager) {
        val adapter = ViewPagerAdapter(childFragmentManager)
        adapter.addFragment(LowersFragment(), getString(R.string.lowers))
        adapter.addFragment(MostViewedFragment(), getString(R.string.most_discounted))
        adapter.addFragment(NearFragment(), getString(R.string.nearest))
        viewPager.adapter = adapter
    }
}
