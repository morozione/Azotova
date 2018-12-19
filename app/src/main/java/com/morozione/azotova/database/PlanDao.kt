package com.morozione.azotova.database

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.morozione.azotova.Constants
import com.morozione.azotova.entity.Plan
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single

class PlanDao {
    private val databaseReference = FirebaseDatabase.getInstance().reference
            .child(Constants.DATABASE_PLAN)

    fun getAllPlans(): Observable<Plan> = Observable.create<Plan> { emitter ->
            databaseReference
                    .addListenerForSingleValueEvent(object : ValueEventListener {
                        override fun onDataChange(dataSnapshot: DataSnapshot) {
                            for (child in dataSnapshot.children) {
                                val plan = child.getValue(Plan::class.java)
                                plan?.let {
                                    emitter.onNext(it)
                                }
                            }
                        }

                        override fun onCancelled(databaseError: DatabaseError) {
                            emitter.onError(RuntimeException(databaseError.message))
                        }
                    })
        }

    fun getAllPlansWithFilter(): Observable<Plan> = Observable.create<Plan> { emitter ->
            databaseReference
                    .addListenerForSingleValueEvent(object : ValueEventListener {
                        override fun onDataChange(dataSnapshot: DataSnapshot) {
                            for (child in dataSnapshot.children) {
                                val plan = child.getValue(Plan::class.java)
                                plan?.let {
                                    emitter.onNext(it)
                                }
                            }
                        }

                        override fun onCancelled(databaseError: DatabaseError) {
                            emitter.onError(RuntimeException(databaseError.message))
                        }
                    })
        }

    fun insertPlan(plan: Plan): Completable {
        val key = databaseReference.push().key
        plan.id = key
        return Completable.create { emitter ->
            databaseReference.child(key).setValue(plan)
                    .addOnSuccessListener { _ -> emitter.onComplete() }
                    .addOnFailureListener { emitter.onError(it) }
        }
    }

    fun getPlanById(id: String): Single<Plan> = Single.create<Plan> { emitter ->
            databaseReference.child(id)
                    .addListenerForSingleValueEvent(object : ValueEventListener {
                        override fun onDataChange(dataSnapshot: DataSnapshot) {
                            val plan = dataSnapshot.getValue(Plan::class.java)
                            plan?.let {
                                emitter.onSuccess(plan)
                            }
                        }

                        override fun onCancelled(databaseError: DatabaseError) {
                            emitter.onError(RuntimeException(databaseError.message))
                        }
                    })
        }

    fun getPlansOfUser(userId: String): Observable<Plan> = Observable.create<Plan> { emitter ->
            databaseReference
                    .orderByChild(Constants.USER_ID).equalTo(userId)
                    .addListenerForSingleValueEvent(object : ValueEventListener {
                        override fun onDataChange(dataSnapshot: DataSnapshot) {
                            for (child in dataSnapshot.children) {
                                val plan = child.getValue(Plan::class.java)
                                plan?.let {
                                    emitter.onNext(it)
                                }
                            }
                        }

                        override fun onCancelled(databaseError: DatabaseError) {
                            emitter.onError(RuntimeException(databaseError.message))
                        }
                    })
        }
}
