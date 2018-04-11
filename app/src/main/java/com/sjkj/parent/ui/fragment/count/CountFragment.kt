package com.sjkj.parent.ui.fragment.count

import android.view.View
import com.qmuiteam.qmui.widget.QMUITopBar
import com.qmuiteam.qmui.widget.grouplist.QMUICommonListItemView
import com.qmuiteam.qmui.widget.grouplist.QMUIGroupListView
import com.sjkj.parent.R
import com.sjkj.parent.ui.fragment.BaseFragment
import com.sjkj.parent.ui.fragment.main.MainFragment

/**
 * @author by dingl on 2017/9/18.
 * @desc CountFragment
 */
class CountFragment : BaseFragment() {
    override fun initView(view: View?) {
        mTopBar = view?.findViewById(R.id.topbar)
        mCountGruopListView = view?.findViewById(R.id.count_group)
        initTopBar()
        initGroup()
    }

    override fun getLayoutId(): Int = R.layout.fragment_count

    private var mTopBar: QMUITopBar? = null

    private var mCountGruopListView: QMUIGroupListView? = null

    private fun initGroup() {

        val itemSubjectHand = mCountGruopListView?.createItemView(getString(R.string.count_subject_hand))
        itemSubjectHand?.accessoryType = QMUICommonListItemView.ACCESSORY_TYPE_CHEVRON

        val itemSubjectRight = mCountGruopListView?.createItemView(getString(R.string.count_subject_right))
        itemSubjectRight?.accessoryType = QMUICommonListItemView.ACCESSORY_TYPE_CHEVRON

        val itemHwRight = mCountGruopListView?.createItemView(getString(R.string.count_hw_right))
        itemHwRight?.accessoryType = QMUICommonListItemView.ACCESSORY_TYPE_CHEVRON

        val itemKnowRight = mCountGruopListView?.createItemView(getString(R.string.count_know_right))
        itemKnowRight?.accessoryType = QMUICommonListItemView.ACCESSORY_TYPE_CHEVRON

        QMUIGroupListView.newSection(context)
                .addItemView(itemSubjectHand, { (parentFragment as MainFragment).startBrotherFragment(CountSubjectHandFragment()) })
                .addItemView(itemSubjectRight, { (parentFragment as MainFragment).startBrotherFragment(CountSubjectRightFragment()) })
                .addItemView(itemHwRight, { (parentFragment as MainFragment).startBrotherFragment(CountHwRightFragment()) })
                .addItemView(itemKnowRight, { (parentFragment as MainFragment).startBrotherFragment(CountKnowRightFragment()) })
                .addTo(mCountGruopListView)

    }

    private fun initTopBar() {

        mTopBar?.setTitle(getString(R.string.count))

    }
}
