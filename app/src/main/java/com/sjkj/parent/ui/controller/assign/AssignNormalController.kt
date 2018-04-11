package com.sjkj.parent.ui.controller.assign

import android.content.Context
import android.support.v7.widget.DividerItemDecoration
import com.sjkj.parent.R
import com.sjkj.parent.data.server.Library
import com.sjkj.parent.mvp.contract.assign.AssignContract
import com.sjkj.parent.mvp.presenter.assign.AssignPresenter
import com.sjkj.parent.ui.controller.base.BaseRecycleController
import com.sjkj.parent.ui.fragment.BackBaseFragment
import com.sjkj.parent.ui.fragment.assign.AssignDetailNormalFragment
import com.sjkj.parent.ui.widget.tree.Node
import com.sjkj.parent.ui.widget.tree.SimpleTreeAdapter
import com.sjkj.parent.ui.widget.tree.TreeHelper
import kotlinx.android.synthetic.main.controller_recycle_base.view.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import java.util.*

/**
 * @author by dingl on 2017/9/21.
 * @desc AssignWrongController
 */
class AssignNormalController(context: Context) : BaseRecycleController<Library>(context), AssignContract.BaseView {

    private var assignWrongPresenter: AssignPresenter? = null

    private var mNodes: List<Node> = ArrayList()

    private var mAllNodes: List<Node> = ArrayList()

    private var courseInfoId: Int = 0

    fun setCourseInfoID(courseInfoID: Int) {
        this.courseInfoId = courseInfoID
        if (baseList.size == 0)
            assignWrongPresenter?.getNormalData(courseInfoID)
    }

    override fun initListener() {
        nodeAdapter?.setOnItemChildClickListener { _, view, position ->
            when (view.id) {
                R.id.action_ex -> {
                    expandOrCollapse(position)
                }
                R.id.id_click_icon -> {
                    val n = mNodes[position]

                    if (n.click == 1)
                        setListClick(n, -1)
                    else
                        setListClick(n, 1)

                    nodeAdapter?.setNewData(mNodes)

                }
            }
        }
    }

    private var nodeAdapter: SimpleTreeAdapter? = null

    override fun initData() {
        assignWrongPresenter = AssignPresenter(this)
        base_srl.isEnabled = false
        nodeAdapter = SimpleTreeAdapter(mNodes)
        base_rv?.adapter = nodeAdapter
        base_rv?.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
    }

    override fun onClickLoadData() {
        assignWrongPresenter?.getNormalData(courseInfoId)
    }

    override fun showEmptyView() {

    }

    override fun setNewData(t: List<Library>) {

        showAll()
        doAsync {
            t.map {
                with(it) {
                    click = -1
                    QuestionSum = 0
                }
            }
            mAllNodes = TreeHelper.getSortedNodes(t, 1, true)
            mNodes = TreeHelper.filterVisibleNode(mAllNodes)
            uiThread {
                hideAll()
                nodeAdapter?.setNewData(mNodes)
            }
        }

        baseList = t as ArrayList<Library>
    }

    private fun setListClick(node: Node, click: Int) {
        node.click = click
        val childrenList = node.children
        if (childrenList.size > 0) {
            for (i in childrenList.indices) {
                setListClick(childrenList[i], click)
            }
        }
    }

    private fun expandOrCollapse(position: Int) {

        val n = mNodes[position]

        if (!n.isLeaf) {

            n.isExpand = !n.isExpand

            mNodes = TreeHelper.filterVisibleNode(mAllNodes)

            nodeAdapter?.setNewData(mNodes)
        }

    }

    private var mHomeWorkControlListener: HomeWorkControlListener? = null

    interface HomeWorkControlListener {
        fun startFragment(fragment: BackBaseFragment)
    }

    fun startFragment(fragment: BackBaseFragment) {
        mHomeWorkControlListener?.startFragment(fragment)
    }

    fun setHomeControlListener(homeWorkControlListener: HomeWorkControlListener) {
        mHomeWorkControlListener = homeWorkControlListener
    }

    fun preview() {
        val fragment = AssignDetailNormalFragment().newInstance(getLibraryIds(), courseInfoId)
        startFragment(fragment)
    }

    private fun getLibraryIds(): String {

        val nodeList = mAllNodes

        var WordKPointIDs = ""

        nodeList.indices
                .asSequence()
                .filter { nodeList[it].click == 1 }
                .forEach { WordKPointIDs = WordKPointIDs + "," + nodeList[it].id }

        WordKPointIDs = if (WordKPointIDs.length > 1) {
            WordKPointIDs.substring(1)
        } else {
            ""
        }
        return WordKPointIDs
    }

}
