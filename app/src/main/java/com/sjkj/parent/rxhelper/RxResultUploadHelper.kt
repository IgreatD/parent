package com.sjkj.parent.rxhelper

import okhttp3.ResponseBody
import rx.Observable

/**
 * @author by dingl on 2017/9/30.
 * @desc RxResultUploadHelper
 */
object RxResultUploadHelper {

    fun handleResult(): Observable.Transformer<ResponseBody, Boolean> {

        return Observable.Transformer { tObservable ->
            tObservable.flatMap {
                val response = it.string()
                when (response) {
                    "Success" -> Observable.just(true)
                    "Error" -> Observable.just(false)
                    else -> Observable.just(false)
                }
            }
        }
    }

}
