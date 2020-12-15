package com.dreamteam.dentismart.ui.tratamientos


import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.dreamteam.dentismart.R
import com.dreamteam.dentismart.databinding.CardDetalleTratamientoLayoutBinding
import com.dreamteam.dentismart.models.Detalle
import com.dreamteam.dentismart.utils.Rutinas


class TratamientoDetalleAdapter(val detalleList: ArrayList<Detalle>, val context: Context) : RecyclerView.Adapter<TratamientoDetalleAdapter.ViewHolder>() {
    override fun onCreateViewHolder(viewGroup: ViewGroup, pos: Int): ViewHolder {
        val v = LayoutInflater.from(viewGroup.context).inflate(
            R.layout.card_detalle_tratamiento_layout,
            viewGroup,
            false
        )
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return detalleList.size
    }
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val binding = CardDetalleTratamientoLayoutBinding.bind(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.servicioDetalleTextview.text  = detalleList[position].servicio.nombre
        holder.binding.tratamientoObservacionDetalle.text  = detalleList[position].observacion
        holder.binding.tratamientoSintomasDetalle.text  = detalleList[position].sintomas
        holder.binding.tratamientoRecetaDetalle.text  = detalleList[position].receta
        if (detalleList[position].evidencia != "") {
            val img = Rutinas.convertBase64ToBitMap(detalleList[position].evidencia.split(',')[1])
            holder.binding.evidenciaDetalle.setImageBitmap(img)
            holder.binding.evidenciaDetalle.visibility = View.VISIBLE
        }
    }
}
