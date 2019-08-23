package mz.co.avana.presentation.ui.main

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import mz.co.avana.R
import mz.co.avana.presentation.ui.user.UserLoginActivity


class SplashScreenActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)
    }

    override fun onResume() {
        super.onResume()
        handler()
    }

    private fun handler() {
        val handle = Handler()
        handle.postDelayed(this::showLogin, 3000)
    }

    private fun showLogin() {
        val intent = Intent(this, UserLoginActivity::class.java)
        startActivity(intent)
        finish()
    }
}
