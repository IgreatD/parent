package com.sjkj.parent.ui.controller.classroom

import android.content.Context
import com.sjkj.parent.data.server.QuestionBean
import com.sjkj.parent.mvp.contract.classroom.ClassRoomDetailContract
import com.sjkj.parent.mvp.presenter.classroom.ClassRoomDetailCheckPresenter
import com.sjkj.parent.ui.adapter.classroom.ClassRoomDetailAdapter
import com.sjkj.parent.ui.adapter.classroom.ClassRoomDetailViewHolder
import com.sjkj.parent.ui.controller.base.BaseRecycleVController
import com.sjkj.parent.ui.fragment.BackBaseFragment
import com.sjkj.parent.ui.fragment.ImageFragment
import org.jetbrains.anko.support.v4.onRefresh

/**
 * @author by dingl on 2017/9/18.
 * @desc ClassRoomDetailCheckController
 */
class ClassRoomDetailCheckController(context: Context) : BaseRecycleVController<QuestionBean, ClassRoomDetailViewHolder>(context), ClassRoomDetailContract.CheckBaseView {

    private lateinit var classRoomDetailCheckPresenter: ClassRoomDetailCheckPresenter

    private var mClassRoomTeachingID: Int = 0

    private var mCategory: Int = 0

    override fun initListener() {
        base_srl?.onRefresh {
            classRoomDetailCheckPresenter.getClassRoomDetailCheckData(mClassRoomTeachingID, mCategory)
        }
    }

    override fun initData() {
        classRoomDetailCheckPresenter = ClassRoomDetailCheckPresenter(this)
        baseAdapter = ClassRoomDetailAdapter {
            startFragment(ImageFragment().newInstance(it))
        }
    }

    override fun onClickLoadData() {
        classRoomDetailCheckPresenter.getClassRoomDetailCheckData(mClassRoomTeachingID, mCategory)
    }

    fun setClassRoomTeachingID(classRoomTeachingID: Int, category: Int) {
        if (baseList.size == 0 && mCategory != category)
            classRoomDetailCheckPresenter.getClassRoomDetailCheckData(classRoomTeachingID, category)
        mClassRoomTeachingID = classRoomTeachingID
        mCategory = category
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
