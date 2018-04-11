package com.sjkj.parent.mvp.presenter

import android.content.Intent
import com.blankj.utilcode.util.SPUtils
import com.google.gson.Gson
import com.sjkj.parent.api.ParentUri
import com.sjkj.parent.common.Common
import com.sjkj.parent.data.server.LoginRequestBody
import com.sjkj.parent.data.server.UserBean
import com.sjkj.parent.mvp.contract.LoginContract
import com.sjkj.parent.rxhelper.RxSchedulersHelper
import com.sjkj.parent.rxhelper.RxSubscriber
import com.sjkj.parent.utils.UserUtils
import com.sjkj.parent.utils.create
import com.sjkj.parent.utils.getApi
import com.sjkj.parent.utils.getApp
import com.sjkj.parent.xmpp.service.XmppService

/**
 * @author by dingl on 2017/9/12.
 * @desc LoginPresenter
 */
class LoginPresenter constructor(private val mView: LoginContract.BaseView) : BasePresenter(), LoginContract.Presenter {

    override fun login(userName: String, password: String) {
        addSubscription(getApi().getLoginData(ParentUri.LOGIN_URL, create(LoginRequestBody(userName, password)))
                .compose(RxSchedulersHelper.io_main())
                .subscribe(object : RxSubscriber<UserBean>(mView) {

                    override fun _onNext(t: UserBean) {
                        SPUtils.getInstance().put(Common.USER_NAME, userName)
                        SPUtils.getInstance().put(Common.USER_PASSWORD, password)
                        t.CourseInfoList?.get(0)?.isSelect = true
                        t.Name = String.format("${t.Name}家长")
                        SPUtils.getInstance().put(Common.USER, Gson().toJson(t))
                        UserUtils.setUser(Gson().fromJson(SPUtils.getInstance().getString(Common.USER), UserBean::class.java))
                        getApp().startService(Intent(getApp(), XmppService::class.java))
                        mView.jump(t)
                    }

                    override fun _onEmptyNext(msg: String) {
                        super._onEmptyNext(msg)
                        mView.loginFailed()
                    }

                    override fun _onError(toast: String) {
                        super._onError(toast)
                        mView.loginFailed()
                    }

                }))
    }

}
