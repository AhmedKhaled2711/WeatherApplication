package com.example.weatherapplication.alarm.view

import android.app.AlarmManager
import android.app.Dialog
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.weatherapplication.R
import com.example.weatherapplication.alarm.Notification
import com.example.weatherapplication.channelID
import com.example.weatherapplication.databinding.FragmentAlertBinding
import com.example.weatherapplication.notificationID
import java.util.Calendar
import java.util.Date


class AlertFragment : Fragment() {

    private lateinit var binding: FragmentAlertBinding
    private lateinit var dialog: Dialog

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentAlertBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnAddNotification.setOnClickListener{
            initDialog()
            dialog.show()
        }
    }




    private fun initDialog(){
        dialog= Dialog(requireActivity())
        dialog.setContentView(R.layout.notification_layout)
        dialog.window?.setLayout(500, 500)
        dialog.window?.setBackgroundDrawable(
            ContextCompat.getDrawable(requireActivity(), R.drawable.bg_home_six)
        )
        dialog.setCancelable(false)
    }






}