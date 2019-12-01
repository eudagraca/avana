package mz.co.avana.services

import android.annotation.SuppressLint
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import mz.co.avana.R
import mz.co.avana.presentation.ui.item.NotificationItemDetailsActivity
import mz.co.avana.presentation.ui.main.HomeActivity
import mz.co.avana.utils.Constants

class NotificationMessage: FirebaseMessagingService() {

    private val notificationId = 1001
    private var clickAction: String? = null

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        val body: String?
        val title: String?
        val itemId: String?
        val store: String?
        try {
            itemId = remoteMessage.data["itemId"]
            title = remoteMessage.notification!!.title.toString()
            body  = remoteMessage.notification!!.body.toString()
            store  = remoteMessage.data["store"]
            clickAction = remoteMessage.notification!!.clickAction
            sendBroadcastNotification(title, body, itemId!!, store!!)
        }catch (e: Exception){
            Log.i("FAILEDDD", e.toString())
        }
        super.onMessageReceived(remoteMessage)
    }

    @SuppressLint("DefaultLocale")
    @RequiresApi(Build.VERSION_CODES.M)
    fun sendBroadcastNotification(title: String, message: String, itemId: String, store: String){
        val intent = Intent(this, NotificationItemDetailsActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        intent.putExtra("itemId", itemId)
        val pendingIntent: PendingIntent = PendingIntent.getActivity(this, 0, intent,
            PendingIntent.FLAG_UPDATE_CURRENT)

        val builder = NotificationCompat.Builder(this, Constants.CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_avana_logo_withe)
            .setColor(getColor(R.color.colorPrimary))
            .setContentTitle(getString(R.string.app_name).toUpperCase()+getString(R.string.found_int_the_city, message).toUpperCase())
            .setContentText(getString(R.string.find_this_at, title.capitalize(), store.capitalize()))
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            // Set the intent that will fire when the user taps the notification
            .setContentIntent(pendingIntent)
            .setContentInfo(getString(R.string.app_name))
            .setAutoCancel(true)
            .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
            .setStyle(NotificationCompat.BigTextStyle())
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        builder.setContentIntent(pendingIntent)
        val notificationManager: NotificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationManager.notify(Constants.BROADCAST_NOTIFY_ID, builder.build())
        }

       notificationManager.notify(notificationId, builder.build())
    }
}