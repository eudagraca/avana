package mz.co.avana.presentation.ui.categorie

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import mz.co.avana.R
import mz.co.avana.model.Categories
import mz.co.avana.utils.Utils


class CategoriesAdapter(private val categoriesList: List<Categories>, val context: Context,
                        private val onItemClickListener: (categories: Categories) -> Unit)
    : RecyclerView.Adapter<CategoriesAdapter.ItemViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {

        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_category, parent, false)

        return ItemViewHolder(view, context, onItemClickListener)
    }

    override fun getItemCount() = categoriesList.size

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val categories = categoriesList[position]
        holder.bindViews(categories)
    }

    class ItemViewHolder(itemView: View, private val context: Context,
                         private val onItemClickListener: ((categories: Categories) -> Unit)) : RecyclerView.ViewHolder(itemView) {
        private var name: TextView? = null
        private var image: ImageView? = null

        init {
            name = itemView.findViewById(R.id.categoryName)
            image = itemView.findViewById(R.id.imageCategory)
        }

        fun bindViews(categories: Categories) {
            name!!.text = categories.name
            Glide.with(context).
                load(context.getDrawable(categories.image!!))
                .apply(
                    RequestOptions.overrideOf(Utils.getScreenWidth()/6
                    , Utils.getScreenHeight()/4))
                .into(this.image!!)

            itemView.setOnClickListener {
                onItemClickListener.invoke(categories)
            }

        }
    }
}