package com.example.rapidroad.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rapidroad.data.repository.Repository
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody

class LaporViewModel(private val repository: Repository) : ViewModel() {
    val addStorySuccess = MutableLiveData<Boolean>()

    fun addStory(
        file: MultipartBody.Part,
        id: RequestBody,
        kota: RequestBody,
        desa: RequestBody,
        kecamatan: RequestBody,
        nama_jalan: RequestBody,
        keterangan: RequestBody,
        longitude: RequestBody,
        latitude: RequestBody
    ) {
        viewModelScope.launch {
            try {
                val response = repository.addStory(file, id, kota, desa, kecamatan, nama_jalan, keterangan, longitude, latitude)
                println("Add Story Response: $response")
                addStorySuccess.postValue(true)  // Indicate success

            } catch (e: Exception) {
                println("Error adding story: ${e.message}")
                addStorySuccess.postValue(false)  // Indicate success

            }
        }
    }
}
