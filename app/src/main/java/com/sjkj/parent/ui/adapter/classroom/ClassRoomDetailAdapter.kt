package com.sjkj.parent.ui.adapter.classroom

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter
import com.sjkj.parent.R
import com.sjkj.parent.common.QuestionType
import com.sjkj.parent.data.server.QuestionBean
import com.sjkj.parent.ui.adapter.classroom.question.bindChildData
import com.sjkj.parent.ui.adapter.classroom.question.bindFuData


/**
 * @author by dingl on 2017/9/19.
 * @desc HomeWorkDetailAdapter
 */
class ClassRoomDetailAdapter(private val itemClick: (List<String>?) -> Unit) : BaseMultiItemQuickAdapter<QuestionBean, ClassRoomDetailViewHolder>(null) {

    private var fuPosition = -1

    private var isShowAnswerAndStatus = false

    private var isShowAddButton = false

    fun setShowAddButton(flag: Boolean) {
        isShowAddButton = flag
    }

    fun setShowAnswerAndStatus(flag: Boolean) {
        isShowAnswerAndStatus = flag
    }

    init {
        addItemType(QuestionType.QUESTION_TYPE_DAN, R.layout.item_question_child)
        addItemType(QuestionType.QUESTION_TYPE_FU, R.layout.item_question_fu)
    }

    override fun convert(helper: ClassRoomDetailViewHolder?, item: QuestionBean?) {
        helper?.bindBaseData(fuPosition, itemClick, item)
        when (helper?.itemViewType) {
            QuestionType.QUESTION_TYPE_FU ->
                helper.bindFuData(item, itemClick, isShowAnswerAndStatus, isShowAddButton)
            else ->
                helper?.bindChildData(itemClick, isShowAnswerAndStatus, isShowAddButton)
        }

    }

    fun setFuPosition(adapterPosition: Int) {
        fuPosition = adapterPosition
    }
}

