package com.dreamteam.dentismart.ui.citas

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

class PageAdapter(fm:FragmentManager): FragmentPagerAdapter(fm) {
   override fun getItem(position: Int): Fragment {
      when (position) {
         0 -> {return AgregarCitaFragment()}
         1 -> {return VerCitasFragment()}
         else -> {return AgregarCitaFragment()}
      }
   }
   override fun getCount(): Int {
      return 2
   }

   override fun getPageTitle(position: Int): CharSequence? {
      when (position) {
         0 -> {return "AGREGAR CITA"}
         1 -> {return "PROXIMAS CITAS"}
         else -> {return "AGREGAR CITA"}
      }
   }
}