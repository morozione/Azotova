package com.morozione.azotova.presenter

import com.morozione.azotova.database.PlanDao
import com.morozione.azotova.entity.Plan

import io.reactivex.Observer
import io.reactivex.Single
import io.reactivex.SingleObserver
import io.reactivex.SingleSource
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

class PlanDetailsPresenter {

    private val planDao = PlanDao()
    private var mView: PlanDetailsView? = null

    private var disposable = CompositeDisposable()

    fun attach(mView: PlanDetailsView) {
        this.mView = mView
        disposable = CompositeDisposable()
    }

    fun detach() {
        mView = null
        disposable.dispose()
    }

    fun getPlanById(id: String) {
        planDao.getPlanById(id).subscribe(object : SingleObserver<Plan> {
            override fun onSubscribe(d: Disposable) {
                disposable.add(d)
            }

            override fun onSuccess(plan: Plan) {
                mView?.sendPlan(plan)
            }

            override fun onError(e: Throwable) {
            }
        })
    }
}
