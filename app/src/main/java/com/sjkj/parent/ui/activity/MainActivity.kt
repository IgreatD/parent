package com.sjkj.parent.ui.activity

import android.os.Bundle
import com.liulishuo.filedownloader.FileDownloadMonitor
import com.liulishuo.filedownloader.FileDownloader
import com.sjkj.parent.R
import com.sjkj.parent.ui.fragment.main.MainFragment
import com.sjkj.parent.utils.downloadutils.GlobalMonitor

/**
 * @author by dingl on 2017/10/11.
 * @desc MainActivity
 */
class MainActivity : BaseFragmentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        FileDownloadMonitor.setGlobalMonitor(GlobalMonitor.getImp)

        if (findFragment(MainFragment::class.java) == null) {
            loadRootFragment(R.id.fl_container, MainFragment())
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        FileDownloader.getImpl().pauseAll()
        FileDownloader.getImpl().unBindServiceIfIdle()
        FileDownloadMonitor.releaseGlobalMonitor()
    }
}
