package com.sjkj.parent.ui.fragment.assign

import android.os.Bundle
import android.view.View
import android.widget.FrameLayout
import com.sjkj.parent.R
import com.sjkj.parent.common.Common
import com.sjkj.parent.ui.controller.assign.AssignDetailNormalController
import com.sjkj.parent.ui.fragment.BackBaseFragment
import com.sjkj.parent.ui.fragment.main.MainFragment
import org.jetbrains.anko.sdk25.coroutines.onClick

/**
 * @author by dingl on 2017/9/26.
 * @desc AssignDetailWrongFragment
 */
class AssignDetailNormalFragment : BackBaseFragment() {

    override fun initView(view: View?) {
        mTopBar = view?.findViewById(R.id.topbar)
        mFlContainer = view?.findViewById(R.id.fl_container)
        initTopBar()
    }

    override fun getLayoutId(): Int = R.layout.fragment_base

    fun newInstance(libraryIds: String, courseInfoID: Int): AssignDetailNormalFragment {
        val fragment = AssignDetailNormalFragment()
        val bundle = Bundle()
        bundle.putString(Common.KONWIDS, libraryIds)
        bundle.putInt(Common.COURSEINFOID, courseInfoID)
        fragment.arguments = bundle
        return fragment
    }

    private var mFlContainer: FrameLayout? = null

    override fun onEnterAnimationEnd(savedInstanceState: Bundle?) {
        super.onEnterAnimationEnd(savedInstanceState)
        
        val view = AssignDetailNormalController(context)

        view.setHomeControlListener(object : AssignDetailNormalController.HomeControlListener {
            override fun pop() {
                this@AssignDetailNormalFragment.pop()
            }

            override fun startFragment(fragment: BackBaseFragment) {
                start(fragment)
            }
        })

        mFlContainer?.addView(view)

        view.loadData(arguments.getString(Common.KONWIDS), arguments.getInt(Common.COURSEINFOID))

        mTopBar?.addRightTextButton(R.string.assign_hw, R.id.topbar_right_about_button)?.onClick {
            view.assignHw()
        }
    }

    private fun initTopBar() {

        mTopBar?.setTitle(getString(R.string.assign_choice))

        mTopBar?.addLeftBackImageButton()?.onClick { popTo(MainFragment::class.java, true) }

    }

}
