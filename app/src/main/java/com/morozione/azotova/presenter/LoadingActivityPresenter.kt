package com.morozione.azotova.presenter

import com.google.firebase.auth.FirebaseUser
import com.morozione.azotova.database.UserDao

import io.reactivex.CompletableObserver
import io.reactivex.disposables.Disposable

class LoadingActivityPresenter(private var mView: LoadingActivityView?) {
    private val userDao: UserDao

    val currentUser: FirebaseUser?
        get() = userDao.currentUser

    init {
        userDao = UserDao()
    }

    fun attach(mView: LoadingActivityView) {
        this.mView = mView
    }

    fun detach() {
        mView = null
    }


    fun singUp(login: String, password: String) {
        userDao.signUp(login, password)
                .subscribe(object : CompletableObserver {
                    override fun onSubscribe(d: Disposable) {}

                    override fun onComplete() {
                        mView!!.sendAuthorizationResult(true)
                    }

                    override fun onError(e: Throwable) {
                        mView!!.sendAuthorizationResult(false)
                    }
                })
    }

    fun singIn(login: String, password: String) {
        userDao.signIn(login, password)
                .subscribe(object : CompletableObserver {
                    override fun onSubscribe(d: Disposable) {}

                    override fun onComplete() {
                        mView!!.sendAuthorizationResult(true)
                    }

                    override fun onError(e: Throwable) {
                        mView!!.sendAuthorizationResult(false)
                    }
                })
    }
}
