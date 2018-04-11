package com.sjkj.parent.ui.controller.homework

import android.content.Context
import com.sjkj.parent.data.server.QuestionBean
import com.sjkj.parent.mvp.contract.homework.HomeWorkDetailContract
import com.sjkj.parent.mvp.presenter.homework.HomeWorkDetailPresenter
import com.sjkj.parent.ui.adapter.classroom.ClassRoomDetailAdapter
import com.sjkj.parent.ui.adapter.classroom.ClassRoomDetailViewHolder
import com.sjkj.parent.ui.controller.base.BaseRecycleVController
import com.sjkj.parent.ui.fragment.BackBaseFragment
import com.sjkj.parent.ui.fragment.ImageFragment
import org.jetbrains.anko.support.v4.onRefresh

/**
 * @author by dingl on 2017/9/19.
 * @desc HomeWorkDetailController
 */
class HomeWorkDetailController(context: Context)
    : BaseRecycleVController<QuestionBean, ClassRoomDetailViewHolder>(context), HomeWorkDetailContract.BaseView {

    private var mHomeWorkInfoID: Int = 0

    private var mStatusInfo: Int = 0

    private var homeWorkDetailPresenter: HomeWorkDetailPresenter? = null

    override fun initListener() {
        base_srl?.onRefresh {
            homeWorkDetailPresenter?.getHomeWorkDetailData(mHomeWorkInfoID)
        }
    }

    override fun initData() {
        homeWorkDetailPresenter = HomeWorkDetailPresenter(this)
        baseAdapter = ClassRoomDetailAdapter({ path ->
            startFragment(ImageFragment().newInstance(path))
        })
    }

    override fun onClickLoadData() {
        homeWorkDetailPresenter?.getHomeWorkDetailData(mHomeWorkInfoID)
    }

    fun getHomeWorkData(homeWorkInfoID: Int, statusInfo: Int) {
        if (baseList.size == 0)
            homeWorkDetailPresenter?.getHomeWorkDetailData(homeWorkInfoID)
        mHomeWorkInfoID = homeWorkInfoID
        mStatusInfo = statusInfo
    }

    private var mHomeControlListener: HomeControlListener? = null

    interface HomeControlListener {
        fun startFragment(fragment: BackBaseFragment)
    }

    fun startFragment(fragment: BackBaseFragment) {
        if (mHomeControlListener != null) {
            mHomeControlListener?.startFragment(fragment)
        }
    }

    fun setHomeControlListener(homeControlListener: HomeControlListener) {
        mHomeControlListener = homeControlListener
    }
}
