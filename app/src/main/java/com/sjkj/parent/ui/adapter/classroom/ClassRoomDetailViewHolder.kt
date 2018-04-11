package com.sjkj.parent.ui.adapter.classroom

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.blankj.utilcode.util.StringUtils
import com.chad.library.adapter.base.BaseViewHolder
import com.sjkj.parent.R
import com.sjkj.parent.data.server.QuestionBean
import com.sjkj.parent.utils.loadImg
import org.jetbrains.anko.sdk25.coroutines.onClick

/**
 * @author by dingl on 2017/9/20.
 * @desc BaseHomeWorkDetailViewHolder
 */
open class ClassRoomDetailViewHolder(view: View) : BaseViewHolder(view) {

    private val tv_position = view.findViewById<TextView>(R.id.tv_position)

    private val tv_know = view.findViewById<TextView>(R.id.tv_know)

    private val tv_content = view.findViewById<TextView>(R.id.tv_content)

    private val iv_content = view.findViewById<ImageView>(R.id.iv_content)

    var item: QuestionBean? = null

    fun bindBaseData(fuPosition: Int, itemClick: (List<String>?) -> Unit, item: QuestionBean?) {
        this.item = item

        if (fuPosition == -1) {
            tv_position.text = String.format("${adapterPosition.plus(1)}. ")
        } else {
            tv_position.text = String.format("${fuPosition.plus(1)}.${adapterPosition + 1} ")
        }

        tv_know.text = this.item?.KnowledgePointNames

        if (!StringUtils.isEmpty(this.item?.ContentImage)) {
            tv_content.visibility = View.GONE
            iv_content.visibility = View.VISIBLE
            loadImg(itemView.context, this.item?._getContentImage(), iv_content)
            iv_content.onClick { itemClick(this@ClassRoomDetailViewHolder.item?._getContentImage()?.split(",")) }
        } else {
            tv_content.visibility = View.VISIBLE
            iv_content.visibility = View.GONE
            tv_content.text = this.item?.Content
        }

    }


}

