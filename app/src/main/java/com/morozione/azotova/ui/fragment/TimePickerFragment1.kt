package com.morozione.azotova.ui.fragment

import android.app.Dialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.text.format.DateFormat

import java.util.Calendar

class TimePickerFragment : DialogFragment() {
    private var listener: TimePickerDialog.OnTimeSetListener? = null

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        val c = Calendar.getInstance()
        val hour = c.get(Calendar.DAY_OF_MONTH)
        val minute = c.get(Calendar.MINUTE)

        return TimePickerDialog(activity, listener, hour, minute, DateFormat.is24HourFormat(activity))
    }

    fun setListener(listener: TimePickerDialog.OnTimeSetListener) {
        this.listener = listener
    }
}