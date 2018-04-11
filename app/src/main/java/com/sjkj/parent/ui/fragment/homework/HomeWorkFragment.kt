package com.sjkj.parent.ui.fragment.homework

import android.support.v4.view.PagerAdapter
import android.support.v4.view.ViewPager
import android.util.SparseArray
import android.view.View
import android.view.ViewGroup
import com.getbase.floatingactionbutton.FloatingActionButton
import com.getbase.floatingactionbutton.FloatingActionsMenu
import com.qmuiteam.qmui.util.QMUIDisplayHelper
import com.qmuiteam.qmui.widget.QMUITabSegment
import com.qmuiteam.qmui.widget.QMUITopBar
import com.qmuiteam.qmui.widget.dialog.QMUIBottomSheet
import com.sjkj.parent.R
import com.sjkj.parent.data.server.CourseInfoListBean
import com.sjkj.parent.ui.controller.homework.HomeWorkController
import com.sjkj.parent.ui.fragment.BackBaseFragment
import com.sjkj.parent.ui.fragment.BaseFragment
import com.sjkj.parent.ui.fragment.assign.AssignNormalFragment
import com.sjkj.parent.ui.fragment.assign.AssignWrongFragment
import com.sjkj.parent.ui.fragment.main.MainFragment
import com.sjkj.parent.ui.view.SwitchMultiButton
import com.sjkj.parent.utils.getCourseInfoList
import org.jetbrains.anko.sdk25.coroutines.onClick


/**
 * @author by dingl on 2017/9/18.
 * @desc HomeWorkFragment
 */
class HomeWorkFragment : BaseFragment() {
    override fun initView(view: View?) {
        mTopBar = view?.findViewById(R.id.topbar)
        mFlMenu = view?.findViewById(R.id.fl_menu)
        mContentViewPager = view?.findViewById(R.id.hw_vp)
        mTabSegment = view?.findViewById(R.id.hw_tabs)
        mSwitchButton = view?.findViewById(R.id.hw_switch_button)
        initTopBar()
        initData()
        initPage()
        initTabs()
        initMenu()
        initListener()
    }

    override fun getLayoutId(): Int = R.layout.fragment_homework

    private var mTopBar: QMUITopBar? = null

    private var mFlMenu: FloatingActionsMenu? = null

    private var mSwitchButton: SwitchMultiButton? = null

    private var courseInfoList: List<CourseInfoListBean>? = null

    private val mPageMap = SparseArray<View>()

    private var mContentViewPager: ViewPager? = null
    private var mTabSegment: QMUITabSegment? = null

    private var homeWorkType: Int = 0

    private var courseInfoID: Int = 0

    private var category: Int = 0

    private fun initListener() {

        mTabSegment?.addOnTabSelectedListener(object : QMUITabSegment.OnTabSelectedListener {
            override fun onDoubleTap(index: Int) {
            }

            override fun onTabReselected(index: Int) {
            }

            override fun onTabUnselected(index: Int) {
            }

            override fun onTabSelected(index: Int) {
                courseInfoID = courseInfoList?.get(index)?.CourseInfoID ?: 0
                if (mPageMap[courseInfoID] == null)
                    getPageView(courseInfoID)
                else
                    (mPageMap[courseInfoID] as HomeWorkController).setCourseInfoID(courseInfoID, homeWorkType, category)
            }

        })

        mSwitchButton?.setOnSwitchListener(object : SwitchMultiButton.OnSwitchListener {
            override fun onSwitch(position: Int, tabText: String) {
                homeWorkType = position
                (mPageMap[courseInfoID] as HomeWorkController).setCourseInfoID(courseInfoID, homeWorkType, category)
            }
        })
    }

    private fun initData() {

        courseInfoList = getCourseInfoList()

        courseInfoList?.forEach { bean ->
            with(bean) {
                mTabSegment?.addTab(QMUITabSegment.Tab(Name))
            }
        }
        courseInfoID = courseInfoList?.get(0)?.CourseInfoID ?: 0
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
            view = HomeWorkController(context)
            view.setCourseInfoID(courseInfoID, homeWorkType, category)
            view.setHomeControlListener(object : HomeWorkController.HomeWorkControlListener {
                override fun startFragment(fragment: BackBaseFragment) {
                    (parentFragment as MainFragment).startBrotherFragment(fragment)
                }

                override fun toggle() {
                    mFlMenu?.collapse()

                }

            })
            mPageMap.put(courseInfoID, view)
        }
        return view
    }

    private fun initMenu() {
        val actionB = FloatingActionButton(context)
        actionB.title = getString(R.string.add_noraml)
        actionB.setColorNormalResId(R.color.app_color_blue_disabled)
        actionB.setColorPressedResId(R.color.app_color_blue_pressed)
        mFlMenu?.addButton(actionB)

        val actionA = FloatingActionButton(context)
        actionA.title = getString(R.string.add_error)
        actionA.setColorNormalResId(R.color.app_color_blue_disabled)
        actionA.setColorPressedResId(R.color.app_color_blue_pressed)
        mFlMenu?.addButton(actionA)

        actionA.onClick {
            mFlMenu?.toggle()
            (parentFragment as MainFragment).startBrotherFragment(AssignWrongFragment())
        }

        actionB.onClick {
            mFlMenu?.toggle()
            (parentFragment as MainFragment).startBrotherFragment(AssignNormalFragment())
        }

    }

    private fun initTopBar() {
        mTopBar?.setTitle(getString(R.string.homework))

        mTopBar?.addRightTextButton(R.string.filter, R.id.topbar_right_about_button)?.onClick {
            showBottomSheetList()
        }

    }

    private fun showBottomSheetList() {

        QMUIBottomSheet.BottomListSheetBuilder(activity)
                .addItem(getString(R.string.homework_category_1))
                .addItem(getString(R.string.homework_category_2))
                .addItem(getString(R.string.homework_category_3))
                .addItem(getString(R.string.homework_category_4))
                .setOnSheetItemClickListener { dialog, _, position, _ ->
                    dialog.dismiss()
                    category = when (position) {
                        0 -> 1
                        1 -> 2
                        2 -> 3
                        3 -> 4
                        else -> 1
                    }
                    (mPageMap[courseInfoID] as HomeWorkController).setCourseInfoID(courseInfoID, homeWorkType, category)
                }
                .build()
                .show()

    }
}
