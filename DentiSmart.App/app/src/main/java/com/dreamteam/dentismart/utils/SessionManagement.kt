package com.dreamteam.dentismart.utils

import android.content.Context
import android.content.SharedPreferences
import com.dreamteam.dentismart.models.LoginResult
import com.google.gson.Gson


class SessionManagement(context: Context) {
    private var editor: SharedPreferences.Editor? = null
    private var _context: Context? = null
    private var pref: SharedPreferences? = null
    private val PRIVATE_MODE = 0
    private val PREF_NAME = "_store"

    init {
        _context = context
        pref = _context!!.getSharedPreferences(PREF_NAME, PRIVATE_MODE)
        editor = pref!!.edit()
    }

    fun createLoginSession(user: LoginResult?) {
        editor!!.putString("currentUser", Gson().toJson(user))
        editor!!.putBoolean("isLogin", true)
        // commit changes
        editor!!.commit()
    }

    fun logoutUser() {
        editor!!.clear()
        editor!!.commit()
    }

    fun getCurrentUser(): LoginResult? {
        val currentUserJson = pref!!.getString("currentUser", null)
        return Gson().fromJson(currentUserJson, LoginResult::class.java)
    }

    fun isLogin(): Boolean {
        return pref!!.getBoolean("isLogin", false)
    }

    fun updateCurrentUser(currentUser: LoginResult?) {
        editor!!.clear()
        editor!!.putString("currentUser", Gson().toJson(currentUser))
        editor!!.putBoolean("isLogin", true)
        editor!!.commit()
    }
}