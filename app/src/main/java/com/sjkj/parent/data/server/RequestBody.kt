package com.sjkj.parent.data.server

import com.sjkj.parent.utils.getUser

/**
 * @author by dingl on 2017/9/21.
 * @desc RequestBody
 */
data class StudentIDRequestBody(val StudentID: Int)

data class NoticeRequestBody(val StudentID: Int, val PageSize: Int, val PageIndex: Int)

data class NoticeStateRequestBody(val StudentID: Int, val MessageID: Int)

data class LoginRequestBody(val UserName: String, val PassWord: String)

data class HomeWorkRequestBody(val StudentID: Int, val CourseInfoID: Int, val HomeworkType: Int, val Category: Int, val PageSize: Int, val PageIndex: Int)

data class HomeWorkDetaialRequestBody(val StudentID: Int, val HomeworkInfoID: Int)

data class HomeRequestBody(val PageSize: Int, val PageIndex: Int, val StudentID: Int, val CourseInfoID: Int)

data class ClassRoomDetailRequestBody(val StudentID: Int, val ClassroomTeachingID: Int, val ClassroomTeachingCategory: Int)

data class MineChangePdRequestBody(val OldPassword: String, val NewPassword: String, val UserID: Int)

data class AssignWrongRequestBody(val CourseInfoID: Int?, val StudentID: Int)

data class AssignWrongListRequestBody(val CourseInfoID: Int, val StudentID: Int, val KnowledgePointIDs: String,
                                      val PageSize: Int, val PageIndex: Int,
                                      val StartDate: String?, val EndDate: String?)

data class AssignNormalListRequestBody(val CourseInfoID: Int, val UserLibraryIDs: String, val PageSize: Int, val PageIndex: Int)

data class CountListRequestBody(val StudentID: Int, val StartDate: String, val EndDate: String) {
    var CourseInfoID: Int? = 0
}

data class ChatMsgHistroyRequestBody(val ToUserName: String, val FromUserName: String, val PageSize: Int, val PageIndex: Int)

data class MinePersonalModifyRequestBody(val StudentID: Int = getUser().StudentID) {
    var Name: String? = null
    var HeadImg: String? = null
    var Tel: String? = null
    var QQ: String? = null
    var Email: String? = null
}

data class AssignHwRequestBody(val CourseInfoID: Int?, val Name: String?,
                               val WorkType: Int?, val RevisedCheckType: Int?,
                               val AssignTime: String?, val FinishTime: String?,
                               val UserID: Int, val StudentID: Int?,
                               val QuestionInfoIDs: String?)
