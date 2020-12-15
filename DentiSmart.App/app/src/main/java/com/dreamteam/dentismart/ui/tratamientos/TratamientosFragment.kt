package com.dreamteam.dentismart.ui.tratamientos

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dreamteam.dentismart.R
import com.dreamteam.dentismart.models.LoginResult
import com.dreamteam.dentismart.models.Pago
import com.dreamteam.dentismart.models.Tratamiento
import com.dreamteam.dentismart.ui.pagos.PagoAdapter
import com.dreamteam.dentismart.utils.SessionManagement
import com.dreamteam.dentismart.viewmodels.PagoViewModel
import com.dreamteam.dentismart.viewmodels.TratamientoViewModel

class TratamientosFragment : Fragment(),  SearchView.OnQueryTextListener {

    private lateinit var tratamientoViewModel: TratamientoViewModel
    private lateinit var tratamientoAdapter: TratamientoAdapter
    @SuppressLint("WrongConstant")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_tratamientos, container, false)
        val recyclerView = root.findViewById<RecyclerView>(R.id.recyclerTratamiento)
        recyclerView.layoutManager = LinearLayoutManager(context, LinearLayout.VERTICAL, false)
        tratamientoViewModel = TratamientoViewModel()
        val session = context?.applicationContext?.let { SessionManagement(it) }
        val currentUser: LoginResult? = session?.getCurrentUser()
        val svSearch: SearchView = root.findViewById(R.id.tratamientoSearch)
        val cl: LinearLayout = root.findViewById(R.id.frag_tratamientos_layout)
        val progressBar = ProgressBar(context, null, android.R.attr.progressBarStyleLarge)
        val lp = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        progressBar.layoutParams = lp
        progressBar.isIndeterminate = true
        cl.addView(progressBar)
        val observerTratamiento: Observer<List<Tratamiento>> = Observer<List<Tratamiento>> { response ->
            tratamientoAdapter = context?.let { TratamientoAdapter(response as ArrayList<Tratamiento>, it) }!!
            recyclerView.adapter= tratamientoAdapter
            svSearch.setOnQueryTextListener(this)
            progressBar.visibility = View.INVISIBLE
        }
        tratamientoViewModel.getTratamientosPaciente(currentUser!!.id).observe(viewLifecycleOwner, observerTratamiento)
        return root
    }

    override fun onQueryTextSubmit(query: String): Boolean {
       return false
    }

    override fun onQueryTextChange(newText: String): Boolean {
        tratamientoAdapter.filter(newText)
        return false
    }


}