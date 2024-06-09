package com.example.rapidroad.data.preference

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.map


val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "session_login")

class UserPreference private constructor(private val dataStore: DataStore<Preferences>) {

    fun getSession() = dataStore.data.map { preference ->
        UserModel(
            preference[ID] ?: "",
            preference[USERNAME] ?: "",
            preference[EMAIL] ?: "",
            preference[PASSWORD] ?: "",
            preference[IS_LOGGED_IN] ?: false
        )
    }


    suspend fun saveSession(user: UserModel) {
        dataStore.edit { preference ->
            preference[ID] = user.userId
            preference[USERNAME] = user.userName
            preference[EMAIL] = user.userEmail
            preference[PASSWORD] = user.userPassword
            preference[IS_LOGGED_IN] = true
        }
    }

    suspend fun logout() {
        dataStore.edit { preference ->
            preference.clear()
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: UserPreference? = null

        private val ID = stringPreferencesKey("userId")
        private val USERNAME = stringPreferencesKey("userName")
        private val EMAIL = stringPreferencesKey("userEmail")
        private val PASSWORD = stringPreferencesKey("userPassword")
        private val IS_LOGGED_IN = booleanPreferencesKey("isLoggedIn")

        fun getInstance(dataStore: DataStore<Preferences>): UserPreference {
            return INSTANCE ?: synchronized(this) {
                val instance = UserPreference(dataStore)
                INSTANCE = instance
                instance
            }
        }
    }

}