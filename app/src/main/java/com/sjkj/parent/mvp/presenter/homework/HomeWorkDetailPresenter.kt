package com.sjkj.parent.mvp.presenter.homework

import com.sjkj.parent.data.server.HomeWorkDetaialRequestBody
import com.sjkj.parent.data.server.QuestionBean
import com.sjkj.parent.mvp.contract.homework.HomeWorkDetailContract
import com.sjkj.parent.mvp.presenter.BasePresenter
import com.sjkj.parent.rxhelper.RxSchedulersHelper
import com.sjkj.parent.rxhelper.RxSubscriber
import com.sjkj.parent.utils.create
import com.sjkj.parent.utils.getApi
import com.sjkj.parent.utils.getStudentID


/**
 * @author by dingl on 2017/9/19.
 * @desc HomeWorkDetailPresenter
 */
class HomeWorkDetailPresenter constructor(private val mView: HomeWorkDetailContract.BaseView) : BasePresenter(), HomeWorkDetailContract.Presenter {

    override fun getHomeWorkDetailData(homeWorkInfoID: Int) {
        addSubscription(getApi().getHomeWorkQuestionList(create(HomeWorkDetaialRequestBody
        (getStudentID(), homeWorkInfoID)))
                .compose(RxSchedulersHelper.io_main())
                .subscribe(object : RxSubscriber<List<QuestionBean>>(mView) {

                    override fun _onNext(t: List<QuestionBean>) {
                        mView.setNewData(t)
                        mView.loadEnd()
                    }

                    override fun _onEmptyNext(msg: String) {
                        mView.loadEnd()
                    }

                }))
    }


}
