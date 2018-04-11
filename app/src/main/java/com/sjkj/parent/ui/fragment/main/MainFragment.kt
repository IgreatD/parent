package com.sjkj.parent.ui.fragment.main

import android.os.Bundle
import android.view.View
import com.sjkj.parent.R
import com.sjkj.parent.common.Which
import com.sjkj.parent.data.server.ChatListBean
import com.sjkj.parent.data.server.MessageEvent
import com.sjkj.parent.mvp.contract.MainContract
import com.sjkj.parent.mvp.presenter.MainPresenter
import com.sjkj.parent.ui.fragment.BaseFragment
import com.sjkj.parent.ui.fragment.chat.ChatListFragment
import com.sjkj.parent.ui.fragment.classroom.ClassRoomFragment
import com.sjkj.parent.ui.fragment.count.CountFragment
import com.sjkj.parent.ui.fragment.homework.HomeWorkFragment
import com.sjkj.parent.ui.fragment.mine.MineFragment
import com.sjkj.parent.ui.view.bottombar.BottomBar
import com.sjkj.parent.ui.view.bottombar.BottomBarTab
import me.yokeyword.fragmentation.ISupportFragment
import me.yokeyword.fragmentation.SupportFragment
import org.jetbrains.anko.support.v4.toast
import org.litepal.crud.DataSupport

/**
 * @author by dingl on 2017/9/15.
 * @desc MainFragment
 */
class MainFragment : BaseFragment(), MainContract.BaseView {

    companion object {
        val FIRST = 0
        val SECOND = 1
        val THIRD = 2
        val FOURTH = 3
        val FIFTH = 4
    }

    private var mMainTabs: BottomBar? = null

    private var mainPresenter: MainPresenter? = null

    private val mFragments = arrayOfNulls<BaseFragment>(5)
    
    override fun getLayoutId(): Int = R.layout.fragment_main

    override fun initView(view: View?) {
        mMainTabs = view?.findViewById(R.id.bottomBar)
        mainPresenter = MainPresenter(this)
        initData()
        initPage()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val firstFragment = findChildFragment(ClassRoomFragment()::class.java)
        if (firstFragment == null) {
            mFragments[FIRST] = ClassRoomFragment()
            mFragments[SECOND] = HomeWorkFragment()
            mFragments[THIRD] = ChatListFragment()
            mFragments[FOURTH] = CountFragment()
            mFragments[FIFTH] = MineFragment()

            loadMultipleRootFragment(R.id.fl_container, FIRST, mFragments[FIRST], mFragments[SECOND], mFragments[THIRD], mFragments[FOURTH], mFragments[FIFTH])

        } else {
            mFragments[FIRST] = firstFragment
            mFragments[SECOND] = findChildFragment(HomeWorkFragment::class.java)
            mFragments[THIRD] = findChildFragment(ChatListFragment::class.java)
            mFragments[FOURTH] = findChildFragment(CountFragment::class.java)
            mFragments[FIFTH] = findChildFragment(MineFragment::class.java)
        }
    }

    override fun setMsgCount(msgCount: Int) {
        if (msgCount == 0)
            mMainTabs?.getItem(4)?.unreadCount = 0
        else
            mMainTabs?.getItem(4)?.unreadCount = msgCount
    }

    private fun initPage() {

        mainPresenter?.getMsgCount()

    }

    private fun initData() {
        mMainTabs?.addItem(BottomBarTab(_mActivity, R.drawable.icon_classsroom, getString(R.string.classroom)))
                ?.addItem(BottomBarTab(_mActivity, R.drawable.icon_homework, getString(R.string.homework)))
                ?.addItem(BottomBarTab(_mActivity, R.drawable.icon_im, getString(R.string.im)))
                ?.addItem(BottomBarTab(_mActivity, R.drawable.icon_count, getString(R.string.count)))
                ?.addItem(BottomBarTab(_mActivity, R.drawable.icon_mine, getString(R.string.mine)))

        mMainTabs?.setOnTabSelectedListener(object : BottomBar.OnTabSelectedListener {
            override fun onTabSelected(position: Int, prePosition: Int) {
                showHideFragment(mFragments[position], mFragments[prePosition])
            }

            override fun onTabUnselected(position: Int) {
            }

            override fun onTabReselected(position: Int) {
            }
        })

        setChatMsgNum()
    }

    private fun setChatMsgNum() {

        val list = DataSupport.select("*").find(ChatListBean::class.java) as List<ChatListBean>
        var msgCount = 0
        list.forEach {
            msgCount += it.msgNum
        }
        if (msgCount == 0)
            mMainTabs?.getItem(2)?.unreadCount = 0
        else
            mMainTabs?.getItem(2)?.unreadCount = msgCount
    }

    override fun toBus(event: MessageEvent<*>?) {
        super.toBus(event)
        when (event?.which) {
            Which.NOTICE_STATE -> mainPresenter?.getMsgCount()
            Which.NEW_MSG -> setChatMsgNum()
        }
    }

    fun newInstance(): ISupportFragment {
        val fragment = MainFragment()
        val bundle = Bundle()
        fragment.arguments = bundle
        return fragment
    }

    fun startBrotherFragment(targetFragment: SupportFragment) {
        start(targetFragment)
    }

    private val WAIT_TIME = 2000L
    private var TOUCH_TIME: Long = 0

    override fun onBackPressedSupport(): Boolean {
        if (System.currentTimeMillis() - TOUCH_TIME < WAIT_TIME) {
            _mActivity.finish()
        } else {
            TOUCH_TIME = System.currentTimeMillis()
            toast(R.string.app_exit_again)
        }
        return true
    }
}
