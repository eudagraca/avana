package mz.co.avana.presentation.ui.item

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import mz.co.avana.R

class NotificationItemDetailsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notification_item_details)

        val itemId = intent.getStringExtra("itemId")

    }
}
