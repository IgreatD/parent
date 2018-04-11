package com.sjkj.parent.ui.controller.base

import android.content.Context
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.SimpleItemAnimator
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.sjkj.parent.R
import com.sjkj.parent.mvp.contract.BaseContract
import kotlinx.android.synthetic.main.controller_recycle_base.view.*
import org.jetbrains.anko.toast


/**
 * @author by dingl on 2017/9/18.
 * @desc BaseRecycleVController
 */
abstract class BaseRecycleVController<T, V : BaseViewHolder>(context: Context) : FrameLayout(context), BaseContract.BaseRecycleBaseView<T>, BaseQuickAdapter.RequestLoadMoreListener {

    override fun onLoadMoreRequested() {

    }

    protected var baseList = ArrayList<T>()

    open var baseAdapter: BaseQuickAdapter<T, V>? = null

    protected var base_srl: SwipeRefreshLayout? = null

    protected var base_rv: RecyclerView? = null

    init {
        LayoutInflater.from(context).inflate(R.layout.controller_recycle_base, this)
        base_srl = findViewById(R.id.base_srl)
        base_rv = findViewById(R.id.base_rv)
        initView()
    }

    fun initView() {

        base_srl?.setColorSchemeResources(R.color.app_color_blue)

        initData()

        initRv()

        initListener()
    }

    open fun initRv() {

        base_rv?.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

        base_rv?.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))

        baseAdapter?.openLoadAnimation(BaseQuickAdapter.SCALEIN)

        base_rv?.adapter = baseAdapter

        baseAdapter?.setOnLoadMoreListener(this, base_rv)

        (base_rv?.itemAnimator as SimpleItemAnimator).supportsChangeAnimations = false
    }

    abstract fun initListener()

    abstract fun initData()

    override fun setNewData(t: List<T>) {
        baseList.clear()
        baseList.addAll(t)
        baseAdapter?.setNewData(t)
    }

    override fun setMoreData(t: List<T>) {
        baseList.addAll(t)
        baseAdapter?.addData(t)
    }

    override fun loadError() {
        base_srl?.isRefreshing = false
        baseAdapter?.loadMoreEnd(false)
    }

    override fun loadEnd() {
        base_srl?.isRefreshing = false
        baseAdapter?.loadMoreEnd()
    }

    override fun loadComplete() {
        base_srl?.isRefreshing = false
        baseAdapter?.loadMoreComplete()
    }

    override fun showToast(toast: String) {
        context.toast(toast)
    }

    override fun showLoading() {
        if (baseList.size == 0) {
            base_rv?.visibility = View.GONE
            content_emptyView.setLoadingShowing(true)
        }
    }

    override fun hideLoading() {
        content_emptyView.setLoadingShowing(false)
    }

    override fun showLoadError() {
        base_srl?.isRefreshing = false
        if (baseList.size == 0)
            content_emptyView.show(false, resources.getString(R.string.emptyView_mode_desc_fail_title), null, resources.getString(R.string.emptyView_mode_desc_retry), {
                onClickLoadData()
            })
    }

    abstract fun onClickLoadData()

    override fun showLoadNetWorkError() {
        base_srl?.isRefreshing = false
        if (baseList.size == 0)
            content_emptyView.show(false, resources.getString(R.string.emptyView_mode_desc_fail_title), resources.getString(R.string.emptyView_mode_desc_fail_desc), resources.getString(R.string.emptyView_mode_desc_retry), { onClickLoadData() })
    }

    open fun showAll() {
        base_rv?.visibility = View.GONE
        content_emptyView.show()
    }

    override fun hideAll() {
        base_rv?.visibility = View.VISIBLE
        content_emptyView.hide()
    }

    override fun showEmptyView() {
        if (baseList.size == 0)
            content_emptyView.show(context?.getString(R.string.null_data), null)

    }

}
