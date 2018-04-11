package com.sjkj.parent.ui.adapter.classroom.question

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.sjkj.parent.R
import com.sjkj.parent.data.server.QuestionBean
import com.sjkj.parent.ui.adapter.classroom.ClassRoomDetailViewHolder
import com.sjkj.parent.utils.getApp
import org.jetbrains.anko.imageResource
import org.jetbrains.anko.sdk25.coroutines.onClick

/**
 * @author by dingl on 2017/10/24.
 * @desc QuestionStatus
 */
fun ClassRoomDetailViewHolder.setStatus(item: QuestionBean?, itemClick: (List<String>?) -> Unit) {

    val bt_status: TextView = itemView.findViewById(R.id.bt_status)

    val iv_right: ImageView = itemView.findViewById(R.id.iv_right)

    val tv_middle = itemView.findViewById<TextView>(R.id.tv_middle)

    if (item?.StudentAnswer.isNullOrEmpty()) {
        tv_middle.text = getApp().getString(R.string.student_answer_null_)
    } else
        when (item?.QuestionBasicTypeID) {
            1, 2 -> {
                tv_middle.text = item.StudentAnswer
                        ?.replace("1", "  A")
                        ?.replace("2", "  B  ")
                        ?.replace("3", "  C  ")
                        ?.replace("4", "  D  ")
                        ?.replace("5", "  E  ")
                        ?.replace("6", "  F  ")
                        ?.replace("7", "  G")
            }
            3, 4 -> {
                if (item.StudentAnswer.isNullOrEmpty()) {
                    tv_middle.text = getApp().getString(R.string.student_answer_null_)
                } else {
                    tv_middle.text = getApp().getString(R.string.student_answer_notnull)
                    tv_middle.onClick { itemClick(item._getStudentAnswer().split(",")) }
                }
            }
            5 -> {
                tv_middle.text = item.StudentAnswer
                        ?.replace("1", "  对")
                        ?.replace("0", "  错")
            }
        }
    
    when (item?.IsCorrect) {
        1 -> {
            iv_right.visibility = View.VISIBLE
            iv_right.imageResource = R.drawable.ic_right
        }
        2 -> {
            iv_right.visibility = View.VISIBLE
            iv_right.imageResource = R.drawable.ic_half_right
        }
        0 -> {
            iv_right.visibility = View.VISIBLE
            iv_right.imageResource = R.drawable.iv_error
        }
        else -> {
            iv_right.visibility = View.GONE
        }
    }
    bt_status.text = getApp().getString(R.string.student_answer)

    when (item?.StatusInfo) {
        1 -> {
            iv_right.visibility = View.GONE
        }
        else -> {
            iv_right.visibility = View.VISIBLE
        }
    }
}
