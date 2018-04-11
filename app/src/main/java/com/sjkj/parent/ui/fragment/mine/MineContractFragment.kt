package com.sjkj.parent.ui.fragment.mine

import android.os.Bundle
import android.view.View
import android.widget.TextView
import com.qmuiteam.qmui.widget.grouplist.QMUICommonListItemView
import com.qmuiteam.qmui.widget.grouplist.QMUIGroupListView
import com.sjkj.parent.R
import com.sjkj.parent.ui.fragment.BackBaseFragment
import com.sjkj.parent.utils.call
import com.sjkj.parent.utils.getVersionCode
import com.sjkj.parent.utils.openBrower
import org.jetbrains.anko.support.v4.act

/**
 * @author by dingl on 2017/9/15.
 * @desc MineContractFragment
 */
class MineContractFragment : BackBaseFragment() {
    override fun initView(view: View?) {
        mTopBar = view?.findViewById(R.id.topbar)
        mVersionCode = view?.findViewById(R.id.mine_versonCode)
        mMine_group = view?.findViewById(R.id.mine_group)
        initData()
    }

    override fun getLayoutId(): Int = R.layout.fragment_mine_contract

    private var mVersionCode: TextView? = null
    private var mMine_group: QMUIGroupListView? = null
    override fun onEnterAnimationEnd(savedInstanceState: Bundle?) {
        super.onEnterAnimationEnd(savedInstanceState)
//        val itemUse = mMine_group?.createItemView(getString(R.string.mine_use))
//        itemUse?.accessoryType = QMUICommonListItemView.ACCESSORY_TYPE_CHEVRON
//        itemUse?.tag = 0

        val itemCustomer = mMine_group?.createItemView(getString(R.string.mine_customer))
        itemCustomer?.setDetailText(getString(R.string.mine_customer_tel))
        itemCustomer?.tag = 1


        val itemHttp = mMine_group?.createItemView(getString(R.string.mine_http))
        itemHttp?.setDetailText(getString(R.string.mine_http_d))
        itemHttp?.tag = 2

        val onClickListener = View.OnClickListener { v ->
            if (v is QMUICommonListItemView) {
                when (v.tag) {
//                    0 -> start(MinePersonalFragment())
                    1 -> act.call(getString(R.string.mine_customer_tel))
                    2 -> act.openBrower(getString(R.string.mine_http_d))
                }
            }
        }

        QMUIGroupListView.newSection(context)
//                .addItemView(itemUse, onClickListener)
                .addItemView(itemCustomer, onClickListener)
                .addItemView(itemHttp, onClickListener)
                .addTo(mMine_group)

    }

    private fun initData() {
        mTopBar?.setBackgroundDividerEnabled(false)
        mTopBar?.setTitle(getString(R.string.mine_contract))
        mVersionCode?.text = String.format("%s%s", getString(R.string.mine_version), getVersionCode())
    }
}
