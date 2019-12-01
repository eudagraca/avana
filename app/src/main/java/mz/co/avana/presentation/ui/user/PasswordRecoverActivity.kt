package mz.co.avana.presentation.ui.user

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_password_recover.*
import mz.co.avana.R
import mz.co.avana.utils.Validator
import mz.co.avana.utils.Validator.Companion.validateEmail

class PasswordRecoverActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_password_recover)

        openLogin.setOnClickListener {
            startActivity(Intent(this, UserLoginActivity::class.java))
            finish()
        }

        recoverPassword.setOnClickListener {
            val email = emailRecover.editText!!.text.toString().trim()
            if (Validator.validate(emailRecover)) {
                emailRecover.error = resources.getString(R.string.emails_required)
                emailRecover.requestFocus()
            } else if (!validateEmail(email)) {
                emailRecover.error = resources.getString(R.string.invalid_email)
                emailRecover.requestFocus()

            }

        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        val intent = Intent(Intent.ACTION_MAIN)
        intent.addCategory(Intent.CATEGORY_HOME)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
    }
}
