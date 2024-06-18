package com.example.rapidroad.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rapidroad.data.repository.Repository
import com.example.rapidroad.data.repository.ResultState
import com.example.rapidroad.data.retrofit.response.DataItem
import kotlinx.coroutines.launch

class DashboardViewModel(private val repository: Repository) : ViewModel() {
    fun getAllReportLocation(): LiveData<ResultState<List<DataItem>>> {
        return repository.getAllReportLocation()
    }
}
