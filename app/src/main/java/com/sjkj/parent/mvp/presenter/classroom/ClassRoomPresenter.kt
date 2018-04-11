package com.sjkj.parent.mvp.presenter.classroom

import com.sjkj.parent.common.Common
import com.sjkj.parent.data.server.HomeBean
import com.sjkj.parent.data.server.HomeRequestBody
import com.sjkj.parent.mvp.contract.classroom.ClassRoomContract
import com.sjkj.parent.mvp.presenter.BasePresenter
import com.sjkj.parent.rxhelper.RxSchedulersHelper
import com.sjkj.parent.rxhelper.RxSubscriber
import com.sjkj.parent.utils.create
import com.sjkj.parent.utils.getApi
import com.sjkj.parent.utils.getStudentID

/**
 * @author by dingl on 2017/9/15.
 * @desc ClassRoomPresenter
 */
class ClassRoomPresenter constructor(private val mView: ClassRoomContract.BaseView) : BasePresenter(), ClassRoomContract.Presenter {

    override fun getMoreData(courseInfoID: Int) {
        addSubscription(getApi().getClassroomData(create(com.sjkj.parent.data.server.HomeRequestBody(Common.PAGESIZE, PageIndex, getStudentID(), courseInfoID)))
                .compose(RxSchedulersHelper.io_main())
                .subscribe(object : RxSubscriber<List<HomeBean>>(mView) {

                    override fun _onEmptyNext(msg: String) {
                        mView.loadEnd()
                    }

                    override fun _onNext(t: List<HomeBean>) {
                        if (t.size == Common.PAGESIZE) {
                            PageIndex++
                            mView.loadComplete()
                        } else if (t.size < Common.PAGESIZE) {
                            mView.loadEnd()
                        }
                        mView.setMoreData(t)

                    }

                }))
    }

    private var PageIndex: Int = 1

    override fun getData(courseInfoID: Int) {
        PageIndex = 1
        addSubscription(getApi().getClassroomData(create(HomeRequestBody(Common.PAGESIZE, PageIndex, getStudentID(), courseInfoID)))
                .compose(RxSchedulersHelper.io_main())
                .subscribe(object : RxSubscriber<List<HomeBean>>(mView) {

                    override fun _onNext(t: List<HomeBean>) {
                        mView.setNewData(t)
                        mView.loadComplete()
                        PageIndex++
                    }

                    override fun _onEmptyNext(msg: String) {
                        mView.loadEnd()
                    }
                }))
    }

}
