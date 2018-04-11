package com.sjkj.parent.ui.adapter

import android.graphics.Bitmap
import android.graphics.drawable.AnimationDrawable
import android.view.View
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import com.blankj.utilcode.util.ScreenUtils
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.chad.library.adapter.base.util.MultiTypeDelegate
import com.daimajia.numberprogressbar.NumberProgressBar
import com.google.gson.Gson
import com.liulishuo.filedownloader.BaseDownloadTask
import com.qmuiteam.qmui.alpha.QMUIAlphaLinearLayout
import com.sjkj.parent.R
import com.sjkj.parent.api.ParentUri
import com.sjkj.parent.common.ChatType
import com.sjkj.parent.common.GlideApp
import com.sjkj.parent.data.server.ChatFile
import com.sjkj.parent.data.server.ChatMessage
import com.sjkj.parent.utils.AndroidFileUtil
import com.sjkj.parent.utils.audio.AudioPlayManager
import com.sjkj.parent.utils.audio.IAudioPlayListener
import com.sjkj.parent.utils.downloadutils.TasksManager
import com.sjkj.parent.utils.getApp
import com.sjkj.parent.utils.getFilePath
import com.sjkj.parent.utils.logE
import org.jetbrains.anko.sdk25.coroutines.onClick
import org.jetbrains.anko.toast
import java.io.File


/**
 * @author by dingl on 2017/9/28.
 * @desc ChatAdapter
 */
