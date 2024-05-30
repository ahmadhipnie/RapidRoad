package com.example.rapidroad.data.di

import android.content.Context
import com.example.rapidroad.data.preference.UserPreference
import com.example.rapidroad.data.preference.dataStore
import com.example.rapidroad.data.repository.Repository
import com.example.rapidroad.data.retrofit.api.ApiConfig
object Injection {
    fun provideRepository(context: Context): Repository {
        val pref = UserPreference.getInstance(context.dataStore)
        val apiService = ApiConfig().getApiService()
        return Repository.getInstance(apiService, pref)
    }
}