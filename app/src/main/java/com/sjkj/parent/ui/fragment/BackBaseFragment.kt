package com.sjkj.parent.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.qmuiteam.qmui.util.QMUIKeyboardHelper
import com.qmuiteam.qmui.widget.QMUITopBar
import com.sjkj.parent.data.server.MessageEvent
import com.sjkj.parent.mvp.contract.BaseContract
import com.sjkj.parent.utils.getApp
import me.yokeyword.fragmentation.anim.DefaultHorizontalAnimator
import me.yokeyword.fragmentation.anim.FragmentAnimator
import me.yokeyword.fragmentation_swipeback.SwipeBackFragment
import org.jetbrains.anko.sdk25.coroutines.onClick
import org.jetbrains.anko.toast

/**
 * @author by dingl on 2017/10/27.
 * @desc BackBaseFragment
 */
abstract class BackBaseFragment : SwipeBackFragment(), BaseContract.BaseView {

    protected var mTopBar: QMUITopBar? = null

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater?.inflate(getLayoutId(), container, false)
        initView(view)
        initTopBarBack()
        getApp().addFragment(this)
        return attachToSwipeBack(view)
    }

    open fun initTopBarBack() {
        mTopBar?.addLeftBackImageButton()?.onClick { pop() }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setParallaxOffset(0.5f)
    }

    override fun onCreateFragmentAnimator(): FragmentAnimator {
        return DefaultHorizontalAnimator()
    }

    abstract fun initView(view: View?)

    abstract fun getLayoutId(): Int

    override fun onDestroyView() {
        getApp().removeFragment(this)
        if (QMUIKeyboardHelper.isKeyboardVisible(_mActivity)) {
            QMUIKeyboardHelper.hideKeyboard(view)
        }
        super.onDestroyView()
    }

    override fun showToast(toast: String) {
        context.toast(toast)
    }

    override fun showLoading() {

    }

    override fun hideLoading() {

    }

    override fun showLoadError() {

    }

    override fun showLoadNetWorkError() {

    }

    override fun hideAll() {

    }

    override fun showEmptyView() {

    }

    open fun toBus(event: MessageEvent<*>?) {

    }

}
