package com.dreamteam.dentismart.ui.citas

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.viewpager.widget.ViewPager
import androidx.viewpager2.widget.ViewPager2
import com.dreamteam.dentismart.R
import com.google.android.material.tabs.TabLayout

class CitasFragment : Fragment() {
    private lateinit var tabLayout: TabLayout
    private lateinit var viewPager: ViewPager
    private lateinit var v: View
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_citas, container, false)
        viewPager = v.findViewById(R.id.viewpage)
        tabLayout=  v.findViewById(R.id.tabLayout2)
        viewPager.adapter=PageAdapter(this.childFragmentManager)
        tabLayout.setupWithViewPager(viewPager)
        tabLayout.getTabAt(0)?.setIcon(R.drawable.ic_calendar_plus)
        tabLayout.getTabAt(1)?.setIcon(R.drawable.ic_calendar_clock)
        return v
    }
}


