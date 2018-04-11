package com.sjkj.parent.mvp.presenter.mine

import com.sjkj.parent.api.ParentUri
import com.sjkj.parent.data.server.MinePersonalModifyRequestBody
import com.sjkj.parent.mvp.contract.mine.MinePersonalModifyContract
import com.sjkj.parent.mvp.presenter.BasePresenter
import com.sjkj.parent.rxhelper.RxSchedulersHelper
import com.sjkj.parent.rxhelper.RxSubscriber
import com.sjkj.parent.utils.create
import com.sjkj.parent.utils.getApi

/**
 * @author by dingl on 2017/10/17.
 * @desc MinePersonalModifyPresenter
 */
class MinePersonalModifyPresenter constructor(private val mView: MinePersonalModifyContract.BaseView) : BasePresenter(), MinePersonalModifyContract.Presenter {
    override fun modify(param: MinePersonalModifyRequestBody) {

        addSubscription(getApi().editParentInfo(ParentUri.MODIFY_PERSONAL, create(param))
                .compose(RxSchedulersHelper.io_main())
                .subscribe(object : RxSubscriber<String>(mView) {

                    override fun _onNext(t: String) {
                    }

                    override fun _onEmptyNext(msg: String) {
                        mView.modifySuccess(param)
                    }

                    override fun _onError(toast: String) {
                        super._onError(toast)
                        mView.modifyFailed()
                    }
                }))
    }
}
