package com.sjkj.parent.ui.controller.base

import android.content.Context
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
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
 * @desc BaseRecycleController
 */
abstract class BaseRecycleController<T>(context: Context) : FrameLayout(context), BaseContract.BaseRecycleBaseView<T> {

    open var baseAdapter: BaseQuickAdapter<T, BaseViewHolder>? = null

    protected var baseList = ArrayList<T>()

    init {
        initView()
    }

    fun initView() {
        LayoutInflater.from(context).inflate(R.layout.controller_recycle_base, this)

        base_srl.setColorSchemeResources(R.color.app_color_blue)

        base_rv.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

        base_rv.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))

        baseAdapter?.openLoadAnimation()

        initData()
        initListener()
        
    }

    abstract fun initListener()


    abstract fun initData()

    override fun setNewData(t: List<T>) {
        baseAdapter?.setNewData(t)
        baseList.clear()
        baseList.addAll(t)
    }

    override fun setMoreData(t: List<T>) {
        baseAdapter?.addData(t)
        baseList.addAll(t)
    }

    override fun loadError() {
        baseAdapter?.loadMoreFail()
        base_srl?.isRefreshing = false
    }

    override fun loadEnd() {
        baseAdapter?.loadMoreEnd(true)
        base_srl?.isRefreshing = false
    }

    override fun loadComplete() {
        baseAdapter?.loadMoreComplete()
        base_srl?.isRefreshing = false
    }

    override fun showToast(toast: String) {
        context.toast(toast)
    }

    override fun showLoading() {
        if (baseList.size == 0) {
            base_rv.visibility = View.GONE
            content_emptyView.setLoadingShowing(true)
        }
    }

    override fun hideLoading() {
        content_emptyView.setLoadingShowing(false)
    }

    override fun showLoadError() {
        base_srl?.isRefreshing = false
        baseAdapter?.loadMoreFail()
        if (baseList.size == 0)
            content_emptyView.show(false, resources.getString(R.string.emptyView_mode_desc_fail_title), null, resources.getString(R.string.emptyView_mode_desc_retry), {
                onClickLoadData()
            })
    }

    abstract fun onClickLoadData()

    override fun showLoadNetWorkError() {
        base_srl?.isRefreshing = false
        baseAdapter?.loadMoreFail()
        if (baseList.size == 0)
            content_emptyView.show(false, resources.getString(R.string.emptyView_mode_desc_fail_title), resources.getString(R.string.emptyView_mode_desc_fail_desc), resources.getString(R.string.emptyView_mode_desc_retry), { onClickLoadData() })
    }

    fun showAll() {
        base_rv.visibility = View.GONE
        content_emptyView.show()
        showLoading()
    }

    override fun hideAll() {
        base_rv.visibility = View.VISIBLE
        content_emptyView.hide()
        hideLoading()
    }

    override fun showEmptyView() {
        if (baseList.size == 0)
            content_emptyView.show(context?.getString(R.string.null_data), null)

    }

}
