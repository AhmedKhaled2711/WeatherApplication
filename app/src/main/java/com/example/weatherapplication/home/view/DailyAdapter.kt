package com.example.weatherapplication.home.view

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.weatherapplication.databinding.DailyCardBinding
import com.example.weatherapplication.databinding.HourlyCardBinding
import com.example.weatherapplication.getDay
import com.example.weatherapplication.getHourTime
import com.example.weatherapplication.model.Daily
import com.example.weatherapplication.model.Hourly

class DailyAdapter(private var context: Context) :
    ListAdapter <Daily , DailyAdapter.MyViewHolder> (DailyDiffUtil()){
    private lateinit var binding : DailyCardBinding
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DailyAdapter.MyViewHolder {
        val inflater: LayoutInflater =
            parent.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        binding = DailyCardBinding.inflate(inflater, parent, false)
        Log.i("Adapter", "onCreateViewHolder: ")
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DailyAdapter.MyViewHolder, position: Int) {
        val current = getItem(position)
        holder.binding.tvDay.text = getDay(current.dt)
        Glide.with(context).load("https://openweathermap.org/img/wn/"+
                current.weather[0].icon+"@2x.png").into(holder.binding.ivTemperatureIcon)
        holder.binding.tvTemperatureDegree.text = current.weather[0].description
        holder.binding.desc.text = current.temp.max.toInt().toString()+
                "° / "+ current.temp.min.toInt().toString()+"°"
    }

    class DailyDiffUtil : DiffUtil.ItemCallback<Daily>(){
        override fun areItemsTheSame(oldItem: Daily, newItem: Daily): Boolean {
            return oldItem.dt == newItem.dt
        }

        override fun areContentsTheSame(oldItem: Daily, newItem: Daily): Boolean {
            return oldItem == newItem
        }

    }

    inner class MyViewHolder(var binding: DailyCardBinding) : RecyclerView.ViewHolder(binding.root)


}

