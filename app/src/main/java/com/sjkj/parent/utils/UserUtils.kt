package com.sjkj.parent.utils

import com.blankj.utilcode.util.SPUtils
import com.google.gson.Gson
import com.sjkj.parent.common.Common
import com.sjkj.parent.data.server.CourseInfoListBean
import com.sjkj.parent.data.server.UserBean

/**
 * @author by dingl on 2017/9/15.
 * @desc UserUtils
 */

object UserUtils {

    var userInfo: UserBean = Gson().fromJson(SPUtils.getInstance().getString(Common.USER), UserBean::class.java)

    var courseInfoList: List<CourseInfoListBean> = ArrayList()

    fun setUser(userInfo: UserBean) {
        this.userInfo = userInfo
    }

    fun setCourseList(courseInfoList: List<CourseInfoListBean>) {
        this.courseInfoList = courseInfoList
    }
}

fun getCourseInfoList() = getUser().CourseInfoList

fun getUser(): UserBean {
    return UserUtils.userInfo
}

fun getUserName(): String {
    return getUser().UserName.toString()
}

fun getName() = getUser().Name

fun getStudentID(): Int {
    return getUser().StudentID
}

fun getStudentUserName(): String {
    return getUser().StudentUserName.toString()
}
