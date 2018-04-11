package com.sjkj.parent.mvp.contract.homework

import com.sjkj.parent.data.server.HomeWorkBean
import com.sjkj.parent.data.server.QuestionBean
import com.sjkj.parent.mvp.contract.BaseContract

/**
 * @author by dingl on 2017/9/19.
 * @desc HomeWorkContract
 */
class HomeWorkContract : BaseContract() {

    interface BaseView : BaseRecycleBaseView<HomeWorkBean>

    interface Presenter : BasePresenter {
        fun getHomeWorkList(courseInfoID: Int, homeWorkType: Int, category: Int)
        fun getMoreHomeWorkList(courseInfoID: Int, homeWorkType: Int, category: Int)
    }

}

class HomeWorkDetailContract {

    interface BaseView : BaseContract.BaseRecycleBaseView<QuestionBean>

    interface Presenter {
        fun getHomeWorkDetailData(homeWorkInfoID: Int)
    }

}
