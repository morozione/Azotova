package com.morozione.azotova.presenter

import com.morozione.azotova.database.PlanDao
import com.morozione.azotova.entity.Plan

import io.reactivex.Observer
import io.reactivex.disposables.Disposable

class PlanDetailsPresenter {

    private val planDao: PlanDao
    private var mView: PlanDetailsView? = null

    init {
        planDao = PlanDao()
    }

    fun getPlanById(id: String) {
        planDao.getPlanById(id).subscribe(object : Observer<Plan> {
            override fun onSubscribe(d: Disposable) {

            }

            override fun onNext(plan: Plan) {
                mView!!.sendPlan(plan)
            }

            override fun onError(e: Throwable) {

            }

            override fun onComplete() {

            }
        })
    }

    fun attach(mView: PlanDetailsView) {
        this.mView = mView
    }

    fun detach() {
        mView = null
    }

}
