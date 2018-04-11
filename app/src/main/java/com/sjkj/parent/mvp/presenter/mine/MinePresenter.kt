package com.sjkj.parent.mvp.presenter.mine

import com.sjkj.parent.api.ParentUri
import com.sjkj.parent.data.server.MineChangePdRequestBody
import com.sjkj.parent.data.server.StudentIDRequestBody
import com.sjkj.parent.data.server.ChatListBean
import com.sjkj.parent.mvp.contract.mine.MineChangePdContract
import com.sjkj.parent.mvp.contract.mine.MineTeacherContract
import com.sjkj.parent.mvp.presenter.BasePresenter
import com.sjkj.parent.rxhelper.RxSchedulersHelper
import com.sjkj.parent.rxhelper.RxSubscriber
import com.sjkj.parent.utils.create
import com.sjkj.parent.utils.getApi
import com.sjkj.parent.utils.getStudentID
import com.sjkj.parent.utils.getUser

/**
 * @author by dingl on 2017/9/18.
 * @desc MineTeacherPresenter
 */
class MineTeacherPresenter constructor(
        private val mView: MineTeacherContract.BaseView) : BasePresenter(), MineTeacherContract.Presenter {
    override fun getTeacherList() {
        addSubscription(getApi().getTeacherList(create(StudentIDRequestBody(getStudentID())))
                .compose(RxSchedulersHelper.io_main())
                .subscribe(object : RxSubscriber<List<ChatListBean>>(mView) {

                    override fun _onNext(t: List<ChatListBean>) {
                        mView.setNewData(t)
                        mView.loadEnd()
                    }

                    override fun _onEmptyNext(msg: String) {
                        mView.loadEnd()
                    }
                }))

    }
}

class MineChangePdPresenter constructor(
        private val mView: MineChangePdContract.BaseView) : BasePresenter(), MineChangePdContract.Presenter {
    override fun modifyPd(oldPd: String, newPd: String) {
        addSubscription(getApi().modifyPassWord(ParentUri.MODIFY_URL, create(MineChangePdRequestBody(oldPd, newPd, getUser().UserID)))
                .compose(RxSchedulersHelper.io_main())
                .subscribe(object : RxSubscriber<String>(mView) {
                    override fun _onNext(t: String) {
                    }

                    override fun _onEmptyNext(msg: String) {
                        mView.modifySuccess()
                    }

                    override fun _onError(toast: String) {
                        super._onError(toast)
                        mView.modifyFailed()
                    }

                }))
    }

}
