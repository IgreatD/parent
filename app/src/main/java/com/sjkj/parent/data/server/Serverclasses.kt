package com.sjkj.parent.data.server

import com.chad.library.adapter.base.entity.MultiItemEntity
import com.sjkj.parent.api.ParentUri
import com.sjkj.parent.common.QuestionType
import com.sjkj.parent.ui.widget.tree.*
import com.sjkj.parent.utils.getStudentUserName
import org.litepal.crud.DataSupport
import java.io.Serializable

/**
 * @author by dingl on 2017/9/21.
 * @desc Serverclasses
 */
data class JSONResult<out T>(
        val Code: Int,
        val JsonData: T,
        val Message: String)

data class UserBean(
        var Tel: String?,
        var QQ: String?,
        var Email: String?,
        val UserID: Int,
        val StudentID: Int,
        val GradeStageID: Int,
        val StudentUserName: String?,
        var HeadImg: String?,
        var Name: String?,
        val UserName: String?,
        val UserPassword: String?,
        val TargetSchoolIP: String?,
        val AppCode: String?,
        val AppVersion: String?,
        val AppPath: String?,
        val AppUpgradeRemark: String?,
        val CourseInfoList: List<CourseInfoListBean>?) : Serializable {

    fun _getHeadImg() = String.format("${ParentUri.BASE_FILE_URL}HeadImg")

}

data class CourseInfoListBean(
        var isSelect: Boolean = false,
        var CourseInfoID: Int,
        var Code: String?,
        var Name: String?,
        var UserLibraryVersion: Int,
        var KnowledgePointVersion: Int,
        var StudyMaterialsLibraryVersion: Int,
        var WordPointVersion: Int) : Serializable

data class ChatListBean(
        var Name: String?,
        var UserName: String?,
        var MsgContent: String?,
        var MsgType: Int,
        var creationDate: String?,
        var msgNum: Int) : DataSupport()

data class HomeBean(

        var ClassroomTeachingID: Int,
        var Name: String?,
        var CourseInfoID: Int,
        var KnowledgePointID: Int,
        var CreateUserID: Int,
        var CreateDate: String?,
        var UpdateUserID: String?,
        var UpdateDate: String?,
        var KnowledgePointName: String?,
        var CourseInfoName: String?,
        var IsReceived: Int,
        var ClassroomTeachingDescription: String?,
        var VideoUrl: String?,
        var ListClassroomTeachingObjectQuestionJson: String?,
        var LlistClassroomTeachingQuestionDto: String?)

data class NoteBean(

        var MessageID: Int,
        var MessageTitle: String?,
        var MessageContent: String?,
        var CreateUserID: Int,
        var PCreateDate: String?,
        var MessageObjectID: Int,
        var UserID: Int,
        var UserName: String?,
        var Name: String?,
        var IsSend: Int,
        var PReceivedDate: String?,
        var UserIDs: String?,
        var SendPictures: String?
)

data class PositionInfo(
        var fuPosition: Int,
        var position: Int,
        var category: Int = 0
)

data class MessageCountBean(val MessageCount: Int)

data class HomeWorkBean(
        val AllQuestionCount: Int,
        val AssignTime: String,
        val AvgConsumptionTime: Int,
        val CourseInfoID: Int,
        val CourseInfoName: String,
        val CourseName: String,
        val DisplayMode: String,
        val FinishTime: String,
        val FormType: Int,
        val HomeworkInfoID: Int,
        val HomeworkRanking: Int,
        val HomeworkRightWrong: String,
        val HomeworkScores: String,
        val IsFromParent: Int,
        val MinConsumptionTime: Int,
        val Name: String,
        val PublicTime: String,
        val QuestionInfo: String,
        val RevisedCheckType: Int,
        val RightRate: String,
        val SelfConsumptionTime: Int,
        val StatusInfo: Int,
        val StudentID: Int,
        val WorkType: Int,
        val WrongQuestionCount: Int
)

data class ClassRoomDetailSourceBean(
        val ClassroomStudyFileID: String,
        val ClassroomTeachingCategory: Int,
        val ClassroomTeachingID: Int,
        val ClassroomTeachingObjectStudyFileID: Int,
        val FileFormat: String,
        val FileName: String,
        val FilePath: String,
        val StudyMaterialsID: Int
) {
    fun getFileUrl(): String {
        return ParentUri.BASE_FILE_URL + FilePath
    }
}

