package com.sjkj.parent.mvp.presenter.homework

import com.sjkj.parent.common.Common
import com.sjkj.parent.data.server.HomeWorkBean
import com.sjkj.parent.data.server.HomeWorkRequestBody
import com.sjkj.parent.mvp.contract.homework.HomeWorkContract
import com.sjkj.parent.mvp.presenter.BasePresenter
import com.sjkj.parent.rxhelper.RxSchedulersHelper
import com.sjkj.parent.rxhelper.RxSubscriber
import com.sjkj.parent.utils.create
import com.sjkj.parent.utils.getApi
import com.sjkj.parent.utils.getStudentID

/**
 * @author by dingl on 2017/9/19.
 * @desc HomeWorkPresenter
 */
class HomeWorkPresenter constructor(
        private val mView: HomeWorkContract.BaseView) : BasePresenter(), HomeWorkContract.Presenter {

    private var pageIndex = 1

    override fun getMoreHomeWorkList(courseInfoID: Int, homeWorkType: Int, category: Int) {
        addSubscription(getApi().
                getHomeWorkInofList(
                        create(HomeWorkRequestBody(getStudentID(), courseInfoID, homeWorkType, category, Common.PAGESIZE, pageIndex)))
                .compose(RxSchedulersHelper.io_main())
                .subscribe(object : RxSubscriber<List<HomeWorkBean>>(mView) {

                    override fun _onNext(t: List<HomeWorkBean>) {
                        if (t.size == Common.PAGESIZE) {
                            pageIndex++
                            mView.loadComplete()
                        } else if (t.size < Common.PAGESIZE)
                            mView.loadEnd()
                        mView.setMoreData(t)
                    }

                    override fun _onEmptyNext(msg: String) {
                        mView.loadEnd()
                    }
                }))
    }

    override fun getHomeWorkList(courseInfoID: Int, homeWorkType: Int, category: Int) {
        pageIndex = 1
        addSubscription(getApi().
                getHomeWorkInofList(
                        create(HomeWorkRequestBody(getStudentID(), courseInfoID, homeWorkType, category, Common.PAGESIZE, pageIndex)))
                .compose(RxSchedulersHelper.io_main())
                .subscribe(object : RxSubscriber<List<HomeWorkBean>>(mView) {

                    override fun _onNext(t: List<HomeWorkBean>) {
                        mView.setNewData(t)
                        mView.loadComplete()
                        pageIndex++
                    }

                    override fun _onEmptyNext(msg: String) {
                        mView.loadEnd()
                    }
                }))
    }

}
