package com.morozione.azotova.ui.activity

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.morozione.azotova.MainActivity
import com.morozione.azotova.R
import com.morozione.azotova.core.PresenterStorage
import com.morozione.azotova.presenter.LoadingActivityPresenter
import com.morozione.azotova.presenter.LoadingActivityView
import com.morozione.azotova.utils.bind


class LoadingActivity : AppCompatActivity(), View.OnClickListener, LoadingActivityView {
    private val bLogin by bind<Button>(R.id.login)
    private val bRegistration by bind<Button>(R.id.registration)
    private val etLogin by bind<EditText>(R.id.email)
    private val etPassword by bind<EditText>(R.id.password)

    private var presenter: LoadingActivityPresenter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_loading)

        initPresenter()

        setListeners()
        checkOnLoginning()
    }

    private fun initPresenter() {
        if (PresenterStorage.instance.getPresenter(LoadingActivityPresenter.TAG) == null)
            PresenterStorage.instance.storagePresenter(LoadingActivityPresenter.TAG, LoadingActivityPresenter())

        presenter = PresenterStorage.instance.getPresenter(LoadingActivityPresenter.TAG) as LoadingActivityPresenter
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.login -> singIn()
            R.id.registration -> singUp()
        }
    }

    override fun onResume() {
        super.onResume()
        presenter?.attach(this)
    }

    override fun onPause() {
        super.onPause()
        presenter?.detach()
    }

    override fun onStop() {
        super.onStop()
        if (isFinishing)
            PresenterStorage.instance.clear(LoadingActivityPresenter.TAG)
    }

    private fun checkOnLoginning() {
        if (presenter?.currentUser != null) {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }

    private fun setListeners() {
        bLogin.setOnClickListener(this)
        bRegistration.setOnClickListener(this)
    }

    private fun singIn() {
        val login = etLogin.text.toString()
        val password = etPassword.text.toString()
        if (!login.isEmpty() && !password.isEmpty()) {
            presenter?.singIn(login, password)
        } else {
            Toast.makeText(this, "Input available data", Toast.LENGTH_SHORT).show()
        }
    }

    private fun singUp() {
        val login = etLogin.text.toString()
        val password = etPassword.text.toString()
        if (!login.isEmpty() && !password.isEmpty()) {
            presenter?.singUp(login, password)
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
