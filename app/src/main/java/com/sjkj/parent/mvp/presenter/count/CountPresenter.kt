package com.sjkj.parent.mvp.presenter.count

import com.sjkj.parent.data.server.CountInfo
import com.sjkj.parent.data.server.CountListRequestBody
import com.sjkj.parent.mvp.contract.count.CountContract
import com.sjkj.parent.mvp.presenter.BasePresenter
import com.sjkj.parent.rxhelper.RxSchedulersHelper
import com.sjkj.parent.rxhelper.RxSubscriber
import com.sjkj.parent.utils.create
import com.sjkj.parent.utils.getApi
import com.sjkj.parent.utils.getStudentID

/**
 * @author by dingl on 2017/10/19.
 * @desc CountPresenter
 */
class CountSubjectHandPresenter constructor(
        private val mView: CountContract.View) : BasePresenter(), CountContract.DetailPresenter {

    override fun getSubjectRightData(startDate: String, endDate: String) {
        addSubscription(getApi().getCountSubjectRightList(create(CountListRequestBody(getStudentID(), startDate, endDate)))
                .compose(RxSchedulersHelper.io_main())
                .subscribe(object : RxSubscriber<List<CountInfo>>(mView) {

                    override fun _onNext(t: List<CountInfo>) {
                        mView.setNewData(t)
                    }

                    override fun _onEmptyNext(msg: String) {
                        super._onEmptyNext(msg)
                        mView.loadEnd()
                    }

                }))
    }

    override fun getSubjectHandData(startDate: String, endDate: String) {
        addSubscription(getApi().getCountSubjectHandList(create(CountListRequestBody(getStudentID(), startDate, endDate)))
                .compose(RxSchedulersHelper.io_main())
                .subscribe(object : RxSubscriber<List<CountInfo>>(mView) {

                    override fun _onNext(t: List<CountInfo>) {
                        mView.setNewData(t)
                    }

                    override fun _onEmptyNext(msg: String) {
                        super._onEmptyNext(msg)
                        mView.loadEnd()
                    }

                }))
    }


}


class CountSubjectRightPresenter constructor(
        private val mView: CountContract.View) : BasePresenter(), CountContract.RightPresenter {
    override fun getKnowRightData(startDate: String, endDate: String, courseInfoID: Int?) {
        val requestBody = CountListRequestBody(getStudentID(), startDate, endDate)
        requestBody.CourseInfoID = courseInfoID
        addSubscription(getApi().getCountKnowRightList(create(requestBody))
                .compose(RxSchedulersHelper.io_main())
                .subscribe(object : RxSubscriber<List<CountInfo>>(mView) {

                    override fun _onNext(t: List<CountInfo>) {
                        mView.setNewData(t)
                    }

                    override fun _onEmptyNext(msg: String) {
                        super._onEmptyNext(msg)
                        mView.loadEnd()
                    }

                }))
    }

    override fun getHwRightData(startDate: String, endDate: String, courseInfoID: Int?) {
        val requestBody = CountListRequestBody(getStudentID(), startDate, endDate)
        requestBody.CourseInfoID = courseInfoID
        addSubscription(getApi().getCountHwRightList(create(requestBody))
                .compose(RxSchedulersHelper.io_main())
                .subscribe(object : RxSubscriber<List<CountInfo>>(mView) {

                    override fun _onNext(t: List<CountInfo>) {
                        mView.setNewData(t)
                    }

                    override fun _onEmptyNext(msg: String) {
                        super._onEmptyNext(msg)
                        mView.loadEnd()
                    }

                }))
    }


}
