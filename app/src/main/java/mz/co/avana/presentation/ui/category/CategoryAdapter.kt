package mz.co.avana.presentation.ui.category

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import mz.co.avana.R
import mz.co.avana.model.Category

class CategoryAdapter(private val categoryList: List<Category>, val context: Context,
                      private val onItemClickListener: (category: Category) -> Unit)
    : RecyclerView.Adapter<CategoryAdapter.ItemViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {

        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_category, parent, false)

        return ItemViewHolder(view, context, onItemClickListener)
    }

    override fun getItemCount() = categoryList.size

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val categories = categoryList[position]
        holder.bindViews(categories)
    }

    class ItemViewHolder(itemView: View, private val context: Context,
                         private val onItemClickListener: ((categories: Category) -> Unit)) : RecyclerView.ViewHolder(itemView) {
        private var name: TextView? = null
        private var image: ImageView? = null

        init {
            name = itemView.findViewById(R.id.categoryName)
            image = itemView.findViewById(R.id.imageCategory)
        }

        fun bindViews(category: Category) {
            name!!.text = category.name

            Glide.with(context).load("https://miro.medium.com/max/1920/1*pUfkEqu7fdN_khSnach9BQ.png").into(this.image!!)

            itemView.setOnClickListener {
                onItemClickListener.invoke(category)
            }

        }
    }
}