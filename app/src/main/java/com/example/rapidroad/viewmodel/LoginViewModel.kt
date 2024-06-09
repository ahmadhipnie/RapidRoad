package com.example.rapidroad.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rapidroad.data.preference.UserModel
import com.example.rapidroad.data.repository.Repository
import com.example.rapidroad.data.retrofit.response.LoginResponse
import kotlinx.coroutines.launch

class LoginViewModel(private val repository: Repository) : ViewModel(){

    fun saveSession(user: UserModel) {
        viewModelScope.launch {
            repository.saveSession(user)
        }
    }

    suspend fun login(email:String?, password:String?): LoginResponse {
        return   repository.login(email!!,password!!)
    }
}