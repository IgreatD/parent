package com.sjkj.parent.ui.controller.mine

import android.content.Context
import android.view.LayoutInflater
import android.view.inputmethod.EditorInfo
import com.sjkj.parent.R
import com.sjkj.parent.mvp.contract.mine.MineChangePdContract
import com.sjkj.parent.mvp.presenter.mine.MineChangePdPresenter
import com.sjkj.parent.ui.controller.base.BaseController
import com.sjkj.parent.utils.getUser
import kotlinx.android.synthetic.main.controller_mine_change_pd.view.*
import org.jetbrains.anko.sdk25.coroutines.onClick
import org.jetbrains.anko.toast

/**
 * @author by dingl on 2017/9/21.
 * @desc MineChangePDController
 */
class MineChangePDController(context: Context) : BaseController(context), MineChangePdContract.BaseView {

    private var mHomeWorkControlListener: HomeWorkControlListener? = null

    interface HomeWorkControlListener {
        fun popBack()
    }

    private fun popBack() {
        mHomeWorkControlListener?.popBack()
    }

    fun setHomeControlListener(homeWorkControlListener: HomeWorkControlListener) {
        mHomeWorkControlListener = homeWorkControlListener
    }

    override fun modifySuccess() {
        popBack()
    }

    override fun modifyFailed() {
    }


    init {
        LayoutInflater.from(context).inflate(R.layout.controller_mine_change_pd, this)
        initData()
    }

    private fun initData() {
        basePresenter = MineChangePdPresenter(this)

        mine_pd_new_2.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                changePd()
            }
            true
        }

        mine_confirm.onClick {

            changePd()

        }
        
    }

    private fun changePd() {
        if (mine_pd_old.text.isNullOrEmpty()) {
            context.toast(R.string.mine_pd_old)
            return
        }

        if (mine_pd_old.text.toString() != getUser().UserPassword) {
            context.toast(R.string.old_pd_error)
            mine_pd_old.setText("")
            return
        }

        if (mine_pd_new_1.text.isNullOrEmpty()) {
            context.toast(R.string.mine_pd_new_1)
            return
        }

        if (mine_pd_new_2.text.isNullOrEmpty()) {
            context.toast(R.string.mine_pd_new_2)
            return
        }

        if (mine_pd_new_1.text.toString() == mine_pd_new_2.text.toString()) {
            (basePresenter as MineChangePdPresenter).modifyPd(mine_pd_old.text.toString(), mine_pd_new_1.text.toString())
        } else {
            context.toast(R.string.twice_diff)
        }
    }

}
