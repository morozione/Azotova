package com.morozione.azotova.presenter

import com.morozione.azotova.database.PlanDao
import com.morozione.azotova.database.UserDao
import com.morozione.azotova.entity.Plan
import io.reactivex.CompletableObserver
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit

class MainActivityPresenter {

    private val planDao = PlanDao()
    private val userDao = UserDao()
    private var mHome: MainActivityView.HomeView? = null
    private var mMessage: MainActivityView.MessageView? = null

    private var disposable = CompositeDisposable()

    private var allPlansLoading = false
    private var userPlansLoading = false

    fun attach(mView: MainActivityView.HomeView) {
        this.mHome = mView
        disposable = CompositeDisposable()
    }

    fun attach(mView: MainActivityView.MessageView) {
        this.mMessage = mView
        disposable = CompositeDisposable()
    }

    fun detach() {
        mHome = null
        mMessage = null
        disposable.dispose()
    }

    fun insertPlan(plan: Plan) {
        plan.userId = userDao.currentUser!!.uid
        planDao.insertPlan(plan).subscribe(object : CompletableObserver {
            override fun onSubscribe(d: Disposable) {
                disposable.add(d)
            }

            override fun onComplete() {
                mMessage?.sendPlanResult(true)
            }

            override fun onError(e: Throwable) {
                mMessage?.sendPlanResult(false)
            }
        })
    }

    fun getAllPlans() {
        planDao.getAllPlans()
                .buffer(100, TimeUnit.MILLISECONDS)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Observer<List<Plan>> {
                    override fun onSubscribe(d: Disposable) {
                        disposable.add(d)
                    }

                    override fun onNext(plans: List<Plan>) {
                        mHome?.sendAllPlans(plans, allPlansLoading)
                        allPlansLoading = true
                    }

                    override fun onError(e: Throwable) {
                        allPlansLoading = false
                    }

                    override fun onComplete() {
                        allPlansLoading = false
                    }
                })
    }

    fun getPlansOfUser() {
        planDao.getPlansOfUser(userDao.currentUser!!.uid)
                .buffer(100, TimeUnit.MILLISECONDS)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Observer<List<Plan>> {
                    override fun onSubscribe(d: Disposable) {
                        disposable.add(d)
                    }

                    override fun onNext(plans: List<Plan>) {
                        mMessage?.sendUserPlans(plans, userPlansLoading)
                        userPlansLoading = true
                    }

                    override fun onError(e: Throwable) {
                        userPlansLoading = false
                    }

                    override fun onComplete() {
                        userPlansLoading = false
                    }
                })
    }

    fun getAllPlantsWithFilter() {
        //TODO: add filters
        planDao.getAllPlansWithFilter()
    }
}
