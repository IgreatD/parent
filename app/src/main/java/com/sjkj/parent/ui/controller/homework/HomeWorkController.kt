package com.sjkj.parent.ui.controller.homework

import android.content.Context
import android.support.v7.widget.RecyclerView
import com.sjkj.parent.data.server.HomeWorkBean
import com.sjkj.parent.mvp.contract.homework.HomeWorkContract
import com.sjkj.parent.mvp.presenter.homework.HomeWorkPresenter
import com.sjkj.parent.ui.adapter.HomeWorkAdapter
import com.sjkj.parent.ui.controller.base.BaseRecycleController
import com.sjkj.parent.ui.fragment.BackBaseFragment
import com.sjkj.parent.ui.fragment.homework.HomeWorkDetailFragment
import kotlinx.android.synthetic.main.controller_recycle_base.view.*
import org.jetbrains.anko.support.v4.onRefresh

/**
 * @author by dingl on 2017/9/19.
 * @desc HomeWorkController
 */
class HomeWorkController(context: Context) : BaseRecycleController<HomeWorkBean>(context), HomeWorkContract.BaseView {

    private lateinit var homeWorkPresenter: HomeWorkPresenter

    private var mCorseInfoID: Int = 0

    private var mHomeWorkType: Int = 0
    private var mCategory: Int = 0

    override fun initListener() {
        base_srl.onRefresh {
            homeWorkPresenter.getHomeWorkList(mCorseInfoID, mHomeWorkType, mCategory)
        }

        baseAdapter?.setOnLoadMoreListener({
            homeWorkPresenter.getMoreHomeWorkList(mCorseInfoID, mHomeWorkType, mCategory)
        }, base_rv)

        baseAdapter?.setOnItemChildClickListener({ _, _, position ->
            val fragment = HomeWorkDetailFragment().newInstance(baseList[position].HomeworkInfoID, baseList[position].StatusInfo)
            startFragment(fragment)
        })

        base_rv?.addOnScrollListener(object : RecyclerView.OnScrollListener() {

            override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (Math.abs(dy) > 10) {
                    mHomeWorkControlListener?.toggle()
                }
            }
        })
    }

    override fun initData() {
        baseAdapter = HomeWorkAdapter(baseList)
        homeWorkPresenter = HomeWorkPresenter(this)
        base_rv?.adapter = baseAdapter
    }

    override fun onClickLoadData() {
        homeWorkPresenter.getHomeWorkList(mCorseInfoID, mHomeWorkType, mCategory)
    }

    fun setCourseInfoID(courseInfoID: Int, homeWorkType: Int, category: Int) {
        baseList.clear()
        mCorseInfoID = courseInfoID
        mHomeWorkType = homeWorkType
        mCategory = category
        homeWorkPresenter.getHomeWorkList(mCorseInfoID, mHomeWorkType, mCategory)
    }

    private var mHomeWorkControlListener: HomeWorkControlListener? = null

    interface HomeWorkControlListener {
        fun startFragment(fragment: BackBaseFragment)
        fun toggle()
    }

    fun startFragment(fragment: BackBaseFragment) {
        mHomeWorkControlListener?.startFragment(fragment)
    }

    fun setHomeControlListener(homeWorkControlListener: HomeWorkControlListener) {
        mHomeWorkControlListener = homeWorkControlListener
    }
}
