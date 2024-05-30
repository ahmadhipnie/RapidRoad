package com.example.rapidroad.view

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.rapidroad.R
import com.example.rapidroad.databinding.ActivityRegisterBinding

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
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

        with(binding){
            btnRegister.setOnClickListener {
                Intent(this@RegisterActivity, MainActivity::class.java).apply {
                    startActivity(this)
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
        val durationAnimation : Long = 500
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