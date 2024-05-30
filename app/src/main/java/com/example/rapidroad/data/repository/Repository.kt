package com.example.rapidroad.data.repository

import com.example.rapidroad.data.preference.UserPreference
import com.example.rapidroad.data.retrofit.api.ApiService

class Repository private constructor(
    private val apiService: ApiService,
    private val userPreference: UserPreference
) {


    companion object {
        fun getInstance(
            apiService: ApiService,
            userPreference: UserPreference
        ) = Repository(apiService, userPreference)
    }
}