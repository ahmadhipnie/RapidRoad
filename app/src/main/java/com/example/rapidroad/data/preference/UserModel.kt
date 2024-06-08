package com.example.rapidroad.data.preference

data class UserModel(
    val userId: String,

    val userName: String,

    val userEmail: String,

    val userPassword: String,

    val isLoggedIn: Boolean = false
)