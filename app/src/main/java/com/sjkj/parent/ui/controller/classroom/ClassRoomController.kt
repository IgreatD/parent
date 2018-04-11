package com.sjkj.parent.ui.controller.classroom

import android.content.Context
import com.sjkj.parent.data.server.HomeBean
import com.sjkj.parent.mvp.contract.classroom.ClassRoomContract
import com.sjkj.parent.mvp.presenter.classroom.ClassRoomPresenter
import com.sjkj.parent.ui.adapter.classroom.ClassRoomAdapter
import com.sjkj.parent.ui.controller.base.BaseRecycleController
import com.sjkj.parent.ui.fragment.BackBaseFragment
import com.sjkj.parent.ui.fragment.classroom.ClassRoomDetailFragment
import kotlinx.android.synthetic.main.controller_recycle_base.view.*
import org.jetbrains.anko.support.v4.onRefresh

/**
 * @author by dingl on 2017/9/15.
 * @desc ClassRoomController
 */
class ClassRoomController(context: Context) : BaseRecycleController<HomeBean>(context), ClassRoomContract.BaseView {


    override fun onClickLoadData() {

        classRoomPresenter?.getData(courseInfoID)

    }

    private var courseInfoID: Int = 0

    private var classRoomPresenter: ClassRoomPresenter? = null

    override fun initData() {

        classRoomPresenter = ClassRoomPresenter(this)

        baseAdapter = ClassRoomAdapter(baseList)

        base_rv?.adapter = baseAdapter

    }

    override fun initListener() {
        base_srl.onRefresh {
            classRoomPresenter?.getData(courseInfoID)
        }

        baseAdapter?.setOnLoadMoreListener({
            classRoomPresenter?.getMoreData(courseInfoID)
        }, base_rv)

        baseAdapter?.setOnItemChildClickListener { _, _, position ->
            val fragment = ClassRoomDetailFragment().newInstance(baseList[position].ClassroomTeachingID)
            startFragment(fragment)
        }

    }

    fun setCourseInfoID(courseInfoID: Int) {
        baseList.clear()
        this.courseInfoID = courseInfoID
        classRoomPresenter?.getData(courseInfoID)
    }

    private var mHomeControlListener: HomeControlListener? = null

    interface HomeControlListener {
        fun startFragment(fragment: BackBaseFragment)
    }

    fun startFragment(fragment: BackBaseFragment) {
        mHomeControlListener?.startFragment(fragment)
    }

    fun setHomeControlListener(homeControlListener: HomeControlListener) {
        mHomeControlListener = homeControlListener
    }
}
