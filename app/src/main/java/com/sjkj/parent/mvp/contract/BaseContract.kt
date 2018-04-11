package com.sjkj.parent.mvp.contract

/**
 * @author by dingl on 2017/9/12.
 * @desc BaseContract
 */
open class BaseContract {

    interface BaseView {
        fun showToast(toast: String)
        fun showLoading()
        fun hideLoading()
        fun showLoadError()
        fun showLoadNetWorkError()
        fun hideAll()
        fun showEmptyView()
    }

    interface BaseRecycleBaseView<in T> : BaseView {
        fun setNewData(t: List<T>)
        fun setMoreData(t: List<T>)
        fun loadError()
        fun loadEnd()
        fun loadComplete()
    }

    interface BaseRecyclePresenter : BasePresenter {
        fun getData()
        fun getMoreData()
    }

    interface BasePresenter
}
