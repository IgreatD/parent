package com.sjkj.parent.ui.adapter

import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.sjkj.parent.R
import com.sjkj.parent.data.server.HomeWorkBean
import com.sjkj.parent.utils.convertTime

/**
 * @author by dingl on 2017/9/19.
 * @desc HomeWorkAdapter
 */
class HomeWorkAdapter(data: List<HomeWorkBean>?) : BaseQuickAdapter<HomeWorkBean, BaseViewHolder>(R.layout.controller_homework_item, data) {

    override fun convert(helper: BaseViewHolder?, item: HomeWorkBean?) {
        helper?.getView<TextView>(R.id.hw_title)?.text = item?.Name

        helper?.getView<TextView>(R.id.hw_time_start)?.text = String.format("${mContext?.getString(R.string.hw_time_start)}%s", item?.AssignTime)
        helper?.getView<TextView>(R.id.hw_time_end)?.text = String.format("${mContext?.getString(R.string.hw_time_end)}%s", item?.AssignTime)
        helper?.getView<TextView>(R.id.hw_content)?.text = String.format("${mContext?.getString(R.string.hw_content)}%s", item?.QuestionInfo)
        val hw_status = helper?.getView<LinearLayout>(R.id.hw_status)
        if (item?.StatusInfo!! >= 2) {
            hw_status?.visibility = View.VISIBLE
            helper?.getView<TextView>(R.id.hw_avg_all)?.text = String.format("${mContext?.getString(R.string.hw_avg_all)}%s", convertTime(item.AvgConsumptionTime))
            helper?.getView<TextView>(R.id.hw_avg_me)?.text = String.format("${mContext?.getString(R.string.hw_avg_me)}%s", convertTime(item.SelfConsumptionTime))
            helper?.getView<TextView>(R.id.hw_score)?.text =
                    String.format("${mContext?.getString(R.string.hw_score)}%s  第%s名", item.HomeworkScores, item.HomeworkRanking)
        } else
            hw_status?.visibility = View.GONE

        helper?.addOnClickListener(R.id.action_click)

    }

}
