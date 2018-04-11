package com.sjkj.parent.ui.fragment.mine

import android.os.Bundle
import android.view.View
import android.widget.FrameLayout
import com.sjkj.parent.R
import com.sjkj.parent.ui.controller.mine.MineChangePDController
import com.sjkj.parent.ui.fragment.BackBaseFragment

/**
 * @author by dingl on 2017/9/21.
 * @desc MineChangePDFragment
 */
class MineChangePDFragment : BackBaseFragment() {
    override fun initView(view: View?) {
        mTopBar = view?.findViewById(R.id.topbar)
        mFlContainer = view?.findViewById(R.id.fl_container)
        initTopBar()
    }

    override fun getLayoutId(): Int = R.layout.fragment_base

    private var mFlContainer: FrameLayout? = null

    override fun onEnterAnimationEnd(savedInstanceState: Bundle?) {
        super.onEnterAnimationEnd(savedInstanceState)
        val contanierView = MineChangePDController(context)
        contanierView.setHomeControlListener(object : MineChangePDController.HomeWorkControlListener {
            override fun popBack() {
                pop()
            }
        })
        mFlContainer?.addView(contanierView)

    }

    private fun initTopBar() {

        mTopBar?.setTitle(R.string.mine_change_pd)

    }
}
