package com.sjkj.parent.ui.adapter.classroom.question

import android.support.v7.widget.AppCompatImageView
import android.view.View
import android.widget.LinearLayout
import com.sjkj.parent.R
import com.sjkj.parent.ui.adapter.classroom.ClassRoomDetailViewHolder
import org.jetbrains.anko.imageResource
import org.jetbrains.anko.sdk25.coroutines.onClick

/**
 * @author by dingl on 2017/10/27.
 * @desc QuestionDuo
 */
fun ClassRoomDetailViewHolder.bindChildData(itemClick: (List<String>?) -> Unit, showAnswerAndStatus: Boolean, showAddButton: Boolean) {

    val student_ll = itemView.findViewById<LinearLayout>(R.id.student_ll)
    val ll_answer = itemView.findViewById<LinearLayout>(R.id.ll_answer)
    val iv_add = itemView.findViewById<AppCompatImageView>(R.id.iv_add)

    if (showAddButton && item?.ChildQuestionInfoList?.isEmpty() == true) {
        iv_add.visibility = View.VISIBLE
        iv_add.onClick {
            item?.isCheck = !(item?.isCheck ?: false)
            if (item?.isCheck == true)
                iv_add.imageResource = R.drawable.ic_question_checked
            else
                iv_add.imageResource = R.drawable.ic_question_check
        }
    } else
        iv_add.visibility = View.GONE

    if (item?.StatusInfo == 1 || showAnswerAndStatus) {
        student_ll.visibility = View.GONE
        ll_answer.visibility = View.GONE
    } else {
        student_ll.visibility = View.VISIBLE
        ll_answer.visibility = View.VISIBLE
        setStatus(item, itemClick)
        showAnswer(item)
    }

}
