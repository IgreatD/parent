package com.sjkj.parent.ui.fragment.assign

import android.os.Bundle
import android.support.v4.view.PagerAdapter
import android.support.v4.view.ViewPager
import android.util.SparseArray
import android.view.View
import android.view.ViewGroup
import com.qmuiteam.qmui.util.QMUIDisplayHelper
import com.qmuiteam.qmui.widget.QMUITabSegment
import com.sjkj.parent.R
import com.sjkj.parent.data.server.CourseInfoListBean
import com.sjkj.parent.ui.controller.assign.AssignNormalController
import com.sjkj.parent.ui.fragment.BackBaseFragment
import com.sjkj.parent.utils.getCourseInfoList
import org.jetbrains.anko.sdk25.coroutines.onClick

/**
 * @author by dingl on 2017/9/21.
 * @desc AssignWrongFragment
 */
class AssignNormalFragment : BackBaseFragment() {

    override fun initView(view: View?) {
        mContentViewPager = view?.findViewById(R.id.home_vp)
        mTabSegment = view?.findViewById(R.id.home_tabs)
        mTopBar = view?.findViewById(R.id.topbar)
        initData()

    }

    override fun getLayoutId(): Int = R.layout.fragment_home

    private var courseInfoID: Int = 0

    private var courseInfoList: List<CourseInfoListBean>? = null

    private val mPageMap = SparseArray<View>()

    private var mContentViewPager: ViewPager? = null

    private var mTabSegment: QMUITabSegment? = null

    private fun initData() {
        mTopBar?.setTitle(getString(R.string.assign_hw))
        mTopBar?.addRightTextButton(R.string.preview, R.id.topbar_right_about_button)
                ?.onClick {
                    val view = mPageMap.get(courseInfoID)
                    if (view != null)
                        (view as AssignNormalController).preview()
                }
        courseInfoList = getCourseInfoList()
        courseInfoList?.forEach { bean ->
            with(bean) {
                mTabSegment?.addTab(QMUITabSegment.Tab(Name))
            }
        }
    }

    override fun onEnterAnimationEnd(savedInstanceState: Bundle?) {
        super.onEnterAnimationEnd(savedInstanceState)
        initPage()
        initTabs()
    }

    private fun initPage() {
        mContentViewPager?.adapter = mPagerAdapter
        mContentViewPager?.setCurrentItem(0, false)
        mContentViewPager?.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {
            }

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
            }

            override fun onPageSelected(position: Int) {
                courseInfoID = courseInfoList?.get(position)?.CourseInfoID ?: 0
                val view = mPageMap.get(courseInfoID)
                if (view != null)
                    (view as AssignNormalController).setCourseInfoID(courseInfoID)

            }
        })
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
            val view = getPageView(courseInfoID, position)
            val params = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
            container.addView(view, params)
            return view
        }

        override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
            container.removeView(`object` as View)
        }
    }

    private fun getPageView(courseInfoID: Int, position: Int): View {
        var view = mPageMap[courseInfoID]
        if (view == null) {
            view = AssignNormalController(context)
            if (position == 0) {
                this.courseInfoID = courseInfoID
                view.setCourseInfoID(courseInfoID)
            }
            view.setHomeControlListener(object : AssignNormalController.HomeWorkControlListener {
                override fun startFragment(fragment: BackBaseFragment) {
                    startWithPop(fragment)
                }
            })
            mPageMap.put(courseInfoID, view)
        }
        return view
    }

}
