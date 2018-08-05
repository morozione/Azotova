package com.morozione.azotova.ui.fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView

import com.morozione.azotova.Constants
import com.morozione.azotova.R
import com.morozione.azotova.entity.Plan
import com.morozione.azotova.presenter.PlanDetailsPresenter
import com.morozione.azotova.presenter.PlanDetailsView
import com.morozione.azotova.utils.Utils

import de.hdodenhof.circleimageview.CircleImageView

class PlanDetailFragment : Fragment(), PlanDetailsView {
    private var presenter: PlanDetailsPresenter? = null

    private var civImage: CircleImageView? = null
    private var tvTitle: TextView? = null
    private var tvDescription: TextView? = null
    private var tvCity: TextView? = null
    private var tvTime: TextView? = null
    private var bJoin: Button? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.fragment_plan_detail, container, false)

        presenter = PlanDetailsPresenter()

        initView(rootView)
        loadData()

        return rootView
    }

    override fun onStart() {
        super.onStart()
        presenter!!.attach(this)
    }

    override fun onStop() {
        super.onStop()
        presenter!!.detach()
    }

    private fun loadData() {
        if (arguments == null)
            return

        val id = arguments!!.getString(Constants.EXTRA_ID)
        presenter!!.getPlanById(id!!)
    }

    private fun initView(rootView: View) {
        civImage = rootView.findViewById(R.id.civ_icon)
        tvTitle = rootView.findViewById(R.id.tv_title)
        tvDescription = rootView.findViewById(R.id.tv_description)
        tvCity = rootView.findViewById(R.id.tv_city)
        tvTime = rootView.findViewById(R.id.tv_time)
        bJoin = rootView.findViewById(R.id.b_join)
    }

    private fun fillView(plan: Plan) {
        tvTitle!!.text = plan.title
        tvDescription!!.text = plan.description
        tvCity!!.text = plan.city
        tvTime!!.text = Utils.getFullDate(plan.date)
    }

    override fun sendPlan(plan: Plan) {
        fillView(plan)
    }

    companion object {
        private var planDetailFragment: PlanDetailFragment? = null

        fun getInstance(id: String): PlanDetailFragment {
            if (planDetailFragment == null)
                planDetailFragment = PlanDetailFragment()

            val args = Bundle()
            args.putString(Constants.EXTRA_ID, id)
            planDetailFragment!!.arguments = args

            return planDetailFragment as PlanDetailFragment
        }
    }
}
