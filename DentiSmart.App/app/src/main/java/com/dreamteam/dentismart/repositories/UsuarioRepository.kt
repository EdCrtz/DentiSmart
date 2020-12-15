package com.dreamteam.dentismart.repositories

import androidx.lifecycle.MutableLiveData
import com.dreamteam.dentismart.models.Usuario
import com.dreamteam.dentismart.services.DentiSmartService
import com.dreamteam.dentismart.services.RetrofitService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UsuarioRepository {
    private var dentismartService: DentiSmartService = RetrofitService.dentiSmartService
    var usuariosResponse: MutableLiveData<List<Usuario>> = MutableLiveData<List<Usuario>>()
    var usuarioResponse: MutableLiveData<Usuario> = MutableLiveData<Usuario>()
    var dentistaResponse: MutableLiveData<List<Usuario>> = MutableLiveData<List<Usuario>>()
    fun getUsuarios(): MutableLiveData<List<Usuario>> {
        val call: Call<List<Usuario>> = this.dentismartService.getUsuarios()
        call.enqueue(object : Callback<List<Usuario>> {
            override fun onFailure(call: Call<List<Usuario>>?, t: Throwable?) {
                // TO DO
            }

            override fun onResponse(
                call: Call<List<Usuario>>?,
                response: Response<List<Usuario>>?
            ) {
                if (response!!.isSuccessful) {
                    this@UsuarioRepository.usuariosResponse.value = response.body()
                }
            }
        })
        return usuariosResponse
    }
    fun getUserByRole(role:String): MutableLiveData<List<Usuario>> {
        val call: Call<List<Usuario>> = this.dentismartService.getUserByRole(role)
        call.enqueue(object : Callback<List<Usuario>> {
            override fun onFailure(call: Call<List<Usuario>>?, t: Throwable?) {
                // TO DO
            }

            override fun onResponse(
                call: Call<List<Usuario>>?,
                response: Response<List<Usuario>>?
            ) {
                if (response!!.isSuccessful) {
                    this@UsuarioRepository.usuariosResponse.value = response.body()
                }
            }
        })
        return usuariosResponse
    }

    fun getDentistasByIdConsultiorio(idConsultorio: String): MutableLiveData<List<Usuario>> {
        val call: Call<List<Usuario>> = this.dentismartService.getDentistasByIdConsultorio(idConsultorio)
        call.enqueue(object : Callback<List<Usuario>> {
            override fun onFailure(call: Call<List<Usuario>>?, t: Throwable?) {
                // TO DO
            }

            override fun onResponse(
                call: Call<List<Usuario>>?,
                response: Response<List<Usuario>>?
            ) {
                if (response!!.isSuccessful) {
                    this@UsuarioRepository.dentistaResponse.value = response.body()
                }
            }
        })
        return dentistaResponse
    }

    fun getUserById(iduser: String): MutableLiveData<Usuario> {
        val call: Call<Usuario> = this.dentismartService.getUserById(iduser)
        call.enqueue(object : Callback<Usuario> {
            override fun onFailure(call: Call<Usuario>?, t: Throwable?) {
                // TO DO
            }

            override fun onResponse(
                call: Call<Usuario>?,
                response: Response<Usuario>?
            ) {
                if (response!!.isSuccessful) {
                    this@UsuarioRepository.usuarioResponse.value = response.body()
                }
            }
        })
        return usuarioResponse
    }
}
