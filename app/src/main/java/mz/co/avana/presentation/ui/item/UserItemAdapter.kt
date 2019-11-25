package mz.co.avana.presentation.ui.item

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.developer.kalert.KAlertDialog
import com.google.android.material.button.MaterialButton
import mz.co.avana.R
import mz.co.avana.callbacks.MessageCallback
import mz.co.avana.model.Item
import mz.co.avana.repository.item.ItemRepository
import mz.co.avana.utils.Constants
import mz.co.avana.utils.Message
import mz.co.avana.utils.Utils

class UserItemAdapter(
        private val itemList: List<Item>, val context: Context,
        private val onItemClickListener: (item: Item) -> Unit
) : RecyclerView.Adapter<UserItemAdapter.ItemViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {

        val view = LayoutInflater.from(parent.context).inflate(R.layout.my_items, parent, false)

        return ItemViewHolder(view, context, onItemClickListener)
    }

    override fun getItemCount() = itemList.size

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val items = itemList[position]
        holder.bindViews(items)
    }

    class ItemViewHolder(
            itemView: View,
            private val context: Context,
            private val onItemClickListener: (item: Item) -> Unit
    ) : RecyclerView.ViewHolder(itemView) {
//        private var name: TextView? = null
//        private var price: TextView? = null
        private var image: ImageView? = null
//        private var endAt: TextView? = null
//        private var createdAt: TextView? = null
//        private var delete: MaterialButton? = null
//        private var update: MaterialButton? = null

        init {
//            name = itemView.findViewById(R.id.tvNome)
//            price = itemView.findViewById(R.id.tvPrice)
            image = itemView.findViewById(R.id.ivItemUser)
//            endAt = itemView.findViewById(R.id.endOfPromo)
//            createdAt = itemView.findViewById(R.id.hourText)
//            delete = itemView.findViewById(R.id.btn_delete)
//            update = itemView.findViewById(R.id.btn_update)
        }

        fun bindViews(item: Item) {
//            name!!.text = item.name.toUpperCase()
//            endAt!!.text = String.format(
//                    context.resources.getString(
//                            R.string.end_at
//                    ), Utils.toNormalDate(item.date)
//            )
//            createdAt!!.text = String.format(
//                    context.resources.getString(
//                            R.string.publication_time
//                    ), Utils.toNormalDate(item.createdAt)
//            )
//            price!!.text = String.format(context.resources.getString(R.string.show_new_price), item.currentPrice)
            Glide.with(context).load(item.images!![0])
                .transform(CenterCrop(), RoundedCorners(30))
                .into(this.image!!)
            itemView.setOnClickListener {
                onItemClickListener.invoke(item)
            }
//            val itemRepository = ItemRepository(context)
//
//            delete!!.setOnClickListener {
//                KAlertDialog(context, KAlertDialog.WARNING_TYPE)
//                        .setTitleText(context.getString(R.string.are_you_sure))
//                        .setContentText(context.getString(R.string.wont_be_able_to_recover_this_file))
//                        .setConfirmText(context.getString(R.string.yes_delete_it))
//                        .setConfirmClickListener {
//                            it.dismissWithAnimation()
//                            Message.messageToast(context, "Deleting ${item.name}")
//                            itemRepository.deleteItem(item.itemId!!, object : MessageCallback {
//                                override fun onSuccess(successMessage: String) {
//                                    Message.messageToast(context, successMessage)
//                                }
//
//                                override fun onError(errorMessage: String) {
//                                    it.dismissWithAnimation()
//                                    Message.messageToast(context, errorMessage)
//                                }
//                            })
//                        }
//                        .cancelButtonColor(R.drawable.alert_button_cancel)
//                        .setCancelText(context.getString(R.string.no_cancel))
//                        .setCancelClickListener { sDialog ->
//                            sDialog.dismissWithAnimation()
//                        }.show()
//            }
//
//            update!!.setOnClickListener {
//                KAlertDialog(context, KAlertDialog.WARNING_TYPE)
//                        .setTitleText(context.getString(R.string.are_you_sure))
//                        .setContentText(context.getString(R.string.edit_this_item))
//                        .confirmButtonColor(R.drawable.alert_button_positive)
//                        .setConfirmText(context.getString(R.string.yes_delete_it))
//                        .cancelButtonColor(R.drawable.alert_button_cancel)
//                        .setConfirmText(context.getString(R.string.yes_change_it))
//                        .setCancelText(context.getString(R.string.no_cancel))
//                        .setConfirmClickListener { sDialog ->
//                            sDialog.dismissWithAnimation()
//
//                            val intent = Intent(context, UpdateItemActivity::class.java)
//                            intent.putExtra(Constants.ITEM, item)
//                            intent.putExtra("fragment", "profile")
//                            context.startActivity(intent)
//                        }
//                        .setCancelClickListener { sDialog ->
//                            sDialog.dismissWithAnimation()
//                        }.show()
//            }
        }
    }
}