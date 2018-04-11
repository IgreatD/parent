package com.sjkj.parent.ui.fragment.mine

import android.graphics.Bitmap
import android.view.View
import android.widget.TextView
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition
import com.qmuiteam.qmui.widget.QMUIRadiusImageView
import com.qmuiteam.qmui.widget.QMUITopBar
import com.qmuiteam.qmui.widget.grouplist.QMUICommonListItemView
import com.qmuiteam.qmui.widget.grouplist.QMUIGroupListView
import com.sjkj.parent.R
import com.sjkj.parent.common.GlideApp
import com.sjkj.parent.common.Which
import com.sjkj.parent.data.server.MessageEvent
import com.sjkj.parent.ui.fragment.BaseFragment
import com.sjkj.parent.ui.fragment.main.MainFragment
import com.sjkj.parent.utils.getName
import com.sjkj.parent.utils.getUser
import org.jetbrains.anko.imageBitmap

/**
 * @author by dingl on 2017/9/15.
 * @desc MineFragment
 */
class MineFragment : BaseFragment() {
    
    override fun initView(view: View?) {
        mTopBar = view?.findViewById(R.id.topbar)
        mMine_name = view?.findViewById(R.id.mine_name)
        mMine_group = view?.findViewById(R.id.mine_group)
        mMine_head = view?.findViewById(R.id.mine_head)

        initData()
        initGroup()
        modifyHead()
    }

    override fun getLayoutId(): Int = R.layout.fragment_mine

    private var mTopBar: QMUITopBar? = null
    private var mMine_name: TextView? = null
    private var mMine_group: QMUIGroupListView? = null
    private var mMine_head: QMUIRadiusImageView? = null

    private fun initGroup() {

        val itemNotice = mMine_group?.createItemView(getString(R.string.mine_notice))
        itemNotice?.accessoryType = QMUICommonListItemView.ACCESSORY_TYPE_CHEVRON

        val itemMine = mMine_group?.createItemView(getString(R.string._mine))
        itemMine?.accessoryType = QMUICommonListItemView.ACCESSORY_TYPE_CHEVRON

//        val itemTeacher = mMine_group?.createItemView(getString(R.string.mine_teacher))
//        itemTeacher?.accessoryType = QMUICommonListItemView.ACCESSORY_TYPE_CHEVRON
        
        val itemContract = mMine_group?.createItemView(getString(R.string.mine_contract))
        itemContract?.accessoryType = QMUICommonListItemView.ACCESSORY_TYPE_CHEVRON

        val fragment = parentFragment as MainFragment
        
        QMUIGroupListView.newSection(context)
                .addItemView(itemNotice, {fragment.startBrotherFragment(MineNoticeFragmnet())})
                .addItemView(itemMine, {fragment.startBrotherFragment(MinePersonalFragment())})
//                .addItemView(itemTeacher, onClickListener)
                .addItemView(itemContract, { fragment.startBrotherFragment(MineContractFragment())})
                .addTo(mMine_group)
        
    }
 
    private fun initData() {
        mTopBar?.setBackgroundDividerEnabled(false)
        mTopBar?.setTitle(getString(R.string.mine))

        mMine_name?.text = getName()

    }

    override fun toBus(event: MessageEvent<*>?) {
        super.toBus(event)
        when (event?.which) {
            Which.MODIFY_NAME -> mMine_name?.text = getName()
            Which.MODIFY_HEAD -> modifyHead()
        }
    }

    private fun modifyHead() {
        GlideApp.with(context).asBitmap().load(getUser()._getHeadImg()).into(object : SimpleTarget<Bitmap>() {
            override fun onResourceReady(resource: Bitmap?, transition: Transition<in Bitmap>?) {
                mMine_head?.imageBitmap = resource
            }

        })
    }
}
