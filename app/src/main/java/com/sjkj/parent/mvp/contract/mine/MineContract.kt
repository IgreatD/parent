package com.sjkj.parent.mvp.contract.mine

import com.sjkj.parent.data.server.MinePersonalModifyRequestBody
import com.sjkj.parent.data.server.NoteBean
import com.sjkj.parent.data.server.ChatListBean
import com.sjkj.parent.mvp.contract.BaseContract

/**
 * @author by dingl on 2017/9/21.
 * @desc MineContract
 */
class MineTeacherContract : BaseContract() {

    interface BaseView : BaseRecycleBaseView<ChatListBean>

    interface Presenter {
        fun getTeacherList()
    }

}

class MineNoticeContract : BaseContract() {

    interface BaseView : BaseRecycleBaseView<NoteBean> {
        fun modifyMsgNumSuccess()
        fun modifyMsgNumError()
    }

    interface Presenter : BaseRecyclePresenter {
        fun modifyMsgNum(messageID: Int)
    }

}

class MineChangePdContract {
    interface BaseView : BaseContract.BaseView {
        fun modifySuccess()
        fun modifyFailed()
    }

    interface Presenter {
        fun modifyPd(oldPd: String, newPd: String)
    }
}

class MinePersonalModifyContract {
    interface BaseView : BaseContract.BaseView {
        fun modifySuccess(param: MinePersonalModifyRequestBody)
        fun modifyFailed()
    }

    interface Presenter {
        fun modify(param: MinePersonalModifyRequestBody)
    }
}

