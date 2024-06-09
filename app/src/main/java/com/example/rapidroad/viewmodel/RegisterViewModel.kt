package com.example.rapidroad.viewmodel

import androidx.lifecycle.ViewModel
import com.example.rapidroad.data.repository.Repository
import com.example.rapidroad.data.retrofit.response.RegisterResponse

class RegisterViewModel(private val repository: Repository) : ViewModel(){

    suspend fun register(name: String, email: String, password: String): RegisterResponse {
        return repository.register(name,email,password)
    }
}