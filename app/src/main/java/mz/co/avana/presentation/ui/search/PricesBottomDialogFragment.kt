package mz.co.avana.presentation.ui.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.android.synthetic.main.bottom_sheet_filter.*
import kotlinx.android.synthetic.main.bottom_sheet_filter.view.*
import mz.co.avana.R
import mz.co.avana.callbacks.ItemPriceCallback

class PricesBottomDialogFragment() : BottomSheetDialogFragment() {

    private var priceCallback: ItemPriceCallback? = null
    private var category: String? = null
    private var min = ""
    private var max = ""
    private var item = ""

    constructor(min: String, max: String, name: String, category: String, priceCallback: ItemPriceCallback) : this() {
        item = name
        this.min = min
        this.max = max
        this.category = category
        this.priceCallback = priceCallback
    }

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.bottom_sheet_filter, container, false)

        view.name.text = item
        view.select_category.text = category
        view.reset.setOnClickListener {
            view.et_min.text = null
            view.et_max.text = null
            view.name.text = null
            min = ""
            max = ""
        }

        if (min.isNotEmpty() || max.isNotEmpty()) {
            view.et_min.setText(min)
            view.et_max.setText(max)
        }

        view.btn_done_filter_price.setOnClickListener {
            if (view.et_min.text.toString().isNotEmpty()) {
                min = view.et_min.text.toString()
            }

            if (view.et_max.text.toString().isNotEmpty()) {
                max = view.et_max.text.toString()
            }

            priceCallback!!.values(min, max, item)
            if (category != null && select_category.text.isNotEmpty()) {
                priceCallback!!.categories(select_category.text.toString(), category!!)
            } else {
                priceCallback!!.categories("All", "")
            }
            dismissAllowingStateLoss()
        }

        view.select_category.setOnClickListener {
            getCategories(view)
        }
        return view
    }

    private fun getCategories(view: View) {
        val addPhotoBottomDialogFragment =
                CategoriesBottomDialogFragment(object : ItemPriceCallback {
                    override fun values(min: String, max: String, item: String) {
                    }

                    override fun categories(name: String, databaseName: String) {
                        view.select_category.text = name
                        category = databaseName
                    }
                })
        addPhotoBottomDialogFragment.show(
                childFragmentManager,
                "add_photo_dialog_fragment"
        )
    }

    fun reset(view: View) {
        view.et_min.setText("")
        view.et_max.setText("")
        min = ""
        max = ""
    }
}