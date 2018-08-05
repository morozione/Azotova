package com.morozione.azotova.ui.actiity

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast

import com.morozione.azotova.MainActivity
import com.morozione.azotova.R
import com.morozione.azotova.presenter.LoadingActivityPresenter
import com.morozione.azotova.presenter.LoadingActivityView

import butterknife.BindView
import butterknife.ButterKnife

class LoadingActivity : AppCompatActivity(), View.OnClickListener, LoadingActivityView {
    @BindView(R.id.b_login)
    internal var bLogin: Button? = null
    @BindView(R.id.b_registration)
    internal var bRegistration: Button? = null
    @BindView(R.id.et_login)
    internal var etLogin: EditText? = null
    @BindView(R.id.et_password)
    internal var etPassword: EditText? = null

    private var presenter: LoadingActivityPresenter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_loading)
        ButterKnife.bind(this)

        presenter = LoadingActivityPresenter(this)

        setListeners()
        checkOnLoginning()
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.b_login -> singIn()
            R.id.b_registration -> singUp()
        }
    }

    override fun onResume() {
        super.onResume()
        presenter!!.attach(this)
    }

    override fun onPause() {
        super.onPause()
        presenter!!.detach()
    }

    private fun checkOnLoginning() {
        if (presenter!!.currentUser != null) {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }

    private fun setListeners() {
        bLogin!!.setOnClickListener(this)
        bRegistration!!.setOnClickListener(this)
    }

    private fun singIn() {
        val login = etLogin!!.text.toString()
        val password = etPassword!!.text.toString()
        if (!login.isEmpty() && !password.isEmpty()) {
            presenter!!.singIn(login, password)
        } else {
            Toast.makeText(this, "Input available data", Toast.LENGTH_SHORT).show()
        }
    }

    private fun singUp() {
        val login = etLogin!!.text.toString()
        val password = etPassword!!.text.toString()
        if (!login.isEmpty() && !password.isEmpty()) {
            presenter!!.singUp(login, password)
        } else {
            Toast.makeText(this, "Input available data", Toast.LENGTH_SHORT).show()
        }
    }

    override fun sendAuthorizationResult(isSuccess: Boolean) {
        if (isSuccess) {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }
}
