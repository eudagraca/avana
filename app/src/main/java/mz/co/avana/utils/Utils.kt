package mz.co.avana.utils

import android.content.Context
import android.util.Patterns
import android.view.View
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import com.makeramen.roundedimageview.RoundedImageView
import mz.co.avana.R
import java.text.SimpleDateFormat
import java.util.*
import java.util.regex.Matcher


class Utils {

    companion object {

        private val pattern = Patterns.EMAIL_ADDRESS
        private var matcher: Matcher? = null

        fun validateEmail(email: String): Boolean {
            matcher = pattern.matcher(email)
            return matcher!!.matches()
        }

        fun validatePassword(password: String): Boolean {
            return password.length > 6
        }

        fun dateToMills(date: String): Long {
            val df = SimpleDateFormat("yyyy.MM.dd HH:mm")
            return df.parse(date).time
        }

        fun toNormalDate(date: Long): String {
            val date = Date(date)
            val format = SimpleDateFormat("dd.MM.yyyy")
            return format.format(date)
        }

        @JvmStatic
        @BindingAdapter("imageUrl")
        fun loadImage(view: RoundedImageView, url: String) { // This methods should not have any return type, = declaration would make it return that object declaration.
            Glide.with(view.context).load(url).into(view)
        }

        fun showMessage(context: Context, view: View, message: String) {
            val snackBar = Snackbar.make(view, message, Snackbar.LENGTH_LONG)
            snackBar.setTextColor(context.resources.getColor(R.color.md_white_1000))
            snackBar.show()
        }

    }


}