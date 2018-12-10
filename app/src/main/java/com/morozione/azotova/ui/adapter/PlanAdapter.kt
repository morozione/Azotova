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
        return plans!!.size
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
        private val ivIcon: ImageView
        private val tvTitle: TextView
        private val tvDescription: TextView
        private val tvCity: TextView
        private val tvTime: TextView

        init {
            ivIcon = rootView.findViewById(R.id.civ_icon)
            tvTitle = rootView.findViewById(R.id.tv_title)
            tvDescription = rootView.findViewById(R.id.tv_description)
            tvCity = rootView.findViewById(R.id.tv_city)
            tvTime = rootView.findViewById(R.id.tv_time)
        }

        fun bind(plan: Plan) {
            tvTitle.text = plan.title
            tvDescription.text = plan.description
            tvCity.text = plan.city
            tvTime.text = Utils.getFullDate(plan.date)

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
