package com.example.rapidroad.view

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.example.rapidroad.R
import com.example.rapidroad.components.CustomDialogLoading
import com.example.rapidroad.databinding.ActivityChangePasswordBinding
import com.example.rapidroad.viewmodel.ChangePasswordViewModel
import com.example.rapidroad.viewmodel.MainViewModel
import com.example.rapidroad.viewmodel.ViewModelFactory
import kotlinx.coroutines.launch
import retrofit2.HttpException

class ChangePasswordActivity : AppCompatActivity() {

    private lateinit var binding: ActivityChangePasswordBinding
    private lateinit var dialogLoading: CustomDialogLoading

    private val changePasswordViewModel by viewModels<ChangePasswordViewModel> {
        ViewModelFactory.getInstance(this)
    }

    private val mainViewModel by viewModels<MainViewModel> {
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChangePasswordBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        mainViewModel.getSession().observe(this@ChangePasswordActivity) { session ->
            Log.d(TAG, "onCreate: ${session.userEmail}")
        }

        with(binding) {
            btnKembaliUbahPassword.setOnClickListener {
                finish()
            }

            btnUbahPassword.setOnClickListener {
                dialogLoading = CustomDialogLoading(this@ChangePasswordActivity)
                dialogLoading.setLoadingVisible(true)

                val oldPassword = etPasswordLamaUbahPassword.text.toString()
                val newPassword = etPasswordBaruUbahPassword.text.toString()
                val confirmNewPassword = etKonfirmasiPasswordBaruUbahPassword.text.toString()

                if (oldPassword.isEmpty() || newPassword.isEmpty() || confirmNewPassword.isEmpty()) {
                    dialogLoading.setLoadingVisible(false)
                    Toast.makeText(
                        this@ChangePasswordActivity,
                        "Isi semua kolom",
                        Toast.LENGTH_SHORT
                    ).show()
                    return@setOnClickListener
                }

                if (newPassword != confirmNewPassword) {
                    dialogLoading.setLoadingVisible(false)
                    Toast.makeText(
                        this@ChangePasswordActivity,
                        "Password baru tidak sama",
                        Toast.LENGTH_SHORT
                    ).show()
                    return@setOnClickListener
                }

                // Retrieve the email asynchronously and then proceed with password change
                mainViewModel.getSession().observe(this@ChangePasswordActivity) { session ->
                    val email = session.userEmail

                    lifecycleScope.launch {
                        try {
                            Log.d(TAG, email)
                            val response = changePasswordViewModel.changePasswordUser(
                                email,
                                oldPassword,
                                newPassword
                            )
                            dialogLoading.setLoadingVisible(false)
                            if (response.status.toString() == "true") {
                                Toast.makeText(
                                    this@ChangePasswordActivity,
                                    "Berhasil mengubah password",
                                    Toast.LENGTH_SHORT
                                ).show()
                                changePasswordViewModel.logout()
                                Intent(
                                    this@ChangePasswordActivity,
                                    LoginActivity::class.java
                                ).apply {
                                    startActivity(this)
                                    finish()
                                }
                            } else {
                                Toast.makeText(
                                    this@ChangePasswordActivity,
                                    response.message,
                                    Toast.LENGTH_SHORT
                                ).show()
                                Log.d(TAG, "onCreate: ini masuk ke else")
                            }
                        } catch (e: HttpException) {
                            dialogLoading.setLoadingVisible(false)

                            Log.d(TAG, "onCreate: ini masuk ke catch")
                        }
                    }
                }
            }
        }
    }
}
