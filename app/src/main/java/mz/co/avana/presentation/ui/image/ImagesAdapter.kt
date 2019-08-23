package mz.co.avana.presentation.ui.image

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import mz.co.avana.R
import mz.co.avana.model.Images


class ImagesAdapter(private val context: Context, val imagesFiles: ArrayList<Images>,
                    private val onItemClickListener: (images: Images) -> Unit) : RecyclerView.Adapter<ImagesAdapter.ViewHolder>() {


    override fun getItemCount() = imagesFiles.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(context)
        return ViewHolder(
                inflater.inflate(R.layout.list_pictures, parent, false), context, onItemClickListener
        )
    }

    @SuppressLint("CheckResult")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val images = imagesFiles[position]
        holder.bindViews(images)
    }

    class ViewHolder(
            itemView: View,
            val context: Context,
            val onItemClickListener: (images: Images) -> Unit
    ) : RecyclerView.ViewHolder(itemView) {

        var imageOne: ImageView? = null
        var imageThree: ImageView? = null
        var imageTwo: ImageView? = null

        init {
            imageOne = itemView.findViewById(R.id.imgOne)
            imageTwo = itemView.findViewById(R.id.imgTwo)
            imageThree = itemView.findViewById(R.id.imgThree)
        }

        fun bindViews(image: Images) {
            Glide.with(context).load(image.imageOne).into(imageOne!!)
            Glide.with(context).load(image.imageTwo).into(imageTwo!!)
            Glide.with(context).load(image.imageThree).into(imageThree!!)

            itemView.setOnClickListener {
                onItemClickListener.invoke(image)
            }
        }
    }
}