package mz.co.avana.presentation.ui.dialog

import android.annotation.SuppressLint
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.LinearLayout
import androidx.fragment.app.DialogFragment
import kotlinx.android.synthetic.main.dialog.view.*
import mz.co.avana.R

class CustomDialog(val primay: String, val secondary: String) : DialogFragment(){

    @SuppressLint("InflateParams")
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        val builder = Dialog(activity!!)
        val layoutInflaterAndroid = LayoutInflater.from(context)
        val mView = layoutInflaterAndroid.inflate(R.layout.dialog, null)
        builder.setContentView(mView)
        builder.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        builder.window?.setLayout((resources.displayMetrics.widthPixels*0.80).toInt(), LinearLayout.LayoutParams.WRAP_CONTENT)
        mView.tvPrimaryInfo.text = primay
        mView.tvSecondaryInfo.text = secondary
        return builder
    }
}