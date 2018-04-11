package com.sjkj.parent.ui.fragment

import android.os.Bundle
import android.support.v4.view.PagerAdapter
import android.support.v4.view.ViewPager
import android.util.SparseArray
import android.view.View
import android.view.ViewGroup
import com.sjkj.parent.R
import com.sjkj.parent.ui.controller.base.ImageController
import java.util.*

/**
 * @author by dingl on 2017/9/20.
 * @desc ImageFragment
 */
class ImageFragment : BackBaseFragment() {
    override fun initView(view: View?) {
        mTopBar = view?.findViewById(R.id.topbar)
        mViewPager = view?.findViewById(R.id.image_vp)
        initTopBar()
    }

    override fun getLayoutId(): Int = R.layout.fragment_image

    private var mViewPager: ViewPager? = null

    private var mImgList: ArrayList<String> = ArrayList()

    private val mPageMap = SparseArray<View>()

    private fun initTopBar() {

        mTopBar?.setTitle(getString(R.string.image_look))

    }

    override fun onEnterAnimationEnd(savedInstanceState: Bundle?) {
        super.onEnterAnimationEnd(savedInstanceState)
        mImgList = arguments.getStringArrayList("it")
        mViewPager?.adapter = mPagerAdapter

    }

    fun newInstance(it: List<String>?): ImageFragment {
        val fragment = ImageFragment()
        val bundle = Bundle()
        bundle.putStringArrayList("it", it as ArrayList<String>?)
        fragment.arguments = bundle
        return fragment
    }

    private val mPagerAdapter = object : PagerAdapter() {
        override fun isViewFromObject(view: View, `object`: Any): Boolean {
            return view === `object`
        }

        override fun getCount(): Int {
            return mImgList.size
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
        var view = mPageMap.get(position)
        if (view == null) {
            view = ImageController(context)
            view.loadImage(mImgList[position])
            mPageMap.put(position, view)

        }
        return view
    }

}
