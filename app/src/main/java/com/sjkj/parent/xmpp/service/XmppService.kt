package com.sjkj.parent.xmpp.service

import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import android.text.TextUtils
import com.blankj.utilcode.util.NetworkUtils
import com.google.gson.Gson
import com.sjkj.parent.R
import com.sjkj.parent.common.ChatService
import com.sjkj.parent.common.ChatType
import com.sjkj.parent.common.Common
import com.sjkj.parent.common.Which
import com.sjkj.parent.data.server.ChatFile
import com.sjkj.parent.data.server.ChatListBean
import com.sjkj.parent.data.server.ChatMessage
import com.sjkj.parent.data.server.MessageEvent
import com.sjkj.parent.ui.activity.ChatActivity
import com.sjkj.parent.utils.NotificationUtils
import com.sjkj.parent.utils.getCurrentActivity
import com.sjkj.parent.utils.getUserName
import com.sjkj.parent.utils.logE
import com.sjkj.parent.xmpp.tool.XmppTool
import org.greenrobot.eventbus.EventBus
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.runOnUiThread
import org.jivesoftware.smack.packet.Message
import org.litepal.crud.DataSupport


/**
 * @author by dingl on 2017/9/27.
 * @desc XmppService
 */
open class XmppService : Service() {

    private var mConnectedState = ChatService.DISCONNECTED

    open val mBinder: Binder = XmppBinder()

    inner class XmppBinder : Binder() {
        fun getService(): XmppService {
            if (isConnection()) {
                this@XmppService.postConnectedSuccessful()
            } else
                this@XmppService.postConnectedError()
            return this@XmppService
        }
    }

    var xmppTool: XmppTool? = null

    override fun onBind(p0: Intent?): IBinder {
        return mBinder
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        login()

        return START_STICKY
    }

    fun login() {
        val userName = getUserName()

        if (userName.isEmpty()) {
            return
        }
        if (!NetworkUtils.isConnected()) {
            postConnectedError()
            return
        }

        if (mConnectedState == ChatService.CONNECTED) {
            postConnectedSuccessful()
            return
        }

        if (mConnectedState == ChatService.CONNECTING) {

            postConnectIng()
            return
        }

        if (isConnection()) {
            postConnectedSuccessful()
            return
        }

        doAsync {
            postConnectIng()
            xmppTool = XmppTool.create(this@XmppService)
            val isLogin = xmppTool?.login(userName) ?: false
            if (isLogin)
                postConnectedSuccessful()
            else
                postConnectedError()
        }

    }

    fun isConnection(): Boolean {

        return xmppTool?.isAuthenticated() ?: false

    }

    fun postConnectedSuccessful() {
        mConnectedState = ChatService.CONNECTED

        runOnUiThread {
            EventBus.getDefault().post(MessageEvent(Which.CONNECT_STATE, mConnectedState))
        }
    }

    fun postConnectedError() {
        mConnectedState = ChatService.DISCONNECTED

        runOnUiThread {
            EventBus.getDefault().post(MessageEvent(Which.CONNECT_STATE, mConnectedState))
        }
    }

    fun postConnectIng() {
        mConnectedState = ChatService.CONNECTING

        runOnUiThread {
            EventBus.getDefault().post(MessageEvent(Which.CONNECT_STATE, mConnectedState))
        }
    }

    fun handData(message: Message?) {
        runOnUiThread {
            if (message?.body?.isEmpty() != false)
                return@runOnUiThread
            val push = Gson().fromJson<ChatMessage>(message.body, ChatMessage::class.java)
            when (push.category) {
                9 -> {
                    val activity = getCurrentActivity()
                    if (activity !is ChatActivity) {
                        val intent = Intent(applicationContext, ChatActivity::class.java)
                        intent.putExtra(Common.NAME, push.fromName)
                        intent.putExtra(Common.USER_NAME, push.fromUser)
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        var content = push.msgContent
                        if (!TextUtils.isEmpty(content)) {
                            try {
                                when (push.msgType) {
                                    ChatType.MSGTYPE_IMG -> content = "{图片}  " + push.msgContent?.split("/")?.get(3)
                                    ChatType.MSGTYPE_FILE -> {
                                        val chatFile = Gson().fromJson<ChatFile>(push.msgContent, ChatFile::class.java)
                                        content = "{文件}  " + chatFile.fileName
                                    }
                                    ChatType.MSGTYPE_VOICE -> {
                                        val chatVoice = Gson().fromJson<ChatFile>(push.msgContent, ChatFile::class.java)
                                        content = "{语音}  " + chatVoice.duration
                                    }
                                    ChatType.MSGTYPE_MICRO -> {
                                        val chatMicro = Gson().fromJson<ChatFile>(push.msgContent, ChatFile::class.java)
                                        content = "{微课}  " + chatMicro.fileName
                                    }
                                }
                            } catch (e: Exception) {
                                logE(XmppService::class.java, e.message)
                            }

                        }
                        val chatUserList = DataSupport.select("*").where("UserName = \"" + push.fromUser + "\"").find(ChatListBean::class.java)
                        if (chatUserList != null && chatUserList.size > 0) {
                            val chatUser = chatUserList[0]
                            chatUser.UserName = push.fromUser
                            chatUser.creationDate = push.creationDate
                            chatUser.MsgContent = push.msgContent
                            chatUser.MsgType = push.msgType
                            chatUser.msgNum = chatUser.msgNum + 1
                            chatUser.Name = push.fromName
                            chatUser.saveOrUpdate("UserName = \"" + push.fromUser + "\"")
                        } else {
                            val chatUser = ChatListBean(push.fromName, push.fromUser, push.msgContent, push.msgType, push.creationDate, 1)
                            chatUser.saveOrUpdate("UserName = \"" + push.fromUser + "\"")
                        }
                        val event = MessageEvent<ChatMessage>(Which.NEW_MSG)
                        EventBus.getDefault().post(event)
                        NotificationUtils.notify(applicationContext, push.fromName ?: getString(R.string.app_name), content ?: "", intent, false)
                    }
                }
            }
        }

    }
}
