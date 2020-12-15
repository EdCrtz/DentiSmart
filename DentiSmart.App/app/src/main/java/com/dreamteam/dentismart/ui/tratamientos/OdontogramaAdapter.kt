package com.dreamteam.dentismart.ui.tratamientos

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.dreamteam.dentismart.R
import com.dreamteam.dentismart.databinding.CardDienteOdontogramaLayoutBinding
import com.dreamteam.dentismart.models.Diente


class OdontogramaAdapter(val odontograma: ArrayList<Diente>, val context: Context) : RecyclerView.Adapter<OdontogramaAdapter.ViewHolder>() {
    override fun onCreateViewHolder(viewGroup: ViewGroup, pos: Int): ViewHolder {
        val v = LayoutInflater.from(viewGroup.context).inflate(
            R.layout.card_diente_odontograma_layout,
            viewGroup,
            false
        )
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return odontograma.size
    }
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val binding = CardDienteOdontogramaLayoutBinding.bind(itemView)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.dienteDetalleTextview.text  = "Diente #${(position+1)}"
        holder.binding.dienteCentro.text  = obtenerValorOdontograma(odontograma[position].centro)
        holder.binding.dienteDerecha.text  = obtenerValorOdontograma(odontograma[position].derecho)
        holder.binding.dienteIzquierda.text  = obtenerValorOdontograma(odontograma[position].izquierdo)
        holder.binding.dienteSur.text  = obtenerValorOdontograma(odontograma[position].sur)
        holder.binding.dienteSuperior.text  = obtenerValorOdontograma(odontograma[position].norte)
    }
    private fun obtenerValorOdontograma(tipo:Int): String
    {
        return when (tipo) {
            1 -> "Carie"
            2 -> "Amalgama"
            3 -> "Endodoncia"
            4 -> "Ausente"
            5 -> "Resina"
            6 -> "Implante"
            7 -> "Sellante"
            8 -> "Corona"
            else -> "Normal"
        }
    }
}
