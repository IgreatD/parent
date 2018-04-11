package com.sjkj.parent.ui.adapter

import android.text.TextUtils
import android.view.View
import android.widget.FrameLayout
import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.google.gson.Gson
import com.sjkj.parent.R
import com.sjkj.parent.common.ChatType
import com.sjkj.parent.data.server.ChatFile
import com.sjkj.parent.data.server.ChatListBean
import java.text.SimpleDateFormat
import java.util.*


/**
 * @author by dingl on 2017/9/18.
 * @desc MineTeacherAdapter
 */
class MineTeacherAdapter(data: List<ChatListBean>?)
    : BaseQuickAdapter<ChatListBean, BaseViewHolder>(R.layout.adapter_mine_teacher_item, data) {

    override fun convert(helper: BaseViewHolder?, item: ChatListBean?) {

        helper?.getView<TextView>(R.id.name)?.text = item?.Name

        helper?.addOnClickListener(R.id.friend_ll)

        val msgNum = item?.msgNum

        val tv_msgNum = helper?.getView<TextView>(R.id.msgNum)

        if (msgNum == 0) {
            helper?.getView<FrameLayout>(R.id.num_ll)?.visibility = View.GONE
            tv_msgNum?.text = "0"
        } else {
            helper?.getView<FrameLayout>(R.id.num_ll)?.visibility = View.VISIBLE
            tv_msgNum?.text = msgNum.toString()
        }

        if (!TextUtils.isEmpty(item?.MsgContent)) {
            helper?.setVisible(R.id.content, true)
            try {
                when (item?.MsgType) {
                    ChatType.MSGTYPE_TXT -> helper?.setText(R.id.content, item.MsgContent)
                    ChatType.MSGTYPE_IMG -> helper?.setText(R.id.content, "{图片}    " + item.MsgContent?.split("/")?.get(2))
                    ChatType.MSGTYPE_FILE -> {
                        val chatFile = Gson().fromJson<ChatFile>(item.MsgContent, ChatFile::class.java)
                        helper?.setText(R.id.content, String.format("{文件}    ${chatFile.fileName}"))
                    }
                    ChatType.MSGTYPE_VOICE -> {
                        val chatFile = Gson().fromJson<ChatFile>(item.MsgContent, ChatFile::class.java)
                        helper?.setText(R.id.content, String.format("{语音}    ${chatFile.duration}"))
                    }
                    ChatType.MSGTYPE_MICRO -> {
                        val chatFile = Gson().fromJson<ChatFile>(item.MsgContent, ChatFile::class.java)
                        helper?.setText(R.id.content, String.format("{微课}    ${chatFile.duration}"))
                    }
                }
                val date = item?.creationDate
                val format = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.SIMPLIFIED_CHINESE).parse(date)
                val format1 = SimpleDateFormat("yyyy-MM-dd E HH:mm:ss", Locale.SIMPLIFIED_CHINESE).format(format)
                helper?.setText(R.id.creationDate, format1)
            } catch (e: Exception) {
                helper?.setText(R.id.creationDate, item?.creationDate)
            }

        } else {
            helper?.setVisible(R.id.content, false)
            helper?.setText(R.id.creationDate, "")
            helper?.setText(R.id.content, "")
        }
    }

}
