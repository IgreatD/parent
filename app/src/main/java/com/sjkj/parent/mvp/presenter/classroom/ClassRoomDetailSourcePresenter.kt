package com.sjkj.parent.mvp.presenter.classroom

import com.sjkj.parent.data.server.ClassRoomDetailSourceBean
import com.sjkj.parent.mvp.contract.classroom.ClassRoomDetailContract
import com.sjkj.parent.mvp.presenter.BasePresenter
import com.sjkj.parent.rxhelper.RxSchedulersHelper
import com.sjkj.parent.rxhelper.RxSubscriber
import com.sjkj.parent.utils.create
import com.sjkj.parent.utils.getApi
import com.sjkj.parent.utils.getStudentID

/**
 * @author by dingl on 2017/9/18.
 * @desc ClassRoomDetailSourcePresenter
 */
class ClassRoomDetailSourcePresenter constructor(private val mSourceView: ClassRoomDetailContract.SourceBaseView) : BasePresenter(), ClassRoomDetailContract.SourceAdapter {

    override fun getClassRoomDetailSourceData(ClassroomTeachingID: Int, ClassroomTeachingCategory: Int) {
        addSubscription(getApi().getClassRoomSourceDetail(create(com.sjkj.parent.data.server.ClassRoomDetailRequestBody(getStudentID(), ClassroomTeachingID, ClassroomTeachingCategory)))
                .compose(RxSchedulersHelper.io_main())
                .subscribe(object : RxSubscriber<List<ClassRoomDetailSourceBean>>(mSourceView) {

                    override fun _onNext(t: List<ClassRoomDetailSourceBean>) {

                        mSourceView.setNewData(t)
                        mSourceView.loadComplete()
                    }

                    override fun _onEmptyNext(msg: String) {
                        mSourceView.loadEnd()
                    }

                }))
    }

}
