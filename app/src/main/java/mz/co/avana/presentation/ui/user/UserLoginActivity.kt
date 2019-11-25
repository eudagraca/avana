package mz.co.avana.presentation.ui.user

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.iid.FirebaseInstanceId
import kotlinx.android.synthetic.main.activity_user_login.*
import mz.co.avana.R
import mz.co.avana.callbacks.MessageCallback
import mz.co.avana.callbacks.UserTokenCallback
import mz.co.avana.model.User
import mz.co.avana.presentation.ui.dialog.CustomDialog
import mz.co.avana.presentation.ui.main.HomeActivity
import mz.co.avana.repository.user.UserRepository
import mz.co.avana.utils.Message
import mz.co.avana.utils.Validator.Companion.validate


class UserLoginActivity : AppCompatActivity() {

    val user: User = User()
    var loader: CustomDialog? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_login)


        loader = CustomDialog(getString(R.string.logging), getString(R.string.please_wait))
        loader!!.isCancelable = false
        loader!!.show(supportFragmentManager, "missiles")
        loader!!.dismissAllowingStateLoss()
        openRegister.setOnClickListener {
            startActivity(Intent(this@UserLoginActivity, UserRegistrationActivity::class.java))
            finish()
        }

        forgotPassword.setOnClickListener {
            startActivity(Intent(this, PasswordRecoverActivity::class.java))
            finish()
        }

        signIn.setOnClickListener {
            if (getInput()) {

                loader!!.show(supportFragmentManager, "missiles")
                buildUserToken(object : UserTokenCallback {
                    override fun token(token: String) {
                        user.token = token

                        val login = UserRepository(baseContext, user, this@UserLoginActivity)
                        login.signInWithEmailAndPassword(object : MessageCallback {
                            override fun onSuccess(successMessage: String) {
                                login.setToken(object : MessageCallback {
                                    override fun onSuccess(successMessage: String) {
                                        loader!!.dismissAllowingStateLoss()
                                        val intent = Intent(baseContext, HomeActivity::class.java)
                                        startActivity(intent)
                                        finish()
                                    }

                                    override fun onError(errorMessage: String) {
                                        loader!!.dismissAllowingStateLoss()
                                        Message.snackbarMessage(baseContext, rl_user_login, errorMessage)
                                    }
                                })
                            }

                            override fun onError(errorMessage: String) {
                                loader!!.dismissAllowingStateLoss()
                                Message.snackbarMessage(baseContext, rl_user_login, errorMessage)
                            }
                        })
                    }
                })
            }
        }
    }

    private fun getInput(): Boolean {
        var isValid = false
        when {
            validate(emilSignIn) -> {
                emilSignIn.requestFocus()
                emilSignIn.error = getString(R.string.enter_your_email)
            }
            validate(emilSignIn) -> {
                emilSignIn.requestFocus()
                emilSignIn.error = getString(R.string.enter_valid_email)
            }
            validate(passwordSignIn) -> {
                emilSignIn.clearFocus()
                emilSignIn.isErrorEnabled = false
                passwordSignIn.error = getString(R.string.enter_your_password)
                passwordSignIn.requestFocus()
            }
            else -> {
                user.email = emilSignIn.editText!!.text.toString()
                user.password = passwordSignIn.editText!!.text.toString()
                passwordSignIn.clearFocus()
                passwordSignIn.isErrorEnabled = false
                isValid = true
            }
        }
        return isValid
    }

    override fun onStart() {
        super.onStart()
        if (UserRepository.isLogged()) {
            startActivity(Intent(this, HomeActivity::class.java))
            finish()
        }
    }

    private fun buildUserToken(userTokenCallback: UserTokenCallback) {

        FirebaseInstanceId.getInstance().instanceId
            .addOnSuccessListener { instanceIdResult ->
                val newToken = instanceIdResult.token
                userTokenCallback.token(newToken)
            }
    }

    override fun onPause() {
        super.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        if (loader!!.isVisible){
            loader!!.dismissAllowingStateLoss()
        }
    }

}
