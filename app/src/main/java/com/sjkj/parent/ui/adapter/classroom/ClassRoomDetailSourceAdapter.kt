package com.sjkj.parent.ui.adapter.classroom

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.blankj.utilcode.util.FileUtils
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.liulishuo.filedownloader.BaseDownloadTask
import com.liulishuo.filedownloader.FileDownloadListener
import com.liulishuo.filedownloader.FileDownloader
import com.qmuiteam.qmui.widget.QMUIProgressBar
import com.sjkj.parent.R
import com.sjkj.parent.data.server.ClassRoomDetailSourceBean
import com.sjkj.parent.utils.AndroidFileUtil
import com.sjkj.parent.utils.getApp
import com.sjkj.parent.utils.getClassRoomPath
import com.sjkj.parent.utils.logD
import org.jetbrains.anko.imageResource
import org.jetbrains.anko.sdk25.coroutines.onClick
import org.jetbrains.anko.toast
import java.io.File


/**
 * @author by dingl on 2017/9/18.
 * @desc ClassRoomDetailSourcePresenter
 */
class ClassRoomDetailSourceAdapter(data: List<ClassRoomDetailSourceBean>)
    : BaseQuickAdapter<ClassRoomDetailSourceBean, BaseViewHolder>(R.layout.adapter_classroom_detail_source_item, data) {

    override fun convert(helper: BaseViewHolder?, item: ClassRoomDetailSourceBean?) {

        helper?.getView<TextView>(R.id.classroom_content)?.text = item?.FileName

        val circle_pb = helper?.getView<QMUIProgressBar>(R.id.action_download)

        val action_open = helper?.getView<ImageView>(R.id.action_open)

        val classroom_iv = helper?.getView<ImageView>(R.id.classroom_iv)

        val fileFormat = item?.FileFormat

        when (fileFormat?.toLowerCase()) {
            "mp4", "rmvb", "rm", "flv", "3gp", "avi", "mkv" -> classroom_iv?.imageResource = R.drawable.studyfile_mp4
            "png", "jpg" -> classroom_iv?.imageResource = R.drawable.studyfile_image
            "doc" -> classroom_iv?.imageResource = R.drawable.studyfile_word
            "ppt" -> classroom_iv?.imageResource = R.drawable.studyfile_ppt
            "xls" -> classroom_iv?.imageResource = R.drawable.studyfile_excel
            "mp3", "wmv" -> classroom_iv?.imageResource = R.drawable.studyfile_mp3
            "txt" -> classroom_iv?.imageResource = R.drawable.studyfile_file
            else -> classroom_iv?.imageResource = R.drawable.studyfile_unknown
        }

        val file = File(getClassRoomPath(), item?.FileName + "." + item?.FileFormat)

        if (file.exists()) {
            action_open?.visibility = View.VISIBLE
            circle_pb?.visibility = View.GONE
            openFile(action_open, file)
        } else {
            circle_pb?.qmuiProgressBarTextGenerator = QMUIProgressBar.QMUIProgressBarTextGenerator { _, value, maxValue -> (100 * value / maxValue).toString() + "%" }
            action_open?.visibility = View.VISIBLE
            circle_pb?.visibility = View.GONE
            FileDownloader.setup(mContext)
            action_open?.imageResource = R.drawable.studyfile_download
            val task = FileDownloader.getImpl().create(item?.getFileUrl())
            action_open?.onClick {
                if (task.isUsing) {
                    getApp().toast("正在下载中，请稍候...")
                    return@onClick
                }
                task.setTag(item?.ClassroomStudyFileID)
                        .setPath(getClassRoomPath() + item?.FileName + "." + item?.FileFormat)
                        .setWifiRequired(true)
                        .setAutoRetryTimes(1)
                        .setListener(object : FileDownloadListener() {
                            override fun pending(task: BaseDownloadTask, soFarBytes: Int, totalBytes: Int) {
                                action_open.visibility = View.GONE
                                circle_pb?.visibility = View.VISIBLE
                            }

                            override fun connected(task: BaseDownloadTask?, etag: String?, isContinue: Boolean, soFarBytes: Int, totalBytes: Int) {
                                action_open.visibility = View.GONE
                                circle_pb?.visibility = View.VISIBLE
                            }

                            override fun progress(task: BaseDownloadTask, soFarBytes: Int, totalBytes: Int) {
                                val percent = soFarBytes / totalBytes.toFloat()
                                val progress: Int = (percent * 100).toInt()
                                circle_pb?.progress = progress
                                logD(ClassRoomDetailSourceAdapter::class.java, progress.toString())
                            }

                            override fun blockComplete(task: BaseDownloadTask?) {}

                            override fun retry(task: BaseDownloadTask?, ex: Throwable?, retryingTimes: Int, soFarBytes: Int) {
                                action_open.visibility = View.GONE
                                circle_pb?.visibility = View.VISIBLE
                            }

                            override fun completed(task: BaseDownloadTask) {
                                circle_pb?.visibility = View.GONE
                                action_open.visibility = View.VISIBLE
                                action_open.imageResource = R.drawable.studyfile_open
                                AndroidFileUtil.openFile(mContext, task.targetFilePath)
                            }

                            override fun paused(task: BaseDownloadTask, soFarBytes: Int, totalBytes: Int) {}

                            override fun error(task: BaseDownloadTask, e: Throwable) {
                                getApp().toast("素材下载失败，请重试！")
                                action_open.visibility = View.VISIBLE
                                circle_pb?.visibility = View.GONE
                                try {
                                    File(task.targetFilePath).delete()
                                } catch (e: Exception) {
                                    logD(ClassRoomDetailSourceAdapter::class.java, e.message)
                                }
                            }

                            override fun warn(task: BaseDownloadTask) {}

                        }).start()
            }
        }
    }

    private fun openFile(helper: ImageView?, file: File?) {
        helper?.imageResource = R.drawable.studyfile_open
        helper?.onClick {
            if (FileUtils.isFileExists(file))
                AndroidFileUtil.openFile(mContext, file!!.absolutePath)
            else
                mContext?.toast(mContext?.getString(R.string.file_not_exist) ?: "文件不存在")
        }


    }

}
