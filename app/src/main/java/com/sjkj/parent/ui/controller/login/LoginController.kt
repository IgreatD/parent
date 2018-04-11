package com.sjkj.parent.ui.controller.login

import android.content.Context
import android.graphics.Rect
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.inputmethod.EditorInfo
import com.qmuiteam.qmui.util.QMUIActivityLifecycleCallbacks
import com.sjkj.parent.R
import com.sjkj.parent.data.server.UserBean
import com.sjkj.parent.mvp.contract.LoginContract
import com.sjkj.parent.mvp.presenter.LoginPresenter
import com.sjkj.parent.ui.activity.LoginActivity
import com.sjkj.parent.ui.controller.base.BaseController
import com.sjkj.parent.utils.getApp
import kotlinx.android.synthetic.main.controller_login.view.*
import org.jetbrains.anko.sdk25.coroutines.onClick


/**
 * @author by dingl on 2017/9/21.
 * @desc LoginController
 */
class LoginController(context: Context) : BaseController(context), LoginContract.BaseView {

    override fun loginFailed() {

    }

    override fun jump(user: UserBean) {
        mLoginControlListener?.startActivity()
    }

    init {
        LayoutInflater.from(context).inflate(R.layout.controller_login, this)
        initData()
        addlayoutListener()
    }

    private fun addlayoutListener() {
        val listener = {
            val rect = Rect()
            getWindowVisibleDisplayFrame(rect)
            val mainInvisibleHeight = rootView.height - rect.bottom
            if (mainInvisibleHeight > 100) {
                val location = IntArray(2)
                action_login.getLocationInWindow(location)
                val srollHeight = location[1] + action_login.height - rect.bottom + 20
                scrollTo(0, srollHeight)
            } else {
                scrollTo(0, 0)
            }
        }
        viewTreeObserver.addOnGlobalLayoutListener(listener)
        getApp().registerActivityLifecycleCallbacks(object : QMUIActivityLifecycleCallbacks(context as LoginActivity) {
            override fun onTargetActivityDestroyed() {
                viewTreeObserver.removeOnGlobalLayoutListener(listener)
            }
        })
    }

    private fun initData() {

        basePresenter = LoginPresenter(this)

        tv_password.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                login()
            }
            true
        }

        action_login.onClick {
            login()
        }

    }

    private fun login() {
        if (TextUtils.isEmpty(tv_userName?.text)) {
            showToast("用户名不能为空")
            return
        }
        if (TextUtils.isEmpty(tv_password?.text)) {
            showToast("密码不能为空")
            return
        }
        (basePresenter as LoginPresenter).login(tv_userName?.text.toString(), tv_password?.text.toString())
    }

    private var mLoginControlListener: LoginControlListener? = null

    interface LoginControlListener {
        fun startActivity()
    }

    fun setLoginControlListener(loginControlListener: LoginControlListener) {
        mLoginControlListener = loginControlListener
    }
}
