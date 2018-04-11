package com.sjkj.parent.ui.activity

import android.app.Activity
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.content.pm.ActivityInfo
import android.os.IBinder
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.SimpleItemAnimator
import android.text.TextUtils
import com.google.gson.Gson
import com.qmuiteam.qmui.widget.dialog.QMUITipDialog
import com.sjkj.parent.R
import com.sjkj.parent.common.ChatService
import com.sjkj.parent.common.ChatType
import com.sjkj.parent.common.Common
import com.sjkj.parent.common.Which
import com.sjkj.parent.data.server.ChatFile
import com.sjkj.parent.data.server.ChatMessage
import com.sjkj.parent.data.server.MessageEvent
import com.sjkj.parent.data.server.ChatListBean
import com.sjkj.parent.mvp.contract.UpLoadContract
import com.sjkj.parent.mvp.contract.chat.ChatContract
import com.sjkj.parent.mvp.presenter.UploadPresenter
import com.sjkj.parent.mvp.presenter.chat.ChatPresenter
import com.sjkj.parent.ui.adapter.ChatAdapter
import com.sjkj.parent.ui.fragment.ImageFragment
import com.sjkj.parent.ui.widget.CenterLayoutManager
import com.sjkj.parent.ui.widget.ChatInputFragment
import com.sjkj.parent.utils.*
import com.sjkj.parent.utils.audio.AudioPlayManager
import com.sjkj.parent.utils.downloadutils.TasksManager
import com.sjkj.parent.utils.floder.ui.FilePickerActivity
import com.sjkj.parent.xmpp.XmppChatIncomingMessageListener
import com.sjkj.parent.xmpp.service.XmppService
import com.zhihu.matisse.Matisse
import com.zhihu.matisse.MimeType
import com.zhihu.matisse.internal.entity.CaptureStrategy
import com.zhihu.matisse.internal.utils.PhotoMetadataUtils
import kotlinx.android.synthetic.main.activity_chat.*
import org.greenrobot.eventbus.EventBus
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.sdk25.coroutines.onClick
import org.jetbrains.anko.toast
import org.jivesoftware.smack.Chat
import org.litepal.crud.DataSupport
import java.io.File
import java.lang.ref.WeakReference
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*


/**
 * @author by dingl on 2017/9/27.
 * @desc ChatActivity
 */
class ChatActivity : BaseActivity(), ChatContract.BaseView, ChatInputFragment.OnSendListener, UpLoadContract.BaseView {

    override fun postNotifyDataChanged() {
        runOnUiThread {
            chatAdapter?.notifyDataSetChanged()
        }
    }

    override fun upLoadBefore(fileName: String, type: Int, tag: Long) {
        when (type) {
            ChatType.MSGTYPE_FILE -> {
                val chatFile = ChatFile(fileName.split("/").last())
                chatFile.fileSize = uploadPresenter?.getSize()
                chatFile.filePath = fileName
                chatFile.fileName = fileName.split("/").last()
                val chatMessage = ChatMessage(ChatType.TYPE_SEND, type)
                chatMessage.tag = tag
                chatMessage.userID = getUser().UserID
                chatMessage.msgContent = Gson().toJson(chatFile)
                chatMessage.fromUser = getUserName()
                chatMessage.toUser = mReceiverUserName
                chatMessage.fromName = getUser().Name
                val time = SimpleDateFormat("yyyy-MM-dd E HH:mm:ss", Locale.SIMPLIFIED_CHINESE).format(Date())
                chatMessage.creationDate = time
                try {
                    chatAdapter?.addData(chatAdapter?.itemCount ?: 0, chatMessage)
                    chat_rv.smoothScrollToPosition(chatAdapter?.itemCount ?: 0)
                } catch (e: Exception) {
                    e.printStackTrace()
                    toast(R.string.send_failed)
                    var position = -1
                    chatAdapter?.data?.filterIndexed { index, message ->
                        position = index
                        message.tag == tag
                    }
                    if (position >= 0) {

                        chatAdapter?.data?.removeAt(position)
                        chatAdapter?.notifyItemRemoved(position)
                    }
                }
            }
        }
    }

    override fun onUpLoadProgress(currentBytesCount: Long, totalBytesCount: Long, tag: Long) {
        chatAdapter?.data?.forEachIndexed { index, it ->
            if (it.tag == tag) {
                chatAdapter?.data?.get(index)?.progress = ((currentBytesCount * 100 / totalBytesCount)).toInt()
                chatAdapter?.notifyItemChanged(index)
            }
        }
    }

