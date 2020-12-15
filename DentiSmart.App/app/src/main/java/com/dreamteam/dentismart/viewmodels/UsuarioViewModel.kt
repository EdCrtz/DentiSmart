package com.dreamteam.dentismart.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dreamteam.dentismart.models.Usuario
import com.dreamteam.dentismart.repositories.UsuarioRepository

class UsuarioViewModel:ViewModel(){
        private val repository: UsuarioRepository = UsuarioRepository()
        private var usuarioDentista: MutableLiveData<List<Usuario>> = repository.dentistaResponse
    private var usuario: MutableLiveData<List<Usuario>> = repository.usuariosResponse
    private var usuarioUnico: MutableLiveData<Usuario> = repository.usuarioResponse
        fun getDentistasByIdConsultiorio(id: String): MutableLiveData<List<Usuario>> {
            repository.getDentistasByIdConsultiorio(id)
            return usuarioDentista
        }

        fun getUsuarios(): MutableLiveData<List<Usuario>> {
            repository.getUsuarios()
            return usuario
        }
    fun getUserByRole(role:String): MutableLiveData<List<Usuario>> {
        repository.getUserByRole(role)
        return usuario
    }
    fun getUserById(iduser:String): MutableLiveData<Usuario> {
        repository.getUserById(iduser)
        return usuarioUnico
    }
    }
