package com.dreamteam.dentismart.ui.pagos

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.ProgressBar
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dreamteam.dentismart.R
import com.dreamteam.dentismart.models.LoginResult
import com.dreamteam.dentismart.models.Pago
import com.dreamteam.dentismart.ui.citas.CitaAdapter
import com.dreamteam.dentismart.utils.SessionManagement
import com.dreamteam.dentismart.viewmodels.PagoViewModel

class PagoFragment : Fragment(),  SearchView.OnQueryTextListener{
    private lateinit var pagoViewModel: PagoViewModel
    private lateinit var pagoAdapter: PagoAdapter
    @SuppressLint("WrongConstant")
    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_pagos, container, false)
        val recyclerView = root.findViewById<RecyclerView>(R.id.recyclerPagos)
        recyclerView.layoutManager = LinearLayoutManager(context, LinearLayout.VERTICAL, false)
        pagoViewModel = PagoViewModel()
        val session = context?.applicationContext?.let { SessionManagement(it) }
        val currentUser: LoginResult? = session?.getCurrentUser()
        val svSearch: SearchView = root.findViewById(R.id.pagoSearch)
        val cl: LinearLayout = root.findViewById(R.id.frag_pagos_layout)
        val progressBar = ProgressBar(context, null, android.R.attr.progressBarStyleLarge)
        val lp = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        progressBar.layoutParams = lp
        progressBar.isIndeterminate = true
        cl.addView(progressBar)
        val observerPago: Observer<List<Pago>> = Observer<List<Pago>> { response ->
            pagoAdapter = context?.let { PagoAdapter(response as ArrayList<Pago>, it) }!!
            recyclerView.adapter= pagoAdapter
            svSearch.setOnQueryTextListener(this)
            progressBar.visibility = View.INVISIBLE
        }
        pagoViewModel.getPagosPaciente(currentUser!!.id).observe(viewLifecycleOwner, observerPago)
        return root
    }

    override fun onQueryTextSubmit(query: String): Boolean {
        return false
    }

    override fun onQueryTextChange(newText: String): Boolean {
        pagoAdapter.filter(newText)
        return false
    }
}