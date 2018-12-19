package com.morozione.azotova.ui.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView

import com.morozione.azotova.R
import com.morozione.azotova.entity.Plan
import com.morozione.azotova.utils.Utils

class PlanAdapter(private var plans: ArrayList<Plan>?) : RecyclerView.Adapter<PlanAdapter.ViewHolder>() {

    private var onPlanClickListener: OnPlanClickListener? = null

    interface OnPlanClickListener {
        fun onPlanClick(plan: Plan)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlanAdapter.ViewHolder {
        val rootView = LayoutInflater.from(parent.context).inflate(R.layout.plan_item,
                parent, false)

        return ViewHolder(rootView)
    }

    override fun onBindViewHolder(holder: PlanAdapter.ViewHolder, position: Int) {
        holder.bind(plans!![position])
    }

    override fun getItemCount(): Int {
        plans?.let {
            return it.size
        }
        return 0
    }

    fun swapData(plans: ArrayList<Plan>) {
        this.plans = plans
        notifyDataSetChanged()
    }

    fun addData(plans: List<Plan>) {
        this.plans!!.addAll(plans)
        notifyDataSetChanged()
    }

    inner class ViewHolder(rootView: View) : RecyclerView.ViewHolder(rootView) {
        private val mIcon: ImageView
        private val mTitle: TextView
        private val mDescription: TextView
        private val mCity: TextView
        private val mTime: TextView

        init {
            mIcon = rootView.findViewById(R.id.icon)
            mTitle = rootView.findViewById(R.id.title)
            mDescription = rootView.findViewById(R.id.tv_description)
            mCity = rootView.findViewById(R.id.city)
            mTime = rootView.findViewById(R.id.time)
        }

        fun bind(plan: Plan) {
            mTitle.text = plan.title
            mDescription.text = plan.description
            mCity.text = plan.city
            mTime.text = Utils.getFullDate(plan.date)

            itemView.setOnClickListener {
                if (onPlanClickListener != null)
                    onPlanClickListener!!.onPlanClick(plan)
            }
        }
    }

    fun setOnPlanClickListener(onPlanClickListener: OnPlanClickListener) {
        this.onPlanClickListener = onPlanClickListener
    }
}
