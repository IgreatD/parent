package com.sjkj.parent.ui.controller.mine

import android.content.Context
import android.content.Intent
import android.support.v7.widget.DividerItemDecoration
import com.sjkj.parent.common.Common
import com.sjkj.parent.data.server.ChatListBean
import com.sjkj.parent.mvp.contract.mine.MineTeacherContract
import com.sjkj.parent.mvp.presenter.mine.MineTeacherPresenter
import com.sjkj.parent.ui.activity.ChatActivity
import com.sjkj.parent.ui.adapter.MineTeacherAdapter
import com.sjkj.parent.ui.controller.base.BaseRecycleController
import com.sjkj.parent.utils.NotificationUtils
import kotlinx.android.synthetic.main.controller_recycle_base.view.*
import org.jetbrains.anko.support.v4.onRefresh
import org.litepal.crud.DataSupport

/**
 * @author by dingl on 2017/9/22.
 * @desc MineTeacherController
 */
class MineTeacherController(context: Context) : BaseRecycleController<ChatListBean>(context), MineTeacherContract.BaseView {

    private var mineNoticePresenter: MineTeacherPresenter? = null

    override fun initListener() {
        base_srl.onRefresh {
            mineNoticePresenter?.getTeacherList()
        }
        baseAdapter?.setOnItemChildClickListener { _, _, position ->
            startChatActivity(baseList[position])
        }
    }

    private fun startChatActivity(chatListBean: ChatListBean?) {
        val intent = Intent(context, ChatActivity::class.java)
        intent.putExtra(Common.USER_NAME, chatListBean?.UserName)
        intent.putExtra(Common.NAME, chatListBean?.Name)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        NotificationUtils.cancel(context, chatListBean?.Name)
        context.startActivity(intent)
    }

    override fun initData() {
        baseAdapter = MineTeacherAdapter(baseList)
        mineNoticePresenter = MineTeacherPresenter(this)
        base_rv?.adapter = baseAdapter
    }

    override fun onClickLoadData() {
        mineNoticePresenter?.getTeacherList()
    }

    fun loadData(position: Int) {
        base_rv.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
        when (position) {
            0 -> {
                base_srl.isEnabled = false
                val list = DataSupport.select("*").find(ChatListBean::class.java) as List<ChatListBean>
                setNewData(list.sortedByDescending { it.creationDate })
            }
            1 -> mineNoticePresenter?.getTeacherList()
        }
    }

    fun setNewData() {
        val list = DataSupport.select("*").find(ChatListBean::class.java) as List<ChatListBean>
        setNewData(list.sortedByDescending { it.creationDate })
    }
}
