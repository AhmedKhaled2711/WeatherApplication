package com.example.weatherapplication.home.recyclerView

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.weatherapplication.databinding.HourlyCardBinding
import com.example.weatherapplication.getHourTime
import com.example.weatherapplication.model.Hourly

class HourlyAdapter(private var context: Context) :
    ListAdapter <Hourly , HourlyAdapter.MyViewHolder> (HourlyDiffUtil()){
    lateinit var binding : HourlyCardBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val inflater: LayoutInflater =
            parent.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        binding = HourlyCardBinding.inflate(inflater, parent, false)
        Log.i("Adapter", "onCreateViewHolder: ")
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val current = getItem(position)
        holder.binding.tvTime.text = getHourTime(current.dt)
        Glide.with(context).load("https://openweathermap.org/img/wn/"+
                current.weather[0].icon+"@2x.png")
            .into(holder.binding.ivTemperatureIcon)
        holder.binding.tvTemperatureDegree.text = current.temp.toString()

    }



    class HourlyDiffUtil : DiffUtil.ItemCallback<Hourly>(){
        override fun areItemsTheSame(oldItem: Hourly, newItem: Hourly): Boolean {
            return oldItem.dt == newItem.dt
        }

        override fun areContentsTheSame(oldItem: Hourly, newItem: Hourly): Boolean {
            return oldItem == newItem
        }

    }

    inner class MyViewHolder(var binding: HourlyCardBinding) : RecyclerView.ViewHolder(binding.root)


}

