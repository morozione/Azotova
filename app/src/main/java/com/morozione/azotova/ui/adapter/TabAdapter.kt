package com.morozione.azotova.ui.adapter

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter

import com.morozione.azotova.ui.fragment.HomeFragment
import com.morozione.azotova.ui.fragment.UserFragment
import com.morozione.azotova.ui.fragment.UserPlansFragment

import java.util.ArrayList

/**
 * Created by morozione on 2/8/18.
 */

class TabAdapter(fm: FragmentManager, private val numberOfTabs: Int) : FragmentStatePagerAdapter(fm) {

    private val items = ArrayList<Int>()

    private val homeFragment: HomeFragment
    private val userPlansFragment: UserPlansFragment
    private val userFragment: UserFragment

    init {

        homeFragment = HomeFragment()
        userPlansFragment = UserPlansFragment()
        userFragment = UserFragment()
    }

    override fun getCount(): Int {
        return numberOfTabs
    }

    override fun getItem(position: Int): Fragment? {
        when (position) {
            HOME_FRAGMENT_POSITION -> return homeFragment
            USER_PLANS_FRAGMENT_POSITION -> return userPlansFragment
            PERSON_FRAGMENT_POSITION -> return userFragment
        }
        return null
    }

    fun setSelectedItemPosition(position: Int) {
        items.add(position)
    }

    companion object {

        val HOME_FRAGMENT_POSITION = 0
        val USER_PLANS_FRAGMENT_POSITION = 1
        val PERSON_FRAGMENT_POSITION = 2
    }
}
