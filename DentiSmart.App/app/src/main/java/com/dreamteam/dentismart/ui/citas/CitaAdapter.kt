package com.dreamteam.dentismart.ui.citas

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.res.ColorStateList
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.dreamteam.dentismart.R
import com.dreamteam.dentismart.models.Cita
import com.dreamteam.dentismart.models.Servicio
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import java.text.SimpleDateFormat
import java.util.stream.Collectors


class CitaAdapter(val citaList: ArrayList<Cita>, val context: Context) : RecyclerView.Adapter<CitaAdapter.ViewHolder>() {
        private var originalItems:ArrayList<Cita> = ArrayList()
        override fun onCreateViewHolder(viewGroup: ViewGroup, pos: Int): ViewHolder {
                val v = LayoutInflater.from(viewGroup.context).inflate(
                        R.layout.card_citas_layout,
                        viewGroup,
                        false
                )
                return ViewHolder(v)
        }
        override fun getItemCount(): Int {
                return citaList.size
        }
        @SuppressLint("SimpleDateFormat")
        override fun onBindViewHolder(viewHolder: ViewHolder, pos: Int) {

                val parser = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")
                val formatter = SimpleDateFormat("dd/MM/yyyy HH:mm")
                val output: String = formatter.format(parser.parse(citaList[pos].fechaCita))
                viewHolder.fecha.text = output
                val nombrePaciente = "${citaList[pos].paciente.nombre} ${citaList[pos].paciente.apellidoPat} ${citaList[pos].paciente.apellidoMat}"
                val nombreDentista = "${citaList[pos].dentista.nombre} ${citaList[pos].dentista.apellidoPat} ${citaList[pos].dentista.apellidoMat}"
                viewHolder.pacienteNombre.text = nombrePaciente
                viewHolder.dentistaNombre.text = nombreDentista
                viewHolder.addServicios(citaList[pos].servicios, context)
                viewHolder.cita = citaList[pos]
                if(citaList[pos].pagada){
                        viewHolder.chipPaga.chipBackgroundColor =  ColorStateList.valueOf(
                                ContextCompat.getColor(context, R.color.colorGreen)
                        )
                        viewHolder.chipPaga.text="Pagada"
                }
                else{
                        viewHolder.chipPaga.chipBackgroundColor =  ColorStateList.valueOf(
                                ContextCompat.getColor(context, R.color.coloRed)
                        )
                        viewHolder.chipPaga.text="No Pagada"
                }

        }
        fun filter(strSearch: String) {
                if (strSearch.isEmpty()) {
                        citaList.clear()
                        citaList.addAll(originalItems)
                } else {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                                citaList.clear()
                                val collect: List<Cita> = originalItems.stream()
                                        .filter { i ->
                                                i.dentista.nombre.toLowerCase().contains(strSearch)
                                                        || i.dentista.apellidoPat.toLowerCase().contains(
                                                        strSearch
                                                )
                                                        ||  i.dentista.apellidoMat.toLowerCase().contains(
                                                        strSearch
                                                )
                                                        ||  i.fechaCita.toLowerCase().contains(
                                                        strSearch
                                                )
                                        }
                                        .collect(Collectors.toList())
                                citaList.addAll(collect)
                        } else {
                                citaList.clear()
                                for (i in originalItems) {
                                        if (i.dentista.nombre.toLowerCase().contains(strSearch)  || i.dentista.apellidoPat.toLowerCase().contains(
                                                        strSearch
                                                )
                                                ||  i.dentista.apellidoMat.toLowerCase().contains(
                                                        strSearch
                                                )
                                                ||  i.fechaCita.toLowerCase().contains(strSearch) ) {
                                                citaList.add(i)
                                        }
                                }
                        }
                }
                notifyDataSetChanged()
        }
        init{
                this.originalItems = ArrayList()
                originalItems.addAll(citaList)
        }
        class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
                val fecha = itemView.findViewById(R.id.fecha_cita_proximas) as TextView
                val pacienteNombre = itemView.findViewById(R.id.paciente_cita_proximas) as TextView
                val dentistaNombre = itemView.findViewById(R.id.dentista_cita_proximas) as TextView
                val chipPaga = itemView.findViewById(R.id.pagada) as Chip
                private val chipGroup = itemView.findViewById(R.id.chipGroup_servicios_citas) as ChipGroup
                private val btnDetalle = itemView.findViewById(R.id.btn_detalle_cita) as Button
                lateinit var cita:Cita
                init {
                    btnDetalle.setOnClickListener{
                            val activity = itemView.context as AppCompatActivity
                            val intent = Intent(itemView.context, CitaDetalleActivity::class.java)
                            val bundle = Bundle()
                            intent.putExtra("cita", cita )
                            activity.startActivity(intent)
                    }
                }
                fun addServicios(servicios: List<Servicio>, context: Context)
                {
                        chipGroup.removeAllViews()
                        for (item:Servicio in servicios){
                                val chip = Chip(context)
                                chip.text = item.nombre
                                chip.isClickable = true
                                chipGroup.addView(chip as View)
                        }
                }
        }

}