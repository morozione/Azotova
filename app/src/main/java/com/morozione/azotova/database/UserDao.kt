package com.morozione.azotova.database

import com.google.android.gms.tasks.OnFailureListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

import io.reactivex.Completable

class UserDao {
    private val firebaseAuth = FirebaseAuth.getInstance()

    val currentUser: FirebaseUser?
        get() = firebaseAuth.currentUser

    fun signUp(login: String, password: String): Completable {
        return Completable.create { emitter ->
            firebaseAuth.createUserWithEmailAndPassword(login, password)
                    .addOnSuccessListener { _ -> emitter.onComplete() }
                    .addOnFailureListener { emitter.onError(it) }
        }
    }

    fun signIn(login: String, password: String): Completable {
        return Completable.create { emitter ->
            firebaseAuth.signInWithEmailAndPassword(login, password)
                    .addOnSuccessListener { _ -> emitter.onComplete() }
                    .addOnFailureListener { emitter.onError(it) }
        }
    }
}
