package com.example.rapidroad.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.rapidroad.data.preference.UserModel
import com.example.rapidroad.data.repository.Repository
import com.example.rapidroad.data.retrofit.response.RegisterResponse
import kotlinx.coroutines.launch

class ChangePasswordViewModel(private val repository: Repository) : ViewModel() {

    suspend fun changePasswordUser(email: String, oldPassword: String, newPassword: String) : RegisterResponse {
        return repository.changePasswordUser(email, oldPassword, newPassword)
    }

    fun getSession(): LiveData<UserModel> {
        return repository.getSession().asLiveData()
    }

    fun logout() {
        viewModelScope.launch {
            repository.logout()
        }
    }
}