package com.morozione.azotova.presenter

import com.morozione.azotova.entity.Plan

interface PlanDetailsView {
    fun sendPlan(plan: Plan)
}
