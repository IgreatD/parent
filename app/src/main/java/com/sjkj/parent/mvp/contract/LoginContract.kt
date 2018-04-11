package com.sjkj.parent.mvp.contract

import com.sjkj.parent.data.server.UserBean


/**
 * @author by dingl on 2017/9/12.
 * @desc LoginContract
 */
class LoginContract {

    interface BaseView : BaseContract.BaseView {
        fun jump(user: UserBean)
        fun loginFailed() {}
    }

    interface Presenter {
        fun login(userName: String, password: String)
    }

}
