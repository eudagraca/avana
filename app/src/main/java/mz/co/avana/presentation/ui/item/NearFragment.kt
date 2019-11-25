package mz.co.avana.presentation.ui.item

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import kotlinx.android.synthetic.main.fragment_itens.view.*
import kotlinx.android.synthetic.main.is_blank.view.*
import kotlinx.android.synthetic.main.loading.view.*
import mz.co.avana.R
import mz.co.avana.utils.Constants
import mz.co.avana.utils.Utils
import mz.co.avana.viewModel.item.ItemViewModel

class NearFragment : Fragment(), SwipeRefreshLayout.OnRefreshListener {
    override fun onRefresh() {
        getData(this.view!!)
    }
    private var cityLocation = String()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_itens, container, false)
        cityLocation = Utils.readPreference(Constants.USER_LOCATION_CITY, Constants.LOCATION, activity!!)

        view!!.loading.visibility = View.VISIBLE
        view.lLisBlank.visibility = View.GONE
        view.swipeRefresh.setOnRefreshListener(this)
        view.swipeRefresh.setColorSchemeColors(ContextCompat.getColor(context!!, R.color.colorAccent))
        getData(view)
        return view
    }

    @SuppressLint("DefaultLocale")
    private fun getData(view: View) {
        view.loading.visibility = View.VISIBLE
        val itemViewModel = ViewModelProviders.of(this).get(ItemViewModel::class.java)
        itemViewModel.itemLiveDataList.observe(this, Observer { items ->
            with(view.rv_itens, {
                layoutManager = GridLayoutManager(context, 2)
                setHasFixedSize(true)
                adapter = ItemAdapter(activity as AppCompatActivity, items, context!!) { item ->
//                    val intent = Intent(activity, NotificationItemDetailsActivity::class.java)
//                    intent.putExtra("itemId", item.itemId)

                    val intent = Intent(activity, ItemDetailsActivity::class.java)
                    intent.putExtra(Constants.ITEM, item)
                    intent.putExtra("fragment", "home")
                    startActivity(intent)
                }
                if (items.isEmpty()) {
                    view.lLisBlank.visibility = View.VISIBLE
                    view.tv_isBlank.text = context.getString(R.string.there_are_no_item_in_your_city)
                }else{
                    view.lLisBlank.visibility = View.GONE
                }
                view.loading.visibility = View.GONE
                view.swipeRefresh.isRefreshing = false
            })
        })
        itemViewModel.itemNear(cityLocation.toLowerCase())
    }
}
