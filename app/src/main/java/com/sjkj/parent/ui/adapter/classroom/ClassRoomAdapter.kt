package com.sjkj.parent.ui.adapter.classroom

import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.sjkj.parent.R
import com.sjkj.parent.data.server.HomeBean

/**
 * @author by dingl on 2017/9/15.
 * @desc ClassRoomAdapter
 */
class ClassRoomAdapter(data: List<HomeBean>?)
    : BaseQuickAdapter<HomeBean, BaseViewHolder>(R.layout.controller_home_item, data) {

    override fun convert(helper: BaseViewHolder?, item: HomeBean?) {

        helper?.getView<TextView>(R.id.home_title)?.text = String.format("${mContext?.getString(R.string.classroom_name)}%s", item?.Name)

        helper?.getView<TextView>(R.id.home_time)?.text = String.format("${mContext?.getString(R.string.classroom_time)}%s", item?.CreateDate)

        helper?.getView<TextView>(R.id.home_content)?.text = String.format("${mContext?.getString(R.string.classroom_content)}%s", item?.ClassroomTeachingDescription)

        helper?.addOnClickListener(R.id.action_click)

    }

}
