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
import com.morozione.azotova.core.PresenterStorage
import com.morozione.azotova.entity.Plan
import com.morozione.azotova.presenter.PlanDetailsPresenter
import com.morozione.azotova.presenter.PlanDetailsView
import com.morozione.azotova.utils.Utils
import de.hdodenhof.circleimageview.CircleImageView

class PlanDetailFragment : Fragment(), PlanDetailsView {
    private var presenter: PlanDetailsPresenter? = null

    private var mImage: CircleImageView? = null
    private var mTitle: TextView? = null
    private var mDescription: TextView? = null
    private var mCity: TextView? = null
    private var mTime: TextView? = null
    private var mJoin: Button? = null

    companion object {
        fun getInstance(id: String): PlanDetailFragment {
            val planDetailFragment = PlanDetailFragment()

            val args = Bundle()
            args.putString(Constants.EXTRA_ID, id)
            planDetailFragment.arguments = args

            return planDetailFragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.fragment_plan_detail, container, false)

        initPresenter()

        initView(rootView)
        loadData()

        return rootView
    }

    private fun initPresenter() {
        if (PresenterStorage.instance.getPresenter(PlanDetailsPresenter.TAG) == null)
            PresenterStorage.instance.storagePresenter(PlanDetailsPresenter.TAG, PlanDetailsPresenter())

        presenter = PresenterStorage.instance.getPresenter(PlanDetailsPresenter.TAG) as PlanDetailsPresenter
    }

    override fun onStart() {
        super.onStart()
        presenter!!.attach(this)
    }

    override fun onStop() {
        super.onStop()
        presenter!!.detach()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        PresenterStorage.instance.clear(PlanDetailsPresenter.TAG)
    }

    private fun loadData() {
        if (arguments == null)
            return

        val id = arguments!!.getString(Constants.EXTRA_ID)
        presenter!!.getPlanById(id!!)
    }

    private fun initView(rootView: View) {
        mImage = rootView.findViewById(R.id.icon)
        mTitle = rootView.findViewById(R.id.title)
        mDescription = rootView.findViewById(R.id.tv_description)
        mCity = rootView.findViewById(R.id.city)
        mTime = rootView.findViewById(R.id.time)
        mJoin = rootView.findViewById(R.id.join)
    }

    private fun fillView(plan: Plan) {
        mTitle!!.text = plan.title
        mDescription!!.text = plan.description
        mCity!!.text = plan.city
        mTime!!.text = Utils.getFullDate(plan.date)
    }

    override fun sendPlan(plan: Plan) {
        fillView(plan)
    }
}
