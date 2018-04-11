package com.sjkj.parent.xmpp

import com.google.gson.Gson
import com.sjkj.parent.common.ChatType
import com.sjkj.parent.common.Which
import com.sjkj.parent.data.server.ChatMessage
import com.sjkj.parent.data.server.MessageEvent
import org.greenrobot.eventbus.EventBus
import org.jivesoftware.smack.Chat
import org.jivesoftware.smack.MessageListener
import org.jivesoftware.smack.packet.Message
import java.text.SimpleDateFormat
import java.util.*

/**
 * @author by dingl on 2017/9/29.
 * @desc XmppChatInComingMessageListener
 */
class XmppChatIncomingMessageListener : MessageListener {

    override fun processMessage(chat: Chat?, message: Message?) {
        if (message == null)
            return
        val push = Gson().fromJson(message.body, ChatMessage::class.java)
        if (push.category == 9) {
            push.type = ChatType.TYPE_RECEIVE
            val date = push.creationDate
            try {
                val format = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.SIMPLIFIED_CHINESE).parse(date)
                push.creationDate = SimpleDateFormat("yyyy-MM-dd E HH:mm:ss", Locale.SIMPLIFIED_CHINESE).format(format)
            } catch(e: Exception) {
            }
            val chatMessageEvent = MessageEvent(Which.CHAT_MSG, push)
            EventBus.getDefault().post(chatMessageEvent)
        }
    }
}
