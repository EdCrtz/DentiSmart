package com.dreamteam.dentismart.ui.tratamientos

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.dreamteam.dentismart.R
import com.dreamteam.dentismart.models.Servicio
import com.dreamteam.dentismart.models.Tratamiento
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import java.text.SimpleDateFormat
import java.util.stream.Collectors

class TratamientoAdapter(val trtamientoList: ArrayList<Tratamiento>, val context: Context) : RecyclerView.Adapter<TratamientoAdapter.ViewHolder>() {
    var originalItems:ArrayList<Tratamiento> = ArrayList()
    override fun onCreateViewHolder(viewGroup: ViewGroup, pos: Int): ViewHolder {
        val v = LayoutInflater.from(viewGroup.context).inflate(
            R.layout.card_tratamiento_layout,
            viewGroup,
            false
        )
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return trtamientoList.size
    }

    @SuppressLint("SimpleDateFormat", "SetTextI18n")
    override fun onBindViewHolder(viewHolder: ViewHolder, pos: Int) {
        val parser = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")
        val formatter = SimpleDateFormat("dd/MM/yyyy HH:mm")
        val output: String = formatter.format(parser.parse(trtamientoList[pos].cita.fechaCita))
        val parserTratamiento = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")
        val formatterTratamiento = SimpleDateFormat("dd/MM/yyyy")
        val outputTratamiento: String = formatterTratamiento.format(parserTratamiento.parse(trtamientoList[pos].fechaCreacion))
        viewHolder.fecha.text = output
        val nombrePaciente =
            "${trtamientoList[pos].cita.paciente.nombre} ${trtamientoList[pos].cita.paciente.apellidoPat} ${trtamientoList[pos].cita.paciente.apellidoMat}"
        val nombreDentista =
            "${trtamientoList[pos].cita.dentista.nombre} ${trtamientoList[pos].cita.dentista.apellidoPat} ${trtamientoList[pos].cita.dentista.apellidoMat}"
        viewHolder.pacienteNombre.text = nombrePaciente
        viewHolder.dentistaNombre.text = nombreDentista
        viewHolder.addServicios(trtamientoList[pos].cita.servicios, context)
        viewHolder.fechaTratamiento.text = outputTratamiento
        viewHolder.tratamiento = trtamientoList[pos]
    }
    fun filter(strSearch: String) {
        if (strSearch.isEmpty()) {
            trtamientoList.clear()
            trtamientoList.addAll(originalItems)
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                trtamientoList.clear()
                val collect: List<Tratamiento> = originalItems.stream()
                    .filter { i ->
                        i.cita.dentista.nombre.toLowerCase().contains(strSearch)
                                || i.cita.dentista.apellidoPat.toLowerCase().contains(strSearch)
                                ||  i.cita.dentista.apellidoMat.toLowerCase().contains(strSearch)
                                ||  i.cita.fechaCita.toLowerCase().contains(strSearch)
                                ||  i.fechaCreacion.toLowerCase().contains(strSearch)
                    }
                    .collect(Collectors.toList())
                trtamientoList.addAll(collect)
            } else {
                trtamientoList.clear()
                for (i in originalItems) {
                    if (i.cita.dentista.nombre.toLowerCase().contains(strSearch)
                        || i.cita.dentista.apellidoPat.toLowerCase().contains(strSearch)
                        ||  i.cita.dentista.apellidoMat.toLowerCase().contains(strSearch)
                        ||  i.cita.fechaCita.toLowerCase().contains(strSearch)
                        ||  i.fechaCreacion.toLowerCase().contains(strSearch) ) {
                        trtamientoList.add(i)
                    }
                }
            }
        }
        notifyDataSetChanged()
    }
    init{
        this.originalItems = ArrayList()
        originalItems.addAll(trtamientoList)
    }
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val fecha = itemView.findViewById(R.id.fecha_cita_tratamiento) as TextView
        val pacienteNombre = itemView.findViewById(R.id.paciente_cita_tratamiento) as TextView
        val dentistaNombre = itemView.findViewById(R.id.dentista_cita_tratamiento) as TextView
        val fechaTratamiento = itemView.findViewById(R.id.fecha_tratamiento) as TextView
        private val chipGroup =
            itemView.findViewById(R.id.chipGroup_servicios_tratamiento) as ChipGroup
        private val btnDetalle = itemView.findViewById(R.id.btn_detalle_tratamiento) as Button

        lateinit var tratamiento: Tratamiento

        init {
            btnDetalle.setOnClickListener {
                val activity = itemView.context as AppCompatActivity
                val intent = Intent(itemView.context, TratamientoDetalleActivity::class.java)
                val bundle = Bundle()
                intent.putExtra("tratamiento", tratamiento)
                activity.startActivity(intent)
            }
        }

        fun addServicios(servicios: List<Servicio>, context: Context) {
            chipGroup.removeAllViews()
            for (item: Servicio in servicios) {
                val chip = Chip(context)
                chip.text = item.nombre
                chip.isClickable = true
                chipGroup.addView(chip as View)
            }
        }
    }
}
