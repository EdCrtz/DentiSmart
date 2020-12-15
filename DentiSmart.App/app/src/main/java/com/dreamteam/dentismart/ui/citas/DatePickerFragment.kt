package com.dreamteam.dentismart.ui.citas

import android.app.DatePickerDialog
import android.app.Dialog
import android.os.Bundle
import android.os.Parcel
import android.os.Parcelable
import android.widget.DatePicker
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import java.util.*

class DatePickerFragment: DialogFragment(){

private var listener:DatePickerDialog.OnDateSetListener?=null
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        super.onCreate(savedInstanceState)
        val c= Calendar.getInstance()
        val year=c.get(Calendar.YEAR)
        val month=c.get(Calendar.MONTH)
        val day=c.get(Calendar.DAY_OF_MONTH)
        val datePickerDialog=DatePickerDialog(requireActivity(),listener,year,month,day)
        c.set(Calendar.YEAR,year)
        datePickerDialog.datePicker.minDate=c.timeInMillis
        c.set(Calendar.YEAR,year+10)
        datePickerDialog.datePicker.maxDate=c.timeInMillis
        return datePickerDialog
    }
    companion object {
        fun newInstance(listener: DatePickerDialog.OnDateSetListener):DatePickerFragment{
            val fragment=DatePickerFragment()
            fragment.listener=listener
            return fragment
        }
    }
}


