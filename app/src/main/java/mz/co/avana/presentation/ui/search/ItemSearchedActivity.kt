package mz.co.avana.presentation.ui.search

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import kotlinx.android.synthetic.main.activity_item_searched.*
import kotlinx.android.synthetic.main.is_blank.*
import kotlinx.android.synthetic.main.loading.*
import kotlinx.android.synthetic.main.search_bar.*
import mz.co.avana.R
import mz.co.avana.callbacks.ItemPriceCallback
import mz.co.avana.presentation.ui.item.ItemAdapter
import mz.co.avana.presentation.ui.item.ItemDetailsActivity
import mz.co.avana.presentation.ui.main.HomeActivity
import mz.co.avana.utils.Constants
import mz.co.avana.viewModel.item.ItemViewModel


class ItemSearchedActivity : AppCompatActivity() {
    var minPrice: String = ""
    var maxPrice: String = ""
    var category: String? = "All"
    var itemSearched = ""

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_item_searched)

        search.isCursorVisible = false
        loading.visibility = View.VISIBLE
        materialButtonBack.backgroundTintList =
            ContextCompat.getColorStateList(baseContext, R.color.md_white_1000)

        itemSearched = intent.getStringExtra("item")!!
        search.setText(itemSearched)
        getItems(itemSearched, "", "", "")

        filter.setOnClickListener {
            setPrices()
        }

        materialButtonBack.setOnClickListener {
            startActivity(Intent(this, HomeActivity::class.java))
            finish()
        }

        search.setOnClickListener {
            val intent = Intent(this@ItemSearchedActivity, SearchActivity::class.java)
            intent.putExtra("search", search.text.toString())
            startActivity(intent)
        }
    }

    private fun setPrices() {
        val addPhotoBottomDialogFragment =
            PricesBottomDialogFragment(
                minPrice,
                maxPrice,
                search.text.toString(),
                category!!,
                object : ItemPriceCallback {
                    override fun values(min: String, max: String, item: String) {
                        minPrice = min
                        maxPrice = max
                    }

                    override fun categories(name: String, databaseName: String) {
                        category = name
                        getItems(itemSearched, databaseName, minPrice, maxPrice)
                    }
                })
        addPhotoBottomDialogFragment.show(
            supportFragmentManager,
            "add_photo_dialog_fragment"
        )
    }

    @SuppressLint("DefaultLocale")
    private fun getItems(name: String, category: String, min: String, max: String) {
        loading.visibility = View.VISIBLE
        val itemsViewModel = ViewModelProviders.of(this).get(ItemViewModel::class.java)
        itemsViewModel.itemLiveDataList.observe(this, androidx.lifecycle.Observer {
            with(rv_items_searched) {
                layoutManager = GridLayoutManager(applicationContext, 2)
                setHasFixedSize(true)
                adapter = ItemAdapter(this@ItemSearchedActivity, it, applicationContext) { item ->
                    val intent = Intent(this@ItemSearchedActivity, ItemDetailsActivity::class.java)
                    intent.putExtra(Constants.ITEM, item)
                    startActivity(intent)
                }
                loading.visibility = View.GONE
                if (it.isNotEmpty()) {
                    rv_items_searched.visibility = View.VISIBLE
                    lLisBlank.visibility = View.GONE
                } else {
                    rv_items_searched.visibility = View.GONE
                    lLisBlank.visibility = View.VISIBLE
                }
            }
        })
        if (name.isNotEmpty() && category != "All" && max != "" && min != "") {
            itemsViewModel.searchItemByAllParams(name.toLowerCase(), category, min, max)
        } else if (name.isNotEmpty() && category != "All" && max == "" && min != "") {
            itemsViewModel.searchItemByCategoryAndNameMin(name.toLowerCase(), category, min)
        } else if (name.isNotEmpty() && category.isNotEmpty() && min == "" && max != "") {
            itemsViewModel.searchItemByCategoryAndNameMax(name.toLowerCase(), category, max)
        } else if (name.isNotEmpty() && min != "" && max != "") {
            itemsViewModel.searchItemByNameAndPrice(name.toLowerCase(), min, max)
        } else if (name.isEmpty() && category == "All" && min != "" && max != "") {
            itemsViewModel.searchItemByPrice(min, max)
        } else if (name.isNotEmpty() && category != "") {
            itemsViewModel.searchItemByNameAndCategory(name.toLowerCase(), category)
        } else if (category != "All" && name.isEmpty()) {
            itemsViewModel.searchItemByCategory(category)
        } else {
            itemsViewModel.searchItemByName(name.toLowerCase())
        }
    }
}