    override fun sendAudio(file: File, duration: Int) {
        uploadPresenter?.upload(file, ChatType.MSGTYPE_VOICE)
        uploadPresenter?.setDuration(duration)
    }

    private val BRUSH_FILE_NAME: Int = 2

    override fun sendBrush() {
//        startActivityForResult(Intent(this, HandWritingActivity::class.java), BRUSH_FILE_NAME)
    }

    /***
     * 上传成功
     * @fileName 文件名
     */
    override fun upLoadSuccess(fileName: String, type: Int, tag: Long) {
        when (type) {
            ChatType.MSGTYPE_VOICE -> {
                val chatFile = ChatFile(fileName)
                chatFile.duration = uploadPresenter?.getDuration() ?: 0
                sent(Gson().toJson(chatFile), ChatType.MSGTYPE_VOICE)
            }
            ChatType.MSGTYPE_FILE -> {
                val chatFile = ChatFile(fileName.split("/").last())
                chatFile.fileSize = uploadPresenter?.getSize()
                chatFile.filePath = fileName
                chatFile.fileName = fileName.split("/").last()
                mChat?.sendMessage(Gson().toJson(chatAdapter?.data?.first { it.tag == tag }))
                chatAdapter?.data?.forEachIndexed { index, it ->
                    if (it.tag == tag) {
                        chatAdapter?.data?.get(index)?.progress = 100
                        chatAdapter?.notifyItemChanged(index)
                        doAsync {
                            val chatUser = ChatListBean(mReceiverName, mReceiverUserName, it.msgContent, type, it.creationDate, 0)
                            chatUser.saveOrUpdate("UserName = \"" + mReceiverUserName + "\"")
                        }
                    }
                }
            }
            ChatType.MSGTYPE_IMG -> sent(fileName, ChatType.MSGTYPE_IMG)
        }
    }

    override fun upLoadError(tag: Long) {
        toast(R.string.send_failed)
        var position = -1
        chatAdapter?.data?.filterIndexed { index, message ->
            position = index
            message.tag == tag
        }
        if (position >= 0) {

            chatAdapter?.data?.removeAt(position)
            chatAdapter?.notifyItemRemoved(position)
        }
    }

