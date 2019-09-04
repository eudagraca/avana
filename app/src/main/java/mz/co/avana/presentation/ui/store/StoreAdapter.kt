package mz.co.avana.presentation.ui.store

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.makeramen.roundedimageview.RoundedImageView
import mz.co.avana.R
import mz.co.avana.model.Store

class StoreAdapter(private val storeList: List<Store>, val context: Context,
                   private val onItemClickListener: (store: Store) -> Unit)
    : RecyclerView.Adapter<StoreAdapter.StoreViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StoreViewHolder {

        val view = LayoutInflater.from(parent.context).inflate(R.layout.store_list, parent, false)

        return StoreViewHolder(view, context, onItemClickListener)
    }

    override fun getItemCount() = storeList.size

    override fun onBindViewHolder(holder: StoreViewHolder, position: Int) {
        val store = storeList[position]
        holder.bindViews(store)
    }

    class StoreViewHolder(itemView: View, private val context: Context, private val onItemClickListener: (store: Store) -> Unit)
        : RecyclerView.ViewHolder(itemView) {
        private var name: TextView? = null
        private var image: RoundedImageView? = null
        init {
            name = itemView.findViewById(R.id.store_name)
            image = itemView.findViewById(R.id.img_store)
        }


        fun bindViews(store: Store) {

            name!!.text = store.name
            Glide.with(context).load("https://i.ytimg.com/vi/p-qjjhOoAjo/maxresdefault.jpg").into(this.image!!)


            itemView.setOnClickListener {
                onItemClickListener.invoke(store)
            }

        }

    }
}