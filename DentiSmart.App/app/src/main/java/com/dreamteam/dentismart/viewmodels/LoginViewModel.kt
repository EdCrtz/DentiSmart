package com.dreamteam.dentismart.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dreamteam.dentismart.models.LoginResult
import com.dreamteam.dentismart.repositories.LoginRepository

class LoginViewModel : ViewModel() {
    private val repository: LoginRepository = LoginRepository()
    val messageResponse: MutableLiveData<String>

    fun getUserLoged(): MutableLiveData<LoginResult> {
        return userLogged
    }

    private var userLogged: MutableLiveData<LoginResult>
    fun onLogin(userName: String, password: String) {
        userLogged = repository.onLogin(userName, password)
    }

    init {
        messageResponse = repository.messageResponse
        userLogged = repository.userLogged
    }
}