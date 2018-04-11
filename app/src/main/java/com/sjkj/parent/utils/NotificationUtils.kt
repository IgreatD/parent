package com.sjkj.parent.utils

import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.support.v4.app.NotificationCompat
import com.sjkj.parent.R


/**
 * @author dingl  on  2017/6/23
 */

object NotificationUtils {

    private var title: String? = null

    fun notify(context: Context, title: String, text: String, intent: Intent?, onGoing: Boolean) {
        val res = context.resources
        val picture = BitmapFactory.decodeResource(res, R.mipmap.logo)
        val builder = NotificationCompat.Builder(context)
                .setDefaults(Notification.DEFAULT_ALL)
                .setContentTitle(title)
                .setContentText(text)
                .setSound(null)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setSmallIcon(R.mipmap.logo)
                .setLargeIcon(picture)
                .setAutoCancel(true)
                .setOngoing(onGoing)

        if (intent != null) {
            builder.setContentIntent(
                    PendingIntent.getActivity(
                            context,
                            0,
                            intent,
                            PendingIntent.FLAG_UPDATE_CURRENT))
        }
        NotificationUtils.title = title
        notify(context, builder.build())
    }

    fun notify(context: Context, text: String, intent: Intent?, onGoing: Boolean) {
        val res = context.resources
        val picture = BitmapFactory.decodeResource(res, R.mipmap.logo)
        val title = res.getString(R.string.app_name)
        val builder = NotificationCompat.Builder(context)
                .setDefaults(Notification.DEFAULT_ALL)
                .setContentTitle(title)
                .setContentText(text)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setSmallIcon(R.mipmap.logo)
                .setLargeIcon(picture)
                .setAutoCancel(true)
                .setOngoing(onGoing)

        if (intent != null) {
            builder.setContentIntent(
                    PendingIntent.getActivity(
                            context,
                            0,
                            intent,
                            PendingIntent.FLAG_UPDATE_CURRENT))
        }

        notify(context, builder.build())
    }

    private fun notify(context: Context, notification: Notification) {
        val nm = context
                .getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        nm.notify(title, 0, notification)
    }

    fun cancel(context: Context, title: String?) {
        val nm = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        nm.cancel(title, 0)
    }
}
