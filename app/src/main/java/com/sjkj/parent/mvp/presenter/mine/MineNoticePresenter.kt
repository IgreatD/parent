package com.sjkj.parent.mvp.presenter.mine

import com.sjkj.parent.common.Common
import com.sjkj.parent.data.server.NoteBean
import com.sjkj.parent.data.server.NoticeRequestBody
import com.sjkj.parent.data.server.NoticeStateRequestBody
import com.sjkj.parent.mvp.contract.mine.MineNoticeContract
import com.sjkj.parent.mvp.presenter.BasePresenter
import com.sjkj.parent.rxhelper.RxSchedulersHelper
import com.sjkj.parent.rxhelper.RxSubscriber
import com.sjkj.parent.utils.create
import com.sjkj.parent.utils.getApi
import com.sjkj.parent.utils.getStudentID

/**
 * @author by dingl on 2017/9/16.
 * @desc MineNoticePresenter
 */
class MineNoticePresenter constructor(
        private val mView: MineNoticeContract.BaseView) : BasePresenter(), MineNoticeContract.Presenter {

    override fun modifyMsgNum(messageID: Int) {
        addSubscription(getApi().updateMsgState(create(NoticeStateRequestBody(getStudentID(), messageID)))
                .compose(RxSchedulersHelper.io_main())
                .subscribe(object : RxSubscriber<String>(mView) {

                    override fun _onNext(t: String) {
                    }

                    override fun _onEmptyNext(msg: String) {
                        mView.modifyMsgNumSuccess()
                    }

                    override fun _onError(toast: String) {
                        mView.modifyMsgNumError()
                    }

                }))
    }

    private var PAGEINDEX = 1

    override fun getMoreData() {

        addSubscription(getApi().getNoticeData(create(NoticeRequestBody(getStudentID(), Common.PAGESIZE, PAGEINDEX)))
                .compose(RxSchedulersHelper.io_main())
                .subscribe(object : RxSubscriber<List<NoteBean>>(mView) {

                    override fun _onNext(t: List<NoteBean>) {
                        if (t.size == Common.PAGESIZE) {
                            PAGEINDEX++
                            mView.loadComplete()
                        } else if (t.size < Common.PAGESIZE) {
                            mView.loadEnd()
                        }
                        mView.setMoreData(t)
                    }

                    override fun _onEmptyNext(msg: String) {
                        mView.loadEnd()
                    }

                }))

    }

    override fun getData() {
        PAGEINDEX = 1
        addSubscription(getApi().getNoticeData(create(NoticeRequestBody(getStudentID(), Common.PAGESIZE, PAGEINDEX)))
                .compose(RxSchedulersHelper.io_main())
                .subscribe(object : RxSubscriber<List<NoteBean>>(mView) {

                    override fun _onNext(t: List<NoteBean>) {
                        mView.setNewData(t)
                        mView.loadComplete()
                    }

                    override fun _onEmptyNext(msg: String) {
                        mView.loadEnd()
                    }

                }))

    }


}
