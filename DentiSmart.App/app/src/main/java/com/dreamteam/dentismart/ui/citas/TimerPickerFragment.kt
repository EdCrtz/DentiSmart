package com.dreamteam.dentismart.ui.citas

import android.app.DatePickerDialog
import android.app.Dialog
import android.app.TimePickerDialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import java.util.*

class TimerPickerFragment:DialogFragment(){
        private var listener: TimePickerDialog.OnTimeSetListener?=null
        override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
            super.onCreate(savedInstanceState)
            val c= Calendar.getInstance()
            val hora=c.get(Calendar.HOUR)
            val minuto=c.get(Calendar.MINUTE)
            val segundo=c.get(Calendar.SECOND)
            val timePickerDialog=TimePickerDialog(requireActivity(),listener,hora,
                minuto,true)
            return timePickerDialog
        }
        companion object {
            fun newInstance(listener: TimePickerDialog.OnTimeSetListener):TimerPickerFragment{
                val fragment=TimerPickerFragment()
                fragment.listener=listener
                return fragment
            }
        }
    }
