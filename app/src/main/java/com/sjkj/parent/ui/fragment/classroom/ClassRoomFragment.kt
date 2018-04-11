package com.sjkj.parent.ui.fragment.classroom

import android.support.v4.view.PagerAdapter
import android.support.v4.view.ViewPager
import android.util.SparseArray
import android.view.View
import android.view.ViewGroup
import com.qmuiteam.qmui.util.QMUIDisplayHelper
import com.qmuiteam.qmui.widget.QMUITabSegment
import com.qmuiteam.qmui.widget.QMUITopBar
import com.sjkj.parent.R
import com.sjkj.parent.data.server.CourseInfoListBean
import com.sjkj.parent.ui.controller.classroom.ClassRoomController
import com.sjkj.parent.ui.fragment.BackBaseFragment
import com.sjkj.parent.ui.fragment.BaseFragment
import com.sjkj.parent.ui.fragment.main.MainFragment
import com.sjkj.parent.utils.getCourseInfoList

/**
 * @author by dingl on 2017/9/14.
 * @desc ClassRoomFragment
 */
class ClassRoomFragment : BaseFragment() {
    override fun initView(view: View?) {
        mContentViewPager = view?.findViewById(R.id.home_vp)
        mTabSegment = view?.findViewById(R.id.home_tabs)
        mTopBar = view?.findViewById(R.id.topbar)
        initData()
        initPage()
        initTabs()
    }

    override fun getLayoutId(): Int = R.layout.fragment_home
    private var courseInfoList: List<CourseInfoListBean>? = null

    private val mPageMap = SparseArray<View>()

    private var mContentViewPager: ViewPager? = null
    private var mTabSegment: QMUITabSegment? = null
    private var mTopBar: QMUITopBar? = null

    private fun initData() {
        mTopBar?.setTitle(getString(R.string.classroom))
        courseInfoList = getCourseInfoList()
        courseInfoList?.forEach { bean ->
            with(bean) {
                mTabSegment?.addTab(QMUITabSegment.Tab(Name))
            }
        }
    }

    private fun initPage() {
        mContentViewPager?.adapter = mPagerAdapter
        mContentViewPager?.setCurrentItem(0, false)

    }

    private fun initTabs() {
        val space = QMUIDisplayHelper.dp2px(context, 25)
        mTabSegment?.setHasIndicator(true)
        mTabSegment?.mode = QMUITabSegment.MODE_SCROLLABLE
        mTabSegment?.setItemSpaceInScrollMode(space)
        mTabSegment?.setupWithViewPager(mContentViewPager, false)
        mTabSegment?.setPadding(space, 0, space, 0)

    }

    private val mPagerAdapter = object : PagerAdapter() {
        override fun isViewFromObject(view: View, `object`: Any): Boolean {
            return view === `object`
        }

        override fun getCount(): Int {
            return courseInfoList?.size ?: 0
        }

        override fun instantiateItem(container: ViewGroup, position: Int): Any {
            val courseInfoID = courseInfoList?.get(position)?.CourseInfoID ?: 0
            val view = getPageView(courseInfoID)
            val params = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
            container.addView(view, params)
            return view
        }

        override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
            container.removeView(`object` as View)
        }
    }

    private fun getPageView(courseInfoID: Int): View {
        var view = mPageMap[courseInfoID]
        if (view == null) {
            view = ClassRoomController(context)
            view.setCourseInfoID(courseInfoID)
            view.setHomeControlListener(object : ClassRoomController.HomeControlListener {
                override fun startFragment(fragment: BackBaseFragment) {
                    (parentFragment as MainFragment).startBrotherFragment(fragment)
                }

            })
            mPageMap.put(courseInfoID, view)
        }
        return view
    }

}
