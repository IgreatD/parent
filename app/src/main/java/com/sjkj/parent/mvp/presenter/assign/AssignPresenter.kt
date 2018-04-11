package com.sjkj.parent.mvp.presenter.assign

import com.sjkj.parent.data.server.AssignWrongRequestBody
import com.sjkj.parent.data.server.Library
import com.sjkj.parent.mvp.contract.assign.AssignContract
import com.sjkj.parent.mvp.presenter.BasePresenter
import com.sjkj.parent.rxhelper.RxSchedulersHelper
import com.sjkj.parent.rxhelper.RxSubscriber
import com.sjkj.parent.utils.create
import com.sjkj.parent.utils.getApi
import com.sjkj.parent.utils.getUser

/**
 * @author by dingl on 2017/9/25.
 * @desc AssignPresenter
 */
class AssignPresenter constructor(private val mView: AssignContract.BaseView) : BasePresenter(), AssignContract.Presenter {
    
    override fun getNormalData(courseInfoID: Int?) {
        addSubscription(getApi().getLibraryList(create(AssignWrongRequestBody(courseInfoID, getUser().StudentID)))
                .compose(RxSchedulersHelper.io_main())
                .subscribe(object : RxSubscriber<List<Library>>(mView) {

                    override fun _onNext(t: List<Library>) {
                        mView.setNewData(t)
                        mView.loadComplete()
                    }

                    override fun _onEmptyNext(msg: String) {
                        mView.loadEnd()
                    }

                }))
    }

    override fun getWrongData(courseInfoID: Int?) {
        addSubscription(getApi().getKonwTreeList(create(AssignWrongRequestBody(courseInfoID, getUser().StudentID)))
                .compose(RxSchedulersHelper.io_main())
                .subscribe(object : RxSubscriber<List<Library>>(mView) {

                    override fun _onNext(t: List<Library>) {
                        mView.setNewData(t)
                        mView.loadComplete()
                    }

                    override fun _onEmptyNext(msg: String) {
                        mView.loadEnd()
                    }

                }))

    }


}
