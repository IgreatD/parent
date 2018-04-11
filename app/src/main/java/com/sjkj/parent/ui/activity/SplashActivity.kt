package com.sjkj.parent.ui.activity

import android.Manifest
import android.content.Intent
import android.text.TextUtils
import com.blankj.utilcode.util.SPUtils
import com.sjkj.parent.R
import com.sjkj.parent.common.Common
import com.sjkj.parent.data.server.UserBean
import com.sjkj.parent.mvp.contract.LoginContract
import com.sjkj.parent.mvp.presenter.LoginPresenter
import org.jetbrains.anko.toast
import pub.devrel.easypermissions.AppSettingsDialog
import pub.devrel.easypermissions.EasyPermissions


/**
 * @author by dingl on 2017/9/13.
 * @desc SplashActivity
 */
class SplashActivity : BaseActivity(), LoginContract.BaseView, EasyPermissions.PermissionCallbacks {

    private val PERS_WEITR_CAMMER_RECORd = 0

    override fun showToast(toast: String) {
        toast(toast)
    }

    override fun showEmptyView() {

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

    override fun getLayoutId(): Int = R.layout.activity_splash

    lateinit var loginPresenter: LoginPresenter


    override fun initView() {
        requestPermissions()
    }

    fun requestPermissions() {
        if (EasyPermissions.hasPermissions(this, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO)) {
            afterPerm()
        } else {
            EasyPermissions.requestPermissions(this, getString(R.string.permission_re), PERS_WEITR_CAMMER_RECORd, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
    }

    override fun onPermissionsGranted(requestCode: Int, perms: List<String>) {
        if (perms.size != 3) {
            finish()
        } else
            afterPerm()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == AppSettingsDialog.DEFAULT_SETTINGS_REQ_CODE) {
            afterPerm()
        } else
            finish()
    }

    override fun onPermissionsDenied(requestCode: Int, perms: List<String>) {
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            AppSettingsDialog.Builder(this).build().show()
        }
    }

    private fun afterPerm() {
        loginPresenter = LoginPresenter(this)
        val userName = SPUtils.getInstance().getString(Common.USER_NAME)
        val passWord = SPUtils.getInstance().getString(Common.USER_PASSWORD)
        if (!TextUtils.isEmpty(userName) && !TextUtils.isEmpty(passWord))
            loginPresenter.login(userName, passWord)
        else
            loginFailed()
    }

    override fun loginFailed() {
        super.loginFailed()
        startActivity(Intent(this@SplashActivity, LoginActivity::class.java))
        finish()
    }

    override fun jump(user: UserBean) {
        startActivity(Intent(this@SplashActivity, MainActivity::class.java))
        finish()
    }


}
