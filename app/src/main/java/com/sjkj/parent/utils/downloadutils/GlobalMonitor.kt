package com.sjkj.parent.utils.downloadutils

import android.util.Log

import com.liulishuo.filedownloader.BaseDownloadTask
import com.liulishuo.filedownloader.FileDownloadListener
import com.liulishuo.filedownloader.FileDownloadMonitor

class GlobalMonitor : FileDownloadMonitor.IMonitor {

    @Volatile
    var markStart: Int = 0
        private set
    @Volatile
    var markOver: Int = 0
        private set

    private object HolderClass {
        val INSTANCE = GlobalMonitor()
    }

    override fun onRequestStart(count: Int, serial: Boolean, lis: FileDownloadListener) {
        markStart = 0
        markOver = 0
        Log.d(TAG, String.format("on request start %d %B", count, serial))
    }

    override fun onRequestStart(task: BaseDownloadTask) {}

    override fun onTaskBegin(task: BaseDownloadTask) {
        markStart++
    }

    override fun onTaskStarted(task: BaseDownloadTask) {

    }

    override fun onTaskOver(task: BaseDownloadTask) {
        markOver++
    }

    companion object {

        val getImp: GlobalMonitor
            get() = HolderClass.INSTANCE

        private val TAG = "GlobalMonitor"
    }
}
