package com.sjkj.parent.ui.controller.base

import android.content.Context
import android.widget.FrameLayout
import com.qmuiteam.qmui.widget.dialog.QMUITipDialog
import com.sjkj.parent.mvp.contract.BaseContract
import com.sjkj.parent.mvp.presenter.BasePresenter
import org.jetbrains.anko.toast

/**
 * @author by dingl on 2017/9/21.
 * @desc BaseController
 */
open class BaseController(context: Context) : FrameLayout(context), BaseContract.BaseView {

    private val loadingDialog: QMUITipDialog = QMUITipDialog.Builder(context).setIconType(QMUITipDialog.Builder.ICON_TYPE_LOADING).create()

    init {
        loadingDialog.setCancelable(true)
        loadingDialog.setOnDismissListener { 
            basePresenter?.unSubscription()
        }
    }

    var basePresenter: BasePresenter? = null

    override fun showToast(toast: String) {
        context.toast(toast)
    }

    override fun showLoading() {
        loadingDialog.show()
    }

    override fun hideLoading() {
        loadingDialog.dismiss()
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
