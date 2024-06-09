package com.example.rapidroad.data.retrofit.api

import com.example.rapidroad.data.retrofit.response.GetAllReportResponse
import com.example.rapidroad.data.retrofit.response.LoginResponse
import com.example.rapidroad.data.retrofit.response.RegisterResponse
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface ApiService {

    @FormUrlEncoded
    @POST("login")
    suspend fun login(
        @Field("email") email: String,
        @Field("password") password: String
    ) : LoginResponse


    @FormUrlEncoded
    @POST("register")
    suspend fun registerUser(
        @Field("email") email: String,
        @Field("username") username: String,
        @Field("password") password: String
    ): RegisterResponse

    @FormUrlEncoded
    @POST("update_password")
    suspend fun changePasswordUser(
        @Field("email") email: String,
        @Field("old_password") oldPassword: String,
        @Field("new_password") NewPassword: String
    ): RegisterResponse


    @GET("laporan/all")
    suspend fun getAllReportLocation(
    ): GetAllReportResponse


}