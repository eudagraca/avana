package mz.co.avana.presentation.ui.user

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_other_user_profile.*
import mz.co.avana.R

class OtherUserProfileActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_other_user_profile)
        setSupportActionBar(toolbar)


    }

}
