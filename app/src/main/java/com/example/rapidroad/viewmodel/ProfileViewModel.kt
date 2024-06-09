package com.example.rapidroad.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.rapidroad.data.preference.UserModel
import com.example.rapidroad.data.repository.Repository
import kotlinx.coroutines.launch

class ProfileViewModel(private val repository: Repository) : ViewModel() {

    fun getSession(): LiveData<UserModel> {
        return repository.getSession().asLiveData()
    }


    fun logout() {
        viewModelScope.launch {
            repository.logout()
        }
    }
}