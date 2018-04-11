package com.sjkj.parent.utils

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.graphics.Point
import android.graphics.Rect
import android.net.ConnectivityManager
import android.net.Uri
import android.os.Environment
import android.util.Log
import android.view.WindowManager
import android.widget.ImageView
import com.blankj.utilcode.util.SPUtils
import com.blankj.utilcode.util.TimeUtils
import com.bumptech.glide.load.resource.bitmap.BitmapTransitionOptions
import com.google.gson.Gson
import com.qmuiteam.qmui.util.QMUIViewHelper
import com.sjkj.parent.BuildConfig
import com.sjkj.parent.ParentApp
import com.sjkj.parent.api.ParentApi
import com.sjkj.parent.common.Common
import com.sjkj.parent.common.GlideApp
import okhttp3.MediaType
import okhttp3.RequestBody
import java.text.DecimalFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*


/**
 * @author by dingl on 2017/9/11.
 * @desc 扩展的工具
 */

/**
 * 获取接口
 */
fun getApi() = ParentApp.parentApi

/**
 * 获取application的实例
 */
fun getApp() = ParentApp.instance

/**
 * 创建一个requestBody,用于请求数据
 */
fun <T> create(t: T): RequestBody {
    logD(ParentApi::class.java, Gson().toJson(t))
    return RequestBody.create(MediaType.parse(Common.CONTENT_TYPE), Gson().toJson(t))
}

fun <T> logD(clazz: Class<T>, message: String?) {
    if (BuildConfig.DEBUG)
        Log.d(clazz.simpleName, message)
}

fun <T> logE(clazz: Class<T>, message: String?) {
    if (BuildConfig.DEBUG)
        Log.e(clazz.simpleName, message)
}

/**
 * 拨打电话
 */
fun Activity.call(phone: String) {
    this.startActivity(Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phone)).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK))
}

/**
 * 打开浏览器
 */
fun Activity.openBrower(http: String) {
    try {
        val uri = Uri.parse(http)
        val intent = Intent()
        intent.action = Intent.ACTION_VIEW
        intent.data = uri
        startActivity(intent)
    } catch (e: Exception) {
        e.printStackTrace()
    }
}

/**
 * 获取app版本号
 */
fun getVersionCode(): String {
    val packageManager = getApp().packageManager
    val packageInfo: PackageInfo
    var versionCode = "V "
    try {
        packageInfo = packageManager.getPackageInfo(getApp().packageName, 0)
        versionCode += packageInfo.versionCode.toString()
    } catch (e: PackageManager.NameNotFoundException) {
        e.printStackTrace()
    }

    return versionCode
}

/**
 * 判断是否有网络
 */
fun hasNetwork(): Boolean {
    val con = getApp().getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val workinfo = con.activeNetworkInfo
    return !(workinfo == null || !workinfo.isAvailable || !workinfo.isConnected)
}

/**
 * 获取app的存储路径
 */
fun getPath(): String {
    return Environment.getExternalStorageDirectory().absolutePath + "/sjkj/"
}

fun getFilePath() = Environment.getExternalStorageDirectory().absolutePath + "/sjkj/chat/file/"

/**
 * 获取课堂素材存储路径
 */
fun getClassRoomPath(): String {
    return Environment.getExternalStorageDirectory().absolutePath + "/sjkj/课堂/"
}

/**
 * 获取app音频存储路径
 */
fun getAudioPath() = Environment.getExternalStorageDirectory().absolutePath + "/sjkj/voice/"

/**
 * 时间转换
 */
fun convertTime(time: Int): String {
    if (time <= 0) {
        return "暂无"
    }
    val Minute = time / 60
    return when {
        Minute == 0 -> time.toString() + "秒"
        Minute < 60 -> Minute.toString() + "分" + time % 60 + "秒"
        else -> (Minute / 60).toString() + "时" + Minute % 60 + "分" + time % 60 + "秒"
    }
}

/**
 * 获取屏幕尺寸
 */
fun getScreenSize(): Point {
    val windowManager = getApp().getSystemService(Context.WINDOW_SERVICE) as WindowManager
    val point = Point()
    windowManager.defaultDisplay?.getSize(point)
    return point
}

/**
 * 获取当前的activity
 */
@SuppressLint("PrivateApi")
fun getCurrentActivity(): Activity? {

    try {
        val activityThreadClass = Class.forName("android.app.ActivityThread")
        val activityThread = activityThreadClass.getMethod("currentActivityThread").invoke(null)
        val activitiesField = activityThreadClass.getDeclaredField("mActivities")
        activitiesField.isAccessible = true
        val activities = activitiesField.get(activityThread) as Map<*, *>
        for (activityRecord in activities.values) {
            val activityRecordClass = activityRecord!!::class.java
            val pausedField = activityRecordClass.getDeclaredField("paused")
            pausedField.isAccessible = true
            if (!pausedField.getBoolean(activityRecord)) {
                val activityField = activityRecordClass.getDeclaredField("activity")
                activityField.isAccessible = true
                return activityField.get(activityRecord) as Activity
            }
        }
    } catch (e: Exception) {
        e.printStackTrace()
    }

    return null
}

/**
 * 获取键盘的高度
 */
fun getKeyBoardHeight(activity: Activity?): Int {
    val r = Rect()

    val activityRoot = QMUIViewHelper.getActivityRoot(activity)

    activityRoot.getWindowVisibleDisplayFrame(r)

    val height = activityRoot.rootView.height - r.bottom

    if (height > 0)
        SPUtils.getInstance().put(Common.KEYBOARD_HEIGHT, height)

    return height
}

/**
 * 获取文件的大小
 */
fun getPrintSize(size: Long): String {
    var s: Long
    if (size < 1024) {
        return size.toString() + "B"
    } else {
        s = size / 1024
    }
    if (s < 1024) {
        return s.toString() + "KB"
    } else {
        s /= 1024
    }
    return if (s < 1024) {
        s *= 100
        (s / 100).toString() + "." + (s % 100).toString() + "MB"
    } else {
        s = s * 100 / 1024
        (s / 100).toString() + "." + (s % 100).toString() + "GB"
    }
}

/**
 * 获取当前日期
 */
fun getMonthToday(): String = TimeUtils.getNowString(SimpleDateFormat("yyyy-MM-dd", Locale.SIMPLIFIED_CHINESE))

fun getDayToday(): String = TimeUtils.getNowString(SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.SIMPLIFIED_CHINESE))

/**
 * 获取当前月的第一天
 */
fun getMonthStartDate(): String {
    val c = Calendar.getInstance()
    val now = Date()
    c.time = Date()
    val year = c.get(Calendar.YEAR)
    return try {
        val start = SimpleDateFormat("yyyy-MM-dd", Locale.SIMPLIFIED_CHINESE).parse(year.toString() + "-8-31")
        if (start.before(now)) {
            year.toString() + "-9-1"
        } else {
            (year - 1).toString() + "-9-1"
        }
    } catch (e: ParseException) {
        ""
    }

}

fun getFloat(num: Float): String {
    if (num == 0f) {
        return "0.0"
    }
    val decimalFormat = DecimalFormat(".0")
    return decimalFormat.format(num.toDouble())
}

/**
 * GlideApp的封装
 */
fun loadImg(context: Context, url: String?, view: ImageView?) {
    GlideApp.with(context)
            .asBitmap()
            .load(url)
            .thumbnail(0.8f)
            .transition(BitmapTransitionOptions.withCrossFade())
            .into(view)
}

