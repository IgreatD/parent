package com.sjkj.parent.mvp.contract

import java.io.File

/**
 * @author by dingl on 2017/9/29.
 * @desc UpLoadContract
 */
class UpLoadContract {

    interface BaseView : BaseContract.BaseView {
        fun upLoadBefore(fileName: String, type: Int, tag: Long) {}
        fun upLoadSuccess(fileName: String, type: Int, tag: Long)
        fun upLoadError(tag: Long){}
        fun onUpLoadProgress(currentBytesCount: Long, totalBytesCount: Long, tag: Long) {}
    }

    interface Presenter {
        fun upload(file: File, type: Int)
    }

}
