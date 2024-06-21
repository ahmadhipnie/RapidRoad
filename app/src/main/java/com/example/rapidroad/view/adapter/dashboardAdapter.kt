package com.example.rapidroad.view.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.rapidroad.R
import com.example.rapidroad.data.retrofit.response.DataItem
import com.example.rapidroad.databinding.ItemCardBinding
import com.example.rapidroad.view.DetailActivity
import java.util.Locale


class DashboardAdapter : ListAdapter<DataItem, DashboardAdapter.ViewHolder>(DiffCallback()) {
    private var fullDataList: List<DataItem> = listOf()

    inner class ViewHolder(private val binding: ItemCardBinding) : RecyclerView.ViewHolder(binding.root) {
        init {
            itemView.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val item = getItem(position)
                    val intent = Intent(itemView.context, DetailActivity::class.java).apply {
                        putExtra(DetailActivity.EXTRA_STORY, item)
                    }
                    itemView.context.startActivity(intent)
                }
            }
        }

        fun bind(dataItem: DataItem) {
            binding.textViewTitle.text = "Lokasi: Jl.${dataItem.namaJalan}, ${dataItem.desa}"
            binding.textViewDescription.text = "Kondisi: ${dataItem.keterangan}"
            Glide.with(binding.root)
                .load(dataItem.pathFotoLaporan)
                .into(binding.ivImage)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val dataItem = getItem(position)
        holder.bind(dataItem)
    }

    fun setDataList(dataList: List<DataItem>) {
        fullDataList = dataList
        submitList(dataList)
    }

    fun filter(query: String) {
        val filteredList = if (query.isEmpty()) {
            fullDataList
        } else {
            val lowerCaseQuery = query.toLowerCase(Locale.ROOT)
            fullDataList.filter {
                it.desa.toLowerCase(Locale.ROOT).contains(lowerCaseQuery) ||
                        it.namaJalan.toLowerCase(Locale.ROOT).contains(lowerCaseQuery)
            }
        }
        submitList(filteredList)
    }

    class DiffCallback : DiffUtil.ItemCallback<DataItem>() {
        override fun areItemsTheSame(oldItem: DataItem, newItem: DataItem): Boolean {
            return oldItem.idLaporan == newItem.idLaporan
        }

        override fun areContentsTheSame(oldItem: DataItem, newItem: DataItem): Boolean {
            return oldItem == newItem
        }
    }
}