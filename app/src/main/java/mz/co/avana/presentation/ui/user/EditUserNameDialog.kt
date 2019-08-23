package mz.co.avana.presentation.ui.user

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import androidx.fragment.app.DialogFragment
import kotlinx.android.synthetic.main.dialog_edit_user_name.view.*
import mz.co.avana.R
import mz.co.avana.model.User
import mz.co.avana.repository.user.UserRepository
import mz.co.avana.utils.Constants
import mz.co.avana.utils.MessageCallback


class EditUserNameDialog(val messageCallback: MessageCallback) : DialogFragment() {

    val user: User = User()

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        val builder = AlertDialog.Builder(activity!!)
        val layoutInflaterAndroid = LayoutInflater.from(context)
        val mView = layoutInflaterAndroid.inflate(R.layout.dialog_edit_user_name, null)
        val name = arguments!!.getString(Constants.NAMES_OF_USER)
        val surname = arguments!!.getString(Constants.SURNAME)

        mView.edit_name_update.editText!!.setText(name)
        mView.edit_surname_update.editText!!.setText(surname)

        builder.setView(mView)
                .setPositiveButton(R.string.save) { _, _ ->
                    user.name = mView.edit_name_update.editText!!.text.toString()
                    user.surname = mView.edit_surname_update.editText!!.text.toString()
                    val repository = UserRepository(context!!, user)

                    repository.updateNamesOfUser(object : MessageCallback {
                        override fun onSuccess(successMessage: String) {
                            messageCallback.onSuccess(successMessage)
                        }

                        override fun onError(errorMessage: String) {
                            messageCallback.onError(errorMessage)
                        }
                    })
                }
                .setNegativeButton(R.string.cancel) { _, _ ->
                    this@EditUserNameDialog.dialog!!.cancel()
                }
        return builder.create()
    }
}