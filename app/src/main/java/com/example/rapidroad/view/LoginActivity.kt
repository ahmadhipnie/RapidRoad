package com.example.rapidroad.view

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Bundle
import android.os.Process
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.example.rapidroad.R
import com.example.rapidroad.components.CustomButtonLogin
import com.example.rapidroad.components.CustomDialogLoading
import com.example.rapidroad.components.CustomEditTextEmail
import com.example.rapidroad.components.CustomEditTextPassword
import com.example.rapidroad.data.preference.UserModel
import com.example.rapidroad.databinding.ActivityLoginBinding
import com.example.rapidroad.viewmodel.LoginViewModel
import com.example.rapidroad.viewmodel.ViewModelFactory
import kotlinx.coroutines.launch
import retrofit2.HttpException

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    private lateinit var etLoginEmailWatcher: CustomEditTextEmail

    private lateinit var etLoginPasswordWatcher: CustomEditTextPassword

    private lateinit var btnLoginWatcher: CustomButtonLogin

    private lateinit var dialogLoading: CustomDialogLoading

    private val loginViewModel by viewModels<LoginViewModel> {
        ViewModelFactory.getInstance(this)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        playAnimationLogin()


        with(binding) {


            btnLogin.isEnabled = false

            etLoginEmailWatcher = etEmailLogin
            etLoginPasswordWatcher = etPasswordLogin
            btnLoginWatcher = btnLogin


            btnLogin.setOnClickListener {
                dialogLoading = CustomDialogLoading(this@LoginActivity)
                dialogLoading.setLoadingVisible(true)

                val email = etEmailLogin.text.toString()
                val password = etPasswordLogin.text.toString()

                lifecycleScope.launch {
                    try {
                        val response = loginViewModel.login(email, password)
                        if (response != null) {
                            val userId = response.user.id
                            val userName = response.user.namaUser
                            val userPassword = response.user.password
                            val userEmail = response.user.email
                            loginViewModel.saveSession(UserModel(userId, userName, userEmail , userPassword))
                            dialogLoading.setLoadingVisible(false)
                            Intent(this@LoginActivity, MainActivity::class.java).apply {
                                startActivity(this)
                                finish()
                            }

                        }
                    } catch (e: HttpException) {
                        dialogLoading.setLoadingVisible(false)
                        Toast.makeText(
                            this@LoginActivity,
                            "Terjadi kesalahan, silahkan coba lagi",
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                }


            }

            btnRegisterLogin.setOnClickListener {
                Intent(this@LoginActivity, RegisterActivity::class.java).apply {
                    startActivity(this)
                }
            }

        }

        etLoginEmailWatcher.addTextChangedListener(textWatcher)
        etLoginPasswordWatcher.addTextChangedListener(textWatcher)
    }

    private val textWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            setLoginButtonEnabled()
        }

        override fun afterTextChanged(s: Editable?) {}
    }

    private fun setLoginButtonEnabled() {
        val password = etLoginEmailWatcher.text.toString()
        val email = etLoginEmailWatcher.text.toString()

        btnLoginWatcher.isEnabled = password.isNotEmpty() && email.isNotEmpty()

    }

    private fun playAnimationLogin() {
        val durationAnimation: Long = 500
        val linearLayoutAnimation =
            ObjectAnimator.ofFloat(binding.llLogin, View.TRANSLATION_Y, 1000f, 0f).setDuration(1000)
        val tvEmailLoginAnimation = ObjectAnimator.ofFloat(binding.tvEmailLogin, View.ALPHA, 1f)
            .setDuration(durationAnimation)
        val etLayoutEmailLoginAnimation =
            ObjectAnimator.ofFloat(binding.etLayoutEmailLogin, View.ALPHA, 1f)
                .setDuration(durationAnimation)
        val tvPasswordLoginAnimation =
            ObjectAnimator.ofFloat(binding.tvPasswordLogin, View.ALPHA, 1f)
                .setDuration(durationAnimation)
        val etLayoutPasswordLoginAnimation =
            ObjectAnimator.ofFloat(binding.etLayoutPasswordLogin, View.ALPHA, 1f)
                .setDuration(durationAnimation)
        val btnLupaPasswordAnimation =
            ObjectAnimator.ofFloat(binding.btnLupaPassword, View.ALPHA, 1f)
                .setDuration(durationAnimation)
        val btnLoginAnimation =
            ObjectAnimator.ofFloat(binding.btnLogin, View.ALPHA, 1f).setDuration(durationAnimation)
        val llRegisterLoginAnimation =
            ObjectAnimator.ofFloat(binding.llRegisterLogin, View.ALPHA, 1f)
                .setDuration(durationAnimation)

        AnimatorSet().apply {
            playSequentially(
                linearLayoutAnimation,
                tvEmailLoginAnimation,
                etLayoutEmailLoginAnimation,
                tvPasswordLoginAnimation,
                etLayoutPasswordLoginAnimation,
                btnLupaPasswordAnimation,
                btnLoginAnimation,
                llRegisterLoginAnimation
            )
            start()
        }
    }


    override fun onBackPressed() {
        // Menutup aplikasi saat tombol kembali ditekan
        moveTaskToBack(true)
        Process.killProcess(Process.myPid())
        System.exit(1)
        super.onBackPressed()
    }
}