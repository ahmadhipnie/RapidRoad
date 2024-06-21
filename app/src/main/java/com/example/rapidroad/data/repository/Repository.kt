package com.example.rapidroad.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.example.rapidroad.data.preference.UserModel
import com.example.rapidroad.data.preference.UserPreference
import com.example.rapidroad.data.retrofit.api.ApiService
import com.example.rapidroad.data.retrofit.response.AddResponse
import com.example.rapidroad.data.retrofit.response.DataItem
import com.example.rapidroad.data.retrofit.response.GetAllReportResponse
import com.example.rapidroad.data.retrofit.response.LoginResponse
import com.example.rapidroad.data.retrofit.response.RegisterResponse
import com.google.gson.Gson
import kotlinx.coroutines.flow.Flow
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.HttpException

class Repository private constructor(
    private val apiService: ApiService,
    private val userPreference: UserPreference

) {

    fun getSession(): Flow<UserModel> {
        return userPreference.getSession()
    }

    suspend fun saveSession(user: UserModel) {
        userPreference.saveSession(user)
    }

    suspend fun login(email: String, password: String): LoginResponse {
        return apiService.login(email, password)
    }

    suspend fun register(username: String, email: String, password: String): RegisterResponse {
        return apiService.registerUser(username, email, password)
    }
    suspend fun changePasswordUser(email: String, oldPassword: String, newPassword: String): RegisterResponse {
        return apiService.changePasswordUser(email, oldPassword, newPassword)
    }

    fun getAllReportLocation(): LiveData<ResultState<List<DataItem>>> = liveData {
        emit(ResultState.Loading)
        try {
            val response = apiService.getAllReportLocation()
            emit(ResultState.Success(response.data))
        } catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            val errorResponse = Gson().fromJson(errorBody, GetAllReportResponse::class.java)
            emit(ResultState.Error(errorResponse.message))
        } catch (e: Exception) {
            emit(ResultState.Error(e.message ?: "Error"))
        }
    }

    suspend fun addStory(
        file: MultipartBody.Part,
        id: RequestBody,
        kota: RequestBody,
        desa: RequestBody,
        kecamatan: RequestBody,
        nama_jalan: RequestBody,
        keterangan: RequestBody,
        longitude: RequestBody,
        latitude: RequestBody
    ): AddResponse {
        try {
            return apiService.addStory(file, id, kota, desa, kecamatan, nama_jalan, keterangan, longitude, latitude)
        } catch (e: HttpException) {
            // Log the error response
            println("HTTP Exception: ${e.response()?.errorBody()?.string()}")
            throw e
        } catch (e: Exception) {
            // Log any other exceptions
            println("Exception: ${e.message}")
            throw e
        }

    }


    suspend fun logout() {
        userPreference.logout()
    }


    companion object {
        fun getInstance(
            apiService: ApiService,
            userPreference: UserPreference
        ) = Repository(apiService, userPreference)
    }
}