class ChatAdapter(private val mReceiverName: String?, private val sendName: String?, private val itemClick: (String) -> Unit)
    : BaseQuickAdapter<ChatMessage, BaseViewHolder>(null) {

    private val SEND_MSG_TXT = 1//文本
    private val SEND_MSG_IMG = 2//图片
    private val SEND_MSG_FILE = 3//文件
    private val SEND_MSG_VOICE = 4//语音
    private val RECEIVE_MSG_TXT = -1
    private val RECEIVE_MSG_IMG = -2
    private val RECEIVE_MSG_FILE = -3
    private val RECEIVE_MSG_VOICE = -4

    private var audio_animator: AnimationDrawable? = null

    init {

        multiTypeDelegate = object : MultiTypeDelegate<ChatMessage>() {
            override fun getItemType(entity: ChatMessage): Int {
                when (entity.type) {
                    ChatType.TYPE_RECEIVE -> {
                        return when (entity.msgType) {
                            ChatType.MSGTYPE_TXT -> RECEIVE_MSG_TXT
                            ChatType.MSGTYPE_FILE -> RECEIVE_MSG_FILE
                            ChatType.MSGTYPE_IMG -> RECEIVE_MSG_IMG
                            ChatType.MSGTYPE_VOICE -> RECEIVE_MSG_VOICE
                            else -> SEND_MSG_TXT
                        }
                    }
                    ChatType.TYPE_SEND -> {
                        return when (entity.msgType) {
                            ChatType.MSGTYPE_TXT -> SEND_MSG_TXT
                            ChatType.MSGTYPE_FILE -> SEND_MSG_FILE
                            ChatType.MSGTYPE_IMG -> SEND_MSG_IMG
                            ChatType.MSGTYPE_VOICE -> SEND_MSG_VOICE
                            else -> SEND_MSG_TXT
                        }
                    }
                    else -> return SEND_MSG_TXT
                }
            }
        }

        multiTypeDelegate
                .registerItemType(SEND_MSG_TXT, R.layout.item_send_txt)
                .registerItemType(SEND_MSG_IMG, R.layout.item_send_img)
                .registerItemType(SEND_MSG_FILE, R.layout.item_send_file)
                .registerItemType(SEND_MSG_VOICE, R.layout.item_send_voice)
                .registerItemType(RECEIVE_MSG_TXT, R.layout.item_receive_txt)
                .registerItemType(RECEIVE_MSG_IMG, R.layout.item_receive_img)
                .registerItemType(RECEIVE_MSG_FILE, R.layout.item_receive_file)
                .registerItemType(RECEIVE_MSG_VOICE, R.layout.item_receive_voice)
    }

    override fun convert(helper: BaseViewHolder?, item: ChatMessage?) {
        when (item?.type) {
            ChatType.TYPE_RECEIVE -> helper?.getView<TextView>(R.id.tv_name)?.text = mReceiverName
            ChatType.TYPE_SEND -> helper?.getView<TextView>(R.id.tv_name)?.text = sendName
        }
        helper?.getView<TextView>(R.id.tv_time)?.text = item?.creationDate
        when (helper?.itemViewType) {
            SEND_MSG_TXT, RECEIVE_MSG_TXT -> bindTxt(helper, item)
            SEND_MSG_IMG, RECEIVE_MSG_IMG -> bindImg(helper, item)
            SEND_MSG_FILE -> bindFile(helper, item)
            SEND_MSG_VOICE -> bindVoice(helper, item)
            RECEIVE_MSG_FILE -> bindFile(helper, item)
            RECEIVE_MSG_VOICE -> bindVoice(helper, item)
        }
    }

    /**
     * 绑定语音消息
     */
    private fun bindVoice(helper: BaseViewHolder?, item: ChatMessage?) {
        try {
            val chatVoice = Gson().fromJson<ChatFile>(item?.msgContent, ChatFile::class.java)
            helper?.getView<TextView>(R.id.tv_content)?.text = String.format("${chatVoice.duration}' '")
            val audio_play = helper?.getView<ImageView>(R.id.play_audio)
            helper?.getView<RelativeLayout>(R.id.audio_play)?.onClick {
                audio_animator?.stop()
                audio_animator?.selectDrawable(0)
                AudioPlayManager.getInstance().startPlay(mContext, chatVoice.getAudio(), object : IAudioPlayListener {
                    override fun onStart(var1: String?) {
                        audio_animator = audio_play?.background as AnimationDrawable?
                        audio_animator?.start()
                    }

                    override fun onStop(var1: String?) {
                        audio_animator?.stop()
                        audio_animator?.selectDrawable(0)
                    }

                    override fun onComplete(var1: String?) {
                        audio_animator?.stop()
                        audio_animator?.selectDrawable(0)
                    }
                })
            }
        } catch (e: Exception) {
            logE(ChatAdapter::class.java, e.message)
        }
    }

    /**
     * 绑定img消息
     */
    private fun bindImg(helper: BaseViewHolder?, item: ChatMessage?) {
        val iv_content = helper?.getView<ImageView>(R.id.iv_content)
        iv_content?.onClick { itemClick(item?.getImg() ?: "") }
        GlideApp.with(mContext)
                .asBitmap()
                .thumbnail(0.5f)
                .placeholder(R.drawable.ic_png_place)
                .error(R.drawable.ic_png_error)
                .load(item?.getImg())
                .into(object : SimpleTarget<Bitmap>() {
                    override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>) {
                        try {
                            var width = resource.width
                            var height = resource.height
                            val params = iv_content?.layoutParams
                            if (width > ScreenUtils.getScreenWidth() / 3) {
                                val per = width.toFloat() / height.toFloat()
                                width = ScreenUtils.getScreenWidth() / 3
                                height = (width / per).toInt()
                            }
                            params?.width = width
                            params?.height = height
                            iv_content?.layoutParams = params
                            iv_content?.setImageBitmap(resource)
                        } catch (e: Exception) {
                        }
                    }

                })
    }

    /**
     * 绑定txt消息
     */
    private fun bindTxt(helper: BaseViewHolder?, item: ChatMessage?) {
        val params = helper?.getView<RelativeLayout>(R.id.rl_content)?.layoutParams
        params?.width = (ScreenUtils.getScreenWidth() / 2)
        helper?.getView<RelativeLayout>(R.id.rl_content)?.layoutParams = params
        helper?.getView<TextView>(R.id.tv_content)?.text = item?.msgContent
    }

    /**
     * 绑定file消息
     */
    private fun bindFile(helper: BaseViewHolder?, item: ChatMessage?) {
        val file_progress = helper?.getView<NumberProgressBar>(R.id.file_progress)
        val ll_chat = helper?.getView<QMUIAlphaLinearLayout>(R.id.ll_chat)
        try {
            val chatFile = Gson().fromJson<ChatFile>(item?.msgContent, ChatFile::class.java)
            helper?.getView<TextView>(R.id.tv_content)?.text = chatFile?.fileName
            helper?.getView<TextView>(R.id.tv_size)?.text = chatFile?.fileSize
            ll_chat?.onClick {
                val file = File(getFilePath(), chatFile.fileName)
                if (file.exists()) run {
                    try {
                        AndroidFileUtil.openFile(mContext, file.absolutePath)
                    } catch (e: Exception) {
                        getApp().toast(R.string.open_failed)
                    }
                } else
                    if (TasksManager.getImp.isReady)
                        if (chatFile?.filePath.isNullOrEmpty().not()) {
                            val taskId = TasksManager.getImp.startTask(ParentUri.BASE_FILE_URL + chatFile?.filePath, getFilePath() + chatFile?.fileName)
                            TasksManager.getImp.addUpdater(object : TasksManager.DownloadStatusUpdater {
                                override fun updateDownloaded(task: BaseDownloadTask?) {
                                    if (taskId == task?.id) {
                                        file_progress?.visibility = View.GONE
                                        AndroidFileUtil.openFile(mContext, task.targetFilePath)
                                    }
                                }

                                override fun updateNotDownloaded(task: BaseDownloadTask?, status: Byte, largeFileSoFarBytes: Long, largeFileTotalBytes: Long) {
                                    if (taskId == task?.id)
                                        file_progress?.visibility = View.GONE
                                }

                                override fun updateStart(task: BaseDownloadTask?) {
                                    if (taskId == task?.id)
                                        file_progress?.visibility = View.VISIBLE
                                }

                                override fun updateDownloading(task: BaseDownloadTask?, status: Byte, soFarBytes: Int, totalBytes: Int) {
                                    if (taskId == task?.id) {
                                        val percent = soFarBytes / totalBytes.toFloat()
                                        val progress = (percent * 100).toInt()
                                        file_progress?.progress = progress
                                    }
                                }

                            })
                        }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        val progress = item?.progress ?: 0
        if (progress in 1..99) {
            file_progress?.visibility = View.VISIBLE
            file_progress?.progress = progress
        } else
            file_progress?.visibility = View.GONE

        val params = ll_chat?.layoutParams
        params?.width = (ScreenUtils.getScreenWidth() / 2)
        ll_chat?.layoutParams = params

    }
}
