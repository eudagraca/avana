package mz.co.avana.presentation.ui.item

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import kotlinx.android.synthetic.main.activity_notification_item_details.*
import mz.co.avana.R

class NotificationItemDetailsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notification_item_details)

        val itemId = intent.getStringExtra("itemId")

    }
}
