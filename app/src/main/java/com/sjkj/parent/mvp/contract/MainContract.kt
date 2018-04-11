package com.sjkj.parent.mvp.contract

/**
 * @author by dingl on 2017/9/18.
 * @desc MainContract
 */
class MainContract {

    interface BaseView : BaseContract.BaseView {
        fun setMsgCount(msgCount: Int)
    }

    interface Presenter {
        fun getMsgCount()
    }

}
