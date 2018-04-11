package com.sjkj.parent.rxhelper

import com.sjkj.parent.exception.RequestFailedException
import com.sjkj.parent.exception.RequestIllegalArgumentException
import com.sjkj.parent.exception.RequestNetWorkException
import com.sjkj.parent.exception.RequestNullDataException
import com.sjkj.parent.mvp.contract.BaseContract
import com.sjkj.parent.utils.hasNetwork
import com.sjkj.parent.utils.logD
import rx.Subscriber
import java.net.SocketTimeoutException
import java.net.UnknownHostException

/**
 * @author by dingl on 2017/9/12.
 * *
 * @desc RxSubscriber
 */

abstract class RxSubscriber<T>(private val mBaseView: BaseContract.BaseView) : Subscriber<T>() {

    override fun onStart() {
        mBaseView.showLoading()
        if (!hasNetwork()) {
            onError(RequestNetWorkException("暂无网络，请连接网络后再试!"))
        }
    }

    override fun onCompleted() {

    }

    override fun onError(e: Throwable) {
        logD(this::class.java, e.message)
        mBaseView.hideLoading()
        val msg: String
        when (e) {
            is RequestNullDataException -> {
                msg = e.message as String
                _onEmptyNext(msg)
                mBaseView.showEmptyView()
                return
            }
            is RequestNetWorkException -> {
                msg = "暂无网络，请连接网络后再试!"
                mBaseView.showLoadNetWorkError()
            }
            is RequestFailedException -> {
                msg = e.message as String
                mBaseView.showLoadError()
            }
            is RequestIllegalArgumentException -> {
                msg = e.message as String
                mBaseView.showLoadError()
            }
            is UnknownHostException -> {
                msg = "暂无网络，请连接网络后再试!"
                mBaseView.showLoadNetWorkError()
            }
            is SocketTimeoutException -> {
                msg = "请求超时，请稍后重试..."
                mBaseView.showLoadError()
            }
            else -> {
                mBaseView.showLoadError()
                msg = "请求失败，请稍后重试..."
            }
        }
        _onError(msg)
    }

    open fun _onEmptyNext(msg: String) {
        mBaseView.showToast(msg)
    }

    open fun _onError(toast: String) {
        mBaseView.showToast(toast)
        unsubscribe()
    }

    override fun onNext(t: T) {
        mBaseView.hideAll()
        mBaseView.hideLoading()
        _onNext(t)
        unsubscribe()
    }

    abstract fun _onNext(t: T)

}
