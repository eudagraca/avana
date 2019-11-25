package mz.co.avana.presentation.ui.item

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import kotlinx.android.synthetic.main.fragment_itens.view.*
import kotlinx.android.synthetic.main.is_blank.view.*
import kotlinx.android.synthetic.main.loading.view.*
import mz.co.avana.R
import mz.co.avana.model.Destaque
import mz.co.avana.presentation.ui.image.DestaqueAdapter
import mz.co.avana.utils.Constants
import mz.co.avana.utils.Message
import mz.co.avana.utils.Utils
import mz.co.avana.viewModel.item.ItemViewModel


class LowersFragment : Fragment(), SwipeRefreshLayout.OnRefreshListener {
    override fun onRefresh() {
        getData(this.view!!)
    }

    private var cityLocation = String()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_itens, container, false)
        view!!.loading.visibility = View.VISIBLE
        cityLocation = Utils.readPreference(Constants.USER_LOCATION_CITY, Constants.LOCATION, activity!!)
        view.loading.visibility = View.VISIBLE
        view.lLisBlank.visibility = View.GONE
        getData(view)
        view.swipeRefresh.setOnRefreshListener(this)
        view.swipeRefresh.setColorSchemeColors(ContextCompat.getColor(context!!, R.color.colorAccent))


//        val imageUrl = ArrayList<Destaque>()
//        imageUrl.add(
//            Destaque(
//                "https://gigaom.com/wp-content/uploads/sites/1/2011/12/cover2.jpg",
//                "1", "Uma simples")
//        )
//        imageUrl.add(
//            Destaque(
//                "https://o.aolcdn.com/images/dims?quality=85&image_uri=https%3A%2F%2Fo.aolcdn.com%2Fimages%2Fdims%3Fresize%3D2000%252C2000%252Cshrink%26image_uri%3Dhttps%253A%252F%252Fs.yimg.com%252Fos%252Fcreatr-uploaded-images%252F2019-11%252F97483210-ff48-11e9-bb5d-4cbb75163a6b%26client%3Da1acac3e1b3290917d92%26signature%3Daa4544654242d3fe7ba99447d56b87398a770a33&client=amp-blogside-v2&signature=6e5f59ca05107c5459d70de0efc6eb6bd592f3bb"
//                , "2", "Segunda")
//        )
//        imageUrl.add(Destaque("https://i.stack.imgur.com/Cw2oP.png", "3", "Terceira"))
//        view.imageSlider.layoutParams.height = Utils.getScreenHeight() / 5

//        val adapter = DestaqueAdapter(context!!, imageUrl) {
//            Message.messageToast(context!!, it.description)
//        }

//        view.imageSlider.sliderAdapter = adapter
//        view.imageSlider.setIndicatorAnimation(IndicatorAnimations.WORM) //set indicator animation by using SliderLayout.IndicatorAnimations. :WORM or THIN_WORM or COLOR or DROP or FILL or NONE or SCALE or SCALE_DOWN or SLIDE and SWAP!!
//        view.imageSlider.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION)
//        view.imageSlider.autoCycleDirection = SliderView.AUTO_CYCLE_DIRECTION_BACK_AND_FORTH
//        view.imageSlider.indicatorSelectedColor = Color.WHITE
//        view.imageSlider.indicatorUnselectedColor = Color.GRAY
//        view.imageSlider.scrollTimeInSec = 5 //set scroll delay in seconds :
//        view.imageSlider.startAutoCycle()
        return view
    }

    private fun getData(view: View) {
        view.loading.visibility = View.VISIBLE
        val itemViewModel = ViewModelProviders.of(this).get(ItemViewModel::class.java)
        itemViewModel.itemLiveDataList.observe(this, Observer { items ->
            with(view.rv_itens, {
                layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
                setHasFixedSize(true)
                adapter = ItemAdapter(activity as AppCompatActivity, items, context!!) { item ->
                    val intent = Intent(activity, ItemDetailsActivity::class.java)
                    intent.putExtra(Constants.ITEM, item)
                    intent.putExtra("fragment", "home")
                    startActivity(intent)
                }
                if (items.isEmpty()) {
                    view.lLisBlank.visibility = View.VISIBLE
                    view.tv_isBlank.text = context.getString(R.string.there_are_no_item)
                }else{
                    view.lLisBlank.visibility = View.GONE
                }
                view.loading.visibility = View.GONE
                view.swipeRefresh.isRefreshing = false
            })
        })
        itemViewModel.itemsLowers()
    }
}