package mz.co.avana.presentation.ui.user

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_user_login.*
import mz.co.avana.R
import mz.co.avana.model.User
import mz.co.avana.presentation.ui.main.HomeActivity
import mz.co.avana.repository.user.UserRepository
import mz.co.avana.utils.MessageCallback

class UserLoginActivity : AppCompatActivity() {

    val user: User = User()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_login)

        openRegister.setOnClickListener {
            startActivity(Intent(this@UserLoginActivity, UserRegistationActivity::class.java))
            finish()
        }

        signIn.setOnClickListener {
            getInput()
            val login = UserRepository(baseContext, user)
            login.signInWithEmailAndPassword(object : MessageCallback {
                override fun onSuccess(successMessage: String) {
                    Toast.makeText(baseContext, successMessage, Toast.LENGTH_SHORT).show()
                }

                override fun onError(errorMessage: String) {
                    Toast.makeText(baseContext, errorMessage, Toast.LENGTH_SHORT).show()
                }
            })
        }
    }

    fun getInput() {
        user.email = emilSignIn.editText!!.text.toString()
        user.password = passwordSignIn.editText!!.text.toString()
    }

    override fun onStart() {
        super.onStart()
        if (UserRepository.isLogged()) {
            startActivity(Intent(this, HomeActivity::class.java))
            finish()
        }
    }
}
