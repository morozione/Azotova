package com.morozione.azotova.ui.fragment

import android.Manifest
import android.app.AlertDialog
import android.content.ContentValues
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.support.v4.app.Fragment
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.afollestad.materialdialogs.MaterialDialog
import com.github.clans.fab.FloatingActionButton
import com.morozione.azotova.Constants
import com.morozione.azotova.R
import com.morozione.azotova.entity.Plan
import com.morozione.azotova.presenter.MainActivityPresenter
import com.morozione.azotova.presenter.MainActivityView
import com.morozione.azotova.ui.activity.DetailsPlanActivity
import com.morozione.azotova.ui.adapter.PlanAdapter
import com.morozione.azotova.ui.dialog.CreatePlanDialog
import com.morozione.azotova.ui.dialog.DialogFactory
import permissions.dispatcher.*
import java.util.*

@RuntimePermissions
class UserPlansFragment : Fragment(), MainActivityView.MessageView, CreatePlanDialog.OnCreatePlanListener, PlanAdapter.OnPlanClickListener {

    private lateinit var mCreate: FloatingActionButton
    private lateinit var mList: RecyclerView
    private lateinit var mRefresh: SwipeRefreshLayout

    private lateinit var presenter: MainActivityPresenter
    private lateinit var adapter: PlanAdapter

    private val plans = ArrayList<Plan>()
    private var imageUri: Uri? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.fragment_user_plans, container, false)

        presenter = MainActivityPresenter()
        initView(rootView)
        setListeners()

        return rootView
    }

    private fun initView(view: View) {
        mCreate = view.findViewById(R.id.create)
        mList = view.findViewById(R.id.homes_list)
        mRefresh = view.findViewById(R.id.refresh)
        mList.setHasFixedSize(true)
        mList.layoutManager = LinearLayoutManager(context)

        adapter = PlanAdapter(plans)
        adapter.setOnPlanClickListener(this)
        mList.adapter = adapter
    }

    private fun setListeners() {
        mList.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (dy > 0 && mCreate.visibility == View.VISIBLE) {
                    mCreate.hide(true)
                } else if (dy < 0 && mCreate.visibility != View.VISIBLE) {
                    mCreate.show(true)
                }
            }
        })
        mCreate.setOnClickListener {
            val createPlanDialog = CreatePlanDialog()
            createPlanDialog.setOnCreatePlanListener(this@UserPlansFragment)
            if (fragmentManager != null)
                createPlanDialog.show(fragmentManager!!, CreatePlanDialog::class.java.simpleName)
        }
        mRefresh.setOnRefreshListener { presenter.getPlansOfUser() }
    }

    private fun loadData() {
        presenter.getPlansOfUser()
        mRefresh.isRefreshing = true
    }

    override fun onResume() {
        super.onResume()
        presenter.attach(this)
        loadData()
    }

    override fun sendPlanResult(isSuccess: Boolean) {
        if (isSuccess) {
            loadData()
        }
    }

    override fun sendUserPlans(plans: List<Plan>, isLoading: Boolean) {
        if (!isLoading)
            adapter.swapData(ArrayList())

        adapter.addData(plans)
        mRefresh.isRefreshing = false
    }

    override fun onPlanCreate(plan: Plan) {
        presenter.insertPlan(plan)
    }

    override fun onPlanClick(plan: Plan) {
        val intent = Intent(context, DetailsPlanActivity::class.java)
        intent.putExtra(Constants.EXTRA_ID, plan.id)
        startActivity(intent)
    }

    private fun showDialog() {
        DialogFactory.createDialogSelectMakingImage(context!!, MaterialDialog.ListCallback { _, _, position, _ ->
            when (position) {
                0 -> openGallery()
                1 -> openCamera()
            }
        }).show()
    }

    private fun openGallery() {
        val i = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(i, Constants.ACTION_SELECT_IMAGE)
    }

    private fun openCamera() {
        val value = ContentValues()
        value.put(MediaStore.Images.Media.TITLE, "IMG")
        value.put(MediaStore.Images.Media.DESCRIPTION, "Camera")
        if (activity != null)
            imageUri = activity!!.contentResolver
                    .insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, value)

        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri)
        startActivityForResult(intent, Constants.ACTION_IMAGE_CAPTURE)
    }

    @NeedsPermission(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)
    internal fun show() {
        showDialog()
    }

    @OnShowRationale(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)
    internal fun showRationale(request: PermissionRequest) {
        AlertDialog.Builder(activity)
                .setMessage(R.string.permission_camera_rationale)
                .setPositiveButton(R.string.permission_button_allow) { _, _ -> request.proceed() }
                .setNegativeButton(R.string.permission_button_deny) { _, _ -> request.cancel() }
                .show()
    }

    @OnPermissionDenied(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)
    internal fun showDenied() {
        Toast.makeText(activity, R.string.permission_camera_denied, Toast.LENGTH_SHORT).show()
    }

    @OnNeverAskAgain(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)
    internal fun showNeverAsk() {
        Toast.makeText(activity, R.string.permission_neverask, Toast.LENGTH_SHORT).show()
    }
}
