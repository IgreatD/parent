package com.sjkj.parent.mvp.presenter.assign

import com.sjkj.parent.common.Common
import com.sjkj.parent.data.server.AssignNormalListRequestBody
import com.sjkj.parent.data.server.AssignWrongListRequestBody
import com.sjkj.parent.data.server.QuestionBean
import com.sjkj.parent.mvp.contract.assign.AssignContract
import com.sjkj.parent.mvp.presenter.BasePresenter
import com.sjkj.parent.rxhelper.RxSchedulersHelper
import com.sjkj.parent.rxhelper.RxSubscriber
import com.sjkj.parent.utils.create
import com.sjkj.parent.utils.getApi
import com.sjkj.parent.utils.getStudentID

/**
 * @author by dingl on 2017/9/25.
 * @desc AssignDetailPresenter
 */
class AssignDetailPresenter constructor(private val mView: AssignContract.DetailBaseView) : BasePresenter(), AssignContract.DetailPresenter {
    override fun getNormalData(libraryIds: String, courseInfoID: Int) {
        PageIndex = 1
        addSubscription(getApi().getQuestionLibraryList(
                create(AssignNormalListRequestBody(courseInfoID, libraryIds, Common.PAGESIZE, PageIndex)))
                .compose(RxSchedulersHelper.io_main())
                .subscribe(object : RxSubscriber<List<QuestionBean>>(mView) {

                    override fun _onNext(t: List<QuestionBean>) {
                        mView.setNewData(t)
                        mView.loadComplete()
                        PageIndex++
                    }

                    override fun _onEmptyNext(msg: String) {
                        mView.loadEnd()
                    }
                }))
    }

    override fun getNormalMoreData(libraryIds: String, courseInfoID: Int) {
        addSubscription(getApi().getQuestionLibraryList(
                create(AssignNormalListRequestBody(courseInfoID, libraryIds, Common.PAGESIZE, PageIndex)))
                .compose(RxSchedulersHelper.io_main())
                .subscribe(object : RxSubscriber<List<QuestionBean>>(mView) {

                    override fun _onEmptyNext(msg: String) {
                        mView.loadEnd()
                    }

                    override fun _onNext(t: List<QuestionBean>) {
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

    private var PageIndex = 1

    override fun getWrongData(knowledgePointIDs: String, courseInfoID: Int, startDate: String?, endDate: String?) {
        PageIndex = 1
        addSubscription(getApi().getQuestionErrorList(
                create(AssignWrongListRequestBody(courseInfoID, getStudentID(), knowledgePointIDs, Common.PAGESIZE, PageIndex, startDate, endDate)))
                .compose(RxSchedulersHelper.io_main())
                .subscribe(object : RxSubscriber<List<QuestionBean>>(mView) {

                    override fun _onNext(t: List<QuestionBean>) {
                        mView.setNewData(t)
                        mView.loadComplete()
                        PageIndex++
                    }

                    override fun _onEmptyNext(msg: String) {
                        mView.loadEnd()
                    }
                }))
    }

    override fun getWrongMoreData(knowledgePointIDs: String, courseInfoID: Int, startDate: String?, endDate: String?) {
        addSubscription(getApi().getQuestionErrorList(
                create(AssignWrongListRequestBody(courseInfoID, getStudentID(), knowledgePointIDs, Common.PAGESIZE, PageIndex, startDate, endDate)))
                .compose(RxSchedulersHelper.io_main())
                .subscribe(object : RxSubscriber<List<QuestionBean>>(mView) {

                    override fun _onEmptyNext(msg: String) {
                        mView.loadEnd()
                    }

                    override fun _onNext(t: List<QuestionBean>) {
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

}
