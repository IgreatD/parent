package com.sjkj.parent.ui.controller.mine

import android.content.Context
import com.sjkj.parent.common.Which
import com.sjkj.parent.data.server.MessageEvent
import com.sjkj.parent.data.server.NoteBean
import com.sjkj.parent.mvp.contract.mine.MineNoticeContract
import com.sjkj.parent.mvp.presenter.mine.MineNoticePresenter
import com.sjkj.parent.ui.adapter.MineNoticeAdapter
import com.sjkj.parent.ui.controller.base.BaseRecycleController
import com.sjkj.parent.ui.fragment.BackBaseFragment
import com.sjkj.parent.ui.fragment.mine.MineNoticeDetailFragment
import kotlinx.android.synthetic.main.controller_recycle_base.view.*
import org.greenrobot.eventbus.EventBus
import org.jetbrains.anko.support.v4.onRefresh

/**
 * @author by dingl on 2017/9/19.
 * @desc MineNoticeController
 */
class MineNoticeController(context: Context) : BaseRecycleController<NoteBean>(context), MineNoticeContract.BaseView {

    override fun modifyMsgNumSuccess() {
        baseList[mPosition].IsSend = 1
        baseAdapter?.notifyItemChanged(mPosition)
        EventBus.getDefault().post(MessageEvent(Which.NOTICE_STATE, null))
    }

    override fun modifyMsgNumError() {

    }

    private lateinit var mineNoticePresenter: MineNoticePresenter

    private var mPosition = 0

    override fun initListener() {
        base_srl.onRefresh {
            mineNoticePresenter.getData()
        }
    }

    override fun initData() {
        baseAdapter = MineNoticeAdapter(baseList) {
            msgContent, messageID, position ->
            mPosition = position
            mineNoticePresenter.modifyMsgNum(messageID ?: 0)
            startFragment(MineNoticeDetailFragment().newInstance(msgContent))

        }
        mineNoticePresenter = MineNoticePresenter(this)
        base_rv?.adapter = baseAdapter

    }

    override fun onClickLoadData() {
        mineNoticePresenter.getData()
    }

    fun loadData() {

        mineNoticePresenter.getData()

    }

    private var mHomeControlListener: HomeControlListener? = null

    interface HomeControlListener {
        fun startFragment(fragment: BackBaseFragment)
    }

    fun startFragment(fragment: BackBaseFragment) {
        if (mHomeControlListener != null) {
            mHomeControlListener?.startFragment(fragment)
        }
    }

    fun setHomeControlListener(homeControlListener: HomeControlListener) {
        mHomeControlListener = homeControlListener
    }
}
