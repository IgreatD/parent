package com.sjkj.parent.ui.fragment.assign

import android.os.Bundle
import android.view.View
import android.widget.FrameLayout
import com.sjkj.parent.R
import com.sjkj.parent.common.Common
import com.sjkj.parent.ui.controller.assign.AssignDetailWrongController
import com.sjkj.parent.ui.fragment.BackBaseFragment
import org.jetbrains.anko.sdk25.coroutines.onClick

/**
 * @author by dingl on 2017/9/26.
 * @desc AssignDetailWrongFragment
 */
class AssignDetailWrongFragment : BackBaseFragment() {

    override fun initView(view: View?) {
        mTopBar = view?.findViewById(R.id.topbar)
        mFlContainer = view?.findViewById(R.id.fl_container)
        initTopBar()
    }

    override fun getLayoutId(): Int = R.layout.fragment_base

    fun newInstance(knowIDs: String, courseInfoID: Int, startDate: String?, endDate: String?): AssignDetailWrongFragment {
        val fragment = AssignDetailWrongFragment()
        val bundle = Bundle()
        bundle.putString(Common.KONWIDS, knowIDs)
        bundle.putInt(Common.COURSEINFOID, courseInfoID)
        bundle.putString(Common.START_DATE, startDate)
        bundle.putString(Common.END_DATE, endDate)
        fragment.arguments = bundle
        return fragment
    }

    private var mFlContainer: FrameLayout? = null

    override fun onEnterAnimationEnd(savedInstanceState: Bundle?) {
        super.onEnterAnimationEnd(savedInstanceState)
        val view = AssignDetailWrongController(context)

        view.setHomeControlListener(object : AssignDetailWrongController.HomeControlListener {
            override fun pop() {
                this@AssignDetailWrongFragment.pop()
            }

            override fun startFragment(fragment: BackBaseFragment) {
                start(fragment)
            }
        })

        mFlContainer?.addView(view)

        view.loadData(arguments.getString(Common.KONWIDS), arguments.getInt(Common.COURSEINFOID),
                arguments.getString(Common.START_DATE), arguments.getString(Common.END_DATE))

        mTopBar?.addRightTextButton(R.string.assign_hw, R.id.topbar_right_about_button)?.onClick {
            view.assignHw()
        }
    }

    private fun initTopBar() {

        mTopBar?.setTitle(getString(R.string.assign_choice))

    }

}
