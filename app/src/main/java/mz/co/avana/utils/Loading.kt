package mz.co.avana.utils

import android.content.Context
import android.graphics.Color
import com.developer.kalert.KAlertDialog

object Loading {
    fun loading(context: Context, message: String, contentMessage: String): KAlertDialog{
        val pDialog = KAlertDialog(context, KAlertDialog.PROGRESS_TYPE)
        pDialog.progressHelper.barColor = Color.parseColor("#FF9800")
        pDialog.titleText = message
        pDialog.contentText = contentMessage
        pDialog.setCancelable(false)
        return  pDialog
    }
}