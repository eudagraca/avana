package mz.co.avana.utils

import android.content.Context
import android.view.View
import android.widget.Toast
import com.google.android.material.snackbar.Snackbar
import mz.co.avana.R

object Message {

    fun messageToast(context: Context, message: String) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show()
    }

    fun snackbarMessage(context: Context, view: View, message: String) {
        val snackBar = Snackbar.make(view, message, Snackbar.LENGTH_LONG)
        snackBar.setTextColor(context.resources.getColor(R.color.md_white_1000))
        snackBar.show()
    }

}