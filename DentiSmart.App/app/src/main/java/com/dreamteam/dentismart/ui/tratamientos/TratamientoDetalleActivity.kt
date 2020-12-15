package com.dreamteam.dentismart.ui.tratamientos

import android.annotation.SuppressLint
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.dreamteam.dentismart.databinding.ActivityTratamientoDetalleBinding
import com.dreamteam.dentismart.models.Detalle
import com.dreamteam.dentismart.models.Diente
import com.dreamteam.dentismart.models.Tratamiento
import java.text.SimpleDateFormat

class TratamientoDetalleActivity : AppCompatActivity() {
    private lateinit var tratamiento: Tratamiento
    private lateinit var binding: ActivityTratamientoDetalleBinding
    private var detalle = true
    @SuppressLint("WrongConstant")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTratamientoDetalleBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        tratamiento = intent.getSerializableExtra("tratamiento") as Tratamiento
        supportActionBar?.setBackgroundDrawable(ColorDrawable(Color.parseColor("#448aff")))
        val parser = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")
        val formatter = SimpleDateFormat("dd/MM/yyyy HH:mm")
        val output: String = formatter.format(parser.parse(tratamiento.cita.fechaCita))
        title = "$output - ${tratamiento.cita.dentista.nombre}"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val tratamientoDetalleAdapter = TratamientoDetalleAdapter(tratamiento.detalle as ArrayList<Detalle>, this)
        binding.recyclerDetalle.layoutManager = LinearLayoutManager(this, LinearLayout.VERTICAL, false)
        binding.recyclerDetalle.adapter= tratamientoDetalleAdapter
        val odontograma: List<Diente> = tratamiento.odontograma.dientesSuperior + tratamiento.odontograma.dientesInferior
        val odontogramaAdapter = OdontogramaAdapter( odontograma  as ArrayList<Diente>, this)
        binding.recyclerOdontograma.layoutManager = LinearLayoutManager(this, LinearLayout.VERTICAL, false)
        binding.recyclerOdontograma.adapter= odontogramaAdapter

        binding.btnviewsDetalle.setOnClickListener{
            if(detalle){
                binding.btnviewsDetalle.text="Detalle"
                binding.recyclerDetalle.visibility =  View.GONE
                binding.recyclerOdontograma.visibility =  View.VISIBLE
                detalle = false
            }
            else{
                binding.btnviewsDetalle.text="Odontograma"
                binding.recyclerDetalle.visibility =  View.VISIBLE
                binding.recyclerOdontograma.visibility =  View.GONE
                detalle = true
            }
        }
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
        }
        return super.onOptionsItemSelected(item)
    }
}