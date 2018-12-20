package com.morozione.azotova

import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v4.view.ViewPager
import android.support.v7.app.AppCompatActivity
import com.morozione.azotova.core.PresenterStorage
import com.morozione.azotova.presenter.MainActivityPresenter
import com.morozione.azotova.ui.adapter.TabAdapter
import com.morozione.azotova.utils.BottomNavigationViewHelper
import com.morozione.azotova.utils.bind

class MainActivity : AppCompatActivity() {
    private val navigation by bind<BottomNavigationView>(R.id.navigation)
    private val vpContainer by bind<ViewPager>(R.id.pager_container)

    private lateinit var tabAdapter: TabAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initPresenter()

        initUI()
        setListeners()
    }

    private fun initPresenter() {
        if (PresenterStorage.instance.getPresenter(MainActivityPresenter.TAG) == null)
            PresenterStorage.instance.storagePresenter(MainActivityPresenter.TAG, MainActivityPresenter())
    }

    private fun initUI() {
        tabAdapter = TabAdapter(supportFragmentManager, 3)
        vpContainer.adapter = tabAdapter
        BottomNavigationViewHelper.disableShiftMode(navigation)
    }

    private fun setListeners() {
        navigation.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.i_home -> {
                    vpContainer.currentItem = TabAdapter.HOME_FRAGMENT_POSITION
                }
                R.id.i_message -> {
                    vpContainer.currentItem = TabAdapter.PERSON_FRAGMENT_POSITION
                }
                R.id.i_user -> {
                    vpContainer.currentItem = TabAdapter.USER_PLANS_FRAGMENT_POSITION
                }
            }
            false
        }
        vpContainer.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {

            }

            override fun onPageSelected(position: Int) {
                navigation.menu.getItem(position).isChecked = true
            }

            override fun onPageScrollStateChanged(state: Int) {

            }
        })
    }

    override fun onStop() {
        super.onStop()
        if (isFinishing)
            PresenterStorage.instance.clear(MainActivityPresenter.TAG)
    }
}
