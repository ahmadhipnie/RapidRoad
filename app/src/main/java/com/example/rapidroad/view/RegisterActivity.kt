package com.example.rapidroad.view

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.example.rapidroad.R
import com.example.rapidroad.components.CustomDialogLoading
import com.example.rapidroad.databinding.ActivityRegisterBinding
import com.example.rapidroad.viewmodel.RegisterViewModel
import com.example.rapidroad.viewmodel.ViewModelFactory
import kotlinx.coroutines.launch

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding

    private lateinit var dialogLoading: CustomDialogLoading

    private val registerViewModel by viewModels<RegisterViewModel> {
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        playAnimationRegister()

        with(binding) {
            btnRegister.setOnClickListener {
                dialogLoading = CustomDialogLoading(this@RegisterActivity)
                dialogLoading.setLoadingVisible(true)
                val email = etEmailRegister.text.toString()
                val username = etUsernameRegister.text.toString()
                val password = etPasswordRegister.text.toString()

                if (email.isEmpty() || username.isEmpty() || password.isEmpty()) {
                    dialogLoading.setLoadingVisible(false)
                    Toast.makeText(this@RegisterActivity, "isi semua kontol", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }

                lifecycleScope.launch {
                    try {
                        val response = registerViewModel.register(email, username, password)
                        dialogLoading.setLoadingVisible(false)
                        if (response.status.toString() == "true") {
                            Toast.makeText(this@RegisterActivity, "Register Success", Toast.LENGTH_SHORT).show()
                            Intent(this@RegisterActivity, LoginActivity::class.java).apply {
                                startActivity(this)
                            }
                            finish()
                        } else {
                            Toast.makeText(this@RegisterActivity, "Email already registered", Toast.LENGTH_SHORT).show()
                        }
                    } catch (e: Exception) {
                        dialogLoading.setLoadingVisible(false)
                        Toast.makeText(this@RegisterActivity, "Register Failed", Toast.LENGTH_SHORT).show()
                        Log.d(TAG, "onCreate: " + e.message)
                        e.printStackTrace()
                    }
                }
            }

            btnLoginRegister.setOnClickListener {
                Intent(this@RegisterActivity, LoginActivity::class.java).apply {
                    startActivity(this)
                }
            }
        }
    }

    private fun playAnimationRegister() {
        val durationAnimation: Long = 500
        val linearLayoutAnimation = ObjectAnimator.ofFloat(binding.llRegister, View.TRANSLATION_Y, 1000f, 0f).setDuration(1000)
        val tvEmailRegisterAnimation = ObjectAnimator.ofFloat(binding.tvEmailRegister, View.ALPHA, 1f).setDuration(durationAnimation)
        val etLayoutEmailRegisterAnimation = ObjectAnimator.ofFloat(binding.etLayoutEmailRegister, View.ALPHA, 1f).setDuration(durationAnimation)
        val tvUsernameRegisterAnimation = ObjectAnimator.ofFloat(binding.tvUsernameRegister, View.ALPHA, 1f).setDuration(durationAnimation)
        val etLayoutUsernameRegisterAnimation = ObjectAnimator.ofFloat(binding.etLayoutUsernameRegister, View.ALPHA, 1f).setDuration(durationAnimation)
        val tvPasswordRegisterAnimation = ObjectAnimator.ofFloat(binding.tvPasswordRegister, View.ALPHA, 1f).setDuration(durationAnimation)
        val etLayoutPasswordRegisterAnimation = ObjectAnimator.ofFloat(binding.etLayoutPasswordRegister, View.ALPHA, 1f).setDuration(durationAnimation)
        val btnRegisterAnimation = ObjectAnimator.ofFloat(binding.btnRegister, View.ALPHA, 1f).setDuration(durationAnimation)
        val llLoginRegisterAnimation = ObjectAnimator.ofFloat(binding.llLoginRegister, View.ALPHA, 1f).setDuration(durationAnimation)

        AnimatorSet().apply {
            playSequentially(
                linearLayoutAnimation,
                tvEmailRegisterAnimation,
                etLayoutEmailRegisterAnimation,
                tvUsernameRegisterAnimation,
                etLayoutUsernameRegisterAnimation,
                tvPasswordRegisterAnimation,
                etLayoutPasswordRegisterAnimation,
                btnRegisterAnimation,
                llLoginRegisterAnimation
            )
            start()
        }
    }
}
