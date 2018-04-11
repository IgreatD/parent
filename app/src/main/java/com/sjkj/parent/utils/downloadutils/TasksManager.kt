package com.sjkj.parent.utils.downloadutils

import android.text.TextUtils
import android.util.SparseArray
import com.liulishuo.filedownloader.*
import com.liulishuo.filedownloader.model.FileDownloadStatus
import com.liulishuo.filedownloader.util.FileDownloadUtils
import com.sjkj.parent.ui.activity.BaseActivity
import org.litepal.crud.DataSupport
import java.lang.ref.WeakReference
import java.util.*


class TasksManager {

    private object HolderClass {
        val INSTANCE = TasksManager()
    }

    companion object {

        val getImp: TasksManager
            get() = HolderClass.INSTANCE
    }

    private var listener: FileDownloadConnectListener? = null

    val isReady: Boolean
        get() = FileDownloader.getImpl().isServiceConnected

    private val taskSparseArray = SparseArray<BaseDownloadTask>()

    //    #--------------------------------------------listener start----------------------------------------

    private val updaterList = ArrayList<DownloadStatusUpdater>()

    private var taskDownloadListener: FileDownloadListener = object : FileDownloadSampleListener() {

        override fun pending(task: BaseDownloadTask?, soFarBytes: Int, totalBytes: Int) {
            super.pending(task, soFarBytes, totalBytes)
            updateDownloading(task, FileDownloadStatus.pending, soFarBytes, totalBytes)
        }

        override fun started(task: BaseDownloadTask?) {
            super.started(task)
            updateStart(task)
        }

        override fun connected(task: BaseDownloadTask?, etag: String?, isContinue: Boolean, soFarBytes: Int, totalBytes: Int) {
            super.connected(task, etag, isContinue, soFarBytes, totalBytes)
            updateDownloading(task, FileDownloadStatus.connected, soFarBytes, totalBytes)
        }

        override fun progress(task: BaseDownloadTask?, soFarBytes: Int, totalBytes: Int) {
            super.progress(task, soFarBytes, totalBytes)
            updateDownloading(task, FileDownloadStatus.progress, soFarBytes, totalBytes)
        }

        override fun error(task: BaseDownloadTask?, e: Throwable?) {
            super.error(task, e)
            updateNotDownloaded(task, FileDownloadStatus.error, task!!.largeFileSoFarBytes, task.largeFileTotalBytes)
            removeTaskForViewHolder(task.id)
        }

        override fun paused(task: BaseDownloadTask?, soFarBytes: Int, totalBytes: Int) {
            super.paused(task, soFarBytes, totalBytes)
            updateNotDownloaded(task, FileDownloadStatus.paused, task!!.largeFileSoFarBytes, task.largeFileTotalBytes)
            removeTaskForViewHolder(task.id)
        }

        override fun completed(task: BaseDownloadTask?) {
            super.completed(task)
            updateDownloaded(task)
            removeTaskForViewHolder(task!!.id)
        }
    }

    private fun releaseTask() {
        updaterList.clear()
    }

    private fun registerServiceConnectionListener(activityWeakReference: WeakReference<BaseActivity>?) {

        if (listener != null) {
            FileDownloader.getImpl().removeServiceConnectListener(listener)
        }

        listener = object : FileDownloadConnectListener() {

            override fun connected() {
                if (activityWeakReference?.get() == null) {
                    return
                }
                activityWeakReference.get()?.postNotifyDataChanged()
            }

            override fun disconnected() {
                if (activityWeakReference?.get() == null) {
                    return
                }

                activityWeakReference.get()?.postNotifyDataChanged()
            }
        }

        FileDownloader.getImpl().addServiceConnectListener(listener)
    }

    private fun unregisterServiceConnectionListener() {
        FileDownloader.getImpl().removeServiceConnectListener(listener)
        listener = null
    }

    fun onCreate(activityWeakReference: WeakReference<BaseActivity>) {
        if (!FileDownloader.getImpl().isServiceConnected) {
            FileDownloader.getImpl().bindService()
            registerServiceConnectionListener(activityWeakReference)
        }
    }

