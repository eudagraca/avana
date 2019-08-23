package mz.co.avana.presentation.ui.user

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import androidx.fragment.app.DialogFragment
import kotlinx.android.synthetic.main.dialog_edit_location.view.*
import mz.co.avana.R
import mz.co.avana.model.User
import mz.co.avana.repository.user.UserRepository
import mz.co.avana.utils.Constants
import mz.co.avana.utils.MessageCallback


class EditLocationDialog(val messageCallback: MessageCallback) : DialogFragment() {

    val user: User = User()

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(activity)
        val layoutInflaterAndroid = LayoutInflater.from(context)
        val mView = layoutInflaterAndroid.inflate(R.layout.dialog_edit_location, null)
        val location = arguments!!.getString(Constants.LOCATION)

        mView.edit_location_update.editText!!.setText(location)

        builder.setView(mView)
                .setPositiveButton(mz.co.avana.R.string.save) { _, _ ->

                    user.location = mView.edit_location_update.editText!!.text.toString()
                    val repository = UserRepository(context!!, user)

                    repository.updateLocation(object : MessageCallback {
                        override fun onSuccess(successMessage: String) {
                            messageCallback.onSuccess(successMessage)
                        }

                        override fun onError(errorMessage: String) {
                            messageCallback.onError(errorMessage)
                        }
                    })

                }
                .setNegativeButton(mz.co.avana.R.string.cancel) { _, _ ->

                    this@EditLocationDialog.dialog!!.cancel()
                }
        return builder.create()

    }
}