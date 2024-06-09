package com.example.rapidroad.view

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.rapidroad.R
import com.example.rapidroad.databinding.ActivityFaqBinding
import com.example.rapidroad.viewmodel.MainViewModel
import com.example.rapidroad.viewmodel.ViewModelFactory


class FaqActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFaqBinding

    private val mainViewModel by viewModels<MainViewModel> {
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFaqBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }


        binding.btnKirimPertanyaanKeWhatsapp.setOnClickListener {
            kirimPesanWhatsApp()
        }
    }

    private fun kirimPesanWhatsApp() {
        val pertanyaan: String = binding.etPertanyaanFaq.getText().toString().trim()

        if (TextUtils.isEmpty(pertanyaan)) {
            // Validasi jika pertanyaan kosong
            binding.etPertanyaanFaq.setError("Pertanyaan tidak boleh kosong")
            return
        }

        // Nomor WhatsApp yang ingin dihubungi
        val nomorWhatsApp = "+6281333100497"

        mainViewModel.getSession().observe(this@FaqActivity) { it ->
            val namaPengirim = it.userName.toString()
            val email = it.userEmail


            // Membuat pesan dengan data diri
            val pesan = """
                Pertanyaan dari: $namaPengirim
                Email : $email
                Pertanyaan : 
                
                $pertanyaan
                """.trimIndent()

            // Membuat format URI untuk intent WhatsApp
            val url =
                "https://api.whatsapp.com/send?phone=" + nomorWhatsApp + "&text=" + Uri.encode(pesan)
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            startActivity(intent)
        }
    }
}