package com.sjkj.parent.ui.fragment.count

import android.os.Bundle
import android.view.View
import android.widget.FrameLayout
import com.qmuiteam.qmui.widget.dialog.QMUIBottomSheet
import com.sjkj.parent.R
import com.sjkj.parent.data.server.CourseInfoListBean
import com.sjkj.parent.ui.controller.count.CountHwRightController
import com.sjkj.parent.ui.fragment.BackBaseFragment
import com.sjkj.parent.utils.getCourseInfoList
import org.jetbrains.anko.sdk25.coroutines.onClick

/**
 * @author by dingl on 2017/10/19.
 * @desc CountSubjectHandFragment
 */
class CountHwRightFragment : BackBaseFragment() {
    override fun initView(view: View?) {
        mTopBar = view?.findViewById(R.id.topbar)
        mFlContainer = view?.findViewById(R.id.fl_container)
        initTopBar()
        initContainer()
    }

    override fun getLayoutId(): Int = R.layout.fragment_base

    private var mFlContainer: FrameLayout? = null

    private var courseInfoList: List<CourseInfoListBean>? = null
    
    private lateinit var mCountSubjectHandController: CountHwRightController

    private fun initContainer() {
        mCountSubjectHandController = CountHwRightController(context)
        mFlContainer?.addView(mCountSubjectHandController)

    }

    override fun onEnterAnimationEnd(savedInstanceState: Bundle?) {
        super.onEnterAnimationEnd(savedInstanceState)
        mCountSubjectHandController.loadData(courseInfoList?.first()?.CourseInfoID)
    }

    private fun initTopBar() {

        mTopBar?.setTitle(getString(R.string.count_hw_right))

        courseInfoList = getCourseInfoList()

        mTopBar?.addRightTextButton(courseInfoList?.first()?.Name, R.id.topbar_right_about_button)?.onClick {
            showBottomSheetList()
        }

    }

    private fun showBottomSheetList() {

        val builder = QMUIBottomSheet.BottomListSheetBuilder(activity)
        courseInfoList?.forEach {
            builder.addItem(it.Name)
        }
        builder.setOnSheetItemClickListener { dialog, _, position, _ ->
            dialog.dismiss()
            mCountSubjectHandController.loadData(courseInfoList?.get(position)?.CourseInfoID)
        }.build().show()

    }
}
