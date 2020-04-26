package mz.co.avana.services

import android.annotation.SuppressLint
import android.content.Context
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.os.AsyncTask
import android.text.TextUtils
import android.util.Log
import mz.co.avana.R
import mz.co.avana.utils.Message
import java.io.IOException
import java.util.*
import kotlin.collections.ArrayList

class FetchAddressTask() : AsyncTask<Location, Void, String>() {

    @SuppressLint("StaticFieldLeak")
    private lateinit var mContext: Context
    private lateinit var mListener: OnTaskCompleted

    constructor(applicationContext: Context, listener: OnTaskCompleted) : this() {
        mContext = applicationContext
        mListener = listener
    }

    override fun doInBackground(vararg params: Location): String {
        // Set up the geoCoder
        val geoCoder = Geocoder(
            mContext,
            Locale.getDefault()
        )

        // Get the passed in location
        val location = params[0]
        var addresses: List<Address>? = null
        var resultMessage = ""

        try {
            Log.i("LOCALIZACAO", location.latitude.toString())
            if (params.isNotEmpty()) {
                addresses = geoCoder.getFromLocation(
                    location.latitude,
                    location.longitude,
                    // Get a single address
                    2
                )
            }

        } catch (ioException: IOException) {
            // Catch network or other I/O problems
            resultMessage = ""
        } catch (illegalArgumentException: IllegalArgumentException) {
            // Catch invalid latitude or longitude values
            resultMessage = ""
            Message.messageToast(
                mContext,
                mContext.resources.getString(R.string.location_not_found)
            )
        }

        // If no addresses found, print an error message.
        if (addresses == null || addresses.isEmpty()) {
            if (resultMessage.isEmpty()) {
                resultMessage = ""
            }
        } else {
            // If an address is found, read it into resultMessage
            val address = addresses[0]
            val addressParts = ArrayList<String>()

            // Fetch the address lines using getAddressLine,
            // join them, and send them to the thread
            for (i in 0..address.maxAddressLineIndex) {
                addressParts.add(address.locality)
            }
            resultMessage = TextUtils.join(
                "\n",
                addressParts
            )
        }
        return resultMessage
    }

    /**
     * Called once the background thread is finished and updates the
     * UI with the result.
     * @param address The resulting reverse geoCoded address, or error
     * message if the task failed.
     */
    override fun onPostExecute(address: String) {
        mListener.onTaskCompleted(address)
        super.onPostExecute(address)
    }

    interface OnTaskCompleted {
        fun onTaskCompleted(result: String)
    }
}