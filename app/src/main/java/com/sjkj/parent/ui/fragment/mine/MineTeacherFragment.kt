package com.sjkj.parent.ui.fragment.mine

import android.os.Bundle
import android.view.View
import android.widget.FrameLayout
import com.sjkj.parent.R
import com.sjkj.parent.ui.controller.mine.MineTeacherController
import com.sjkj.parent.ui.fragment.BackBaseFragment

/**
 * @author by dingl on 2017/9/15.
 * @desc MineTeacherFragment
 */
class MineTeacherFragment : BackBaseFragment() {
    override fun initView(view: View?) {
        mTopBar = view?.findViewById(R.id.topbar)
        mFlContainer = view?.findViewById(R.id.fl_container)
        initTopBar()
        initContainer()
    }

    override fun getLayoutId(): Int = R.layout.fragment_base

    private var mFlContainer: FrameLayout? = null

    private lateinit var mineTeacherController: MineTeacherController

    private fun initContainer() {
        mineTeacherController = MineTeacherController(context)
        mFlContainer?.addView(mineTeacherController)

    }

    override fun onEnterAnimationEnd(savedInstanceState: Bundle?) {
        super.onEnterAnimationEnd(savedInstanceState)
        mineTeacherController.loadData(1)

    }

    private fun initTopBar() {

        mTopBar?.setTitle(getString(R.string.mine_teacher))
    }
}
