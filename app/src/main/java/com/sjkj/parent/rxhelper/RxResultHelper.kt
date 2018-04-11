package com.sjkj.parent.rxhelper

import com.sjkj.parent.data.server.JSONResult
import com.sjkj.parent.exception.RequestDataException
import com.sjkj.parent.exception.RequestIllegalArgumentException
import com.sjkj.parent.exception.RequestNullDataException
import rx.Observable

/**
 * @author by dingl on 2017/9/12.
 * *
 * @desc RxResultHelper
 */

object RxResultHelper {

    fun <T> handleResult(): Observable.Transformer<JSONResult<T>, T> {

        return Observable.Transformer { tObservable ->
            tObservable.flatMap({ (code, jsonData, message) ->

                when (code) {
                    1 -> {
                        if (jsonData == null)
                            Observable.error<T>(RequestNullDataException(message))
                        else
                            Observable.just<T>(jsonData)
                    }
                    2 -> Observable.error<T>(RequestNullDataException(message))
                    -1 -> Observable.error<T>(RequestDataException(message))
                    -2 -> Observable.error<T>(RequestIllegalArgumentException(message))
                    else -> Observable.error<T>(RequestNullDataException("没有数据"))
                }
            }
            )
        }
    }

}
