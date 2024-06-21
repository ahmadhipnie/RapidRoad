package com.example.rapidroad.view

import android.os.Bundle
import android.view.MenuItem
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.example.rapidroad.R
import com.example.rapidroad.data.retrofit.response.DataItem
import com.example.rapidroad.databinding.ActivityDetailBinding
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

class DetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.apply {
            title = "Detail"
            setDisplayHomeAsUpEnabled(true)
        }

        val dataItem = intent.getParcelableExtra<DataItem>(EXTRA_STORY)
        dataItem?.let {
            val zonedDateTime = ZonedDateTime.parse(it.tanggal)
            val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
            val formattedTimeString = zonedDateTime.format(formatter)
             val check = if (it.status == "rusak") "Menunggu Perbaikan" else it.status

            binding.tvDetailName.text = "Pelapor: ${it.namaUser}"
            binding.tvTime.text = "Tanggal: $formattedTimeString"
            binding.tvLokasi.text = "Lokasi: Jl.${it.namaJalan}, ${it.desa}, ${it.kecamatan}, ${it.kota}"
            binding.tvStatus.text = "Status: $check"
            binding.tvDescription.text = "Keterangan: ${it.keterangan}"
            binding.tvDetailDescription.text = "Deskripsi: ${it.keteranganUser}"

            Glide.with(binding.root)
                .load(it.pathFotoLaporan)
                .into(binding.ivDetailPhoto)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                finish()
                true
            }

            else -> false
        }
    }

    companion object {
        const val EXTRA_STORY = "extra_story"
    }
}