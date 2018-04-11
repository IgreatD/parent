package com.sjkj.parent.mvp.contract.count

import com.sjkj.parent.data.server.CountInfo
import com.sjkj.parent.mvp.contract.BaseContract

/**
 * @author by dingl on 2017/10/19.
 * @desc CountContract
 */
class CountContract : BaseContract() {

    interface View : BaseRecycleBaseView<CountInfo>

    interface DetailPresenter {
        fun getSubjectHandData(startDate: String, endDate: String)
        fun getSubjectRightData(startDate: String, endDate: String)
    }

    interface RightPresenter {
        fun getHwRightData(startDate: String, endDate: String, courseInfoID: Int?)
        fun getKnowRightData(startDate: String, endDate: String, courseInfoID: Int?)
    }
}
