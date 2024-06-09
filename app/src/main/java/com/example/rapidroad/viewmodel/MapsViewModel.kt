package com.example.rapidroad.viewmodel

import androidx.lifecycle.ViewModel
import com.example.rapidroad.data.repository.Repository

class MapsViewModel(private val repository: Repository) : ViewModel() {

    fun getAllReportLocation() = repository.getAllReportLocation()
}