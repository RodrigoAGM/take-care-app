package com.example.takecare.utils

import android.content.Context
import android.content.SharedPreferences

object PreferenceHelper {

    private const val NAME = "APP_DATA"
    private const val MODE = Context.MODE_PRIVATE
    private lateinit var preferences: SharedPreferences

    private val LOGGED_IN = Pair("logged_in", false)
    private val USER_TOKEN = Pair("token", "")
    private val USER_REFRESH_TOKEN = Pair("refresh_token", "")
    private val USER_DATA = Pair("user_data", "")

    fun init(context: Context) {
        preferences = context.getSharedPreferences(NAME, MODE)
    }

    private inline fun SharedPreferences.edit(
        operation: (SharedPreferences.Editor) -> Unit){
        val editor = edit()
        operation(editor)
        editor.apply()
    }

    var loggedIn: Boolean
        get() = preferences.getBoolean(LOGGED_IN.first, LOGGED_IN.second)
        set(value) = preferences.edit{
            it.putBoolean(LOGGED_IN.first, value)
        }

    var token: String?
        get() = preferences.getString(USER_TOKEN.first, USER_TOKEN.second)
        set(value) = preferences.edit{
            it.putString(USER_TOKEN.first, value)
        }

    var refreshToken: String?
        get() = preferences.getString(USER_REFRESH_TOKEN.first, USER_REFRESH_TOKEN.second)
        set(value) = preferences.edit{
            it.putString(USER_REFRESH_TOKEN.first, value)
        }

    var userData: String?
        get() = preferences.getString(USER_DATA.first, USER_DATA.second)
        set(value) = preferences.edit{
            it.putString(USER_DATA.first, value)
        }
}