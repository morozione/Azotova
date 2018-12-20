package com.morozione.azotova

import android.app.Application
import com.google.firebase.FirebaseApp
import com.google.firebase.database.FirebaseDatabase
import com.morozione.azotova.core.PresenterStorage

class App : Application() {
    companion object {
        val presenterStorage = PresenterStorage()
    }

    override fun onCreate() {
        super.onCreate()
        FirebaseApp.initializeApp(this)
        FirebaseDatabase.getInstance().setPersistenceEnabled(true)
    }
}
