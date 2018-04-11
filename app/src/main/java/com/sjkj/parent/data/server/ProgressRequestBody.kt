package com.sjkj.parent.data.server

import okhttp3.MediaType
import okhttp3.RequestBody
import okio.*
import rx.Observable
import rx.android.schedulers.AndroidSchedulers


/**
 * 上传进度显示
 *
 * @author by dingl on 2017/11/2.
 * @desc ProgressRequestBody
 */
class ProgressRequestBody(private val requestBody: RequestBody, private val progressListener: UpLoadProgressListener) : RequestBody() {

    private var bufferedSink: BufferedSink? = null

    override fun contentType(): MediaType = requestBody.contentType()

    override fun contentLength(): Long = requestBody.contentLength()

    override fun writeTo(sink: BufferedSink?) {
        if (null == bufferedSink)
            bufferedSink = Okio.buffer(sink(sink))
        requestBody.writeTo(bufferedSink)
        bufferedSink?.flush()
    }

    private fun sink(sink: BufferedSink?): Sink {
        return object : ForwardingSink(sink) {

            var writtenBytesCount = 0L

            var totalBytesCount = 0L

            override fun write(source: Buffer?, byteCount: Long) {
                super.write(source, byteCount)
                writtenBytesCount += byteCount
                if (totalBytesCount == 0L)
                    totalBytesCount = contentLength()
                Observable.just(writtenBytesCount)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe { progressListener.onProgress(writtenBytesCount, totalBytesCount) }
            }
        }
    }
}

interface UpLoadProgressListener {
    fun onProgress(currentBytesCount: Long, totalBytesCount: Long)
}
