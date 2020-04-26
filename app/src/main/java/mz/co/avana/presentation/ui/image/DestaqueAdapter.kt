package mz.co.avana.presentation.ui.image


import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.smarteist.autoimageslider.SliderViewAdapter
import mz.co.avana.R
import mz.co.avana.model.Destaque

class DestaqueAdapter(
    val context: Context,
    val images: ArrayList<Destaque>,
    private val itemClickListener: (destaque: Destaque) -> Unit
) :
    SliderViewAdapter<DestaqueAdapter.DestaqueAdapterVH>() {
    override fun onCreateViewHolder(parent: ViewGroup?): DestaqueAdapterVH {
        val inflater = LayoutInflater.from(context)
        return DestaqueAdapterVH(
            inflater.inflate(R.layout.image_loader, parent, false), context,
            itemClickListener
        )
    }

    override fun onBindViewHolder(viewHolder: DestaqueAdapterVH, position: Int) {
        val image = images[position]
        viewHolder.bindViews(image)
    }

    override fun getCount() = images.size

    class DestaqueAdapterVH(
        val itemView: View,
        val context: Context, private val itemClickListener: (destaque: Destaque) -> Unit
    ) : SliderViewAdapter.ViewHolder(itemView) {

        fun bindViews(image: Destaque) {

            val imageView = itemView.findViewById<ImageView>(R.id.iv_auto_image_slider)

            imageView.setOnClickListener {
                itemClickListener.invoke(image)
            }
            Glide.with(context)
                .load(image.image)
                .into(imageView)
        }
    }

}