package com.sjkj.parent.ui.fragment.mine

import android.os.Bundle
import android.view.View
import android.widget.FrameLayout
import com.sjkj.parent.R
import com.sjkj.parent.ui.controller.mine.MineNoticeController
import com.sjkj.parent.ui.fragment.BackBaseFragment

/**
 * @author by dingl on 2017/9/16.
 * @desc MineNoticeFragmnet
 */
class MineNoticeFragmnet : BackBaseFragment() {
    
    override fun initView(view: View?) {
        mTopBar = view?.findViewById(R.id.topbar)
        mFlContainer = view?.findViewById(R.id.fl_container)
        initTopBar()
        initContainer()
    }

    override fun getLayoutId(): Int = R.layout.fragment_base

    private var mFlContainer: FrameLayout? = null

    private lateinit var mineNoticeController: MineNoticeController

    private fun initContainer() {

        mineNoticeController = MineNoticeController(context)

        mFlContainer?.addView(mineNoticeController)

        mineNoticeController.setHomeControlListener(object : MineNoticeController.HomeControlListener {
            override fun startFragment(fragment: BackBaseFragment) {
                start(fragment)
            }

        })

    }

    override fun onEnterAnimationEnd(savedInstanceState: Bundle?) {
        super.onEnterAnimationEnd(savedInstanceState)
        mineNoticeController.loadData()
    }

    private fun initTopBar() {
        mTopBar?.setTitle(getString(R.string.mine_notice))

    }

}

