package mz.co.avana.presentation.ui.user

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_user_registation.*
import mz.co.avana.R
import mz.co.avana.model.User
import mz.co.avana.repository.user.UserRepository
import mz.co.avana.utils.MessageCallback

class UserRegistationActivity : AppCompatActivity() {

    var user: User = User()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_registation)

        openLogin.setOnClickListener {
            startActivity(Intent(this@UserRegistationActivity, UserLoginActivity::class.java))
            finish()
        }

        signUp.setOnClickListener {
            getUserInput()
            val register = UserRepository(baseContext, user)
            register.createUserWithEmailPassword(object : MessageCallback {
                override fun onSuccess(successMessage: String) {
                    Toast.makeText(baseContext, successMessage, Toast.LENGTH_SHORT).show()
                }

                override fun onError(errorMessage: String) {
                    Toast.makeText(baseContext, errorMessage, Toast.LENGTH_SHORT).show()
                }
            })
        }

    }

    fun getUserInput() {
        user.email = emailSignup.editText!!.text.toString()
        user.password = passwordSignUp.editText!!.text.toString()
        user.name = nameSigup.editText!!.text.toString()
        user.surname = surnameSignup.editText!!.text.toString()
    }
}


