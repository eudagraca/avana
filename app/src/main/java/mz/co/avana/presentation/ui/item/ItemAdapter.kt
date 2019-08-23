package mz.co.avana.presentation.ui.item

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.chip.Chip
import mz.co.avana.R
import mz.co.avana.model.Item

class ItemAdapter(private val itemList: List<Item>, val context: Context,
                  private val onItemClickListener: (item: Item) -> Unit)
    : RecyclerView.Adapter<ItemAdapter.ItemViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {

        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_of_ltens, parent, false)

        return ItemViewHolder(view, context, onItemClickListener)
    }

    override fun getItemCount() = itemList.size

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val items = itemList[position]
        holder.bindViews(items)
    }

    class ItemViewHolder(itemView: View, private val context: Context, private val onItemClickListener: (item: Item) -> Unit)
        : RecyclerView.ViewHolder(itemView) {
        private var name: TextView? = null
        private var image: ImageView? = null
        private var price: TextView? = null
        private var likes: TextView? = null
        private var location: Chip? = null

        init {
            name = itemView.findViewById(R.id.tvNome)
            price = itemView.findViewById(R.id.tvPrice)
            likes = itemView.findViewById(R.id.tvLikes)
            location = itemView.findViewById(R.id.tvLocation)
            image = itemView.findViewById(R.id.ivItem)
        }


        fun bindViews(item: Item) {

            name!!.text = item.name
            price!!.text = String.format(context.resources.getString(R.string.show_new_price), item.currentPrice)
            Glide.with(context).load(item.images[0]).into(this.image!!)
            location!!.text = item.location

            itemView.setOnClickListener {
                onItemClickListener.invoke(item)
            }

            likes!!.setOnClickListener {
                Toast.makeText(context, item.name, Toast.LENGTH_SHORT).show()
            }
        }

    }
}