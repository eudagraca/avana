package mz.co.avana.presentation.ui.comment

import android.content.Context
import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import mz.co.avana.R
import mz.co.avana.model.Comment
import mz.co.avana.utils.Utils

class CommentsAdapter(private val commentList: List<Comment>, val context: Context) :
    RecyclerView.Adapter<CommentsAdapter.ItemViewHolder>() {
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
        private var text: TextView? = null
        private var date: TextView? = null

        init {
            name = itemView.findViewById(R.id.tv_name_comment)
            text = itemView.findViewById(R.id.tv_comment_text)
            date = itemView.findViewById(R.id.tv_date_comment)
            date!!.setTypeface(Typeface.SANS_SERIF, Typeface.ITALIC)
        }

        fun bindViews(comment: Comment) {
            text!!.text = comment.text
            date!!.text = context.getString(R.string.days_ago, Utils.deferenceBetweenDates(comment.date))
            name!!.text = comment.user

//            itemView.setOnClickListener {
//                onItemClickListener.invoke(comment)
//            }
        }
    }
}