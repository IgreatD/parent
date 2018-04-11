package com.sjkj.parent.ui.adapter.classroom.question

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.sjkj.parent.R
import com.sjkj.parent.data.server.QuestionBean
import com.sjkj.parent.ui.adapter.classroom.ClassRoomDetailViewHolder
import com.sjkj.parent.utils.loadImg

/**
 * @author by dingl on 2017/10/21.
 * @desc QuestionAnswer
 */
fun ClassRoomDetailViewHolder.showAnswer(item: QuestionBean?) {

    val iv_answer = itemView.findViewById<ImageView>(R.id.answer_iv)
    val tv_answer = itemView.findViewById<TextView>(R.id.answer_tv)
    val tv_rate = itemView.findViewById<TextView>(R.id.tv_rate)

    tv_rate.text = item?.RightRate

    if (item?.IsAnswerText == 1) {
        iv_answer.visibility = View.GONE
        tv_answer.visibility = View.VISIBLE
        when (item.QuestionBasicTypeID) {
            1, 2 -> {
                tv_answer.text = item.Answer
                        ?.replace("1", "  A")
                        ?.replace("2", "  B  ")
                        ?.replace("3", "  C  ")
                        ?.replace("4", "  D  ")
                        ?.replace("5", "  E  ")
                        ?.replace("6", "  F  ")
                        ?.replace("7", "  G")
            }
            3, 4 -> {
                tv_answer.text = item.Answer
            }
            5 -> {
                tv_answer.text = item.Answer
                        ?.replace("1", "  对")
                        ?.replace("0", "  错")
            }
        }
    } else {
        iv_answer.visibility = View.VISIBLE
        tv_answer.visibility = View.GONE
        loadImg(itemView.context, item?._getAnswerImage(), iv_answer)
    }

}
