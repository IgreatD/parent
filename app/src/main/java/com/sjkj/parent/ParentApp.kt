package com.sjkj.parent

import android.app.Activity
import android.support.multidex.MultiDexApplication
import android.support.v4.app.Fragment
import com.blankj.utilcode.util.Utils
import com.liulishuo.filedownloader.FileDownloader
import com.liulishuo.filedownloader.connection.FileDownloadUrlConnection
import com.sjkj.parent.api.ParentApi
import com.sjkj.parent.api.RetrofitInit
import me.yokeyword.fragmentation.Fragmentation
import org.litepal.LitePal
import java.net.Proxy

/**
 * @author by dingl on 2017/9/12.
 * *
 * @desc ParentApp
 */

class ParentApp : MultiDexApplication() {

    init {
        instance = this
        parentApi = RetrofitInit().create()
        fragmentList = ArrayList()
        activityList = ArrayList()
    }

    override fun onCreate() {
        super.onCreate()
        Utils.init(this)
        LitePal.initialize(this)

        Fragmentation.builder()
                .stackViewMode(Fragmentation.NONE)
                .debug(true)
                .handleException {}
                .install()

        FileDownloader.setupOnApplicationOnCreate(this)
                .connectionCreator(FileDownloadUrlConnection.Creator(FileDownloadUrlConnection.Configuration()
                        .connectTimeout(15000) // set connection timeout.
                        .readTimeout(15000) // set read timeout.
                        .proxy(Proxy.NO_PROXY) // set proxy
                ))
                .commit()
    }

    companion object {
        lateinit var instance: ParentApp
        lateinit var parentApi: ParentApi
        lateinit var fragmentList: MutableList<Fragment>
        lateinit var activityList: MutableList<Activity>
    }

    fun addFragment(fragment: Fragment) {
        if (fragment !in fragmentList) {
            fragmentList.add(fragment)
        }
    }

    fun removeFragment(fragment: Fragment) {
        fragmentList.remove(fragment)
    }

    fun removeAllFragment() {
        fragmentList.clear()
    }


    fun addActivity(activity: Activity) {
        if (activity !in activityList)
            activityList.add(activity)
    }

    fun removeAllActivity() {
        activityList.forEach {
            if (!it.isFinishing)
                it.finish()
        }
        activityList.clear()
    }

}
