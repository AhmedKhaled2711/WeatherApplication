package com.example.weatherapplication.alarm.view

import android.annotation.SuppressLint
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
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getSystemService
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherapplication.R
import com.example.weatherapplication.StateDB
import com.example.weatherapplication.StateNotification
import com.example.weatherapplication.alarm.Notification
import com.example.weatherapplication.alarm.recyclerView.AdapterAlerts
import com.example.weatherapplication.alarm.viewModel.AlertsViewModel
import com.example.weatherapplication.alarm.viewModel.AlertsViewModelFactory
import com.example.weatherapplication.channelID
import com.example.weatherapplication.databinding.FragmentAlertBinding
import com.example.weatherapplication.databinding.NotificationLayoutBinding
import com.example.weatherapplication.db.WeatherLocalDataSource
import com.example.weatherapplication.db.WeatherLocalDataSourceImpl
import com.example.weatherapplication.favorite.recyclerView.AdapterFav
import com.example.weatherapplication.favorite.viewModel.FavoriteViewModel
import com.example.weatherapplication.favorite.viewModel.FavoriteViewModelFactory
import com.example.weatherapplication.messageExtra
import com.example.weatherapplication.model.AlertNotification
import com.example.weatherapplication.model.Repository
import com.example.weatherapplication.model.RepositoryImpl
import com.example.weatherapplication.notificationID
import com.example.weatherapplication.remoteDataSource.WeatherRemoteDataSource
import com.example.weatherapplication.remoteDataSource.WeatherRemoteDataSourceImpl
import com.example.weatherapplication.titleExtra
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.util.Calendar
import java.util.Date

class AlertFragment : Fragment() , OnRemoveClickListener{

