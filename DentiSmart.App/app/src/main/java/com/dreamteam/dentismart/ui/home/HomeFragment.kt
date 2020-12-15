package com.dreamteam.dentismart.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.dreamteam.dentismart.R


class HomeFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_home, container, false)
        val tratamientosCard:CardView = root.findViewById(R.id.frag_tratamientos)
        val pagosCard:CardView = root.findViewById(R.id.frag_pagos)
        val citasCard:CardView = root.findViewById(R.id.frag_citas)
        tratamientosCard.setOnClickListener  {
            activity?.findNavController(R.id.nav_host_fragment)?.navigate(R.id.nav_tratamientos)
        }
        pagosCard.setOnClickListener  {
            activity?.findNavController(R.id.nav_host_fragment)?.navigate(R.id.nav_pagos)
        }
        citasCard.setOnClickListener  {
            activity?.findNavController(R.id.nav_host_fragment)?.navigate(R.id.nav_citas)
        }
        return root
    }
}