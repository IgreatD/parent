package com.sjkj.parent.xmpp.tool

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import com.sjkj.parent.api.ParentUri
import com.sjkj.parent.utils.getApp
import com.sjkj.parent.utils.logD
import com.sjkj.parent.xmpp.service.XmppService
import org.jetbrains.anko.doAsync
import org.jivesoftware.smack.*
import org.jivesoftware.smack.filter.PacketTypeFilter
import org.jivesoftware.smack.packet.IQ
import org.jivesoftware.smack.packet.Message
import org.jivesoftware.smack.provider.ProviderManager
import org.jivesoftware.smackx.ping.packet.Ping
import java.lang.Exception


/**
 * @author by dingl on 2017/9/27.
 * @desc XmppTool
 */
class XmppTool(private val xmppService: XmppService) {

    var config: XMPPConnection? = null

    private var mPongListener: PacketListener? = null// ping pong服务器动态监听

    private var mPingID: String? = null// ping服务器的id

    private var mPingAlarmPendIntent: PendingIntent? = null// 是通过闹钟来控制ping服务器的时间间隔

    private var mPongTimeoutAlarmPendIntent: PendingIntent? = null// 判断服务器连接超时的闹钟

    private val PING_ALARM = "com.sjkj.study.PING_ALARM"// ping服务器闹钟BroadcastReceiver的Action
    private val PONG_TIMEOUT_ALARM = "com.sjkj.study.PONG_TIMEOUT_ALARM"// 判断连接超时的闹钟BroadcastReceiver的Action
    private val mPingAlarmIntent = Intent(PING_ALARM)
    private val mPongTimeoutAlarmIntent = Intent(PONG_TIMEOUT_ALARM)

    private val PACKET_TIMEOUT = 30000

    private val mPongTimeoutAlarmReceiver = PongTimeoutAlarmReceiver()

    private val mPingAlarmReceiver = PingAlarmReceiver()

    companion object xmppTool {
        fun create(xmppService: XmppService): XmppTool = XmppTool(xmppService)
    }

    init {
        XmppConfigure.configure(ProviderManager.getInstance())
        val connConfig = ConnectionConfiguration(ParentUri.XMPP_URL, 5222)
        connConfig.isSASLAuthenticationEnabled = false
        connConfig.isReconnectionAllowed = true
        connConfig.setSendPresence(true)
        connConfig.securityMode = ConnectionConfiguration.SecurityMode.disabled
        connConfig.isCompressionEnabled = false
        XMPPConnection.DEBUG_ENABLED = true
        config = XMPPConnection(connConfig)

    }

    private fun initListener() {

        config?.addConnectionListener(object : ConnectionListener {

            override fun connectionClosed() {
                logD(XmppTool::class.java, "connectionClosed")
                xmppService.postConnectedError()
            }

            override fun connectionClosedOnError(e: Exception?) {
                logD(XmppTool::class.java, "connectionClosedOnError")
                xmppService.postConnectedError()
            }

            override fun reconnectionSuccessful() {
                logD(XmppTool::class.java, "reconnectionSuccessful")
                xmppService.postConnectedSuccessful()
            }

            override fun reconnectionFailed(e: Exception?) {
                logD(XmppTool::class.java, "reconnectionFailed")
                xmppService.postConnectedError()
            }

            override fun reconnectingIn(seconds: Int) {
                logD(XmppTool::class.java, "reconnectingIn")
                xmppService.postConnectIng()
            }
        })

        config?.addPacketListener(PacketListener { packet ->
            if (packet is Message) {
                xmppService.handData(packet)
            }
        }, PacketTypeFilter(Message::class.java))
    }

    fun login(userName: String): Boolean {

        if (config?.isConnected!!)
            config?.disconnect()

        SmackConfiguration.setPacketReplyTimeout(30000)
        SmackConfiguration.setKeepAliveInterval(-1)
        SmackConfiguration.setDefaultPingInterval(0)

        config?.connect()
        initListener()
        if (config?.isAuthenticated == false) {
            config?.login(userName, "123456", "parent")
        }
        registerPongListener()
        return isAuthenticated()
    }

    fun isAuthenticated(): Boolean {
        return config?.isConnected ?: false && config?.isAuthenticated ?: false
    }

    fun logout() {
        doAsync {
            if (mPongListener != null)
                config?.removePacketListener(mPongListener)
            (xmppService.getSystemService(Context.ALARM_SERVICE) as AlarmManager)
                    .cancel(mPingAlarmPendIntent)
            (xmppService.getSystemService(Context.ALARM_SERVICE) as AlarmManager)
                    .cancel(mPongTimeoutAlarmPendIntent)
            xmppService.unregisterReceiver(mPingAlarmReceiver)
            xmppService.unregisterReceiver(mPongTimeoutAlarmReceiver)
            config?.disconnect()
        }
    }

    private fun registerPongListener() {
        mPingID = null
        if (mPongListener != null)
            config?.removePacketListener(mPongListener)

        mPongListener = PacketListener { packet ->
            if (packet == null)
                return@PacketListener

            if (packet.packetID === mPingID) {
                mPingID = null
                (xmppService
                        .getSystemService(Context.ALARM_SERVICE) as AlarmManager)
                        .cancel(mPongTimeoutAlarmPendIntent)
            }
        }

        config?.addPacketListener(mPongListener, PacketTypeFilter(IQ::class.java))

        mPingAlarmPendIntent = PendingIntent.getBroadcast(xmppService.applicationContext, 0, mPingAlarmIntent,
                PendingIntent.FLAG_UPDATE_CURRENT)

        mPongTimeoutAlarmPendIntent = PendingIntent.getBroadcast(
                xmppService.applicationContext, 0, mPongTimeoutAlarmIntent,
                PendingIntent.FLAG_UPDATE_CURRENT)

        xmppService.registerReceiver(mPingAlarmReceiver, IntentFilter(PING_ALARM))

        xmppService.registerReceiver(mPongTimeoutAlarmReceiver, IntentFilter(PONG_TIMEOUT_ALARM))

        (xmppService.getSystemService(Context.ALARM_SERVICE) as AlarmManager)
                .setInexactRepeating(AlarmManager.RTC_WAKEUP,
                        System.currentTimeMillis() + AlarmManager.INTERVAL_FIFTEEN_MINUTES,
                        AlarmManager.INTERVAL_FIFTEEN_MINUTES, mPingAlarmPendIntent)
    }

    private inner class PongTimeoutAlarmReceiver : BroadcastReceiver() {

        override fun onReceive(ctx: Context, i: Intent) {
            xmppService.postConnectedError()
        }
    }

    private inner class PingAlarmReceiver : BroadcastReceiver() {
        override fun onReceive(p0: Context?, p1: Intent?) {
            if (isAuthenticated())
                sendServerPing()
        }
    }

    fun sendServerPing() {
        if (mPingID != null)
            return
        val ping = Ping()
        ping.type = IQ.Type.GET

        mPingID = ping.packetID

        config?.sendPacket(ping)

        (getApp().getSystemService(Context.ALARM_SERVICE) as AlarmManager).set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis()
                + PACKET_TIMEOUT + 3000, mPongTimeoutAlarmPendIntent)
    }

}