    fun onDestroy() {
        unregisterServiceConnectionListener()
        releaseTask()
    }

    fun isDownloaded(status: Int): Boolean {
        return status == FileDownloadStatus.completed.toInt()
    }

    fun getStatus(id: Int, path: String): Int {
        return FileDownloader.getImpl().getStatus(id, path).toInt()
    }

    fun getTotal(id: Int): Long {
        return FileDownloader.getImpl().getTotal(id)
    }

    fun getSoFar(id: Int): Long {
        return FileDownloader.getImpl().getSoFar(id)
    }

    @JvmOverloads
    fun addTask(url: String, path: String? = createPath(url)) {
        if (TextUtils.isEmpty(url) || TextUtils.isEmpty(path)) {
            return
        }
        val id = FileDownloadUtils.generateId(url, path)

        startTask(url, path)

        if (DataSupport.select("*").where("tag =\" $id\"").findFirst(TasksManagerModel::class.java) != null) {
            return
        }

        val managerModel = TasksManagerModel()
        managerModel.tag = id
        managerModel.name = id.toString()
        managerModel.path = path
        managerModel.url = url
        managerModel.save()
    }

    fun removeTaskForViewHolder(id: Int) {
        taskSparseArray.remove(id)
    }

    fun pauseTask(id: Int) {
        if (taskSparseArray.get(id) != null)
            taskSparseArray.get(id).pause()
    }

    fun startTask(url: String, path: String?): Int {
        val task = FileDownloader
                .getImpl()
                .create(url)
                .setPath(path)
                .setCallbackProgressTimes(2000)
                .setListener(taskDownloadListener)
        task.start()
        if (taskSparseArray.get(task.id) == null)
            taskSparseArray.put(task.id, task)
        return task.id
    }

    private fun createPath(url: String): String? {
        return if (TextUtils.isEmpty(url)) {
            null
        } else FileDownloadUtils.getDefaultSaveFilePath(url)
    }

    fun addUpdater(updater: DownloadStatusUpdater) {
        if (!updaterList.contains(updater)) {
            updaterList.add(updater)
        }
    }

    fun removeUpdater(updater: DownloadStatusUpdater) {
        updaterList.remove(updater)
    }

    private fun updateDownloaded(task: BaseDownloadTask?) {
        val updaterListCopy = updaterList.clone() as List<DownloadStatusUpdater>
        for (updater in updaterListCopy) {
            updater.updateDownloaded(task)
        }
    }

    private fun updateNotDownloaded(task: BaseDownloadTask?, status: Byte, largeFileSoFarBytes: Long, largeFileTotalBytes: Long) {
        val updaterListCopy = updaterList.clone() as List<DownloadStatusUpdater>
        for (updater in updaterListCopy) {
            updater.updateNotDownloaded(task, status, largeFileSoFarBytes, largeFileTotalBytes)
        }
    }

    private fun updateStart(task: BaseDownloadTask?) {
        val updaterListCopy = updaterList.clone() as List<DownloadStatusUpdater>
        for (updater in updaterListCopy) {
            updater.updateStart(task)
        }
    }

    private fun updateDownloading(task: BaseDownloadTask?, status: Byte, soFarBytes: Int, totalBytes: Int) {
        val updaterListCopy = updaterList.clone() as List<DownloadStatusUpdater>
        for (updater in updaterListCopy) {
            updater.updateDownloading(task, status, soFarBytes, totalBytes)
        }
    }

    interface DownloadStatusUpdater {

        fun updateDownloaded(task: BaseDownloadTask?)

        fun updateNotDownloaded(task: BaseDownloadTask?, status: Byte, largeFileSoFarBytes: Long, largeFileTotalBytes: Long)

        fun updateStart(task: BaseDownloadTask?)

        fun updateDownloading(task: BaseDownloadTask?, status: Byte, soFarBytes: Int, totalBytes: Int)
    }

    //    #--------------------------------------------listener end----------------------------------------
}
