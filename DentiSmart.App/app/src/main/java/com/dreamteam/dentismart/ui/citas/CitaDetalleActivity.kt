package com.dreamteam.dentismart.ui.citas

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.dreamteam.dentismart.R
import com.dreamteam.dentismart.databinding.ActivityCitaDetalleBinding
import com.dreamteam.dentismart.models.*
import com.dreamteam.dentismart.utils.SessionManagement
import com.dreamteam.dentismart.viewmodels.*
import com.muddzdev.styleabletoast.StyleableToast
import java.text.SimpleDateFormat
import java.util.*
import android.R.layout.simple_spinner_item as simple_spinner_item1


class CitaDetalleActivity : AppCompatActivity() {
    private lateinit var consultorioViewModel: ConsultorioViewModel
    private lateinit var servicioViewModel: ServiciosViewModel
    private lateinit var usuarioViewModel: UsuarioViewModel
    private lateinit var citasViewModel: CitaViewModel
    private lateinit var role: String
    private lateinit var PacienteSeleccionado: Usuario
    private lateinit var DentistaSeleccionado: Usuario
    private lateinit var ConsultorioSeleccionado: Consultorio
    private lateinit var ServicioSeleccionado: Servicio
    private var FechaCita: String = ""
    private var FechaDate: String = ""
    private var FechaTime: String = ""
    private lateinit var fecha: ImageButton
    private lateinit var ad: ArrayAdapter<Consultorio>
    private lateinit var aduser: ArrayAdapter<Usuario>
    private lateinit var adservicio: ArrayAdapter<Servicio>
    private lateinit var cita: Cita
    private lateinit var textFecha: TextView
    private var flagDentista: Boolean = true
    private var flagServicio: Boolean = true
    private lateinit var binding: ActivityCitaDetalleBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCitaDetalleBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        cita = intent.getSerializableExtra("cita") as Cita
        supportActionBar?.setBackgroundDrawable(ColorDrawable(Color.parseColor("#448aff")))
        val parser = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")
        val formatter = SimpleDateFormat("dd/MM/yyyy HH:mm")
        val output: String = formatter.format(parser.parse(cita.fechaCita))
        title = "$output - ${cita.dentista.nombre}"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        consultorioViewModel = ConsultorioViewModel()
        usuarioViewModel = UsuarioViewModel()
        servicioViewModel = ServiciosViewModel()
        citasViewModel = CitaViewModel()
        binding.fechaButtonDetalle.isEnabled = false
        binding.textViewhora.text= output
        val observer: Observer<CaseResponse> = Observer<CaseResponse> { response ->
              if(response.succes){
                  StyleableToast.makeText(
                      this,
                      response.message,
                      R.style.toastGood
                  ).show()
                  if(response.type == 2){
                      finish()
                  }
              }
        }

        val observerResponse: Observer<ServiceResponse> = Observer<ServiceResponse> { response ->
            if(response.status == 1 ){
                citasViewModel.UpdateCitas(cita)
            }
            else{
                    StyleableToast.makeText(
                        this,
                        "Cita no disponible",
                        R.style.toastBad
                    ).show()
            }
        }
        val observerCita: Observer<Cita> = Observer<Cita> {

                StyleableToast.makeText(
                    this,
                    "Se agendo cita correctamente",
                    R.style.toastGood
                ).show()
        }
        val session =  SessionManagement(this)
        val currentUser: LoginResult? = session.getCurrentUser()
            val observerUsuario: Observer<Usuario> =
                Observer<Usuario> { usuarioLogueado ->
                    PacienteSeleccionado = usuarioLogueado
                }
            currentUser?.id?.let { usuarioViewModel.getUserById(it).observe(
                this,
                observerUsuario
            ) }
        ObtenerConsultorios()
        if (binding.spinDentistaDetalle.selectedItem != "") {
            binding.fechaButtonDetalle.isEnabled = true
        }
        binding.fechaButtonDetalle.setOnClickListener {
            showDatePickerDialog()
        }
        binding.horaButtonDetalle.setOnClickListener {
            showTimePickerDialog()
        }

