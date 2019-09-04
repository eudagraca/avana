package mz.co.avana.presentation.ui.item

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import kotlinx.android.synthetic.main.fragment_itens.view.*
import mz.co.avana.R
import mz.co.avana.utils.Constants
import mz.co.avana.viewModel.item.ItemViewModel


class LowersFragment : Fragment() {
    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_itens, container, false)
        val itemViewModel = ViewModelProviders.of(this).get(ItemViewModel::class.java)

        itemViewModel.itemLiveDataList.observe(this, Observer { items ->
            with(view.rv_itens, {
                layoutManager = GridLayoutManager(context, 2)
                setHasFixedSize(true)
                adapter = ItemAdapter(items, context!!) { item ->
                    val intent = Intent(activity, ItemDetailsActivity::class.java)
                    intent.putExtra(Constants.ITEM, item)
                    startActivity(intent)
                }
            })
        })
        itemViewModel.itensLowers()
        return view
    }
}