package mz.co.avana.presentation.ui.dialog

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.LinearLayout
import androidx.fragment.app.DialogFragment
import kotlinx.android.synthetic.main.dialog_alert_login.view.*
import mz.co.avana.R
import mz.co.avana.presentation.ui.user.UserLoginActivity

class CustomDialogAlertLogin() : DialogFragment() {

    @SuppressLint("InflateParams")
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        val builder = Dialog(activity!!)
        val layoutInflaterAndroid = LayoutInflater.from(context)
        val mView = layoutInflaterAndroid.inflate(R.layout.dialog_alert_login, null)
        builder.setContentView(mView)
        builder.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        builder.window?.setLayout(
            (resources.displayMetrics.widthPixels * 0.90).toInt(),
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        mView.btn_cancel.setOnClickListener {
            builder.dismiss()
        }
        mView.btn_login.setOnClickListener {
            builder.dismiss()
            startActivity(Intent(context, UserLoginActivity::class.java))
        }
        return builder
    }
}