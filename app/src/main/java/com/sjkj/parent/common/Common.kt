package com.sjkj.parent.common

import android.support.annotation.IntDef

/**
 * @author by dingl on 2017/9/12.
 * @desc Common
 */
object Common {
    const val USER_NAME: String = "userName"
    const val USER_PASSWORD: String = "userPassWord"
    const val RECONNECT: Long = 5L
    const val CONTENT_TYPE = "application/json"
    const val PAGESIZE = 10
    const val CLASSROOMTEACHINGID = "classroomteachingid"
    const val HOMEWORKINFOID = "homeworkinfoid"
    const val KONWIDS = "knowids"
    const val COURSEINFOID = "courseinfoid"
    const val START_DATE = "start_date"
    const val END_DATE = "end_date"
    const val NAME = "name"
    const val REQUEST_CODE_CHOOSE: Int = 1
    const val REQUEST_FILE: Int = 3
    const val NOTE_BEAN = "notebean"
    const val USER = "user"
    const val KEYBOARD_HEIGHT: String = "keyboard_height"
    const val FILE_NAME = "file_name"
    const val DEFAULT_MAX_AUDIO_RECORD_TIME_SECOND: Int = 120
    const val STATUSINFO = "statusinfo"

}

object QuestionType {
    const val QUESTION_TYPE_DAN = 1
    const val QUESTION_TYPE_DUO = 2
    const val QUESTION_TYPE_TIAN = 3
    const val QUESTION_TYPE_WAN = 4
    const val QUESTION_TYPE_PAN = 5
    const val QUESTION_TYPE_FU = 6
}

object Which {
    const val CONNECT_STATE = 0x001
    const val CHAT_MSG = 0x002
    const val NEW_MSG = 0x003
    const val MODIFY_HEAD = 0x004
    const val MODIFY_NAME = 0x005
    const val NOTICE_STATE: Int = 0x006
    const val QUESTION_BACK: Int = 0x007
}

object ChatType {
    const val TYPE_RECEIVE = 1
    const val TYPE_SEND = 2
    const val MSGTYPE_TXT = 0
    const val MSGTYPE_IMG = 1
    const val MSGTYPE_VOICE = 2
    const val MSGTYPE_FILE = 3
    const val MSGTYPE_MICRO = 4
}

object ChatService {
    const val CONNECTED = 1

    const val DISCONNECTED = -1

    const val CONNECTING = 2
}

object CodeException {

    const val NETWORD_ERROR = 0x1L
    const val HTTP_ERROR = 0x2L
    const val JSON_ERROR = 0x3L
    const val UNKNOWN_ERROR = 0x4L
    const val RUNTIME_ERROR = 0x5L
    const val UNKOWNHOST_ERROR = 0x6L

    @IntDef(NETWORD_ERROR, HTTP_ERROR, RUNTIME_ERROR, UNKNOWN_ERROR, JSON_ERROR, UNKOWNHOST_ERROR)
    @kotlin.annotation.Retention(AnnotationRetention.SOURCE)
    annotation class CodeEp

}
