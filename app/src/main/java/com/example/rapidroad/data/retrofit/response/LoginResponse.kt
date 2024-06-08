package com.example.rapidroad.data.retrofit.response

import com.google.gson.annotations.SerializedName

data class LoginResponse(

	@field:SerializedName("message")
	val message: String,

	@field:SerializedName("user")
	val user: User
)

data class User(

	@field:SerializedName("password")
	val password: String,

	@field:SerializedName("id")
	val id: String,

	@field:SerializedName("nama_user")
	val namaUser: String,

	@field:SerializedName("email")
	val email: String
)