    private lateinit var binding: FragmentAlertBinding
    lateinit var bindingDialog : NotificationLayoutBinding
    private lateinit var dialog: Dialog
    private lateinit var adapter: AdapterAlerts
    private lateinit var viewModel : AlertsViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentAlertBinding.inflate(inflater, container, false)
        return binding.root
    }
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val remoteDataSource : WeatherRemoteDataSource = WeatherRemoteDataSourceImpl.getInstance()
        val localDataSource : WeatherLocalDataSource =  WeatherLocalDataSourceImpl.getInstance(requireContext())
        val repository: Repository = RepositoryImpl(remoteDataSource ,localDataSource)

        val remoteFactory = AlertsViewModelFactory(repository)

        viewModel = ViewModelProvider(this, remoteFactory).get(AlertsViewModel::class.java)

        createNotificationChannel()

        binding.btnAddNotification.setOnClickListener{
            initDialog()
        }

        setUpRecyclerView()
        initViewModel()

    }

    private fun initDialog() {
        bindingDialog = NotificationLayoutBinding.inflate(LayoutInflater.from(requireContext()))
        dialog = Dialog(requireActivity())
        dialog.setContentView(bindingDialog.root)
        dialog.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        dialog.window?.setBackgroundDrawable(
            ContextCompat.getDrawable(requireActivity(), R.drawable.bg_home)
        )
        dialog.setCancelable(false)

        // Find OK and Cancel buttons in your layout
        val okButton = dialog.findViewById<Button>(R.id.btn_submit)
        val cancelButton = dialog.findViewById<Button>(R.id.btn_cancel)

        // Set click listeners for OK and Cancel buttons
        okButton.setOnClickListener {
            // Handle OK button click
            if (checkNotificationPermissions(requireContext())) {
                scheduleNotification()

            }
            val time = getTime()
            viewModel.insertAlerts(AlertNotification(time))
            dialog.dismiss()
             // Dismiss the dialog when OK is clicked
            // Add any additional logic you want to execute when OK is clicked
        }

        cancelButton.setOnClickListener {
            // Handle Cancel button click
            dialog.dismiss() // Dismiss the dialog when Cancel is clicked
            // Add any additional logic you want to execute when Cancel is clicked
        }

        dialog.show()
    }

    @SuppressLint("ScheduleExactAlarm")
    private fun scheduleNotification() {
        val intent = Intent(requireContext(), Notification::class.java)
        val title = bindingDialog.title.text.toString()
        val message = bindingDialog.message.text.toString()
        val time = getTime()

        intent.putExtra(titleExtra, title)
        intent.putExtra(messageExtra, message)

        val pendingIntent = PendingIntent.getBroadcast(
            requireContext(),
            time.toInt(),
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        val alarmManager = requireContext().getSystemService(Context.ALARM_SERVICE) as AlarmManager

        alarmManager.setExactAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP,
            time,
            pendingIntent
        )

    }

    private fun getTime(): Long {
        val minute = bindingDialog.timePicker.minute
        val hour = bindingDialog.timePicker.hour
        val day = bindingDialog.datePicker.dayOfMonth
        val month = bindingDialog.datePicker.month
        val year = bindingDialog.datePicker.year

        val calendar = Calendar.getInstance()
        calendar.set(year, month, day, hour, minute)

        return calendar.timeInMillis
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel() {
        val name = "Notify Channel"
        val desc = "A Description of the Channel"
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val channel = NotificationChannel(channelID, name, importance)
        channel.description = desc

        val notificationManager = requireContext().getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }

    fun checkNotificationPermissions(context: Context): Boolean {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

            val isEnabled = notificationManager.areNotificationsEnabled()

            if (!isEnabled) {
                val intent = Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS)
                intent.putExtra(Settings.EXTRA_APP_PACKAGE, context.packageName)
                startActivity(intent)

                return false
            }
        } else {
            val areEnabled = NotificationManagerCompat.from(context).areNotificationsEnabled()

            if (!areEnabled) {
                val intent = Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS)
                intent.putExtra(Settings.EXTRA_APP_PACKAGE, context.packageName)
                startActivity(intent)

                return false
            }
        }

        return true
    }

    override fun onRemoveClick(alertNotification: AlertNotification) {
        val builder = android.app.AlertDialog.Builder(requireContext())
        builder.setTitle("Confirm Deletion")
        builder.setMessage("Are you sure you want to remove this  alert?")
        builder.setPositiveButton("Yes") { _, _ ->
            cancelAlarm(alertNotification)
            viewModel.deleteAlert(alertNotification)
            Toast.makeText(requireContext(), "Alert removed", Toast.LENGTH_LONG).show()
        }
        builder.setNegativeButton("No") { _, _ ->
            Toast.makeText(requireContext(), "Deletion cancelled", Toast.LENGTH_LONG).show()
        }
        builder.show()
    }

    fun initViewModel(){
        lifecycleScope.launch {
            viewModel.alertNotifications.collectLatest {result ->
                when(result){
                    is StateNotification.Loading ->{
                        binding.progressBar2.visibility = View.VISIBLE
                        binding.NotificationRV.visibility = View.GONE
                    }

                    is StateNotification.Success ->{
                        binding.progressBar2.visibility = View.GONE
                        binding.NotificationRV.visibility = View.VISIBLE
                        adapter.submitList(result.data)
                    }

                    else ->{
                        binding.progressBar2.visibility = View.GONE
                        Log.i("Error", "Error: ")
                    }
                }

            }
        }

    }

    private fun setUpRecyclerView(){
        var manager = LinearLayoutManager(requireContext())
        manager.orientation = RecyclerView.VERTICAL
        binding.NotificationRV.layoutManager = manager

        adapter = AdapterAlerts(this, requireActivity())
        adapter.submitList(emptyList())
        binding.NotificationRV.adapter = adapter
    }

    private fun cancelAlarm(alertNotification: AlertNotification){
        val intent = Intent(requireContext(), Notification::class.java)
        val pendingIntent = PendingIntent.getBroadcast(
            requireContext(),
            alertNotification.time.toInt(),
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        val alarmManager = requireContext().getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager.cancel(pendingIntent)
    }

}