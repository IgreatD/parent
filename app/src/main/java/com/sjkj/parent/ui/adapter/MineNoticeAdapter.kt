package com.sjkj.parent.ui.adapter

import android.support.v4.content.ContextCompat
import android.widget.LinearLayout
import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.sjkj.parent.R
import com.sjkj.parent.data.server.NoteBean
import org.jetbrains.anko.sdk25.coroutines.onClick

/**
 * @author by dingl on 2017/9/16.
 * @desc MineNoticeAdapter
 */
class MineNoticeAdapter(data: List<NoteBean>?, val itemClick: (String?, Int?, Int) -> Unit)
    : BaseQuickAdapter<NoteBean, BaseViewHolder>(R.layout.adapter_notice_item, data) {

    override fun convert(helper: BaseViewHolder?, item: NoteBean?) {

        val title = helper?.getView<TextView>(R.id.mine_title)

        title?.text = item?.MessageTitle

        if (item?.IsSend != 1)
            title?.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, ContextCompat.getDrawable(mContext, R.drawable.qmui_icon_tip_new), null)
        else
            title?.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, null, null)

        helper?.getView<TextView>(R.id.mine_time)?.text = String.format("${mContext.getString(R.string.mine_time)}%s", item?.PCreateDate)

        helper?.getView<TextView>(R.id.mine_name)?.text = String.format("${mContext.getString(R.string.mine_push_name)}%s", item?.Name)

        helper?.getView<LinearLayout>(R.id.action_click)?.onClick { itemClick(item?.MessageContent, item?.MessageID, helper.adapterPosition) }

    }

}
