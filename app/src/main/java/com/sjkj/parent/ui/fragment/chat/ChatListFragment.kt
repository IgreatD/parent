package com.sjkj.parent.ui.fragment.chat

import android.support.v4.view.PagerAdapter
import android.support.v4.view.ViewPager
import android.util.SparseArray
import android.view.View
import android.view.ViewGroup
import com.qmuiteam.qmui.widget.QMUITabSegment
import com.qmuiteam.qmui.widget.QMUITopBar
import com.sjkj.parent.R
import com.sjkj.parent.common.Which
import com.sjkj.parent.data.server.MessageEvent
import com.sjkj.parent.ui.controller.mine.MineTeacherController
import com.sjkj.parent.ui.fragment.BaseFragment

/**
 * @author by dingl on 2017/9/18.
 * @desc ChatListFragment
 */
class ChatListFragment : BaseFragment() {
    override fun initView(view: View?) {
        mContentViewPager = view?.findViewById(R.id.home_vp)
        mTabSegment = view?.findViewById(R.id.home_tabs)
        mTopBar = view?.findViewById(R.id.topbar)
        initData()
        initPage()
        initTabs()
    }

    override fun getLayoutId(): Int = R.layout.fragment_home

    private var tabList: Array<String>? = null

    private val mPageMap = SparseArray<View>()

    private var mContentViewPager: ViewPager? = null

    private var mTabSegment: QMUITabSegment? = null

    private var mTopBar: QMUITopBar? = null

    private fun initData() {
        mTopBar?.setTitle(getString(R.string.im))
        tabList = resources.getStringArray(R.array.im_tab)
        tabList?.forEach {
            mTabSegment?.addTab(QMUITabSegment.Tab(it))
        }
    }

    private fun initPage() {
        mContentViewPager?.adapter = mPagerAdapter
        mContentViewPager?.setCurrentItem(0, false)

    }

    private fun initTabs() {
        mTabSegment?.setHasIndicator(true)
        mTabSegment?.mode = QMUITabSegment.MODE_FIXED
        mTabSegment?.setupWithViewPager(mContentViewPager, false)

    }

    private val mPagerAdapter = object : PagerAdapter() {
        override fun isViewFromObject(view: View, `object`: Any): Boolean {
            return view === `object`
        }

        override fun getCount(): Int {
            return tabList?.size ?: 0
        }

        override fun instantiateItem(container: ViewGroup, position: Int): Any {
            val view = getPageView(position)
            val params = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
            container.addView(view, params)
            return view
        }

        override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
            container.removeView(`object` as View)
        }
    }

    private fun getPageView(position: Int): View {
        var view = mPageMap[position]
        if (view == null) {
            view = MineTeacherController(context)
            view.loadData(position)
            mPageMap.put(position, view)
        }
        return view
    }

    override fun toBus(event: MessageEvent<*>?) {
        if (event?.which == Which.NEW_MSG) {
            (mPageMap[0] as MineTeacherController).setNewData()
            (mPageMap[1] as MineTeacherController).loadData(1)
        }
    }

}
