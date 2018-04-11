package com.sjkj.parent.mvp.contract.assign

import com.sjkj.parent.data.server.Library
import com.sjkj.parent.data.server.QuestionBean
import com.sjkj.parent.mvp.contract.BaseContract

/**
 * @author by dingl on 2017/9/15.
 * @desc AssignContract
 */
class AssignContract : BaseContract() {

    interface BaseView : BaseRecycleBaseView<Library>

    interface Presenter : BasePresenter {
        fun getWrongData(courseInfoID: Int?)
        fun getNormalData(courseInfoID: Int?)
    }

    interface DetailBaseView : BaseRecycleBaseView<QuestionBean>

    interface DetailPresenter {
        fun getWrongData(knowledgePointIDs: String, courseInfoID: Int, startDate: String?, endDate: String?)
        fun getWrongMoreData(knowledgePointIDs: String, courseInfoID: Int, startDate: String?, endDate: String?)
        fun getNormalData(libraryIds: String, courseInfoID: Int)
        fun getNormalMoreData(libraryIds: String, courseInfoID: Int)
    }

    interface AssignHwView : BaseContract.BaseView {
        fun assignSuccess()
        fun assignError()
    }

    interface AssignHwPresenter {
        fun assignHw(CourseInfoID: Int?, Name: String?,
                     WorkType: Int?, RevisedCheckType: Int?,
                     AssignTime: String?, FinishTime: String?,
                     QuestionInfoIDs: String?)
    }
}
