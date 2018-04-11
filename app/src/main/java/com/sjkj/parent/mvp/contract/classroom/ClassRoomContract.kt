package com.sjkj.parent.mvp.contract.classroom

import com.sjkj.parent.data.server.ClassRoomDetailSourceBean
import com.sjkj.parent.data.server.HomeBean
import com.sjkj.parent.data.server.QuestionBean
import com.sjkj.parent.mvp.contract.BaseContract

/**
 * @author by dingl on 2017/9/15.
 * @desc AssignContract
 */
class ClassRoomContract : BaseContract() {

    interface BaseView : BaseRecycleBaseView<HomeBean>

    interface Presenter : BasePresenter {
        fun getData(courseInfoID: Int)
        fun getMoreData(courseInfoID: Int)
    }

}

class ClassRoomDetailContract {

    interface SourceBaseView : BaseContract.BaseRecycleBaseView<ClassRoomDetailSourceBean>
    interface SourceAdapter {
        fun getClassRoomDetailSourceData(ClassroomTeachingID: Int, ClassroomTeachingCategory: Int)
    }

    interface CheckBaseView : BaseContract.BaseRecycleBaseView<QuestionBean>

    interface CheckAdapter {
        fun getClassRoomDetailCheckData(ClassroomTeachingID: Int, ClassroomTeachingCategory: Int)
    }
}
