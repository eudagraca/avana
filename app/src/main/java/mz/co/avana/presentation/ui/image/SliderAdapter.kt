package mz.co.avana.presentation.ui.image

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.smarteist.autoimageslider.SliderViewAdapter
import mz.co.avana.R

class SliderAdapter(val context: Context, val images: ArrayList<String>) :
        SliderViewAdapter<SliderAdapter.SliderAdapterVH>() {
    override fun onCreateViewHolder(parent: ViewGroup?): SliderAdapterVH {
        val inflater = LayoutInflater.from(context)
        return SliderAdapterVH(
                inflater.inflate(R.layout.image_loader, parent, false), context
        )
    }

    override fun onBindViewHolder(viewHolder: SliderAdapterVH, position: Int) {
        val image = images[position]
        viewHolder.bindViews(image)
    }

    override fun getCount() = images.size

    class SliderAdapterVH(
            val itemView: View,
            val context: Context
    ) : SliderViewAdapter.ViewHolder(itemView) {

        fun bindViews(image: String) {

            val imageView = itemView.findViewById<ImageView>(R.id.iv_auto_image_slider)

            Glide.with(context)
                    .load(image)
                    .into(imageView)
        }
    }

}