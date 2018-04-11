package com.sjkj.parent.ui.fragment.classroom

import android.os.Bundle
import android.os.SystemClock
import android.support.v4.view.PagerAdapter
import android.support.v4.view.ViewPager
import android.util.SparseArray
import android.view.View
import android.view.ViewGroup
import com.liulishuo.filedownloader.FileDownloader
import com.qmuiteam.qmui.widget.QMUITabSegment
import com.sjkj.parent.R
import com.sjkj.parent.common.Common
import com.sjkj.parent.ui.controller.classroom.ClassRoomDetailCheckController
import com.sjkj.parent.ui.controller.classroom.ClassRoomDetailSourceController
import com.sjkj.parent.ui.fragment.BackBaseFragment
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread

/**
 * @author by dingl on 2017/9/15.
 * @desc ClassRoomDetailFragment
 */
class ClassRoomDetailFragment : BackBaseFragment() {

    override fun initView(view: View?) {
        mClassRoomTeachingID = arguments.getInt(Common.CLASSROOMTEACHINGID)
        mTopBar = view?.findViewById(R.id.topbar)
        mClassRoomViewPager = view?.findViewById(R.id.classroom_vp)
        mClassRoomTab = view?.findViewById(R.id.classroom_tab)
        initTopBar()
    }

    override fun getLayoutId(): Int = R.layout.fragment_classroom_detail

    fun newInstance(classRoomTeachingID: Int): ClassRoomDetailFragment {
        val fragment = ClassRoomDetailFragment()
        val bundle = Bundle()
        bundle.putInt(Common.CLASSROOMTEACHINGID, classRoomTeachingID)
        fragment.arguments = bundle
        return fragment
    }

    private val mPageMap = SparseArray<View>()

    private var mClassRoomViewPager: ViewPager? = null

    private var mClassRoomTab: QMUITabSegment? = null

    private var mTabData: Array<String>? = null

    private var mClassRoomTeachingID: Int = 0

    override fun onEnterAnimationEnd(savedInstanceState: Bundle?) {
        super.onEnterAnimationEnd(savedInstanceState)
        mTabData = resources.getStringArray(R.array.classroom_tab)

        mTabData?.forEach { mClassRoomTab?.addTab(QMUITabSegment.Tab(it)) }

        mClassRoomViewPager?.adapter = mPagerAdapter

        mClassRoomTab?.setupWithViewPager(mClassRoomViewPager, false)
    }

    private fun initTopBar() {

        mTopBar?.setTitle(getString(R.string.classroom_detail))
        mClassRoomViewPager?.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {
            }

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
            }

            override fun onPageSelected(position: Int) {
                if (mPageMap[position] == null)
                    getPageView(position)
                else {
                    doAsync {
                        SystemClock.sleep(200)
                        uiThread {
                            when (position) {
                                0 -> {
                                    (mPageMap[position] as ClassRoomDetailSourceController).setClassRoomTeachingID(mClassRoomTeachingID, 1)
                                }
                                1 -> {
                                    (mPageMap[position] as ClassRoomDetailCheckController).setClassRoomTeachingID(mClassRoomTeachingID, 1)
                                }
                                2 -> {
                                    (mPageMap[position] as ClassRoomDetailSourceController).setClassRoomTeachingID(mClassRoomTeachingID, 2)
                                }
                                3 -> {
                                    (mPageMap[position] as ClassRoomDetailCheckController).setClassRoomTeachingID(mClassRoomTeachingID, 2)
                                }
                            }
                        }
                    }
                }
            }

        })

    }

    private val mPagerAdapter = object : PagerAdapter() {
        private var mChildCount = 0
        override fun isViewFromObject(view: View, `object`: Any): Boolean {
            return view === `object`
        }

        override fun getCount(): Int {
            return mTabData!!.size
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

        override fun getItemPosition(`object`: Any): Int {
            if (mChildCount == 0) {
                return PagerAdapter.POSITION_NONE
            }
            return super.getItemPosition(`object`)
        }

        override fun notifyDataSetChanged() {
            mChildCount = count
            super.notifyDataSetChanged()
        }

        override fun getPageTitle(position: Int) = mTabData?.get(position)
    }

    private fun getPageView(position: Int): View {
        var view = mPageMap.get(position)
        if (view == null) {
            when (position) {
                0 -> {
                    view = ClassRoomDetailSourceController(context)
                    view.setClassRoomTeachingID(mClassRoomTeachingID, 1)
                }
                1 -> {
                    view = ClassRoomDetailCheckController(context)
                    view.setHomeControlListener(object : ClassRoomDetailCheckController.HomeControlListener {
                        override fun startFragment(fragment: BackBaseFragment) {
                            this@ClassRoomDetailFragment.start(fragment)
                        }

                    })
                }
                2 -> {
                    view = ClassRoomDetailSourceController(context)
                }
                3 -> {
                    view = ClassRoomDetailCheckController(context)
                    view.setHomeControlListener(object : ClassRoomDetailCheckController.HomeControlListener {
                        override fun startFragment(fragment: BackBaseFragment) {
                            this@ClassRoomDetailFragment.start(fragment)
                        }

                    })
                }
                else -> view = ClassRoomDetailSourceController(context)
            }

            mPageMap.put(position, view)

        }
        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        FileDownloader.getImpl().pauseAll()
    }
}
