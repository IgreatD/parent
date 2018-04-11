package com.sjkj.parent.api

import com.sjkj.parent.data.server.*
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Url
import rx.Observable


/**
 * @author by dingl on 2017/9/11.
 * @desc ParentApi 项目api集合
 */
interface ParentApi {

    @POST //上传接口
    fun upLoadSingle(@Url url: String, @Body uploadParams: RequestBody): Observable<ResponseBody>

    @POST //登陆接口
    fun getLoginData(@Url url: String, @Body loginParams: RequestBody): Observable<JSONResult<UserBean>>

    @POST
    fun editParentInfo(@Url url: String, @Body parentParams: RequestBody): Observable<JSONResult<String>>

    @POST
    fun modifyPassWord(@Url url: String, @Body parentParams: RequestBody): Observable<JSONResult<String>>

    @POST("parent_GetClassroomTeachingList") //获取课堂列表
    fun getClassroomData(@Body classroomParams: RequestBody): Observable<JSONResult<List<HomeBean>>>

    @POST("parent_GetMessageList") //获取消息列表
    fun getNoticeData(@Body noticeParams: RequestBody): Observable<JSONResult<List<NoteBean>>>

    @POST("parent_UpdateMessageState")
    fun updateMsgState(@Body params: RequestBody): Observable<JSONResult<String>>

    @POST("parent_GetNoReceivedMessageCount") //获取未读消息的个数
    fun getMsgCount(@Body msgParams: RequestBody): Observable<JSONResult<MessageCountBean>>

    @POST("parent_GetContactsForParent") //获取家长的联系人
    fun getTeacherList(@Body teacherParams: RequestBody): Observable<JSONResult<List<ChatListBean>>>

    @POST("parent_GetClassroomTeachingObjectStudyFileByUserID") //获取课堂预习素材或者课堂素材
    fun getClassRoomSourceDetail(@Body classroomParams: RequestBody): Observable<JSONResult<List<ClassRoomDetailSourceBean>>>

    @POST("parent_GetClassroomTeachingObjectQuestionByUserID") //获取课堂预习检测或者课堂检测
    fun getClassRoomCheckDetail(@Body classroomParams: RequestBody): Observable<JSONResult<List<QuestionBean>>>

    @POST("parent_GetHomeworkInfoListForPad") //获取作业列表
    fun getHomeWorkInofList(@Body homeRequestBody: RequestBody): Observable<JSONResult<List<HomeWorkBean>>>

    @POST("parent_GetHomeworkQuestionListForPad") //获取作业详情
    fun getHomeWorkQuestionList(@Body hwQuestionParams: RequestBody): Observable<JSONResult<List<QuestionBean>>>

    @POST("parent_GetUserLibraryTreeListByCourseInfoID")
    fun getLibraryList(@Body knowTreeParams: RequestBody): Observable<JSONResult<List<Library>>>

    @POST("parent_GetKnowledgePointTreeListByUserID") //获取知识点
    fun getKonwTreeList(@Body knowTreeParams: RequestBody): Observable<JSONResult<List<Library>>>

    @POST("parent_GetDoErrorHomeworkQuestionListByUserID")//根据知识点获取试题
    fun getQuestionErrorList(@Body questionListParams: RequestBody): Observable<JSONResult<List<QuestionBean>>>

    @POST("parent_GetQuestionInfoFromLibrary")
    fun getQuestionLibraryList(@Body questionListParams: RequestBody): Observable<JSONResult<List<QuestionBean>>>

    @POST("parent_AssignHomework")
    fun assignHw(@Body questionListParams: RequestBody): Observable<JSONResult<String>>

    @POST("parent_GetInstantMessageHistory") //获取消息历史记录
    fun getMsgHistory(@Body msgHistoryParmas: RequestBody): Observable<JSONResult<List<ChatMessage>>>

    @POST("parent_GetAllCourseCompletionRate")
    fun getCountSubjectHandList(@Body params: RequestBody): Observable<JSONResult<List<CountInfo>>>

    @POST("parent_GetAllCourseRightRate")
    fun getCountSubjectRightList(@Body params: RequestBody): Observable<JSONResult<List<CountInfo>>>

    @POST("parent_GetHomeworkRightRate")
    fun getCountHwRightList(@Body params: RequestBody): Observable<JSONResult<List<CountInfo>>>

    @POST("parent_GetKnowledgePointRightRate")
    fun getCountKnowRightList(@Body params: RequestBody): Observable<JSONResult<List<CountInfo>>>

}
