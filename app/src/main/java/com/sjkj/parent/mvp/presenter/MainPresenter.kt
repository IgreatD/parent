package com.sjkj.parent.mvp.presenter

import com.sjkj.parent.data.server.MessageCountBean
import com.sjkj.parent.mvp.contract.MainContract
import com.sjkj.parent.rxhelper.RxSchedulersHelper
import com.sjkj.parent.rxhelper.RxSubscriber
import com.sjkj.parent.utils.create
import com.sjkj.parent.utils.getApi
import com.sjkj.parent.utils.getStudentID

/**
 * @author by dingl on 2017/9/18.
 * @desc MainPresenter
 */
class MainPresenter constructor(private val mView: MainContract.BaseView) : BasePresenter(), MainContract.Presenter {

    override fun getMsgCount() {
        addSubscription(getApi().getMsgCount(create(com.sjkj.parent.data.server.StudentIDRequestBody(getStudentID())))
                .compose(RxSchedulersHelper.io_main())
                .subscribe(object : RxSubscriber<MessageCountBean>(mView) {

                    override fun _onNext(t: MessageCountBean) {
                        mView.setMsgCount(t.MessageCount)
                    }

                    override fun _onEmptyNext(msg: String) {
                        mView.setMsgCount(0)
                    }

                }))
    }


}
