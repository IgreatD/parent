package com.sjkj.parent.ui.view

import android.content.Context
import com.github.mikephil.charting.components.MarkerView
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.utils.MPPointF
import kotlinx.android.synthetic.main.radar_markerview.view.*

/**
 * @author by dingl on 2017/12/6.
 * @desc ReaderMarkView
 */
class ReaderMarkView(context: Context, layout: Int) : MarkerView(context, layout) {

    constructor(context: Context) : this(context, 0)

    override fun refreshContent(e: Entry?, highlight: Highlight?) {
        tvContent.text = e?.y?.toString()

        super.refreshContent(e, highlight)
    }

    override fun getOffset(): MPPointF {
        return MPPointF((-(width / 2)).toFloat(), (-height - 10).toFloat())
    }
    
}

