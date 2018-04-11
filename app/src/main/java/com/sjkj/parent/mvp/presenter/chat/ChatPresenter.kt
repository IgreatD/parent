package com.sjkj.parent.mvp.presenter.chat

import com.sjkj.parent.common.Common
import com.sjkj.parent.data.server.ChatMessage
import com.sjkj.parent.data.server.ChatMsgHistroyRequestBody
import com.sjkj.parent.mvp.contract.chat.ChatContract
import com.sjkj.parent.mvp.presenter.BasePresenter
import com.sjkj.parent.rxhelper.RxSchedulersHelper
import com.sjkj.parent.rxhelper.RxSubscriber
import com.sjkj.parent.utils.create
import com.sjkj.parent.utils.getApi

/**
 * @author by dingl on 2017/9/28.
 * @desc ChatPresenter
 */
class ChatPresenter constructor(private val mView: ChatContract.BaseView) : BasePresenter(), ChatContract.Presenter {

    private var pageIndex = 1

    override fun getData(toUserName: String, fromUserName: String) {
        pageIndex = 1
        addSubscription(getApi().getMsgHistory(create(ChatMsgHistroyRequestBody(toUserName, fromUserName, Common.PAGESIZE, pageIndex)))
                .compose(RxSchedulersHelper.io_main())
                .subscribe(object : RxSubscriber<List<ChatMessage>>(mView) {
                    override fun _onNext(t: List<ChatMessage>) {
                        mView.setNewData(t)
                        mView.loadComplete()
                        pageIndex++
                    }

                    override fun _onEmptyNext(msg: String) {
                        mView.loadEnd()
                    }

                }))
    }

    override fun getMoreData(toUserName: String, fromUserName: String) {
        addSubscription(getApi().getMsgHistory(create(ChatMsgHistroyRequestBody(toUserName, fromUserName, Common.PAGESIZE, pageIndex)))
                .compose(RxSchedulersHelper.io_main())
                .subscribe(object : RxSubscriber<List<ChatMessage>>(mView) {
                    override fun _onNext(t: List<ChatMessage>) {
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
}
