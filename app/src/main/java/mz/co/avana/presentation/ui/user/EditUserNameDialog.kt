package mz.co.avana.presentation.ui.user

import android.annotation.SuppressLint
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import androidx.fragment.app.DialogFragment
import kotlinx.android.synthetic.main.dialog_edit_user_name.view.*
import mz.co.avana.R
import mz.co.avana.callbacks.MessageCallback
import mz.co.avana.model.User
import mz.co.avana.repository.user.UserRepository
import mz.co.avana.utils.Constants
import mz.co.avana.utils.Utils
import mz.co.avana.utils.Validator.Companion.validate


class EditUserNameDialog(val messageCallback: MessageCallback) : DialogFragment() {

    val user: User = User()

    @SuppressLint("InflateParams")
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        val builder = Dialog(activity!!)
        val layoutInflaterAndroid = LayoutInflater.from(context)
        val mView = layoutInflaterAndroid.inflate(R.layout.dialog_edit_user_name, null)
        builder.setContentView(mView)
        builder.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        builder.window?.setLayout(
            (resources.displayMetrics.widthPixels * 0.80).toInt(),
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        val name = Utils.readPreference(
            Constants.NAMES_OF_USER,
            Constants.NAMES_OF_USER,
            activity!!
        )

        val surname = Utils.readPreference(
            Constants.SURNAME,
            Constants.SURNAME,
            activity!!
        )

        mView.edit_name_update.editText!!.setText(name)
        mView.edit_surname_update.editText!!.setText(surname)

        mView.btn_update_names.setOnClickListener {

            if (getInput(mView)) {
                if (builder.isShowing) {
                    builder.dismiss()
                }
                val repository = UserRepository(context!!, user, activity!!)

                repository.updateNamesOfUser(object : MessageCallback {
                    override fun onSuccess(successMessage: String) {
                        messageCallback.onSuccess(successMessage)
                    }

                    override fun onError(errorMessage: String) {
                        messageCallback.onError(errorMessage)
                    }
                })
            }
        }
        mView.btn_update_names_cancel.setOnClickListener {
            builder.cancel()
        }

        builder.show()

        return builder
    }

    private fun getInput(mView: View): Boolean {
        var isValid = false
        when {
            validate(mView.edit_name_update) -> {
                mView.edit_name_update.requestFocus()
                mView.edit_name_update.error = getString(R.string.names_required)
            }
            validate(mView.edit_surname_update) -> {
                mView.edit_name_update.clearFocus()
                mView.edit_name_update.isErrorEnabled = false
                mView.edit_surname_update.error = getString(R.string.surnames_required)
                mView.edit_surname_update.requestFocus()
            }
            else -> {
                user.name = mView.edit_name_update.editText!!.text.toString()
                user.surname = mView.edit_surname_update.editText!!.text.toString()
                mView.edit_surname_update.clearFocus()
                mView.edit_surname_update.isErrorEnabled = false
                isValid = true
            }
        }
        return isValid
    }
}