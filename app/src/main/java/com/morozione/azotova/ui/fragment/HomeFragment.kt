package com.morozione.azotova.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.morozione.azotova.Constants
import com.morozione.azotova.R
import com.morozione.azotova.entity.Plan
import com.morozione.azotova.presenter.MainActivityPresenter
import com.morozione.azotova.presenter.MainActivityView
import com.morozione.azotova.ui.activity.DetailsPlanActivity
import com.morozione.azotova.ui.adapter.PlanAdapter
import java.util.*

class HomeFragment : Fragment(), MainActivityView.HomeView, PlanAdapter.OnPlanClickListener {

    private lateinit var mList: RecyclerView
    private lateinit var mRefresh: SwipeRefreshLayout

    private lateinit var presenter: MainActivityPresenter
    private val plans = ArrayList<Plan>()
    private var adapter: PlanAdapter? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.fragment_home, container, false)

        presenter = MainActivityPresenter()

        initView(rootView)
        setListeners()

        return rootView
    }

    private fun initView(view: View) {
        mList = view.findViewById(R.id.homes_list)
        mRefresh = view.findViewById(R.id.refresh)

        mList.setHasFixedSize(true)
        mList.layoutManager = LinearLayoutManager(context)

        adapter = PlanAdapter(plans)
        adapter!!.setOnPlanClickListener(this)
        mList.adapter = adapter
    }

    private fun setListeners() {
        mRefresh.setOnRefreshListener { presenter.getAllPlans() }
    }

    override fun onResume() {
        super.onResume()
        presenter.attach(this)
        loadData()
    }

    override fun onPause() {
        super.onPause()
        presenter.detach()
    }

    private fun loadData() {
        presenter.getAllPlans()
        mRefresh.isRefreshing = true
    }

    override fun sendAllPlans(plans: List<Plan>, isLoading: Boolean) {
        if (!isLoading)
            adapter!!.swapData(ArrayList())

        adapter!!.addData(plans)
        mRefresh.isRefreshing = false
    }

    override fun onPlanClick(plan: Plan) {
        val intent = Intent(context, DetailsPlanActivity::class.java)
        intent.putExtra(Constants.EXTRA_ID, plan.id)
        startActivity(intent)
    }
}
