package com.morozione.azotova.ui.actiity

import android.os.Bundle
import android.support.v7.app.AppCompatActivity

import com.morozione.azotova.Constants
import com.morozione.azotova.R
import com.morozione.azotova.ui.fragment.PlanDetailFragment
import com.morozione.azotova.utils.FragmentUtil

class DetailsPlanActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details_plan)

        FragmentUtil.changeFragmentTo(this,
                PlanDetailFragment.getInstance(intent.getStringExtra(Constants.EXTRA_ID)),
                PlanDetailFragment::class.java.simpleName)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }
}
