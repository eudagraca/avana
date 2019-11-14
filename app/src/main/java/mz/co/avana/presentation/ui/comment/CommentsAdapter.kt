package mz.co.avana.presentation.ui.comment

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import mz.co.avana.R
import mz.co.avana.model.Comment

class CommentAdapter(private val commentList: List<Comment>, val context: Context)
    : RecyclerView.Adapter<CommentAdapter.ItemViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {

        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_comments, parent, false)

        return ItemViewHolder(view, context)
    }

    override fun getItemCount() = commentList.size

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val comment = commentList[position]
        holder.bindViews(comment)
    }

    class ItemViewHolder(itemView: View, private val context: Context) : RecyclerView.ViewHolder(itemView) {
        private var name: TextView? = null
        private var image: ImageView? = null
        private var text: TextView? = null
        private var date: TextView? = null

        init {
            name = itemView.findViewById(R.id.tv_name_comment)
            image = itemView.findViewById(R.id.iv_logo)
            text  = itemView.findViewById(R.id.tv_comment_text)
            date  = itemView.findViewById(R.id.tv_date_comment)
        }

        fun bindViews(comment: Comment) {
            text!!.text = comment.text
            date!!.text = comment.date

            Glide.with(context).load("https://miro.medium.com/max/1920/1*pUfkEqu7fdN_khSnach9BQ.png").into(this.image!!)

//            itemView.setOnClickListener {
//                onItemClickListener.invoke(comment)
//            }

        }
    }
}