        binding.btnGuardarDetalle.setOnClickListener {
                GuardarCitaRolPaciente()
        }
        binding.btnCancelarDetalle.setOnClickListener{
            val dialog: AlertDialog = AlertDialog.Builder(this)
                .setTitle("Cancelar cita")
                .setMessage("¿Esta seguro de cancelar cita?")
                .setPositiveButton("Si", null)
                .setNegativeButton("No", null)
                .show()
            val positiveButton: Button = dialog.getButton(AlertDialog.BUTTON_POSITIVE)
            positiveButton.setOnClickListener {
                cita.id?.let { id -> citasViewModel.DeleteCitas(id) }
                dialog.dismiss()
            }
        }
        citasViewModel.caseResponse.observe(this, observer)
        citasViewModel.respuesta.observe(this, observerResponse)
        citasViewModel.cita.observe(this, observerCita)
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
        }
        return super.onOptionsItemSelected(item)
    }
    private fun GuardarCitaRolPaciente() {
        if (binding.spinConsultorioDetalle.selectedItem != "" && binding.spinDentistaDetalle.selectedItem != "" && binding.spinServicioDetalle.selectedItem != "") {
            val ser: List<Servicio> = listOf(ServicioSeleccionado)
            if (FechaDate.isEmpty() || FechaTime.isEmpty()) {
                FechaCita = cita.fechaCita
            } else {
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

                    StyleableToast.makeText(
                        this,
                        "El dentista solo atiende\n${DentistaSeleccionado.horaInicio}-${DentistaSeleccionado.horaFin}",
                        R.style.toastWarning
                    ).show()

                    return
                }
                FechaCita = "${FechaDate}T$FechaTime:00Z"
            }
            cita = Cita(
                id = cita.id,
                paciente = PacienteSeleccionado,
                dentista = DentistaSeleccionado,
                fechaCita = FechaCita,
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
                    aduser = ArrayAdapter(this, simple_spinner_item1, dentista)
                    aduser.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                    binding.spinDentistaDetalle.adapter = aduser
                    if(flagDentista && id == cita.dentista.consultorio){
                        binding.spinDentistaDetalle.setSelection(indexOfDentista(dentista, cita.dentista.id))
                        flagDentista = false
                    }
                    binding.spinDentistaDetalle.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                        override fun onNothingSelected(parent: AdapterView<*>?) {
                            // To Do
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
        usuarioViewModel.getDentistasByIdConsultiorio(id).observe(
            this,
            observerDentista
        )
    }
    private fun ObtenerServiciosPorIdConsultorio(id: String) {
        val observerServicios: Observer<List<Servicio>> =
            Observer<List<Servicio>> { servicios ->
                    adservicio = ArrayAdapter(
                        this,
                        android.R.layout.simple_list_item_1,
                        servicios
                    )
                    adservicio.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                binding.spinServicioDetalle.adapter = adservicio
                if(flagDentista && id == cita.dentista.consultorio){
                    binding.spinServicioDetalle.setSelection(indexOfServicio(servicios, cita.servicios[0].id))
                    flagServicio = false
                }
            }
        servicioViewModel.getServicios(id).observe(this, observerServicios)
        binding.spinServicioDetalle.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
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

    private fun showDatePickerDialog() {
        val newFragment = DatePickerFragment.newInstance(DatePickerDialog.OnDateSetListener { _, year, month, day ->
            val selected = "$year-${month.twoDigits()}-${day.twoDigits()}"
            FechaDate = selected

        })
        newFragment.show(supportFragmentManager, "datePicker")
    }

    private fun Int.twoDigits() =
        if (this <= 9) "0$this" else this.toString()

    //Hora en hr:min
    private fun showTimePickerDialog() {
        var selectedHora: String
        val newFragment = TimerPickerFragment.newInstance { _, hora, minuto ->
            val horastr = hora.twoDigits()
            val minutosstr = minuto.twoDigits()
            selectedHora = "$horastr:$minutosstr"
            FechaTime = selectedHora
        }
        newFragment.show(supportFragmentManager, "timePicker")
    }


    private fun ObtenerConsultorios() {
        val observerConsultorio: Observer<List<Consultorio>> =
            Observer<List<Consultorio>> { consultorios ->
                    ad = ArrayAdapter(this, simple_spinner_item1, consultorios)
                    ad.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                    binding.spinConsultorioDetalle.adapter = ad
                    binding.spinConsultorioDetalle.setSelection(
                        indexOfConsultorio(
                            consultorios,
                            cita.dentista.consultorio
                        )
                    )
                    binding.spinConsultorioDetalle.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
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
        consultorioViewModel.getConsultorios().observe(this, observerConsultorio)
    }

    fun indexOfConsultorio(consultorios: List<Consultorio>, id: String?): Int{
        for ((pos, consultorio: Consultorio) in consultorios.withIndex()){
            if(consultorio.id == id)
                return pos
        }
        return  -1
    }
    fun indexOfDentista(dentistas: List<Usuario>, id: String?):Int{
        for ((pos, dentista: Usuario) in dentistas.withIndex()){
            if(dentista.id == id)
                return pos
        }
        return  -1
    }

    fun indexOfServicio(sericios: List<Servicio>, id: String?):Int{
        for ((pos, sericio: Servicio) in sericios.withIndex()){
            if(sericio.id == id)
                return pos
        }
        return  -1
    }
}