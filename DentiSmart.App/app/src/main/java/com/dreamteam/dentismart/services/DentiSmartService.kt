package com.dreamteam.dentismart.services
import com.dreamteam.dentismart.models.*
import retrofit2.Call
import retrofit2.http.*


interface DentiSmartService {

    //Consultorios
    @GET("Consultorio")
    fun getConsultorio(): Call<List<Consultorio>>

    //Login
    @POST("Login")
    fun login(@Body body: LoginCredentials): Call<LoginResult>?

    //Servicios
    @GET("Servicio/Consultorio/{id}")
    fun getServicios(@Path("id")idConsultorio: String): Call<List<Servicio>>
    //Usuarios
    //Dentista
    @GET("Usuario/Dentista/{id}")
    fun getDentistasByIdConsultorio(@Path("id")idDentista: String): Call<List<Usuario>>

    //Paciente
    @GET("Usuario")
    fun getUsuarios(): Call<List<Usuario>>

    @POST("Usuario")
    fun createUsuario(@Body usuario:Usuario): Call<Usuario>

    @POST("Usuario")
    fun updateUsuario(@Body usuario:Usuario): Call<Usuario>

    @GET("Usuario/{id}")
    fun getUserById(@Path("id") idUsuario:String): Call<Usuario>

    @DELETE("Usuario/{id}")
    fun deleteUserById(@Path("id") idUsuario:String)

    @GET("Usuario/Role/{role}")
    fun getUserByRole(@Path("role") role:String): Call<List<Usuario>>

    //Citas
     @GET("Cita")
     fun getCitas():Call<List<Cita>>

     @POST("Cita")
     fun createCitas(@Body cita:Cita): Call<Cita>

     @PUT("Cita")
     fun updateCitas(@Body cita:Cita):Call<Void>

     @GET("Cita/{id}")
     fun getCitasById(@Path("id")idCita: String): Call<List<Cita>>

     @DELETE("Cita/{id}")
     fun deleteCitasById(@Path("id")idCita: String): Call<Void>

     @POST("Cita/Disponible")
     fun checkAvailabilityCitas(@Body cita:Cita): Call<ServiceResponse>

     @GET("Cita/Consultorio/{consultorio}")
     fun getCitasByIdConsultorio(@Path("consultorio")idconsutorio: String): Call<List<Cita>>

    @GET("Cita/Dentista/{dentista}")
    fun getCitasByIdDentista(@Path("dentista")idDentista : String): Call<List<Cita>>

    @GET("Cita/Paciente/{paciente}")
    fun getCitasByIdPaciente(@Path("paciente")idPaciente : String): Call<List<Cita>>

    //Pagos
    @GET("Pago")
    fun getPagos():Call<List<Pago>>

    @GET("Pago/{id}")
    fun getPagoById(@Path("id") id: String): Call<Tratamiento>

    @GET("Pago/Consultorio/{consultorio}")
    fun getPagoByIdConsultorio(@Path("consultorio")idconsutorio: String): Call<List<Pago>>

    @GET("Pago/Dentista/{dentista}")
    fun getPagoByIdDentista(@Path("dentista")idDentista : String): Call<List<Pago>>

    @GET("Pago/Paciente/{paciente}")
    fun getPagoByIdPaciente(@Path("paciente")idPaciente : String): Call<List<Pago>>

    //Tratamientos

    @GET("Tratamiento")
    fun getTratamientos():Call<List<Tratamiento>>

    @GET("Tratamiento/{id}")
    fun getTratamientoById(@Path("id") id: String): Call<Tratamiento>

    @GET("Tratamiento/Consultorio/{consultorio}")
    fun getTratamientoByIdConsultorio(@Path("consultorio")idconsutorio: String): Call<List<Tratamiento>>

    @GET("Tratamiento/Dentista/{dentista}")
    fun getTratamientoByIdDentista(@Path("dentista")idDentista : String): Call<List<Tratamiento>>

    @GET("Tratamiento/Paciente/{paciente}")
    fun getTratamientoByIdPaciente(@Path("paciente")idPaciente : String): Call<List<Tratamiento>>
}
