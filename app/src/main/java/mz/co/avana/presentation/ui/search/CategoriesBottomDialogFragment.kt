package mz.co.avana.presentation.ui.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.android.synthetic.main.bottom_sheet_categories.view.*
import mz.co.avana.R
import mz.co.avana.callbacks.ItemPriceCallback
import mz.co.avana.presentation.ui.categorie.CategoriesAdapter
import mz.co.avana.presentation.ui.categorie.CategoriesList

class CategoriesBottomDialogFragment() : BottomSheetDialogFragment() {
    var priceCallback: ItemPriceCallback? = null

    constructor(priceCallback: ItemPriceCallback) : this() {
        this.priceCallback = priceCallback
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.bottom_sheet_categories, container, false)
        with(view.rv_Categories) {
            layoutManager = GridLayoutManager(context, 3)
            setHasFixedSize(true)
            adapter = CategoriesAdapter(CategoriesList.categories(context), context) { categories ->
                priceCallback!!.categories(categories.name, categories.nameOnDatabase)
                dismiss()
            }
        }

        view.mbt_close.setOnClickListener {
            dismissAllowingStateLoss()
        }
        return view
    }
}