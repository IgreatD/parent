package com.sjkj.parent.ui.adapter.count

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.sjkj.parent.R

/**
 * @author by dingl on 2017/12/5.
 * @desc CountSubjectHandAdapter
 */
class CountSubjectHandAdapter : BaseQuickAdapter<List<String>, BaseViewHolder>(R.layout.adapter_count_subject_hand) {

    override fun convert(helper: BaseViewHolder?, item: List<String>?) {
        
        helper?.setText(R.id.name1_tv,item?.get(0))
        helper?.setText(R.id.name2_tv,item?.get(1))
        helper?.setText(R.id.name3_tv,item?.get(2))
        helper?.setText(R.id.name4_tv,item?.get(3))
        helper?.setText(R.id.name5_tv,item?.get(4))
    }
}
