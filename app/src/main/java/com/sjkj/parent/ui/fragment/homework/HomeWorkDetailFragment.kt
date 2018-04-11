package com.sjkj.parent.ui.fragment.homework

import android.os.Bundle
import android.view.View
import android.widget.FrameLayout
import com.sjkj.parent.R
import com.sjkj.parent.common.Common
import com.sjkj.parent.ui.controller.homework.HomeWorkDetailController
import com.sjkj.parent.ui.fragment.BackBaseFragment

/**
 * @author by dingl on 2017/9/19.
 * @desc HomeWorkDetailFragment
 */
class HomeWorkDetailFragment : BackBaseFragment() {
    override fun initView(view: View?) {
        mTopBar = view?.findViewById(R.id.topbar)
        mFlContainer = view?.findViewById(R.id.fl_container)
        initTopBar()
    }

    override fun getLayoutId(): Int = R.layout.fragment_base
    fun newInstance(homeworkInfoID: Int, statusInfo: Int): BackBaseFragment {
        val fragment = HomeWorkDetailFragment()
        val bundle = Bundle()
        bundle.putInt(Common.HOMEWORKINFOID, homeworkInfoID)
        bundle.putInt(Common.STATUSINFO, statusInfo)
        fragment.arguments = bundle
        return fragment
    }

    private var mFlContainer: FrameLayout? = null

    override fun onEnterAnimationEnd(savedInstanceState: Bundle?) {
        super.onEnterAnimationEnd(savedInstanceState)
        val view = HomeWorkDetailController(context)
        view.getHomeWorkData(arguments.getInt(Common.HOMEWORKINFOID), arguments.getInt(Common.STATUSINFO))
        view.setHomeControlListener(object : HomeWorkDetailController.HomeControlListener {
            override fun startFragment(fragment: BackBaseFragment) {
                this@HomeWorkDetailFragment.start(fragment)
            }
        })
        mFlContainer?.addView(view)

    }

    private fun initTopBar() {

        mTopBar?.setTitle(context.getString(R.string.homework_detail))

    }
}
