package com.sjkj.parent.utils

import android.app.Activity
import android.content.Context
import android.view.MotionEvent
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.LinearLayout
import com.blankj.utilcode.util.SPUtils
import com.sjkj.parent.common.Common
import org.jetbrains.anko.sdk25.coroutines.onClick
import org.jetbrains.anko.sdk25.coroutines.onTouch

/**
 * @author by dingl on 2017/12/13.
 */

class KeyBoard private constructor() {

    private var mActivity: Activity? = null
    private var mInputManager: InputMethodManager? = null//软键盘管理类
    private var mEmotionLayout: View? = null//表情布局
    private var mEditText: EditText? = null//
    private var mContentView: View? = null//内容布局view,即除了表情布局或者软键盘布局以外的布局，用于固定bar的高度，防止跳闪

    private val isSoftInputShown: Boolean
        get() = supportSoftInputHeight != 0

    private val supportSoftInputHeight: Int
        get() {
            return getKeyBoardHeight(mActivity)
        }

    private val keyBoardHeight: Int
        get() = SPUtils.getInstance().getInt(Common.KEYBOARD_HEIGHT, 400)

    fun setmEmotionLayout(mEmotionLayout: View?) {
        this.mEmotionLayout = mEmotionLayout
    }

    fun bindToContent(contentView: View?): KeyBoard {
        mContentView = contentView
        return this
    }

    fun bindToEditText(editText: EditText?): KeyBoard {
        mEditText = editText
        mEditText?.requestFocus()
        mEditText?.onTouch { v, event ->
            if (event.action == MotionEvent.ACTION_UP && mEmotionLayout!!.isShown) {
                lockContentHeight()//显示软件盘时，锁定内容高度，防止跳闪。
                hideEmotionLayout(true)//隐藏表情布局，显示软件盘
                v.postDelayed({ unlockContentHeightDelayed() }, 200L)
            }
        }
        return this
    }

    fun bindToEmotionButton(emotionButton: View?, voice: View?): KeyBoard {
        emotionButton?.onClick {
            voice?.visibility = View.GONE
            mEditText?.visibility = View.VISIBLE
            if (mEmotionLayout?.isShown == true) {
                lockContentHeight()//显示软件盘时，锁定内容高度，防止跳闪。
                hideEmotionLayout(true)//隐藏表情布局，显示软件盘
                unlockContentHeightDelayed()//软件盘显示后，释放内容高度
            } else {
                if (isSoftInputShown) {//同上
                    lockContentHeight()
                    showEmotionLayout()
                    unlockContentHeightDelayed()
                } else {
                    showEmotionLayout()//两者都没显示，直接显示表情布局
                }
            }
        }
        return this
    }

    fun build(): KeyBoard {
        mActivity?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN or WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
        hideSoftInput()
        return this
    }

    private fun showEmotionLayout() {
        var softInputHeight = supportSoftInputHeight
        if (softInputHeight == 0) {
            softInputHeight = keyBoardHeight
        }
        hideSoftInput()
        mEmotionLayout?.layoutParams?.height = softInputHeight
        mEmotionLayout?.visibility = View.VISIBLE
    }

    private fun hideEmotionLayout(showSoftInput: Boolean) {
        if (mEmotionLayout?.isShown == true) {
            mEmotionLayout?.visibility = View.GONE
            if (showSoftInput) {
                showSoftInput()
            }
        }
    }

    private fun lockContentHeight() {
        val params = mContentView?.layoutParams as LinearLayout.LayoutParams
        params.height = mContentView?.height ?: 0
        params.weight = 0.0f
    }

    private fun unlockContentHeightDelayed() {
        mEditText?.postDelayed({ (mContentView?.layoutParams as LinearLayout.LayoutParams).weight = 1.0f }, 200L)
    }

    private fun showSoftInput() {
        mEditText?.requestFocus()
        mEditText?.post { mInputManager?.showSoftInput(mEditText, 0) }
    }

    private fun hideSoftInput() {
        mInputManager!!.hideSoftInputFromWindow(mEditText!!.windowToken, 0)
    }

    companion object {

        fun with(activity: Activity): KeyBoard {
            val emotionInputDetector = KeyBoard()
            emotionInputDetector.mActivity = activity
            emotionInputDetector.mInputManager = activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            return emotionInputDetector
        }
    }
}
