package com.morozione.azotova.presenter

import com.morozione.azotova.database.PlanDao
import com.morozione.azotova.database.UserDao
import com.morozione.azotova.entity.Plan

import io.reactivex.CompletableObserver
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class MainActivityPresenter {

    private val planDao: PlanDao
    private val userDao: UserDao
    private var mHome: MainActivityView.HomeView? = null
    private var mMessage: MainActivityView.MessageView? = null

    init {
        planDao = PlanDao()
        userDao = UserDao()
    }

    fun attach(mView: MainActivityView.HomeView) {
        this.mHome = mView
    }

    fun attach(mView: MainActivityView.MessageView) {
        this.mMessage = mView
    }

    fun detach() {
        mHome = null
        mMessage = null
    }

    fun insertPlan(plan: Plan) {
        plan.userId = userDao.currentUser!!.uid
        planDao.insertPlan(plan).subscribe(object : CompletableObserver {
            override fun onSubscribe(d: Disposable) {}

            override fun onComplete() {
                mMessage!!.sendPlanResult(true)
            }

            override fun onError(e: Throwable) {
                mMessage!!.sendPlanResult(false)
            }
        })
    }

    fun getAllPlans() {
        planDao.allPlans
                .buffer(1)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Observer<List<Plan>> {
                    override fun onSubscribe(d: Disposable) {}

                    override fun onNext(plans: List<Plan>) {
                        if (mHome != null)
                            mHome!!.sendAllPlans(plans)
                    }

                    override fun onError(e: Throwable) {}

                    override fun onComplete() {}
                })
    }

    fun getPlansOfUser() {
        planDao.getPlansOfUser(userDao.currentUser!!.uid)
                .buffer(1)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Observer<List<Plan>> {
                    override fun onSubscribe(d: Disposable) {

                    }

                    override fun onNext(plans: List<Plan>) {
                        if (mMessage != null)
                            mMessage!!.sendUserPlans(plans)
                    }

                    override fun onError(e: Throwable) {

                    }

                    override fun onComplete() {

                    }
                })
    }

    fun getAllPlantsWithFilter() {
        //TODO: add filters
        planDao.allPlansWithFilter
    }
}
