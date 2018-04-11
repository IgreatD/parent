package com.sjkj.parent.ui.fragment.count

import android.os.Bundle
import android.view.View
import android.widget.FrameLayout
import com.sjkj.parent.R
import com.sjkj.parent.ui.controller.count.CountSubjectRightController
import com.sjkj.parent.ui.fragment.BackBaseFragment

/**
 * @author by dingl on 2017/10/19.
 * @desc CountSubjectHandFragment
 */
class CountSubjectRightFragment : BackBaseFragment() {
    override fun initView(view: View?) {
        mTopBar = view?.findViewById(R.id.topbar)
        mFlContainer = view?.findViewById(R.id.fl_container)
        initTopBar()
        initContainer()
    }

    override fun getLayoutId(): Int = R.layout.fragment_base

    private var mFlContainer: FrameLayout? = null
    
    private lateinit var mCountSubjectHandController: CountSubjectRightController

    private fun initContainer() {
        mCountSubjectHandController = CountSubjectRightController(context)
        mFlContainer?.addView(mCountSubjectHandController)

    }

    override fun onEnterAnimationEnd(savedInstanceState: Bundle?) {
        super.onEnterAnimationEnd(savedInstanceState)
        mCountSubjectHandController.loadData()
    }

    private fun initTopBar() {

        mTopBar?.setTitle(getString(R.string.count_subject_right))
    }
}
