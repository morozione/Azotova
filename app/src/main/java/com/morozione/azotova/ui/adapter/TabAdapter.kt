package com.morozione.azotova.ui.adapter

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import com.morozione.azotova.ui.fragment.HomeFragment
import com.morozione.azotova.ui.fragment.UserFragment
import com.morozione.azotova.ui.fragment.UserPlansFragment

class TabAdapter(fm: FragmentManager, private val numberOfTabs: Int) : FragmentStatePagerAdapter(fm) {

    companion object {
        const val HOME_FRAGMENT_POSITION = 0
        const val USER_PLANS_FRAGMENT_POSITION = 1
        const val PERSON_FRAGMENT_POSITION = 2
    }

    private val homeFragment = HomeFragment()
    private val userPlansFragment = UserPlansFragment()
    private val userFragment = UserFragment()

    override fun getCount(): Int {
        return numberOfTabs
    }

    override fun getItem(position: Int): Fragment? {
        return when (position) {
            HOME_FRAGMENT_POSITION -> homeFragment
            USER_PLANS_FRAGMENT_POSITION -> userPlansFragment
            PERSON_FRAGMENT_POSITION -> userFragment
            else -> throw RuntimeException("undefined fragment: $position")
        }
    }
}
