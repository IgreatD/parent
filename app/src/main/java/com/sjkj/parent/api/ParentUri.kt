package com.sjkj.parent.api

/**
 * @author by dingl on 2017/9/11.
 * @desc Const
 */
object ParentUri {

    const val BASE_URL = "http://192.168.1.100:94/ParentQueryJsonService.svc/"

    const val LOGIN_URL = "http://192.168.1.100:95/ParentLoginJsonService.svc/parent_Login"

    const val MODIFY_URL = "http://192.168.1.100:95/ParentLoginJsonService.svc/parent_ModifyPassword"

    const val MODIFY_PERSONAL = "http://192.168.1.100:95/ParentLoginJsonService.svc/parent_EditParentInfo"

    const val BASE_FILE_URL = "http://192.168.1.100:92/"

    const val XMPP_URL = "192.168.1.100"

    const val BASE_UPLOAD = "http://192.168.1.100:92/HttpUpload/HttpVoiceUpload.ashx"

    //http://192.168.1.100:94/ParentQueryJsonService.svc    数据

    //http://192.168.1.100:95/ParentLoginJsonService.svc    登陆
}
