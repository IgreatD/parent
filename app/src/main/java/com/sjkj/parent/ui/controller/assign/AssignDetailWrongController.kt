package com.sjkj.parent.ui.controller.assign

import android.content.Context
import android.support.v7.widget.AppCompatEditText
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import com.google.gson.Gson
import com.qmuiteam.qmui.widget.dialog.QMUITipDialog
import com.sjkj.parent.R
import com.sjkj.parent.data.server.AssignBean
import com.sjkj.parent.data.server.QuestionBean
import com.sjkj.parent.mvp.contract.assign.AssignContract
import com.sjkj.parent.mvp.presenter.assign.AssignDetailPresenter
import com.sjkj.parent.mvp.presenter.assign.AssignHwPresenter
import com.sjkj.parent.ui.adapter.classroom.ClassRoomDetailAdapter
import com.sjkj.parent.ui.adapter.classroom.ClassRoomDetailViewHolder
import com.sjkj.parent.ui.controller.base.BaseRecycleVController
import com.sjkj.parent.ui.fragment.BackBaseFragment
import com.sjkj.parent.ui.fragment.ImageFragment
import com.sjkj.parent.ui.view.SwitchMultiButton
import com.sjkj.parent.utils.CustomDialogBuilder
import com.sjkj.parent.utils.DateDialogUtils
import com.sjkj.parent.utils.getDayToday
import com.sjkj.parent.utils.getMonthToday
import org.jetbrains.anko.sdk25.coroutines.onClick
import org.jetbrains.anko.support.v4.onRefresh

/**
 * @author by dingl on 2017/9/26.
 * @desc AssignDetailWrongController
 */
class AssignDetailWrongController(context: Context) : BaseRecycleVController<QuestionBean, ClassRoomDetailViewHolder>(context), AssignContract.DetailBaseView, AssignContract.AssignHwView {
    override fun assignError() {
        showToast("作业布置失败，请重试！")
    }

    override fun assignSuccess() {
        mHomeControlListener?.pop()
    }

    private var assignDetailPresenter: AssignDetailPresenter? = null
    
    private var assignHwPresenter: AssignHwPresenter? = null

    private var mKnowIDS: String = ""

    private var mCourseInfoID: Int = 0

    private var startDate: String? = null
    
    private var endDate: String? = null

    override fun onLoadMoreRequested() {
        assignDetailPresenter?.getWrongMoreData(mKnowIDS, mCourseInfoID, startDate, endDate)
    }

    override fun initData() {
        assignDetailPresenter = AssignDetailPresenter(this)
        assignHwPresenter = AssignHwPresenter(this)
        baseAdapter = ClassRoomDetailAdapter {
            startFragment(ImageFragment().newInstance(it))
        }
        (baseAdapter as ClassRoomDetailAdapter).setShowAnswerAndStatus(true)
        (baseAdapter as ClassRoomDetailAdapter).setShowAddButton(true)
    }

    override fun onClickLoadData() {
        assignDetailPresenter?.getWrongData(mKnowIDS, mCourseInfoID, startDate, endDate)
    }

    override fun initListener() {
        base_srl?.onRefresh {
            assignDetailPresenter?.getWrongData(mKnowIDS, mCourseInfoID, startDate, endDate)
        }
    }

    fun loadData(knowIDS: String?, courseInfoID: Int, startDate: String?, endDate: String?) {
        mKnowIDS = knowIDS ?: ""
        mCourseInfoID = courseInfoID
        this.startDate = startDate
        this.endDate = endDate
        assignDetailPresenter?.getWrongData(mKnowIDS, mCourseInfoID, startDate, endDate)
    }

    private var mHomeControlListener: HomeControlListener? = null

    interface HomeControlListener {
        fun startFragment(fragment: BackBaseFragment)
        fun pop()
    }

    fun startFragment(fragment: BackBaseFragment) {
        if (mHomeControlListener != null) {
            mHomeControlListener?.startFragment(fragment)
        }
    }

    fun setHomeControlListener(homeControlListener: HomeControlListener) {
        mHomeControlListener = homeControlListener
    }

    fun assignHw() {
        val assignList = ArrayList<AssignBean>()
        baseAdapter?.data?.forEach {
            if (it.isCheck)
                assignList.add(AssignBean(it.QuestionInfoID, it.Score))
        }
        if (assignList.size == 0) {
            val dialog = QMUITipDialog.Builder(context)
                    .setIconType(QMUITipDialog.Builder.ICON_TYPE_INFO)
                    .setTipWord(context.getString(R.string.assign_hw_check))
                    .create()
            dialog.show()
            postDelayed({ dialog.dismiss() }, 1000)
            return
        } else {
            val builder = CustomDialogBuilder(context).setLayout(R.layout.view_assign_hw)
            builder.setTitle("共${assignList.size}题")
            val assignDialog = builder.create()
            val view = builder.view
            val tv_name = view.findViewById<AppCompatEditText>(R.id.tv_name)
            val hw_switch_button_check = view.findViewById<SwitchMultiButton>(R.id.hw_switch_button_check)
            val hw_switch_button_revise = view.findViewById<SwitchMultiButton>(R.id.hw_switch_button_revise)
            val view_line = view.findViewById<View>(R.id.view_line)
            val tv_start = view.findViewById<TextView>(R.id.tv_start)
            val tv_end = view.findViewById<TextView>(R.id.tv_end)
            val ll_revise = view.findViewById<LinearLayout>(R.id.ll_revise)
            val tv_cancle = view.findViewById<TextView>(R.id.tv_cancle)
            val tv_confirm = view.findViewById<TextView>(R.id.tv_confirm)
            hw_switch_button_check.setOnSwitchListener(object : SwitchMultiButton.OnSwitchListener {
                override fun onSwitch(position: Int, tabText: String) {
                    if (position == 0) {
                        ll_revise.visibility = View.VISIBLE
                        view_line.visibility = View.VISIBLE
                    } else {
                        ll_revise.visibility = View.GONE
                        view_line.visibility = View.GONE
                    }
                }
            })
            tv_name.setText(String.format("%s%s", getMonthToday(), context.getString(R.string.hw_parent)))
            tv_name.setSelection(tv_name.text.toString().length)
            tv_start.text = getDayToday()
            tv_end.text = getDayToday()
            DateDialogUtils.getInstance().initData(context, tv_start, tv_end)
            tv_cancle.onClick { assignDialog.dismiss() }
            tv_confirm.onClick {
                val Name = tv_name.text.toString()
                if (Name.isEmpty()) {
                    showToast("请先设置要布置的作业名称")
                    return@onClick
                }
                assignDialog.dismiss()
                assignHwPresenter?.assignHw(mCourseInfoID, Name,
                        hw_switch_button_check.getSelectedTab() + 1, hw_switch_button_revise.getSelectedTab() + 1,
                        tv_start.text.toString(), tv_end.text.toString(), Gson().toJson(assignList))
            }
            assignDialog.show()
        }
    }
}
