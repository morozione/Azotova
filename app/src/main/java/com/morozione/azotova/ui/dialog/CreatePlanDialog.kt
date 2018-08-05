package com.morozione.azotova.ui.dialog

import android.app.AlertDialog
import android.app.TimePickerDialog
import android.content.DialogInterface
import android.os.Bundle
import android.support.design.widget.TextInputLayout
import android.support.v4.app.DialogFragment
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.widget.EditText
import com.morozione.azotova.R
import com.morozione.azotova.entity.Plan
import com.morozione.azotova.ui.fragment.TimePickerFragment
import com.morozione.azotova.utils.Utils
import java.util.*
import kotlin.math.min

class CreatePlanDialog : DialogFragment() {

    private var onCreatePlanListener: OnCreatePlanListener? = null

    interface OnCreatePlanListener {
        fun onPlanCreate(plan: Plan)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): android.app.Dialog {
        val rootView = LayoutInflater.from(context).inflate(R.layout.dialog_create_plan, null)
        val calendar = Calendar.getInstance()

        val tilTitle = rootView.findViewById<TextInputLayout>(R.id.til_title)
        val etTitle = rootView.findViewById<EditText>(R.id.et_title)
        val tilDescription = rootView.findViewById<TextInputLayout>(R.id.til_description)
        val etDescription = rootView.findViewById<EditText>(R.id.et_description)
        val tilCity = rootView.findViewById<TextInputLayout>(R.id.til_city)
        val etCity = rootView.findViewById<EditText>(R.id.et_city)
        val tilTime = rootView.findViewById<TextInputLayout>(R.id.til_time)
        val etTime = rootView.findViewById<EditText>(R.id.et_time)

        val builder = AlertDialog.Builder(context)
                .setTitle(R.string.create_task)
                .setView(rootView)
                .setPositiveButton(R.string.ok) { _, _ ->
                    if (onCreatePlanListener != null) {
                        val title = etTitle.text.toString()
                        val description = etDescription.text.toString()
                        val city = etCity.text.toString()

                        onCreatePlanListener!!.onPlanCreate(Plan(title,
                                description, city, calendar.time.time))

                    }
                }

        etTime.setOnClickListener {
            if (etTime.length() == 0) {
                etTime.setText(" ")
            }
            val timePickerFragment = TimePickerFragment()
            timePickerFragment.setListener(TimePickerDialog.OnTimeSetListener { _, hour, minute ->
                calendar.set(Calendar.HOUR_OF_DAY, hour)
                calendar.set(Calendar.MINUTE, minute)
                calendar.set(Calendar.SECOND, 0)
                etTime.setText(Utils.getTime(calendar.timeInMillis))
            })
            timePickerFragment.show(fragmentManager!!, "TimePickerFragment")
        }

        val dialog = builder.create()
        dialog.setOnShowListener(object : DialogInterface.OnShowListener {
            override fun onShow(dialogInterface: DialogInterface) {
                val button = (dialogInterface as AlertDialog).getButton(DialogInterface.BUTTON_POSITIVE)
                if (etTitle.length() == 0) {
                    button.isEnabled = false
                    tilTitle.error = getString(R.string.error_empty_text_file)
                }
                if (etDescription.length() == 0) {
                    button.isEnabled = false
                    tilDescription.error = getString(R.string.error_empty_text_file)
                }
                if (etCity.length() == 0) {
                    button.isEnabled = false
                    tilCity.error = getString(R.string.error_empty_text_file)
                }

                etTitle.addTextChangedListener(object : TextWatcher {
                    override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {

                    }

                    override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
                        if (etTitle.length() == 0) {
                            tilTitle.error = getString(R.string.error_empty_text_file)
                        } else {
                            tilTitle.error = null
                        }
                        button.isEnabled = checkOnEnableButton()
                    }

                    override fun afterTextChanged(editable: Editable) {

                    }
                })
                etDescription.addTextChangedListener(object : TextWatcher {
                    override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {

                    }

                    override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
                        if (etDescription.length() == 0) {
                            tilDescription.error = getString(R.string.error_empty_text_file)
                        } else {
                            tilDescription.error = null
                        }
                        button.isEnabled = checkOnEnableButton()
                    }

                    override fun afterTextChanged(editable: Editable) {

                    }
                })
                etCity.addTextChangedListener(object : TextWatcher {
                    override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {

                    }

                    override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
                        if (etCity.length() == 0) {
                            tilCity.error = getString(R.string.error_empty_text_file)
                        } else {
                            tilCity.error = null
                        }
                        button.isEnabled = checkOnEnableButton()
                    }

                    override fun afterTextChanged(editable: Editable) {

                    }
                })

            }

            private fun checkOnEnableButton(): Boolean {
                return etTitle.length() != 0 && etDescription.length() != 0 && etCity.length() != 0
            }
        })
        return dialog
    }

    fun setOnCreatePlanListener(onCreatePlanListener: OnCreatePlanListener) {
        this.onCreatePlanListener = onCreatePlanListener
    }
}
