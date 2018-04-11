package com.sjkj.parent.ui.activity

import android.os.Bundle
import com.qmuiteam.qmui.util.QMUIKeyboardHelper
import com.sjkj.parent.ParentApp
import com.sjkj.parent.data.server.MessageEvent
import com.sjkj.parent.mvp.contract.BaseContract
import com.sjkj.parent.ui.fragment.BackBaseFragment
import com.sjkj.parent.ui.fragment.BaseFragment
import com.sjkj.parent.utils.getApp
import me.yokeyword.fragmentation.SupportActivity
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import org.jetbrains.anko.contentView
import org.jetbrains.anko.toast

/**
 * @author by dingl on 2017/09/16.
 *
 * @desc 项目activity的基类
 */
abstract class BaseFragmentActivity : SupportActivity(), BaseContract.BaseView {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        EventBus.getDefault().register(this)
        getApp().addActivity(this)
    }
    
    override fun onDestroy() {
        super.onDestroy()
        if (QMUIKeyboardHelper.isKeyboardVisible(this)) {
            QMUIKeyboardHelper.hideKeyboard(contentView)
        }
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this)
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    open fun onEvent(event: MessageEvent<*>) {
        toBus(event)
    }

    open fun toBus(event: MessageEvent<*>?) {
        ParentApp.fragmentList.forEach {
            if (it is BaseFragment)
                it.toBus(event)
            else
                (it as? BackBaseFragment)?.toBus(event)
        }
    }

    override fun showToast(toast: String) {
        toast(toast)
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
}
