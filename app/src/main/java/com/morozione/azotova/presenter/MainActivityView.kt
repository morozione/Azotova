package com.morozione.azotova.presenter

import com.morozione.azotova.entity.Plan

interface MainActivityView {
    interface HomeView {
        fun sendAllPlans(plans: List<Plan>, isLoading: Boolean)
    }

    interface MessageView {
        fun sendPlanResult(isSuccess: Boolean)
        fun sendUserPlans(plans: List<Plan>, isLoading: Boolean)
    }
}
