package com.sjkj.parent.rxhelper

import com.sjkj.parent.data.server.JSONResult
import okhttp3.ResponseBody
import rx.Observable
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers

/**
 * @author by dingl on 2017/9/12.
 * *
 * @desc RxSchedulersHelper
 */

class RxSchedulersHelper {

    companion object {

        fun <T> io_main(): Observable.Transformer<JSONResult<T>, T> {
            return Observable.Transformer { tObservable ->
                tObservable
                        .retryWhen(CustomerRetryWhen())
                        .onErrorResumeNext({ Observable.error(RxException().analysisExcetpion(it)) })
                        .subscribeOn(Schedulers.io())
                        .unsubscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .compose(RxResultHelper.handleResult())
            }
        }

        fun io_main_upLoad(): Observable.Transformer<ResponseBody, Boolean> {
            return Observable.Transformer { tObservable ->
                tObservable
                        .retryWhen(CustomerRetryWhen())
                        .onErrorResumeNext({ Observable.error(RxException().analysisExcetpion(it)) })
                        .subscribeOn(Schedulers.io())
                        .unsubscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .compose(RxResultUploadHelper.handleResult())
            }
        }
    }

}
