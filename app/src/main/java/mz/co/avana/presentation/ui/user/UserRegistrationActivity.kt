package mz.co.avana.presentation.ui.user

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Window
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_user_registation.*
import mz.co.avana.R
import mz.co.avana.callbacks.MessageCallback
import mz.co.avana.model.User
import mz.co.avana.presentation.ui.dialog.CustomDialog
import mz.co.avana.repository.user.UserRepository
import mz.co.avana.utils.Message
import mz.co.avana.utils.Validator.Companion.validate
import mz.co.avana.utils.Validator.Companion.validateEmail
import mz.co.avana.utils.Validator.Companion.validateLength
import mz.co.avana.utils.Validator.Companion.validatePassword

class UserRegistrationActivity : AppCompatActivity() {

    var user: User = User()
    var load: CustomDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_registation)

        load = CustomDialog(getString(R.string.registering), getString(R.string.please_wait))
        load!!.isCancelable = false

        openLogin.setOnClickListener {
            startActivity(Intent(this@UserRegistrationActivity, UserLoginActivity::class.java))
            finish()
        }

        signUp.setOnClickListener {
            if (getUserInput()) {

                load!!.show(supportFragmentManager, "missiles")
                val register = UserRepository(baseContext, user, this@UserRegistrationActivity)
                register.createUserWithEmailPassword(object : MessageCallback {
                    override fun onSuccess(successMessage: String) {
                        clearInputs()
                        Message.snackbarMessage(baseContext, rl_user_reg, successMessage)
                        load!!.dismissAllowingStateLoss()
                        val dialog = Dialog(this@UserRegistrationActivity)
                        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
                        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                        dialog.setContentView(R.layout.open_gallery)
                        val  text =  dialog.findViewById<TextView>(R.id.tv_text)
                        text.text = getString(R.string.email_inbox)
                        dialog.window?.setLayout((resources.displayMetrics.widthPixels*0.90).toInt(), LinearLayout.LayoutParams.WRAP_CONTENT)
                        val okButton = dialog.findViewById<TextView>(R.id.openGalley)
                        okButton.text = getString(R.string.dialog_ok)
                        okButton.setOnClickListener {
                            openLogin()
                        }
                        dialog.show()
                    }

                    override fun onError(errorMessage: String) {
                        Message.snackbarMessage(baseContext, rl_user_reg, errorMessage)
                        load!!.dismissAllowingStateLoss()
                    }
                })
            }
        }
    }

    private fun getUserInput(): Boolean {
        var isValid = false
        when {
            validate(nameSigup) -> {
                nameSigup.error = getString(R.string.names_required)
                nameSigup.requestFocus()
            }
            validateLength(nameSigup) -> {
                nameSigup.error = getString(R.string.invalid_name)
                nameSigup.requestFocus()
            }
            validate(surnameSignup) -> {
                nameSigup.isErrorEnabled = false
                nameSigup.clearFocus()
                surnameSignup.error = getString(R.string.surnames_required)
                surnameSignup.requestFocus()
            }  validateLength(surnameSignup) -> {
                nameSigup.isErrorEnabled = false
                nameSigup.clearFocus()
                surnameSignup.error = getString(R.string.invalid_surname)
                surnameSignup.requestFocus()
            }
            validate(emailSignup) -> {
                surnameSignup.clearFocus()
                surnameSignup.isErrorEnabled = false
                emailSignup.error = getString(R.string.emails_required)
                emailSignup.requestFocus()
            }
            !validateEmail(emailSignup.editText!!.text.toString()) -> {
                emailSignup.error = getString(R.string.invalid_email)
                emailSignup.requestFocus()
            }
            validate(passwordSignUp) -> {
                emailSignup.isErrorEnabled = false
                emailSignup.clearFocus()
                passwordSignUp.error = getString(R.string.passwords_required)
                passwordSignUp.requestFocus()
            }
            validatePassword(passwordSignUp) -> {
                emailSignup.isErrorEnabled = false
                emailSignup.clearFocus()
                passwordSignUp.error = getString(R.string.passwords_short)
                passwordSignUp.requestFocus()
            }
            else -> {
                passwordSignUp.clearFocus()
                passwordSignUp.isErrorEnabled = false
                user.password = passwordSignUp.editText!!.text.toString()
                user.name = nameSigup.editText!!.text.toString()
                user.surname = surnameSignup.editText!!.text.toString()
                user.email = emailSignup.editText!!.text.toString()
                isValid = true
            }
        }
        return isValid
    }

    private fun clearInputs(){
        nameSigup.editText!!.text = null
        surnameSignup.editText!!.text = null
        passwordSignUp.editText!!.text = null
        emailSignup.editText!!.text = null
    }
    private fun openLogin(){
        if (UserRepository.isLogged()){
            UserRepository.logOut()
        }
        startActivity(Intent(this, UserLoginActivity::class.java))
        finish()
    }

}


