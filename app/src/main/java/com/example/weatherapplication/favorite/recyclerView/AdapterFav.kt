package com.example.weatherapplication.favorite.recyclerView

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.weatherapplication.databinding.ItemFavBinding
import com.example.weatherapplication.favorite.view.OnRemoveClickListener
import com.example.weatherapplication.model.StoreLatitudeLongitude


class AdapterFav  (private val listener: OnRemoveClickListener , private val context: Context?):
    ListAdapter<StoreLatitudeLongitude, AdapterFav.MyViewHolder>(ProductDiffUtil())
{
    private lateinit var binding: ItemFavBinding

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): AdapterFav.MyViewHolder {

        val inflater: LayoutInflater =
            parent.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        binding = ItemFavBinding.inflate(inflater, parent, false)

        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AdapterFav.MyViewHolder, position: Int) {
        val current = getItem(position)
        holder.binding.tvCityName.text = current.name
        binding.ivDeleteCity.setOnClickListener{
            listener.onRemoveClick(current)
        }
    }

    class ProductDiffUtil : DiffUtil.ItemCallback<StoreLatitudeLongitude>(){
        override fun areItemsTheSame(oldItem: StoreLatitudeLongitude, newItem: StoreLatitudeLongitude): Boolean {
            return oldItem.latitude == newItem.latitude
        }

        override fun areContentsTheSame(oldItem: StoreLatitudeLongitude, newItem: StoreLatitudeLongitude): Boolean {
            return oldItem == newItem
        }

    }

    inner class MyViewHolder(var binding: ItemFavBinding) : RecyclerView.ViewHolder(binding.root)
}