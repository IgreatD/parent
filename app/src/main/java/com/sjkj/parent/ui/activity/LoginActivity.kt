package com.sjkj.parent.ui.activity

import android.content.Intent
import com.sjkj.parent.R
import com.sjkj.parent.ui.controller.login.LoginController
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : BaseActivity() {

    override fun getLayoutId(): Int = R.layout.activity_login

    override fun initView() {
        val view = LoginController(this)
        view.setLoginControlListener(object : LoginController.LoginControlListener {
            override fun startActivity() {
                startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                finish()
            }
        })
        login_fl.addView(view)
    }

}

