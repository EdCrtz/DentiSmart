package com.dreamteam.dentismart.repositories

import androidx.lifecycle.MutableLiveData
import com.dreamteam.dentismart.models.LoginCredentials
import com.dreamteam.dentismart.models.LoginResult
import com.dreamteam.dentismart.services.DentiSmartService
import com.dreamteam.dentismart.services.RetrofitService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class LoginRepository {
    private var dentismartService: DentiSmartService = RetrofitService.dentiSmartService
    var userLogged: MutableLiveData<LoginResult> = MutableLiveData<LoginResult>()
    var messageResponse = MutableLiveData<String>()
    fun onLogin(userName: String, password: String): MutableLiveData<LoginResult> {
        if (userName.isEmpty() && password.isEmpty()) {
            messageResponse.value = "No se aceptan vacios llenar los campos"
            return userLogged
        }
        val userCredential = LoginCredentials(userName, password)
        print(userCredential.contrasenia)
        print(userCredential.nombreUsuario)
        val call: Call<LoginResult>? = dentismartService.login(userCredential)
        call!!.enqueue(object : Callback<LoginResult?> {
            override fun onResponse(
                call: Call<LoginResult?>,
                response: Response<LoginResult?>
            ) {
                if (!response.isSuccessful) {
                    messageResponse.value = "Usuario o Contraseña incorrectos"

                    return
                }
                userLogged.value = response.body()
            }

            override fun onFailure(call: Call<LoginResult?>, t: Throwable) {
                messageResponse.value = "Usuario o Contraseña incorrectos"
            }
        })
        return userLogged
    }

}