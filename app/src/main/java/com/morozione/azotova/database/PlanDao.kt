package com.morozione.azotova.database

import com.google.firebase.database.*
import com.morozione.azotova.Constants
import com.morozione.azotova.entity.Plan
import io.reactivex.Completable
import io.reactivex.Observable

class PlanDao {
    private val databaseReference: DatabaseReference

    val allPlans: Observable<Plan>
        get() = Observable.create { emitter ->
            databaseReference
                    .addListenerForSingleValueEvent(object : ValueEventListener {
                        override fun onDataChange(dataSnapshot: DataSnapshot) {
                            for (child in dataSnapshot.children) {
                                emitter.onNext(child.getValue(Plan::class.java)!!)
                            }
                        }

                        override fun onCancelled(databaseError: DatabaseError) {
                            emitter.onError(RuntimeException(databaseError.message))
                        }
                    })
        }

    val allPlansWithFilter: Observable<Plan>
        get() = Observable.create { emitter ->
            databaseReference
                    .addListenerForSingleValueEvent(object : ValueEventListener {
                        override fun onDataChange(dataSnapshot: DataSnapshot) {
                            for (child in dataSnapshot.children) {
                                emitter.onNext(child.getValue(Plan::class.java)!!)
                            }
                        }

                        override fun onCancelled(databaseError: DatabaseError) {
                            emitter.onError(RuntimeException(databaseError.message))
                        }
                    })
        }

    init {
        databaseReference = FirebaseDatabase.getInstance().reference
                .child(Constants.DATABASE_PLAN)
    }

    fun insertPlan(plan: Plan): Completable {
        val key = databaseReference.push().key
        plan.id = key
        return Completable.create { emitter ->
            databaseReference.child(key!!).setValue(plan)
                    .addOnSuccessListener { _ -> emitter.onComplete() }
                    .addOnFailureListener { emitter.onError(it) }
        }
    }

    fun getPlanById(id: String): Observable<Plan> {
        return Observable.create { emitter ->
            databaseReference.child(id)
                    .addListenerForSingleValueEvent(object : ValueEventListener {
                        override fun onDataChange(dataSnapshot: DataSnapshot) {
                            emitter.onNext(dataSnapshot.getValue(Plan::class.java)!!)
                        }

                        override fun onCancelled(databaseError: DatabaseError) {
                            emitter.onError(RuntimeException(databaseError.message))
                        }
                    })
        }
    }

    fun getPlansOfUser(userId: String): Observable<Plan> {
        return Observable.create { emitter ->
            databaseReference
                    .orderByChild(Constants.USER_ID).equalTo(userId)
                    .addListenerForSingleValueEvent(object : ValueEventListener {
                        override fun onDataChange(dataSnapshot: DataSnapshot) {
                            for (child in dataSnapshot.children) {
                                emitter.onNext(child.getValue(Plan::class.java)!!)
                            }
                        }

                        override fun onCancelled(databaseError: DatabaseError) {
                            emitter.onError(RuntimeException(databaseError.message))
                        }
                    })
        }
    }
}
