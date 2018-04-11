package com.sjkj.parent.ui.widget.tree

import java.util.*

/**
 * http://blog.csdn.net/lmj623565791/article/details/40212367

 * @author zhy
 */
class Node(var id: Int,

           private var pId: Int, name: String, var click: Int, var num: Int) {

    var name: String? = name

    /**
     * 当前的级别
     */
    /**
     * 获取level
     */
    var level: Int = 0
        get() = if (parent == null) 0 else parent!!.level + 1

    /**
     * 是否展开
     */
    /**
     * 设置展开

     * @param isExpand
     */
    var isExpand = false
        set(isExpand) {
            field = isExpand
            if (!isExpand) {

                for (node in children) {
                    node.isExpand = isExpand
                }
            }
        }

    var icon: Int = 0

    /**
     * 下一级的子Node
     */
    var children: ArrayList<Node> = ArrayList()

    /**
     * 父Node
     */
    var parent: Node? = null

    fun getpId(): Int {
        return pId
    }

    fun setpId(pId: Int) {
        this.pId = pId
    }

    /**
     * 是否为跟节点

     * @return
     */
    val isRoot: Boolean
        get() = parent == null

    /**
     * 判断父节点是否展开

     * @return
     */
    val isParentExpand: Boolean
        get() {
            if (parent == null)
                return false
            return parent!!.isExpand
        }

    /**
     * 是否是叶子界点

     * @return
     */
    val isLeaf: Boolean
        get() = children.size == 0
}
