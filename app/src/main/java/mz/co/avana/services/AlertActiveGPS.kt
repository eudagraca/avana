package mz.co.avana.services

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import kotlinx.android.synthetic.main.need_active_resource.view.*
import mz.co.avana.R
import kotlin.system.exitProcess


class AlertActiveGPS : DialogFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val view = inflater.inflate(R.layout.need_active_resource, container, false)
        isCancelable = false

        view.dont_enable_gps.setOnClickListener {
            exitProcess(0)
        }
        view.enable_gps.setOnClickListener {
            context!!.startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
        }


        return view
    }

}