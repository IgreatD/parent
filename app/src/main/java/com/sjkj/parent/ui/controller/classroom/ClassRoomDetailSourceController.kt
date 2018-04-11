package com.sjkj.parent.ui.controller.classroom

import android.content.Context
import com.sjkj.parent.data.server.ClassRoomDetailSourceBean
import com.sjkj.parent.mvp.contract.classroom.ClassRoomDetailContract
import com.sjkj.parent.mvp.presenter.classroom.ClassRoomDetailSourcePresenter
import com.sjkj.parent.ui.adapter.classroom.ClassRoomDetailSourceAdapter
import com.sjkj.parent.ui.controller.base.BaseRecycleController
import kotlinx.android.synthetic.main.controller_recycle_base.view.*
import org.jetbrains.anko.support.v4.onRefresh

/**
 * @author by dingl on 2017/9/18.
 * @desc ClassRoomDetailSourceController
 */
class ClassRoomDetailSourceController(context: Context) : BaseRecycleController<ClassRoomDetailSourceBean>(context), ClassRoomDetailContract.SourceBaseView {

    override fun onClickLoadData() {
        classroomDetailPresenter?.getClassRoomDetailSourceData(mClassRoomTeachingID, mPosition)
    }

    private var mClassRoomTeachingID: Int = 0

    private var mPosition: Int = 0

    private var classroomDetailPresenter: ClassRoomDetailSourcePresenter? = null

    override fun initListener() {

        base_srl.onRefresh {
            classroomDetailPresenter?.getClassRoomDetailSourceData(mClassRoomTeachingID, mPosition)
        }
    }

    override fun initData() {

        classroomDetailPresenter = ClassRoomDetailSourcePresenter(this)

        baseAdapter = ClassRoomDetailSourceAdapter(baseList)

        base_rv?.adapter = baseAdapter
    }

    fun setClassRoomTeachingID(classRoomTeachingID: Int, position: Int) {
        mClassRoomTeachingID = classRoomTeachingID
        mPosition = position
        if (baseList.size == 0)
            classroomDetailPresenter?.getClassRoomDetailSourceData(mClassRoomTeachingID, mPosition)
    }
}
