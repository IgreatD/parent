package com.sjkj.parent.mvp.presenter.classroom

import com.sjkj.parent.data.server.ClassRoomDetailRequestBody
import com.sjkj.parent.data.server.QuestionBean
import com.sjkj.parent.mvp.contract.classroom.ClassRoomDetailContract
import com.sjkj.parent.mvp.presenter.BasePresenter
import com.sjkj.parent.rxhelper.RxSchedulersHelper
import com.sjkj.parent.rxhelper.RxSubscriber
import com.sjkj.parent.utils.create
import com.sjkj.parent.utils.getApi
import com.sjkj.parent.utils.getStudentID

/**
 * @author by dingl on 2017/9/18.
 * @desc ClassRoomDetailCheckPresenter
 */
class ClassRoomDetailCheckPresenter constructor(private val mSourceView: ClassRoomDetailContract.CheckBaseView) : BasePresenter(), ClassRoomDetailContract.CheckAdapter {

    override fun getClassRoomDetailCheckData(ClassroomTeachingID: Int, ClassroomTeachingCategory: Int) {
        addSubscription(getApi().getClassRoomCheckDetail(create(ClassRoomDetailRequestBody(getStudentID(), ClassroomTeachingID, ClassroomTeachingCategory)))
                .compose(RxSchedulersHelper.io_main())
                .subscribe(object : RxSubscriber<List<QuestionBean>>(mSourceView) {

                    override fun _onNext(t: List<QuestionBean>) {

                        mSourceView.setNewData(t)
                        mSourceView.loadEnd()
                    }

                    override fun _onEmptyNext(msg: String) {
                        mSourceView.loadEnd()
                    }

                }))
    }

}
