package com.morozione.azotova

import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v4.view.ViewPager
import android.support.v7.app.AppCompatActivity

import com.morozione.azotova.ui.adapter.TabAdapter
import com.morozione.azotova.utils.BottomNavigationViewHelper

import butterknife.BindView
import butterknife.ButterKnife

class MainActivity : AppCompatActivity() {
    @BindView(R.id.b_navigation)
    internal var navigation: BottomNavigationView? = null
    @BindView(R.id.vp_container)
    private var vpContainer: ViewPager? = null

    private var tabAdapter: TabAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        ButterKnife.bind(this)

        initUI()

        setListeners()
    }

    private fun initUI() {
        tabAdapter = TabAdapter(supportFragmentManager, 3)
        vpContainer!!.adapter = tabAdapter
        BottomNavigationViewHelper.disableShiftMode(navigation!!)
    }

    private fun setListeners() {
        navigation!!.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.i_home -> {
                    tabAdapter!!.setSelectedItemPosition(1)
                    vpContainer!!.currentItem = 0
                }
                R.id.i_message -> {
                    tabAdapter!!.setSelectedItemPosition(1)
                    vpContainer!!.currentItem = 1
                }
                R.id.i_user -> {
                    tabAdapter!!.setSelectedItemPosition(2)
                    vpContainer!!.currentItem = 2
                }
            }
            false
        }
        vpContainer!!.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {

            }

            override fun onPageSelected(position: Int) {
                tabAdapter!!.setSelectedItemPosition(position)
                navigation!!.menu.getItem(position).isChecked = true
            }

            override fun onPageScrollStateChanged(state: Int) {

            }
        })
    }
}
