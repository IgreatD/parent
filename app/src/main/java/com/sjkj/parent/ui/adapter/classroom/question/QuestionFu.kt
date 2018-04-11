package com.sjkj.parent.ui.adapter.classroom.question

import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.sjkj.parent.R
import com.sjkj.parent.data.server.QuestionBean
import com.sjkj.parent.ui.adapter.classroom.ClassRoomDetailAdapter
import com.sjkj.parent.ui.adapter.classroom.ClassRoomDetailViewHolder

/**
 * @author by dingl on 2017/10/27.
 * @desc QuestionFu
 */
fun ClassRoomDetailViewHolder.bindFuData(item: QuestionBean?, itemClick: (List<String>?) -> Unit, showAnswerAndStatus: Boolean, showAddButton: Boolean) {

    val rv_fu = itemView.findViewById<RecyclerView>(R.id.fu_rv)
    val adapter = ClassRoomDetailAdapter(itemClick)
    adapter.setShowAnswerAndStatus(showAnswerAndStatus)
    if (item?.ChildQuestionInfoList != null) {
        adapter.setFuPosition(adapterPosition)
        adapter.setShowAddButton(showAddButton)
        adapter.setNewData(item.ChildQuestionInfoList)
    }

    adapter.openLoadAnimation(BaseQuickAdapter.SCALEIN)
    rv_fu?.layoutManager = LinearLayoutManager(itemView.context, LinearLayoutManager.VERTICAL, false)
    rv_fu?.adapter = adapter
}
