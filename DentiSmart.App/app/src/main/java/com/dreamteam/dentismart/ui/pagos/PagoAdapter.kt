package com.dreamteam.dentismart.ui.pagos


import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.dreamteam.dentismart.R
import com.dreamteam.dentismart.models.Cita
import com.dreamteam.dentismart.models.Pago
import com.dreamteam.dentismart.models.Servicio
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import java.text.SimpleDateFormat
import java.util.stream.Collectors

class PagoAdapter(val pagoList: ArrayList<Pago>, val context: Context) : RecyclerView.Adapter<PagoAdapter.ViewHolder>() {
    var originalItems:ArrayList<Pago> = ArrayList()
    override fun onCreateViewHolder(viewGroup: ViewGroup, pos: Int): ViewHolder {
        val v = LayoutInflater.from(viewGroup.context).inflate(
            R.layout.card_pagos_layout,
            viewGroup,
            false
        )
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return pagoList.size
    }

    @SuppressLint("SimpleDateFormat", "SetTextI18n")
    override fun onBindViewHolder(viewHolder: ViewHolder, pos: Int) {
        val parser = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")
        val formatter = SimpleDateFormat("dd/MM/yyyy HH:mm")
        val output: String = formatter.format(parser.parse(pagoList[pos].cita.fechaCita))
        val parserPago = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")
        val formatterPago = SimpleDateFormat("dd/MM/yyyy")
        val outputPago: String = formatterPago.format(parserPago.parse(pagoList[pos].fechaPago))
        viewHolder.fecha.text = output
        val nombrePaciente =
            "${pagoList[pos].cita.paciente.nombre} ${pagoList[pos].cita.paciente.apellidoPat} ${pagoList[pos].cita.paciente.apellidoMat}"
        val nombreDentista =
            "${pagoList[pos].cita.dentista.nombre} ${pagoList[pos].cita.dentista.apellidoPat} ${pagoList[pos].cita.dentista.apellidoMat}"
        viewHolder.pacienteNombre.text = nombrePaciente
        viewHolder.dentistaNombre.text = nombreDentista
        viewHolder.montoPago.text = "$"+pagoList[pos].monto
        viewHolder.addServicios(pagoList[pos].cita.servicios, context)
        viewHolder.fechaPago.text = outputPago
    }
    fun filter(strSearch: String) {
        if (strSearch.isEmpty()) {
            pagoList.clear()
            pagoList.addAll(originalItems)
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                pagoList.clear()
                val collect: List<Pago> = originalItems.stream()
                    .filter { i ->
                        i.cita.dentista.nombre.toLowerCase().contains(strSearch)
                                || i.cita.dentista.apellidoPat.toLowerCase().contains(strSearch)
                                ||  i.cita.dentista.apellidoMat.toLowerCase().contains(strSearch)
                                ||  i.cita.fechaCita.toLowerCase().contains(strSearch)
                                ||  i.fechaPago.toLowerCase().contains(strSearch)
                    }
                    .collect(Collectors.toList())
                pagoList.addAll(collect)
            } else {
                pagoList.clear()
                for (i in originalItems) {
                    if (i.cita.dentista.nombre.toLowerCase().contains(strSearch)
                        || i.cita.dentista.apellidoPat.toLowerCase().contains(strSearch)
                        ||  i.cita.dentista.apellidoMat.toLowerCase().contains(strSearch)
                        ||  i.cita.fechaCita.toLowerCase().contains(strSearch)
                        ||  i.fechaPago.toLowerCase().contains(strSearch) ) {
                        pagoList.add(i)
                    }
                }
            }
        }
        notifyDataSetChanged()
    }
    init{
        this.originalItems = ArrayList()
        originalItems.addAll(pagoList)
    }
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val fecha = itemView.findViewById(R.id.fecha_cita_pago) as TextView
        val pacienteNombre = itemView.findViewById(R.id.paciente_cita_pago) as TextView
        val dentistaNombre = itemView.findViewById(R.id.dentista_cita_pago) as TextView
        val montoPago = itemView.findViewById(R.id.moto_pago) as TextView
        val fechaPago = itemView.findViewById(R.id.fecha_pago) as TextView
        private val chipGroup = itemView.findViewById(R.id.chipGroup_servicios_pago) as ChipGroup
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
