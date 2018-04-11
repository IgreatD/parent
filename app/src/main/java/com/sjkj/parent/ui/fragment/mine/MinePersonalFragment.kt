package com.sjkj.parent.ui.fragment.mine

import android.app.Activity
import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.FrameLayout
import com.sjkj.parent.R
import com.sjkj.parent.common.Common
import com.sjkj.parent.ui.activity.LoginActivity
import com.sjkj.parent.ui.controller.mine.MinePersonalController
import com.sjkj.parent.ui.fragment.BackBaseFragment
import com.sjkj.parent.utils.GlideEngine
import com.sjkj.parent.utils.getApp
import com.zhihu.matisse.Matisse
import com.zhihu.matisse.MimeType
import org.jetbrains.anko.sdk25.coroutines.onClick
import org.jetbrains.anko.support.v4.act


/**
 * @author by dingl on 2017/9/15.
 * @desc MinePersonalFragment
 */
class MinePersonalFragment : BackBaseFragment() {
    override fun initView(view: View?) {
        mTopBar = view?.findViewById(R.id.topbar)
        mFlContainer = view?.findViewById(R.id.fl_container)
        mMine_exit = view?.findViewById(R.id.mine_exit)
        initData()
        eixtLogin()
    }

    override fun getLayoutId(): Int = R.layout.fragment_mine_personal
    private var mFlContainer: FrameLayout? = null
    private var mMine_exit: Button? = null

    private fun eixtLogin() {

        mMine_exit?.onClick {
            getApp().removeAllFragment()
            activity?.finish()
            val intent = Intent(act, LoginActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        }

    }

    private lateinit var minePersonalController: MinePersonalController

    override fun onEnterAnimationEnd(savedInstanceState: Bundle?) {
        super.onEnterAnimationEnd(savedInstanceState)
        minePersonalController = MinePersonalController(context)
        mFlContainer?.addView(minePersonalController)

        minePersonalController.setMinePersonalControlListener(object : MinePersonalController.MinePersonalControlListener {
            override fun startFragment(fragment: BackBaseFragment) {
                this@MinePersonalFragment.start(fragment)
            }

            override fun modifyHead() {
                Matisse.from(this@MinePersonalFragment)
                        .choose(MimeType.allOf())
                        .countable(true)
                        .maxSelectable(1)
                        .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED)
                        .thumbnailScale(0.85f)
                        .imageEngine(GlideEngine())
                        .forResult(Common.REQUEST_CODE_CHOOSE)
            }

        })

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == Common.REQUEST_CODE_CHOOSE && resultCode == Activity.RESULT_OK) {
            minePersonalController.initHead(Matisse.obtainResult(data)[0])
        }
    }

    private fun initData() {

        mTopBar?.setTitle(getString(R.string._mine))

    }
}
