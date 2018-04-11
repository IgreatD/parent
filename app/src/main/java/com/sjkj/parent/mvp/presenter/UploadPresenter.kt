package com.sjkj.parent.mvp.presenter

import com.sjkj.parent.api.ParentUri
import com.sjkj.parent.data.server.ProgressRequestBody
import com.sjkj.parent.data.server.UpLoadProgressListener
import com.sjkj.parent.mvp.contract.UpLoadContract
import com.sjkj.parent.rxhelper.RxSchedulersHelper
import com.sjkj.parent.rxhelper.RxSubscriber
import com.sjkj.parent.utils.getApi
import com.sjkj.parent.utils.getUserName
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File

/**
 * @author by dingl on 2017/9/30.
 * @desc UploadPresenter
 */
class UploadPresenter constructor(val mView: UpLoadContract.BaseView) : BasePresenter(), UpLoadContract.Presenter {

    private var druation = 0
    private var size = ""

    private var currentLongTime = System.currentTimeMillis()

    override fun upload(file: File, type: Int) {

        val fileName = String.format("student/${getUserName()}/${file.name}")
        val requestBody = RequestBody.create(MediaType.parse("all/*"), file)
        val tag = System.currentTimeMillis()
        val multipartBody = MultipartBody.Builder().setType(MultipartBody.FORM)
                .addFormDataPart("FileNames", fileName)
                .addFormDataPart("files", file.name, ProgressRequestBody(requestBody, object : UpLoadProgressListener {
                    override fun onProgress(currentBytesCount: Long, totalBytesCount: Long) {
                        if (System.currentTimeMillis() - currentLongTime > 1000) {
                            mView.onUpLoadProgress(currentBytesCount, totalBytesCount, tag)
                            currentLongTime = System.currentTimeMillis()
                        }
                    }
                }))
                .build()

        addSubscription(getApi().upLoadSingle(ParentUri.BASE_UPLOAD, multipartBody)
                .compose(RxSchedulersHelper.io_main_upLoad())
                .subscribe(object : RxSubscriber<Boolean>(mView) {
                    override fun onStart() {
                        super.onStart()
                        mView.upLoadBefore(fileName, type, tag)
                    }

                    override fun _onNext(t: Boolean) {
                        if (t)
                            mView.upLoadSuccess(fileName, type, tag)
                        else
                            mView.upLoadError(tag)
                    }

                }))
    }

    fun setDuration(duration: Int) {
        this.druation = duration
    }

    fun getDuration() = druation

    fun setSize(printSize: String) {
        this.size = printSize
    }

    fun getSize() = size
}
