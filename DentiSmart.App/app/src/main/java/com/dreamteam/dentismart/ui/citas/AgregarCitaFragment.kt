package com.dreamteam.dentismart.ui.citas

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.dreamteam.dentismart.R
import com.dreamteam.dentismart.models.*
import com.dreamteam.dentismart.utils.SessionManagement
import com.dreamteam.dentismart.viewmodels.*
import com.muddzdev.styleabletoast.StyleableToast
import java.text.SimpleDateFormat
import java.util.*
import android.R.layout.simple_spinner_item as simple_spinner_item1


class AgregarCitaFragment : Fragment() {
    private lateinit var consultorioViewModel: ConsultorioViewModel
    private lateinit var servicioViewModel: ServiciosViewModel
    private lateinit var usuarioViewModel: UsuarioViewModel
    private lateinit var citasViewModel: CitaViewModel
    private lateinit var root: View
    private lateinit var role: String
    private lateinit var PacienteSeleccionado: Usuario
    private lateinit var DentistaSeleccionado: Usuario
    private lateinit var ConsultorioSeleccionado: Consultorio
    private lateinit var ServicioSeleccionado: Servicio
    private var FechaCita: String = ""
    private var FechaDate: String = ""
    private var FechaTime: String = ""
    private lateinit var FechaRegistro: String
    private lateinit var Spiconsultorios: Spinner
    private lateinit var Spiservicios: Spinner
    private lateinit var Spidentistas: Spinner
    private lateinit var Spipacientes: Spinner
    private lateinit var hora: ImageButton
    private lateinit var textPacientes: TextView
    private lateinit var fecha: ImageButton
    private lateinit var btnGuardar: Button
    private lateinit var ad: ArrayAdapter<Consultorio>
    private lateinit var aduser: ArrayAdapter<Usuario>
    private lateinit var adservicio: ArrayAdapter<Servicio>
    private lateinit var ViewModel: LoginViewModel
    private lateinit var cita: Cita
    private var IsPaciente: Boolean = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        root = inflater.inflate(R.layout.fragment_agregar_cita, container, false)
        Spiconsultorios = root.findViewById(R.id.spinConsultorio)
        Spipacientes = root.findViewById(R.id.spinPacientes)
        Spidentistas = root.findViewById(R.id.spinDentista)
        Spiservicios = root.findViewById(R.id.spinServicio)
        hora = root.findViewById(R.id.horaButton)
        btnGuardar = root.findViewById(R.id.btnGuardar)
        fecha = root.findViewById(R.id.fechaButton)
        consultorioViewModel = ConsultorioViewModel()
        usuarioViewModel = UsuarioViewModel()
        servicioViewModel = ServiciosViewModel()
        citasViewModel = CitaViewModel()
        textPacientes = root.findViewById(R.id.textPaciente)
        fecha.isEnabled = false
        val observer: Observer<String> = Observer<String> { message ->
            context?.let {
                StyleableToast.makeText(
                    it,
                    message,
                    R.style.toastGood
                ).show()
            }
        }

        val observerResponse: Observer<ServiceResponse> = Observer<ServiceResponse> { response ->
            if(response.status == 1 ){
                citasViewModel.CreateCitas(cita)
            }
            else{
                context?.let {
                    StyleableToast.makeText(
                        it,
                        "Cita no disponible",
                        R.style.toastBad
                    ).show()
                }
            }
        }
        val observerCita: Observer<Cita> = Observer<Cita> {
            context?.let {
                StyleableToast.makeText(
                    it,
                    "Se agendo cita correctamente",
                    R.style.toastGood
                ).show()
            }
        }
        val session = context?.applicationContext?.let { SessionManagement(it) }
        val currentUser: LoginResult? = session?.getCurrentUser()
        if (!currentUser?.role.equals("Paciente")) {
            IsPaciente = false
            ObtenerPacientes("Paciente") //En caso de no ser paciente obtener todos los pacientes
            ObtenerConsultorios()
        } else {
            IsPaciente = true
            val observerUsuario: Observer<Usuario> =
                Observer<Usuario> { usuarioLogueado ->
                    PacienteSeleccionado = usuarioLogueado
                }
            currentUser?.id?.let {
                usuarioViewModel.getUserById(it).observe(
                    viewLifecycleOwner,
                    observerUsuario
                )
            }

            Spipacientes.visibility = View.GONE
            textPacientes.visibility = View.GONE
            //val userlogin= currentUser?.id?.let { getUserById(it) }
            ObtenerConsultorios()
        }


