package com.example.rapidroad.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.example.rapidroad.data.preference.UserModel
import com.example.rapidroad.data.repository.Repository

class MainViewModel(private val repository: Repository) : ViewModel(){

    fun getSession(): LiveData<UserModel> {
        return repository.getSession().asLiveData()
    }
}