data class QuestionBean(
        var isCheck: Boolean = false,
        val Analysis: String?,
        val Annotation: String?,
        val Answer: String?,
        val AnswerContentImage: String?,
        val ChildQuestionInfoList: List<QuestionBean>,
        val ClassInfoName: String?,
        val ClassroomTeachingCategory: Int,
        val ClassroomTeachingID: Int,
        val ClassroomTeachingObjectQuestionID: Int,
        val ClassroomTeachingQuestionID: Int,
        val Content: String?,
        val ContentImage: String?,
        val CourseInfoID: Int,
        val DifficultyLevelID: Int,
        val DifficultyLevelName: String?,
        val ExerciseNotes: String?,
        val HomeworkAnswer: String?,
        val HomeworkInfoID: Int,
        val HomeworkInfoName: String?,
        val HomeworkObjectQuestionID: Int,
        val HomeworkQuestionID: Int,
        val IsAnalysisText: Int,
        val IsAnswerText: Int,
        val IsCollection: Int,
        val IsExistsMicroClass: Int,
        val IsFromParent: Int,
        val IsMarking: Int,
        val IsCorrect: Int = -1,
        val IsMerge: Int,
        val IsText: Int,
        val IsRevisedAnswerText: Int,
        val IsRevisedCorrect: Int,
        val KnowledgePointID: Int,
        val KnowledgePointNames: String?,
        val Name: String?,
        val OrderNum: Int,
        val ParentID: Int,
        val QuestionBasicTypeID: Int,
        val QuestionExtendTypeID: Int,
        val QuestionExtendTypeName: String?,
        val QuestionInfoID: Int,
        val QuestionOptionCount: Int,
        val RecommendCategory: Int,
        val RevisedAnswer: String?,
        val RevisedCheckType: Int,
        val RevisedDate: String?,
        val RevisedEvaluation: String?,
        val RevisedScore: Int,
        val RightRate: String?,
        val Remark: String?,
        val RowsCount: String?,
        val Score: Int,
        val StatusInfo: Int,
        val StudentAnswer: String?,
        val StudentScore: String?,
        val SubmitDate: String?,
        val StudentNote: String?,
        val TeachingQuestionType: String?,
        val UpdateDate: String?,
        val UserID: Int,
        val UserLibraryNames: String?,
        val UserName: String?,
        val StudyMaterialsSum1: Int,
        val StudyMaterialsSum2: Int,
        val WorkType: Int,
        val ListHomeworkQuestionDto: List<QuestionBean>

) : DataSupport(), Serializable, MultiItemEntity {

    override fun getItemType(): Int {
        return when (QuestionBasicTypeID) {
            6 -> QuestionType.QUESTION_TYPE_FU
            else -> QuestionType.QUESTION_TYPE_DAN
        }
    }

    fun _getContentImage() = ParentUri.BASE_FILE_URL + ContentImage
    fun _getAnswerImage() = ParentUri.BASE_FILE_URL + AnswerContentImage
    fun _getStudentAnswer() = ParentUri.BASE_FILE_URL + getStudentUserName() + "/" + StudentAnswer + ".jpg"
}

data class AssignBean(
        var QuestionInfoID: Int? = 0,
        var Score: Int? = 0
)

class Library {

    @TreeNodeId
    var ID: Int = 0
    var UserLibraryID: Int = 0
    @TreeNodePid
    var ParentID: Int = 0
    @TreeNodeLabel
    var Name: String? = null
    @TreeNodeNum
    var QuestionSum: Int = 0
    @TreeNodeClick
    var click = -1
}

data class ChatMessage(var type: Int, var msgType: Int) {

    var progress: Int = 0

    var tag: Long? = 0

    var fromType: Int = 0

    var userID: Int = 0

    var creationDate: String? = null

    var category: Int = 9

    var msgContent: String? = null

    var fromUser: String? = null

    var toName: String? = null

    var fromName: String? = null

    var toUser: String? = null

    var isFrist: Boolean = true

    var msgNum: Int = 0

    fun getImg() = ParentUri.BASE_FILE_URL + msgContent

}

data class MessageEvent<out T>(val which: Int, val data: T? = null)

data class ChatFile(var fileName: String?) {
    var firstFrame: String? = null
    var filePath: String? = null
    var fileSize: String? = null
    var duration: Int = 0
    fun getAudio() = ParentUri.BASE_FILE_URL + fileName
}

data class UploadBean(val Code: String?)

data class CountInfo(
        var CourseInfoID: Long = 0, //科目ID

        var CourseInfoName: String? = null, //课目名称

        var HomeworkInfoName: String? = null, //作业名称

        var KnowledgePointName: String? = null, //知识点名称

        var RightRate: Float = 0.toFloat(), //正确率

        var ClassRightRate: Float = 0.toFloat(), //班级平均正确率
        var RightRateDifference: Float = 0.toFloat(), //班级平均正确率

        var RightRateClassOrder: Long = 0, //正确率班级排序

        var RightRateGradeOrder: Long = 0, //正确率年级排序

        var CompletionRate: Float = 0.toFloat(), //正确率

        var ClassCompletionRate: Float = 0.toFloat(), //班级平均正确率

        var CompletionRateClassOrder: Long = 0, //正确率班级排序

        var CompletionRateGradeOrder: Long = 0//正确率年级排序
)
