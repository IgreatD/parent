package com.sjkj.parent.ui.activity

import android.os.Bundle
import android.support.v4.view.PagerAdapter
import android.util.SparseArray
import android.view.View
import android.view.ViewGroup
import com.sjkj.parent.R
import com.sjkj.parent.ui.controller.base.ImageController
import com.sjkj.parent.ui.fragment.ImageFragment
import kotlinx.android.synthetic.main.fragment_image.*
import org.jetbrains.anko.sdk25.coroutines.onClick

/**
 * @author by dingl on 2017/9/30.
 * @desc ImageActivity
 */
class ImageActivity : BaseActivity() {

    override fun initView() {
        initTopBar()
        loadData()
    }

    override fun getLayoutId(): Int = R.layout.fragment_image

    private var mImgList: List<String> = ArrayList()

    private val mPageMap = SparseArray<View>()

    private fun initTopBar() {

        topbar?.setTitle(getString(R.string.image_look))

        topbar?.addLeftBackImageButton()?.onClick { finish() }

    }

    fun loadData() {
        mImgList = intent.getStringExtra("it").split(",")
        image_vp?.adapter = mPagerAdapter
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
            view = ImageController(this)
            view.loadImage(mImgList[position])
            mPageMap.put(position, view)

        }
        return view
    }

}
