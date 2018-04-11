package com.sjkj.parent.utils

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Parcelable
import java.io.File
import java.util.*

object AndroidFileUtil {

    fun openFile(filePath: String): Intent? {

        val file = File(filePath)
        if (!file.exists())
            return null
        val end = file.name.substring(file.name.lastIndexOf(".") + 1, file.name.length)
                .toLowerCase()

        if (end == "m4a" || end == "mp3" || end == "mid" || end == "xmf" || end == "ogg"
                || end == "wav") {
            return getAudioFileIntent(filePath)
        } else if (end == "3gp" || end == "mp4" || end == "wmv") {
            return getVideoFileIntent(filePath, end)
        } else if (end == "jpg" || end == "gif" || end == "png" || end == "jpeg"
                || end == "bmp") {
            return getImageFileIntent(filePath)
        } else if (end == "apk") {
            return getApkFileIntent(filePath)
        } else if (end == "ppt") {
            return getPptFileIntent(filePath)
        } else if (end == "xls") {
            return getExcelFileIntent(filePath)
        } else if (end == "doc" || end == "docx" || end == "docm") {
            return getWordFileIntent(filePath)
        } else if (end == "pdf") {
            return getPdfFileIntent(filePath)
        } else if (end == "chm") {
            return getChmFileIntent(filePath)
        } else if (end == "txt") {
            return getTextFileIntent(filePath)

        } else if (end == "flv") {
            return getVideoFileIntent(filePath, end)
        } else {
            return getAllIntent(filePath)
        }
    }

    fun openFile(context: Context, filePath: String) {
        val file = File(filePath)
        if (!file.exists())
            return
        val end = file.name.substring(file.name.lastIndexOf(".") + 1, file.name.length)
                .toLowerCase()
        val type = getMIMEType(filePath)
        val intent = getMyIntent(filePath, type)
        if (end == "m4a" || end == "mp3" || end == "mid" || end == "xmf" || end == "ogg"
                || end == "wav") {
            startActivity(context, "me.abitno.vplayer.t", intent, filePath, type)
        } else if (end == "3gp" || end == "mp4" || end == "wmv" || end == "flv"
                || end == "avi") {
            startActivity(context, "me.abitno.vplayer.t", intent, filePath, type)
        } else if (end == "apk") {
            context.startActivity(intent)
        } else if (end == "swf") {
            startActivity(context, "air.gamerush.flash.filemanager", intent, filePath, type)
        } else if (end == "txt" || end == "pdf" || end == "doc" || end == "docx"
                || end == "ppt" || end == "xls" || end == "xlsx" || end == "pptx") {
            startActivity(context, "cn.wps.moffice_eng", intent, filePath, type)
        } else {
            context.startActivity(intent)
        }
    }

    private fun startActivity(context: Context, pakage: String, intent: Intent, filePath: String, type: String) {
        val resInfo = context.packageManager.queryIntentActivities(intent, 0)
        if (!resInfo.isEmpty()) {
            val targetedShareIntents = ArrayList<Intent>()
            for (info in resInfo) {
                val targeted = Intent(Intent.ACTION_VIEW)
                targeted.setDataAndType(Uri.fromFile(File(filePath)), type)
                val activityInfo = info.activityInfo
                // judgments : activityInfo.packageName, activityInfo.name, etc.
                if (activityInfo.packageName.contains("com.tencent.mobileqq")) {
                    continue
                }
                if (activityInfo.packageName.contains(pakage)) {
                    targeted.`package` = activityInfo.packageName
                    targetedShareIntents.add(targeted)
                }
            }
            if (targetedShareIntents.size != 0) {
                val chooserIntent = Intent.createChooser(targetedShareIntents.removeAt(0), "Select app to Open")
                chooserIntent!!.putExtra(Intent.EXTRA_INITIAL_INTENTS, targetedShareIntents.toTypedArray<Parcelable>())
                try {
                    context.startActivity(chooserIntent)
                } catch (ex: Exception) {
                    context.startActivity(intent)
                }

            } else {
                // IToast.showToast("没有可打开该文件的程序");
                context.startActivity(intent)
            }
        }
    }

    fun getMyIntent(param: String, type: String): Intent {

        // Intent intent = new Intent("android.intent.action.VIEW");
        // intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        // intent.putExtra("oneshot", 0);
        // intent.putExtra("configchange", 0);
        val uri = Uri.fromFile(File(param))
        val intent = Intent(Intent.ACTION_VIEW)
        intent.setDataAndType(uri, type)
        return intent
    }

    // Android��ȡһ�����ڴ�APK�ļ���intent
    fun getAllIntent(param: String): Intent {

        val intent = Intent()
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        intent.action = Intent.ACTION_VIEW
        val uri = Uri.fromFile(File(param))
        intent.setDataAndType(uri, "*/*")
        return intent
    }

