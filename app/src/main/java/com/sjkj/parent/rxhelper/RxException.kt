package com.sjkj.parent.rxhelper

import com.alibaba.fastjson.JSONException
import com.alibaba.fastjson.JSONPathException
import com.sjkj.parent.common.CodeException
import retrofit2.adapter.rxjava.HttpException
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import java.text.ParseException

/**
 * @author by dingl on 2017/11/2.
 * @desc RxException
 */
class RxException {

    private val HttpException_MSG = "网络错误"
    private val ConnectException_MSG = "连接失败"
    private val JSONException_MSG = "数据解析失败"
    private val UnknownHostException_MSG = "无法解析该域名"

    fun analysisExcetpion(e: Throwable): ApiException {
        val apiException = ApiException(e)
        if (e is HttpException) {
            apiException.code = CodeException.HTTP_ERROR
            apiException.msg = HttpException_MSG
        } else if (e is ConnectException || e is SocketTimeoutException) {
            apiException.code = (CodeException.HTTP_ERROR)
            apiException.msg = (ConnectException_MSG)
        } else if (e is JSONPathException || e is JSONException || e is ParseException) {
            apiException.code = (CodeException.JSON_ERROR)
            apiException.msg = (JSONException_MSG)
        } else if (e is UnknownHostException) {
            apiException.code = (CodeException.UNKOWNHOST_ERROR)
            apiException.msg = (UnknownHostException_MSG)
        } else {
            apiException.code = (CodeException.UNKNOWN_ERROR)
            apiException.msg = (e.message)
        }
        return apiException
    }
}

data class ApiException(val e: Throwable) : Exception(e) {
    var code: Long = 0
    var msg: String? = null
}
