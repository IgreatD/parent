package com.sjkj.parent.ui.widget.tree

import android.view.View
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.sjkj.parent.R
import org.jetbrains.anko.imageResource

class SimpleTreeAdapter(datas: List<Node>?) : BaseQuickAdapter<Node, BaseViewHolder>(R.layout.adapter_view_tree_item, datas) {

    override fun convert(helper: BaseViewHolder?, node: Node?) {

        val ic = helper?.getView<ImageView>(R.id.id_treenode_icon)

        val id_click_icon = helper?.getView<CheckBox>(R.id.id_click_icon)

        val label = helper?.getView<TextView>(R.id.id_treenode_label)

        val click_rl = helper?.getView<RelativeLayout>(R.id.click_rl)

        helper?.addOnClickListener(R.id.id_click_icon)?.addOnClickListener(R.id.action_ex)

        node?.level?.times(30)?.let { helper?.itemView?.setPadding(it, 3, 3, 3) }

        if (node?.icon == -1) {
            ic?.visibility = View.INVISIBLE
        } else {
            ic?.visibility = View.VISIBLE
            ic?.imageResource = node?.icon ?: R.drawable.outline_list_collapse
        }
        if (node != null)
            if (node.num > 0) {
                label?.text = String.format("${node.name}(${node.num})")
            } else {
                label?.text = node.name
            }
        when {
            node?.click == 0 -> click_rl?.visibility = View.GONE
            node?.click == -1 -> {
                click_rl?.visibility = View.VISIBLE
                id_click_icon?.isChecked = false
            }
            node?.click == 1 -> {
                click_rl?.visibility = View.VISIBLE
                id_click_icon?.isChecked = true
            }
        }
    }

}
