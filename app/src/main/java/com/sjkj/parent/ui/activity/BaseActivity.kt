package com.sjkj.parent.ui.activity

import android.os.Bundle
import com.qmuiteam.qmui.util.QMUIKeyboardHelper
import com.sjkj.parent.data.server.MessageEvent
import me.yokeyword.fragmentation_swipeback.SwipeBackActivity
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import org.jetbrains.anko.contentView


/**
 * @author by dingl on 2017/9/12.
 * @desc BaseActivity
 */
abstract class BaseActivity : SwipeBackActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setSwipeBackEnable(false)
        setContentView(getLayoutId())
        EventBus.getDefault().register(this)
        initView()
    }

    open fun postNotifyDataChanged() {

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    open fun onMessageEvent(event: MessageEvent<*>) {
        toBus(event)
    }

    open fun toBus(event: MessageEvent<*>?) {

    }

    abstract fun initView()

    abstract fun getLayoutId(): Int

    override fun onDestroy() {
        super.onDestroy()
        if (QMUIKeyboardHelper.isKeyboardVisible(this)) {
            QMUIKeyboardHelper.hideKeyboard(contentView)
        }
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this)
        }
    }

}
