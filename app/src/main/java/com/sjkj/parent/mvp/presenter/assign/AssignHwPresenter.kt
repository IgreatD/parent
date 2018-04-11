package com.sjkj.parent.mvp.presenter.assign

import com.sjkj.parent.data.server.AssignHwRequestBody
import com.sjkj.parent.mvp.contract.assign.AssignContract
import com.sjkj.parent.mvp.presenter.BasePresenter
import com.sjkj.parent.rxhelper.RxSchedulersHelper
import com.sjkj.parent.rxhelper.RxSubscriber
import com.sjkj.parent.utils.*
import org.jetbrains.anko.toast

/**
 * @author by dingl on 2017/12/12.
 * @desc AssignHwPresenter
 */
class AssignHwPresenter constructor(val view: AssignContract.AssignHwView) : BasePresenter(), AssignContract.AssignHwPresenter {
    override fun assignHw(CourseInfoID: Int?, Name: String?, WorkType: Int?, RevisedCheckType: Int?, AssignTime: String?, FinishTime: String?, QuestionInfoIDs: String?) {
        val requestBody = create(AssignHwRequestBody(CourseInfoID, Name, WorkType, RevisedCheckType, AssignTime, FinishTime, getUser().UserID, getStudentID(), QuestionInfoIDs))
        addSubscription(getApi().assignHw(requestBody)
                .compose(RxSchedulersHelper.io_main())
                .subscribe(object : RxSubscriber<String>(view) {
                    override fun _onNext(t: String) {
                        getApp().toast(t)
                        view.assignSuccess()
                    }

                    override fun _onError(toast: String) {
                        super._onError(toast)
                        view.assignError()
                    }

                }))
    }
}
