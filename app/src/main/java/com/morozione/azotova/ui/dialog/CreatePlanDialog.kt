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

class CreatePlanDialog : DialogFragment() {

    private var onCreatePlanListener: OnCreatePlanListener? = null

    interface OnCreatePlanListener {
        fun onPlanCreate(plan: Plan)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): android.app.Dialog {
        val mRootView = LayoutInflater.from(context).inflate(R.layout.dialog_create_plan, null)
        val calendar = Calendar.getInstance()

        val mTitleContainer = mRootView.findViewById<TextInputLayout>(R.id.til_title)
        val mTitle = mRootView.findViewById<EditText>(R.id.et_title)
        val mDescriptionContainer = mRootView.findViewById<TextInputLayout>(R.id.til_description)
        val mDescription = mRootView.findViewById<EditText>(R.id.et_description)
        val mCityContainer = mRootView.findViewById<TextInputLayout>(R.id.til_city)
        val mCity = mRootView.findViewById<EditText>(R.id.et_city)
        val mTimeContainer = mRootView.findViewById<TextInputLayout>(R.id.til_time)
        val mTime = mRootView.findViewById<EditText>(R.id.et_time)

        val builder = AlertDialog.Builder(context)
                .setTitle(R.string.create_task)
                .setView(mRootView)
                .setPositiveButton(R.string.ok) { _, _ ->
                    if (onCreatePlanListener != null) {
                        val title = mTitle.text.toString()
                        val description = mDescription.text.toString()
                        val city = mCity.text.toString()

                        onCreatePlanListener!!.onPlanCreate(Plan(title,
                                description, city, calendar.time.time))

                    }
                }

        mTime.setOnClickListener {
            if (mTime.length() == 0) {
                mTime.setText(" ")
            }
            val timePickerFragment = TimePickerFragment()
            timePickerFragment.setListener(TimePickerDialog.OnTimeSetListener { _, hour, minute ->
                calendar.set(Calendar.HOUR_OF_DAY, hour)
                calendar.set(Calendar.MINUTE, minute)
                calendar.set(Calendar.SECOND, 0)
                mTime.setText(Utils.getTime(calendar.timeInMillis))
            })
            timePickerFragment.show(fragmentManager!!, TimePickerFragment::class.java.simpleName)
        }

        val dialog = builder.create()
        dialog.setOnShowListener(object : DialogInterface.OnShowListener {
            override fun onShow(dialogInterface: DialogInterface) {
                val button = (dialogInterface as AlertDialog).getButton(DialogInterface.BUTTON_POSITIVE)
                if (mTitle.length() == 0) {
                    button.isEnabled = false
                    mTitleContainer.error = getString(R.string.error_empty_text_file)
                }
                if (mDescription.length() == 0) {
                    button.isEnabled = false
                    mDescriptionContainer.error = getString(R.string.error_empty_text_file)
                }
                if (mCity.length() == 0) {
                    button.isEnabled = false
                    mCityContainer.error = getString(R.string.error_empty_text_file)
                }

                mTitle.addTextChangedListener(object : TextWatcher {
                    override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {

                    }

                    override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
                        if (mTitle.length() == 0) {
                            mTitleContainer.error = getString(R.string.error_empty_text_file)
                        } else {
                            mTitleContainer.error = null
                        }
                        button.isEnabled = checkOnEnableButton()
                    }

                    override fun afterTextChanged(editable: Editable) {

                    }
                })
                mDescription.addTextChangedListener(object : TextWatcher {
                    override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {

                    }

                    override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
                        if (mDescription.length() == 0) {
                            mDescriptionContainer.error = getString(R.string.error_empty_text_file)
                        } else {
                            mDescriptionContainer.error = null
                        }
                        button.isEnabled = checkOnEnableButton()
                    }

                    override fun afterTextChanged(editable: Editable) {

                    }
                })
                mCity.addTextChangedListener(object : TextWatcher {
                    override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {

                    }

                    override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
                        if (mCity.length() == 0) {
                            mCityContainer.error = getString(R.string.error_empty_text_file)
                        } else {
                            mCityContainer.error = null
                        }
                        button.isEnabled = checkOnEnableButton()
                    }

                    override fun afterTextChanged(editable: Editable) {

                    }
                })

            }

            private fun checkOnEnableButton(): Boolean {
                return mTitle.length() != 0 && mDescription.length() != 0 && mCity.length() != 0
            }
        })
        return dialog
    }

    fun setOnCreatePlanListener(onCreatePlanListener: OnCreatePlanListener) {
        this.onCreatePlanListener = onCreatePlanListener
    }
}
