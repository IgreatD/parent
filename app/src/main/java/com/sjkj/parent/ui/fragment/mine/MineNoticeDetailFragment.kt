package com.sjkj.parent.ui.fragment.mine

import android.os.Bundle
import android.view.View
import android.widget.TextView
import com.sjkj.parent.R
import com.sjkj.parent.common.Common
import com.sjkj.parent.ui.fragment.BackBaseFragment

/**
 * @author by dingl on 2017/10/17.
 * @desc MineNoticeDetailFragment
 */
class MineNoticeDetailFragment : BackBaseFragment() {
    
    override fun initView(view: View?) {
        notice_content = view?.findViewById(R.id.mine_content)
        mTopBar = view?.findViewById(R.id.topbar)
        initContent()
    }

    override fun getLayoutId(): Int = R.layout.fragment_notice_detail

    fun newInstance(noteContent: String?): MineNoticeDetailFragment {
        val fragment = MineNoticeDetailFragment()
        val bundle = Bundle()
        bundle.putString(Common.NOTE_BEAN, noteContent)
        fragment.arguments = bundle
        return fragment
    }

    private var notice_content: TextView? = null

    private fun initContent() {
        mTopBar?.setTitle(R.string.mine_content)
        notice_content?.text = arguments.getString(Common.NOTE_BEAN)
    }
}
