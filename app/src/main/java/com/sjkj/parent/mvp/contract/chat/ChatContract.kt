package com.sjkj.parent.mvp.contract.chat

import com.sjkj.parent.data.server.ChatMessage
import com.sjkj.parent.mvp.contract.BaseContract

/**
 * @author by dingl on 2017/9/28.
 * @desc ChatContract
 */
class ChatContract {

    interface BaseView : BaseContract.BaseView {
        fun setNewData(t: List<ChatMessage>)
        fun setMoreData(t: List<ChatMessage>)
        fun loadError()
        fun loadEnd()
        fun loadComplete()
    }

    interface Presenter {
        fun getData(toUserName: String, fromUserName: String)
        fun getMoreData(toUserName: String, fromUserName: String)
    }
}