    /**
     * 选择图片
     */
    override fun sendImg() {
        Matisse.from(this)
                .choose(MimeType.allOf())
                .capture(true)
                .captureStrategy(CaptureStrategy(true, getString(R.string.filepath)))
                .countable(true)
                .maxSelectable(1)
                .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED)
                .thumbnailScale(0.85f)
                .imageEngine(GlideEngine())
                .forResult(Common.REQUEST_CODE_CHOOSE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == Common.REQUEST_CODE_CHOOSE && resultCode == Activity.RESULT_OK) {
            val uri = Matisse.obtainResult(data)[0]
            uploadPresenter?.upload(File(PhotoMetadataUtils.getPath(contentResolver, uri)), ChatType.MSGTYPE_IMG)
        } else if (requestCode == BRUSH_FILE_NAME && resultCode == Activity.RESULT_OK) {
            val fileName = data?.getStringExtra(Common.FILE_NAME)
            if (TextUtils.isEmpty(fileName)) {
                toast(R.string.img_save_error)
            } else
                upLoadSuccess(fileName ?: "", ChatType.MSGTYPE_IMG, System.currentTimeMillis())
        } else if (requestCode == Common.REQUEST_FILE && resultCode == Activity.RESULT_OK) {
            if (data == null)
                return
            val path = data.getStringExtra(FilePickerActivity.RESULT_FILE_PATH)
            val size = data.getLongExtra("size", 0)
            val printSize = getPrintSize(size)
            uploadPresenter?.setSize(printSize)
            uploadPresenter?.upload(File(path), ChatType.MSGTYPE_FILE)
        }
    }

    override fun scrollToBottom() {
        chat_rv.smoothScrollToPosition(chatAdapter?.itemCount ?: 0)
    }

    /**
     * 发送消息
     */
    override fun sent(msg: String?, msgType: Int) {
        val chatMessage = ChatMessage(ChatType.TYPE_SEND, msgType)
        chatMessage.userID = getUser().UserID
        chatMessage.msgContent = msg
        chatMessage.fromUser = getUserName()
        chatMessage.toUser = mReceiverUserName
        chatMessage.fromName = getUser().Name
        val time = SimpleDateFormat("yyyy-MM-dd E HH:mm:ss", Locale.SIMPLIFIED_CHINESE).format(Date())
        chatMessage.creationDate = time
        try {
            mChat?.sendMessage(Gson().toJson(chatMessage))
            chatAdapter?.addData(chatAdapter?.itemCount ?: 0, chatMessage)
            chat_rv.smoothScrollToPosition(chatAdapter?.itemCount ?: 0)
            doAsync {
                val chatUser = ChatListBean(mReceiverName, mReceiverUserName, msg, chatMessage.msgType, chatMessage.creationDate, 0)
                chatUser.saveOrUpdate("UserName = \"" + mReceiverUserName + "\"")
            }
        } catch (e: Exception) {
            e.printStackTrace()
            toast(R.string.send_failed)
        }

    }

    override fun showLoading() {

    }

    override fun hideLoading() {
        loadingDialog?.dismiss()
    }

    override fun showLoadError() {
        chatAdapter?.isUpFetching = false
    }

    override fun showLoadNetWorkError() {
        chatAdapter?.isUpFetching = false
    }

    override fun hideAll() {
    }

    override fun showEmptyView() {
    }

    override fun setNewData(t: List<ChatMessage>) {
        t.map {
            with(it) {
                type = if (mReceiverUserName == fromUser)
                    ChatType.TYPE_RECEIVE
                else
                    ChatType.TYPE_SEND
                val date = creationDate
                try {
                    val format = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.SIMPLIFIED_CHINESE).parse(date)
                    creationDate = SimpleDateFormat("yyyy-MM-dd E HH:mm:ss", Locale.SIMPLIFIED_CHINESE).format(format)
                } catch (ex: ParseException) {
                    logE(ChatActivity::class.java, ex.message)
                }

            }
        }
        chatAdapter?.setNewData(t)
        chat_rv.smoothScrollToPosition(chatAdapter?.itemCount ?: 0)
        isFirst = false
    }

    override fun setMoreData(t: List<ChatMessage>) {
        t.map {
            with(it) {
                type = if (mReceiverUserName == fromUser)
                    ChatType.TYPE_RECEIVE
                else
                    ChatType.TYPE_SEND
                val date = creationDate
                try {
                    val format = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.SIMPLIFIED_CHINESE).parse(date)
                    creationDate = SimpleDateFormat("yyyy-MM-dd E HH:mm:ss", Locale.SIMPLIFIED_CHINESE).format(format)
                } catch (ex: ParseException) {
                }
            }
        }
        chatAdapter?.addData(0, t)
        chatAdapter?.isUpFetching = false
    }

    override fun loadError() {
        chatAdapter?.isUpFetchEnable = false
        chatAdapter?.isUpFetching = false
    }

    override fun loadEnd() {
        chatAdapter?.isUpFetchEnable = false
        chatAdapter?.isUpFetching = false
    }

    override fun loadComplete() {
        chatAdapter?.isUpFetching = false
    }

    override fun showToast(toast: String) {
        toast(toast)
    }

    override fun onResume() {
        super.onResume()
        initXmpp()
    }

    override fun initView() {
        initData()
        initTopBar()
        initInput()
        initRv()
        loadData()
        initListener()
        uploadPresenter = UploadPresenter(this)
    }

    private fun initListener() {

        chatAdapter?.setUpFetchListener {
            chatAdapter?.isUpFetching = true
            chatPresenter?.getMoreData(getUserName(), mReceiverUserName!!)
        }

        chat_rv?.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (dy < -30) {
                    chatInputFragment?.closeInput()
                }
            }
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        try {
            TasksManager.getImp.onDestroy()
            unbindService(serviceConnection)
            AudioPlayManager.getInstance().stopPlay()
        } catch (e: Exception) {
            logE(ChatActivity::class.java, e.message)
        }
    }

    private val layoutManager = CenterLayoutManager(this, LinearLayoutManager.VERTICAL, false)

    private fun initRv() {
        chat_rv.layoutManager = layoutManager
        chatAdapter = ChatAdapter(mReceiverName, getUser().Name, {
            start(ImageFragment().newInstance(it.split(",")))
        })
        chat_rv.adapter = chatAdapter
        chatAdapter?.isUpFetchEnable = true
    }

    private fun loadData() {
        chatPresenter = ChatPresenter(this)
        chatPresenter?.getData(getUserName(), mReceiverUserName!!)
    }

    override fun getLayoutId(): Int = R.layout.activity_chat
    private var chatPresenter: ChatPresenter? = null
    private var uploadPresenter: UploadPresenter? = null
    private var chatAdapter: ChatAdapter? = null
    private var mReceiverUserName: String? = null
    private var mReceiverName: String? = null
    private var xmppService: XmppService? = null
    private var mChat: Chat? = null
    private var loadingDialog: QMUITipDialog? = null
    private var chatInputFragment: ChatInputFragment? = null
    private var isFirst = true

    private fun initInput() {
        val transaction = supportFragmentManager.beginTransaction()
        chatInputFragment = ChatInputFragment()
        chatInputFragment?.bindToContent(chat_rv)
        transaction.replace(R.id.fl_container, chatInputFragment)
                .addToBackStack(null)
                .commit()
        chatInputFragment?.setOnSendListener(this)
        TasksManager.getImp.onCreate(WeakReference(this))
    }

    private fun initData() {
        mReceiverUserName = intent.getStringExtra(Common.USER_NAME)
        mReceiverName = intent.getStringExtra(Common.NAME)
        (chat_rv.itemAnimator as SimpleItemAnimator).supportsChangeAnimations = false
    }

    private fun initTopBar() {
        topbar.setTitle(mReceiverName)
        topbar.addLeftBackImageButton().onClick { finish() }
        doAsync {
            val bean = DataSupport.select("*").where("userName = \"" + mReceiverUserName + "\"").find(ChatListBean::class.java).first()
            bean.msgNum = 0
            bean.saveOrUpdate("userName = \"" + mReceiverUserName + "\"")
        }
    }

    private fun initXmpp() {

        bindXmpp()

    }

    private val serviceConnection = object : ServiceConnection {
        override fun onServiceDisconnected(p0: ComponentName?) {
        }

        override fun onServiceConnected(p0: ComponentName?, p1: IBinder?) {
            xmppService = (p1 as XmppService.XmppBinder).getService()
            if (xmppService?.isConnection() == false)
                xmppService?.login()
            else
                connectChat()
        }

    }

    private fun connectChat() {

        if (mChat == null) {
            val chatManager = xmppService?.xmppTool?.config?.chatManager
            mChat = chatManager?.createChat(String.format("$mReceiverUserName@${xmppService?.xmppTool?.config?.serviceName}"), XmppChatIncomingMessageListener())
        }

    }

    private fun bindXmpp() {

        bindService(Intent(this, XmppService::class.java), serviceConnection, Context.BIND_AUTO_CREATE)

    }

    override fun toBus(event: MessageEvent<*>?) {
        when (event?.which) {
            Which.CONNECT_STATE -> {
                val connected: Int = event.data as Int
                if (connected == ChatService.CONNECTED) {
                    if (xmppService != null)
                        connectChat()
                }
            }
            Which.CHAT_MSG -> {
                val data = event.data as ChatMessage
                if (data.fromUser.equals(mReceiverUserName, true)) {
                    data.type = ChatType.TYPE_RECEIVE
                    chatAdapter?.addData(chatAdapter?.itemCount ?: 0, data)
                    chat_rv.smoothScrollToPosition(chatAdapter?.itemCount ?: 0)
                } else {
                    val chatUserList = DataSupport.select("*").where("UserName = \"" + data.fromUser + "\"").find(ChatListBean::class.java)
                    if (chatUserList != null && chatUserList.size > 0) {
                        val chatUser = chatUserList[0]
                        chatUser.UserName = data.fromUser
                        chatUser.creationDate = data.creationDate
                        chatUser.MsgContent = data.msgContent
                        chatUser.MsgType = data.msgType
                        chatUser.msgNum = chatUser.msgNum + 1
                        chatUser.Name = data.fromName
                        chatUser.saveOrUpdate("UserName = \"" + data.fromUser + "\"")
                    } else {
                        val chatUser = ChatListBean(data.fromName, data.fromUser, data.msgContent, data.msgType, data.creationDate, 1)
                        chatUser.saveOrUpdate("UserName = \"" + data.fromUser + "\"")
                    }
                    EventBus.getDefault().post(MessageEvent(Which.NEW_MSG, null))
                }
            }
        }
    }

    override fun onBackPressedSupport() {
        super.onBackPressedSupport()
        val event = MessageEvent(Which.NEW_MSG, null)
        EventBus.getDefault().post(event)
    }

}
