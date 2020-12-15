package com.dreamteam.dentismart.ui.citas

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
import com.dreamteam.dentismart.models.Cita
import com.dreamteam.dentismart.models.LoginResult
import com.dreamteam.dentismart.utils.SessionManagement
import com.dreamteam.dentismart.viewmodels.CitaViewModel


class VerCitasFragment : Fragment(),  SearchView.OnQueryTextListener{
   private lateinit var citasViewModel: CitaViewModel
   private lateinit var citaAdapter: CitaAdapter
   private lateinit var currentUser: LoginResult
   private lateinit var observerCita: Observer<List<Cita>>
   @SuppressLint("WrongConstant")
   override fun onCreateView(
      inflater: LayoutInflater, container: ViewGroup?,
      savedInstanceState: Bundle?
   ): View? {
      // Inflate the layout for this fragment
      val root = inflater.inflate(R.layout.fragment_ver_citas, container, false)
      val recyclerView = root.findViewById<RecyclerView>(R.id.recyclerCitas_2)
      recyclerView.layoutManager = LinearLayoutManager(context, LinearLayout.VERTICAL, false)
      citasViewModel = CitaViewModel()
      val session = context?.applicationContext?.let { SessionManagement(it) }
      currentUser = session?.getCurrentUser()!!
      val svSearch: SearchView = root.findViewById(R.id.citasSearch)

      val cl: LinearLayout = root.findViewById(R.id.frag_ver_citas)
      val progressBar = ProgressBar(context, null, android.R.attr.progressBarStyleLarge)
      val lp = LinearLayout.LayoutParams(
         LinearLayout.LayoutParams.MATCH_PARENT,
         LinearLayout.LayoutParams.WRAP_CONTENT
      )
      progressBar.layoutParams = lp
      progressBar.isIndeterminate = true
      cl.addView(progressBar)
      observerCita = Observer<List<Cita>> { response ->
         citaAdapter = context?.let { CitaAdapter(response as ArrayList<Cita>, it) }!!
         recyclerView.adapter= citaAdapter
         svSearch.setOnQueryTextListener(this)
         progressBar.visibility = View.INVISIBLE
      }
      return root
   }
   override fun setUserVisibleHint(isVisibleToUser: Boolean) {
      super.setUserVisibleHint(isVisibleToUser)
      if (isVisibleToUser) {
         requireFragmentManager().beginTransaction().detach(this).attach(this).commit()
      }
   }

   override fun onQueryTextSubmit(query: String): Boolean {
      return false
   }

   override fun onQueryTextChange(newText: String): Boolean {
      citaAdapter.filter(newText)
      return false
   }
   override fun onResume() {
      super.onResume()
      citasViewModel.getCitasPaciente(currentUser.id).observe(viewLifecycleOwner, observerCita)
   }
}