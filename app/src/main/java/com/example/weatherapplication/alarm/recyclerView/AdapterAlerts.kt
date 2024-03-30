package com.example.weatherapplication.alarm.recyclerView

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherapplication.databinding.ItemAlertBinding
import com.example.weatherapplication.alarm.view.OnRemoveClickListener
import com.example.weatherapplication.model.AlertNotification
import java.util.Date


class AdapterAlerts  (private val listener: OnRemoveClickListener, private val context: Context?):
    ListAdapter<AlertNotification, AdapterAlerts.MyViewHolder>(ProductDiffUtil())
{
    private lateinit var binding: ItemAlertBinding

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): AdapterAlerts.MyViewHolder {

        val inflater: LayoutInflater =
            parent.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        binding = ItemAlertBinding.inflate(inflater, parent, false)

        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AdapterAlerts.MyViewHolder, position: Int) {
        val current = getItem(position)
        val date = Date(current.time)
        val dateFormat = android.text.format.DateFormat.getLongDateFormat(context)
        val timeFormat = android.text.format.DateFormat.getTimeFormat(context)
        holder.binding.tvTimeNotification.text = dateFormat.format(date) + timeFormat.format(date)

        binding.ivDeleteNotification.setOnClickListener{
            listener.onRemoveClick(current)
        }

    }

    class ProductDiffUtil : DiffUtil.ItemCallback<AlertNotification>(){
        override fun areItemsTheSame(oldItem: AlertNotification, newItem: AlertNotification): Boolean {
            return oldItem.time == newItem.time
        }

        override fun areContentsTheSame(oldItem: AlertNotification, newItem: AlertNotification): Boolean {
            return oldItem == newItem
        }

    }

    inner class MyViewHolder(var binding: ItemAlertBinding) : RecyclerView.ViewHolder(binding.root)

}

