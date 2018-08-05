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
import butterknife.BindView
import butterknife.ButterKnife
import com.afollestad.materialdialogs.MaterialDialog
import com.github.clans.fab.FloatingActionButton
import com.morozione.azotova.Constants
import com.morozione.azotova.R
import com.morozione.azotova.entity.Plan
import com.morozione.azotova.presenter.MainActivityPresenter
import com.morozione.azotova.presenter.MainActivityView
import com.morozione.azotova.ui.actiity.DetailsPlanActivity
import com.morozione.azotova.ui.adapter.PlanAdapter
import com.morozione.azotova.ui.dialog.CreatePlanDialog
import com.morozione.azotova.ui.dialog.DialogFactory
import permissions.dispatcher.*
import java.util.*

@RuntimePermissions
class UserPlansFragment : Fragment(), MainActivityView.MessageView, CreatePlanDialog.OnCreatePlanListener, PlanAdapter.OnPlanClickListener {

    @BindView(R.id.fab_create)
    internal var fabCreate: FloatingActionButton? = null
    @BindView(R.id.rv_list)
    internal var rvList: RecyclerView? = null
    @BindView(R.id.srl_refresh)
    internal var srlRefresh: SwipeRefreshLayout? = null

    private var presenter: MainActivityPresenter? = null
    private val plans = ArrayList<Plan>()
    private var adapter: PlanAdapter? = null
    private var imageUri: Uri? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.fragment_user_plans, container, false)
        ButterKnife.bind(this, rootView)

        presenter = MainActivityPresenter()
        initView()
        setListeners()

        return rootView
    }

    private fun setListeners() {
        rvList!!.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (dy > 0 && fabCreate!!.visibility == View.VISIBLE) {
                    fabCreate!!.hide(true)
                } else if (dy < 0 && fabCreate!!.visibility != View.VISIBLE) {
                    fabCreate!!.show(true)
                }
            }
        })
        fabCreate!!.setOnClickListener {
            val createPlanDialog = CreatePlanDialog()
            createPlanDialog.setOnCreatePlanListener(this@UserPlansFragment)
            if (fragmentManager != null)
                createPlanDialog.show(fragmentManager!!, "create_task")
        }
        srlRefresh!!.setOnRefreshListener { presenter!!.getPlansOfUser() }
    }

    fun initView() {
        rvList!!.setHasFixedSize(true)
        rvList!!.layoutManager = LinearLayoutManager(context)

        adapter = PlanAdapter(plans)
        adapter!!.setOnPlanClickListener(this)
        rvList!!.adapter = adapter
    }

    fun loadData() {
        presenter!!.getPlansOfUser()
        srlRefresh!!.isRefreshing = true
    }

    override fun onResume() {
        super.onResume()
        presenter!!.attach(this)
        loadData()
    }

    override fun onPause() {
        super.onPause()
        presenter!!.detach()
    }

    override fun sendPlanResult(isSuccess: Boolean) {
        if (isSuccess) {
            loadData()
        }
    }

    override fun sendUserPlans(plans: List<Plan>) {
        if (srlRefresh!!.isRefreshing)
            adapter!!.swapData(ArrayList())

        adapter!!.addData(plans)
        srlRefresh!!.isRefreshing = false
    }

    override fun onPlanCreate(plan: Plan) {
        presenter!!.insertPlan(plan)
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