    /**
     * 根据文件后缀名获得对应的MIME类型。

     * @param file
     */
    private fun getMIMEType(fName: String): String {

        var type = "*/*"
        //获取后缀名前的分隔符"."在fName中的位置。
        val dotIndex = fName.lastIndexOf(".")
        if (dotIndex < 0) {
            return type
        }
        /* 获取文件的后缀名*/
        val end = fName.substring(dotIndex, fName.length).toLowerCase()
        if (end === "") return type
        //在MIME和文件类型的匹配表中找到对应的MIME类型。
        for (i in MIME_MapTable.indices) { //MIME_MapTable??在这里你一定有疑问，这个MIME_MapTable是什么？
            if (end == MIME_MapTable[i][0])
                type = MIME_MapTable[i][1]
        }
        return type
    }

    private val MIME_MapTable = arrayOf(
            //{后缀名，MIME类型}
            arrayOf(".3gp", "video/3gpp"), arrayOf(".apk", "application/vnd.android.package-archive"), arrayOf(".asf", "video/x-ms-asf"), arrayOf(".avi", "video/x-msvideo"), arrayOf(".bin", "application/octet-stream"), arrayOf(".bmp", "image/bmp"), arrayOf(".c", "text/plain"), arrayOf(".class", "application/octet-stream"), arrayOf(".conf", "text/plain"), arrayOf(".cpp", "text/plain"), arrayOf(".doc", "application/msword"), arrayOf(".docx", "application/vnd.openxmlformats-officedocument.wordprocessingml.document"), arrayOf(".xls", "application/vnd.ms-excel"), arrayOf(".xlsx", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"), arrayOf(".exe", "application/octet-stream"), arrayOf(".gif", "image/gif"), arrayOf(".gtar", "application/x-gtar"), arrayOf(".gz", "application/x-gzip"), arrayOf(".h", "text/plain"), arrayOf(".htm", "text/html"), arrayOf(".html", "text/html"), arrayOf(".jar", "application/java-archive"), arrayOf(".java", "text/plain"), arrayOf(".jpeg", "image/jpeg"), arrayOf(".jpg", "image/jpeg"), arrayOf(".js", "application/x-javascript"), arrayOf(".log", "text/plain"), arrayOf(".m3u", "audio/x-mpegurl"), arrayOf(".m4a", "audio/mp4a-latm"), arrayOf(".m4b", "audio/mp4a-latm"), arrayOf(".m4p", "audio/mp4a-latm"), arrayOf(".m4u", "video/vnd.mpegurl"), arrayOf(".m4v", "video/x-m4v"), arrayOf(".mov", "video/quicktime"), arrayOf(".mp2", "audio/x-mpeg"), arrayOf(".mp3", "audio/x-mpeg"), arrayOf(".mp4", "video/mp4"), arrayOf(".mpc", "application/vnd.mpohun.certificate"), arrayOf(".mpe", "video/mpeg"), arrayOf(".mpeg", "video/mpeg"), arrayOf(".mpg", "video/mpeg"), arrayOf(".mpg4", "video/mp4"), arrayOf(".mpga", "audio/mpeg"), arrayOf(".msg", "application/vnd.ms-outlook"), arrayOf(".ogg", "audio/ogg"), arrayOf(".pdf", "application/pdf"), arrayOf(".png", "image/png"), arrayOf(".pps", "application/vnd.ms-powerpoint"), arrayOf(".ppt", "application/vnd.ms-powerpoint"), arrayOf(".pptx", "application/vnd.openxmlformats-officedocument.presentationml.presentation"), arrayOf(".prop", "text/plain"), arrayOf(".rc", "text/plain"), arrayOf(".rmvb", "audio/x-pn-realaudio"), arrayOf(".rtf", "application/rtf"), arrayOf(".sh", "text/plain"), arrayOf(".tar", "application/x-tar"), arrayOf(".tgz", "application/x-compressed"), arrayOf(".txt", "text/plain"), arrayOf(".wav", "audio/x-wav"), arrayOf(".wma", "audio/x-ms-wma"), arrayOf(".wmv", "audio/x-ms-wmv"), arrayOf(".wps", "application/vnd.ms-works"), arrayOf(".xml", "text/plain"), arrayOf(".z", "application/x-compress"), arrayOf(".zip", "application/x-zip-compressed"), arrayOf("", "*/*"))

    // Android��ȡһ�����ڴ�Swf�ļ���intent
    private fun getFlashFileIntent(param: String): Intent {
        val intent = Intent()
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        intent.action = Intent.ACTION_VIEW
        val uri = Uri.fromFile(File(param))
        intent.setDataAndType(uri, "application/x-shockwave-flash")
        return intent
    }

    // Android��ȡһ�����ڴ�APK�ļ���intent
    fun getApkFileIntent(param: String): Intent {

        val intent = Intent()
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        intent.action = Intent.ACTION_VIEW
        val uri = Uri.fromFile(File(param))
        intent.setDataAndType(uri, "application/vnd.android.package-archive")
        return intent
    }

    // Android��ȡһ�����ڴ�VIDEO�ļ���intent
    fun getVideoFileIntent(param: String, type: String): Intent {

        // Intent intent = new Intent("android.intent.action.VIEW");
        // intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        // intent.putExtra("oneshot", 0);
        // intent.putExtra("configchange", 0);
        val uri = Uri.fromFile(File(param))
        val intent = Intent(Intent.ACTION_VIEW)
        intent.setDataAndType(uri, "video/" + type)
        return intent
    }

    // Android��ȡһ�����ڴ�FLV�ļ���intent
    fun getFlvFileIntent(param: String): Intent {

        // Intent intent = new Intent("android.intent.action.VIEW");
        // intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        // intent.putExtra("oneshot", 0);
        // intent.putExtra("configchange", 0);
        // Uri uri = Uri.fromFile(new File(param));
        // intent.setDataAndType(uri, "application/octet-stream");

        val intent = Intent(Intent.ACTION_VIEW)
        intent.setDataAndType(Uri.fromFile(File(param)), "video/flv")
        return intent
    }

    // Android��ȡһ�����ڴ�AUDIO�ļ���intent
    fun getAudioFileIntent(param: String): Intent {

        val intent = Intent("android.intent.action.VIEW")
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        intent.putExtra("oneshot", 0)
        intent.putExtra("configchange", 0)
        val uri = Uri.fromFile(File(param))
        intent.setDataAndType(uri, "audio/*")
        return intent
    }

    // Android��ȡһ�����ڴ�Html�ļ���intent
    fun getHtmlFileIntent(param: String): Intent {

        val uri = Uri.parse(param).buildUpon().encodedAuthority("com.android.htmlfileprovider").scheme("content")
                .encodedPath(param).build()
        val intent = Intent("android.intent.action.VIEW")
        intent.setDataAndType(uri, "text/html")
        return intent
    }

    // Android��ȡһ�����ڴ�ͼƬ�ļ���intent
    fun getImageFileIntent(param: String): Intent {

        val intent = Intent("android.intent.action.VIEW")
        intent.addCategory("android.intent.category.DEFAULT")
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        val uri = Uri.fromFile(File(param))
        intent.setDataAndType(uri, "image/*")
        return intent
    }

    // Android��ȡһ�����ڴ�PPT�ļ���intent
    fun getPptFileIntent(param: String): Intent {

        val intent = Intent("android.intent.action.VIEW")
        intent.addCategory("android.intent.category.DEFAULT")
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        val uri = Uri.fromFile(File(param))
        intent.setDataAndType(uri, "application/vnd.ms-powerpoint")
        return intent
    }

    // Android��ȡһ�����ڴ�Excel�ļ���intent
    fun getExcelFileIntent(param: String): Intent {

        val intent = Intent("android.intent.action.VIEW")
        intent.addCategory("android.intent.category.DEFAULT")
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        val uri = Uri.fromFile(File(param))
        intent.setDataAndType(uri, "application/vnd.ms-excel")
        return intent
    }

    // Android��ȡһ�����ڴ�Word�ļ���intent
    fun getWordFileIntent(param: String): Intent {

        val intent = Intent("android.intent.action.VIEW")
        intent.addCategory("android.intent.category.DEFAULT")
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        val uri = Uri.fromFile(File(param))
        intent.setDataAndType(uri, "application/msword")
        return intent
    }

    // Android��ȡһ�����ڴ�CHM�ļ���intent
    fun getChmFileIntent(param: String): Intent {

        val intent = Intent("android.intent.action.VIEW")
        intent.addCategory("android.intent.category.DEFAULT")
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        val uri = Uri.fromFile(File(param))
        intent.setDataAndType(uri, "application/x-chm")
        return intent
    }

    // ��txt��ʽ�ļ�
    fun getTextFileIntent(param: String): Intent {

        val intent = Intent("android.intent.action.VIEW")
        intent.addCategory("android.intent.category.DEFAULT")
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        intent.`package` = "com.qo.android.am3"
        val uri = Uri.fromFile(File(param))
        intent.setDataAndType(uri, "text/plain")
        return intent
    }

    // Android��ȡһ�����ڴ�PDF�ļ���intent
    fun getPdfFileIntent(param: String): Intent {

        val intent = Intent("android.intent.action.VIEW")
        intent.addCategory("android.intent.category.DEFAULT")
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        val uri = Uri.fromFile(File(param))
        intent.setDataAndType(uri, "application/pdf")
        return intent
    }
}
