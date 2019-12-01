package mz.co.avana.presentation.ui.user

import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity;
import mz.co.avana.R

import kotlinx.android.synthetic.main.activity_other_user_profile.*

class OtherUserProfileActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_other_user_profile)
        setSupportActionBar(toolbar)


    }

}
