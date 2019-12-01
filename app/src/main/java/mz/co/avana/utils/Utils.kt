@file:Suppress("NAME_SHADOWING")

package mz.co.avana.utils

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.res.Resources
import android.util.Patterns
import androidx.databinding.BindingAdapter
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.bumptech.glide.Glide
import com.makeramen.roundedimageview.RoundedImageView
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit
import java.util.regex.Matcher


object Utils {
    private val pattern = Patterns.EMAIL_ADDRESS
    private var matcher: Matcher? = null

    @SuppressLint("SimpleDateFormat")
    fun dateToMills(date: String): Long {
        val df = SimpleDateFormat("dd/MM/yyyy")
        var parsedDate = Date()
        try {
            parsedDate = df.parse(date)!!
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        return parsedDate.time
    }

    @SuppressLint("SimpleDateFormat")
    fun toNormalDate(date: Long): String {
        val date = Date(date)
        val format = SimpleDateFormat("dd.MM.yyyy")
        return format.format(date)
    }

    @JvmStatic
    @BindingAdapter("imageUrl")
    fun loadImage(
            view: RoundedImageView,
            url: String
    ) { // This methods should not have any return type, = declaration would make it return that object declaration.
        Glide.with(view.context).load(url).into(view)
    }

    fun loadFragment(fragment: Fragment?, fl: Int, supportFragmentManager: FragmentManager) {
        if (fragment != null) {
            supportFragmentManager
                    .beginTransaction()
                    .replace(fl, fragment)
                    .commit()
        }
    }

    fun writeSharedPreferences(key: String, value: String, name: String, activity: Activity){
        val sharedPref = activity.getSharedPreferences(name, Context.MODE_PRIVATE)
        with(sharedPref.edit()){
            putString(key, value)
            apply()
        }
    }

    fun readPreference(key: String, name: String, activity: Activity):String{
        val sharedPref = activity.getSharedPreferences(name, Context.MODE_PRIVATE)
        return sharedPref.getString(key, "-")!!
    }

    fun getScreenWidth(): Int {
        return Resources.getSystem().displayMetrics.widthPixels
    }

    fun getScreenHeight(): Int {
        return Resources.getSystem().displayMetrics.heightPixels
    }

    fun deferenceBetweenDates(date: Long): Long {

        val currentDate = System.currentTimeMillis()
        return try {

            val difference = currentDate - date
            TimeUnit.DAYS.convert(difference, TimeUnit.MILLISECONDS)
        } catch (e: ParseException) {
            e.printStackTrace()
            0
        }
    }
}