        // Objeto para dar de alta y para comprobar disponibilidad --- cita=Cita()

        /*Fecha y hora */
        if (Spidentistas.selectedItem != "") {
            fecha.isEnabled = true
        }
        fecha.setOnClickListener {
            showDatePickerDialog()
        }
        hora.setOnClickListener {
            showTimePickerDialog()
        }
        btnGuardar.setOnClickListener {
            if (IsPaciente) {
                GuardarCitaRolPaciente()
            } else {
                GuardarCitaRolAdmin()
            }
        }
        citasViewModel.messageResponse.observe(viewLifecycleOwner, observer)
        citasViewModel.respuesta.observe(viewLifecycleOwner, observerResponse)
        citasViewModel.cita.observe(viewLifecycleOwner, observerCita)
        return root
    }

    @SuppressLint("SimpleDateFormat")
    private fun GuardarCitaRolPaciente() {
        if (Spiconsultorios.selectedItem != "" && Spidentistas.selectedItem != "" && Spiservicios.selectedItem != "") {
            val ser: List<Servicio> = listOf(ServicioSeleccionado)
            if (FechaDate.isEmpty() || FechaTime.isEmpty()) {
                context?.let {
                    StyleableToast.makeText(
                        it,
                        "Debes seleccionar la hora",
                        R.style.toastWarning
                    ).show()
                }
                return
            }
            val format = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'")
            val datepaciente: Date = format.parse("${FechaDate}T$FechaTime:00Z")
            val datedentistaEntrada: Date =
                format.parse("${FechaDate}T${DentistaSeleccionado.horaInicio}:00Z")
            val datedentistaSalida: Date =
                format.parse("${FechaDate}T${DentistaSeleccionado.horaFin}:00Z")
            val calendar: Calendar = GregorianCalendar.getInstance()
            val calendarEntrada: Calendar = GregorianCalendar.getInstance()
            val calendarSalida: Calendar = GregorianCalendar.getInstance()
            calendar.time = datepaciente
            calendarEntrada.time = datedentistaEntrada
            calendarSalida.time = datedentistaSalida
            if (calendar.get(Calendar.HOUR_OF_DAY) < calendarEntrada.get(Calendar.HOUR_OF_DAY) ||
                calendar.get(Calendar.HOUR_OF_DAY) > calendarSalida.get(Calendar.HOUR_OF_DAY)
            ) {
                context?.let {
                    StyleableToast.makeText(
                        it,
                        "El dentista solo atiende\n${DentistaSeleccionado.horaInicio}-${DentistaSeleccionado.horaFin}",
                        R.style.toastWarning
                    ).show()
                }
                return
            }
            cita = Cita(
                paciente = PacienteSeleccionado,
                dentista = DentistaSeleccionado,
                fechaCita = "${FechaDate}T$FechaTime:00Z",
                servicios = ser,
                status = "Pendiente"
            )
            citasViewModel.checkAvailabilityCitas(cita)
        }

    }

    private fun GuardarCitaRolAdmin() {
        if (Spipacientes.selectedItem != "" && Spiconsultorios.selectedItem != "" && Spidentistas.selectedItem != "" && Spiservicios.selectedItem != "") {
            val ser: List<Servicio> = listOf(ServicioSeleccionado)
            if(FechaDate.isEmpty()|| FechaTime.isEmpty()){
                context?.let {
                    StyleableToast.makeText(
                        it,
                        "Debes seleccionar la hora",
                        R.style.toastWarning
                    ).show()
                }
                return
            }
            //Toast.makeText(context,"${FechaDate}T$FechaTime:00Z", Toast.LENGTH_SHORT).show()
            cita = Cita(
                paciente = PacienteSeleccionado,
                dentista = DentistaSeleccionado,
                fechaCita = "${FechaDate}T$FechaTime:00Z",
                servicios = ser,
                status = "Pendiente"
            )
            citasViewModel.checkAvailabilityCitas(cita)
        }
    }


    private fun getDentistasByIdConsutorio(id: String) {
        val observerDentista: Observer<List<Usuario>> =
                Observer<List<Usuario>>
                { dentista ->
                    var adapter = context?.let {
                        aduser = ArrayAdapter(it, simple_spinner_item1, dentista)
                        aduser.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                        Spidentistas.adapter = aduser
                        Spidentistas.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                            override fun onNothingSelected(parent: AdapterView<*>?) {

                            }

                            override fun onItemSelected(
                                parent: AdapterView<*>?,
                                view: View?,
                                position: Int,
                                id: Long
                            ) {
                                if (parent != null) {
                                    DentistaSeleccionado = parent.getItemAtPosition(position) as Usuario
                                }
                            }
                        }
                    }
                }
        usuarioViewModel.getDentistasByIdConsultiorio(id).observe(
            viewLifecycleOwner,
            observerDentista
        )
    }

    private fun ObtenerServiciosPorIdConsultorio(id: String) {
        val observerServicios: Observer<List<Servicio>> =
                Observer<List<Servicio>> { servicios ->
                    var adapter = context?.let {
                        adservicio = ArrayAdapter(
                            it,
                            android.R.layout.simple_list_item_1,
                            servicios
                        )
                        adservicio.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                        Spiservicios.adapter = adservicio
                    }
                }
        servicioViewModel.getServicios(id).observe(viewLifecycleOwner, observerServicios)
        Spiservicios.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("Not yet implemented")
            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                if (parent != null) {
                    ServicioSeleccionado = parent.getItemAtPosition(position) as Servicio
                }
            }
        }
    }

    private fun ObtenerPacientes(role: String) {
        val observerPaciente: Observer<List<Usuario>> =
                Observer<List<Usuario>> { pacientes ->
                    var adapter = context?.let {
                        aduser = ArrayAdapter(it, android.R.layout.simple_list_item_1, pacientes)
                        aduser.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                        Spipacientes.adapter = aduser
                    }
                }
        usuarioViewModel.getUserByRole(role).observe(viewLifecycleOwner, observerPaciente)
        Spipacientes.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("Not yet implemented")
            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                if (parent != null) {
                    PacienteSeleccionado = parent.getItemAtPosition(position) as Usuario
                }
            }
        }
    }

    //Fecha del dia en dd/mm/yyyy
    private fun showDatePickerDialog() {
        val newFragment = DatePickerFragment.newInstance(DatePickerDialog.OnDateSetListener { _, year, month, day ->
            val selected = "$year-${month.twoDigits()}-${day.twoDigits()}"
            FechaDate = selected

        })
        newFragment.show(this.childFragmentManager, "datePicker")
    }

    fun Int.twoDigits() =
            if (this <= 9) "0$this" else this.toString()

    //Hora en hr:min
    private fun showTimePickerDialog() {
        var selectedHora: String
        val newFragment = TimerPickerFragment.newInstance(TimePickerDialog.OnTimeSetListener()
        { _, hora, minuto ->
            val horastr = hora.twoDigits()
            val minutosstr = minuto.twoDigits()
            selectedHora = "$horastr:$minutosstr"
            FechaTime = selectedHora
        })
        newFragment.show(this.childFragmentManager, "timePicker")
    }


    private fun ObtenerConsultorios() {
        val observerConsultorio: Observer<List<Consultorio>> =
                Observer<List<Consultorio>> { consultorios ->
                    var adapter = context?.let {
                        ad = ArrayAdapter(it, simple_spinner_item1, consultorios)
                        ad.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                        Spiconsultorios.adapter = ad
                        Spiconsultorios.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                            override fun onNothingSelected(parent: AdapterView<*>?) {
                                TODO("Not yet implemented")
                            }

                            override fun onItemSelected(
                                parent: AdapterView<*>?,
                                view: View?,
                                position: Int,
                                id: Long
                            ) {
                                if (parent != null) {
                                    ConsultorioSeleccionado = parent.getItemAtPosition(position) as Consultorio
                                    getDentistasByIdConsutorio(ConsultorioSeleccionado.id)
                                    ObtenerServiciosPorIdConsultorio(ConsultorioSeleccionado.id)
                                }
                            }
                        }
                    }
                }
        consultorioViewModel.getConsultorios().observe(viewLifecycleOwner, observerConsultorio)
    }
}





