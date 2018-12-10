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

    private lateinit var rvList: RecyclerView
    private lateinit var srlRefresh: SwipeRefreshLayout

    private var presenter: MainActivityPresenter? = null
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

    override fun onResume() {
        super.onResume()
        presenter!!.attach(this)
        loadData()
    }

    override fun onPause() {
        super.onPause()
        presenter!!.detach()
    }

    private fun loadData() {
        presenter!!.getAllPlans()
        srlRefresh.isRefreshing = true
    }

    private fun setListeners() {
        srlRefresh.setOnRefreshListener { presenter!!.getAllPlans() }
    }

    fun initView(view: View) {

        rvList = view.findViewById(R.id.rv_list)
        srlRefresh = view.findViewById(R.id.srl_refresh)

        rvList.setHasFixedSize(true)
        rvList.layoutManager = LinearLayoutManager(context)

        adapter = PlanAdapter(plans)
        adapter!!.setOnPlanClickListener(this)
        rvList.adapter = adapter
    }

    override fun sendAllPlans(plans: List<Plan>, isLoading: Boolean) {
        if (!isLoading)
            adapter!!.swapData(ArrayList())

        adapter!!.addData(plans)
        srlRefresh.isRefreshing = false
    }

    override fun onPlanClick(plan: Plan) {
        val intent = Intent(context, DetailsPlanActivity::class.java)
        intent.putExtra(Constants.EXTRA_ID, plan.id)
        startActivity(intent)
    }
}
