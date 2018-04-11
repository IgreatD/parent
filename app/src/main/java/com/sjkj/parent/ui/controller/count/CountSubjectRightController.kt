package com.sjkj.parent.ui.controller.count

import android.content.Context
import android.graphics.Color
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.data.RadarData
import com.github.mikephil.charting.data.RadarDataSet
import com.github.mikephil.charting.data.RadarEntry
import com.github.mikephil.charting.formatter.IAxisValueFormatter
import com.github.mikephil.charting.interfaces.datasets.IRadarDataSet
import com.sjkj.parent.R
import com.sjkj.parent.data.server.CountInfo
import com.sjkj.parent.mvp.contract.count.CountContract
import com.sjkj.parent.mvp.presenter.count.CountSubjectHandPresenter
import com.sjkj.parent.ui.adapter.count.CountSubjectHandAdapter
import com.sjkj.parent.ui.controller.base.BaseController
import com.sjkj.parent.ui.view.ReaderMarkView
import com.sjkj.parent.ui.view.TimeView
import com.sjkj.parent.utils.getApp
import com.sjkj.parent.utils.getFloat
import com.sjkj.parent.utils.getMonthStartDate
import com.sjkj.parent.utils.getMonthToday
import kotlinx.android.synthetic.main.controller_count_subject_hand.view.*
import java.util.*

/**
 * @author by dingl on 2017/10/19.
 * @desc CountSubjectHandController
 */
class CountSubjectRightController(context: Context) : BaseController(context), CountContract.View {

    private var entries1: ArrayList<RadarEntry> = ArrayList()
    private var entries2: ArrayList<RadarEntry> = ArrayList()
    private var mActivities: ArrayList<String> = ArrayList()
    private var list: ArrayList<List<String>> = ArrayList()
    private var adapter: CountSubjectHandAdapter? = null
    override fun setNewData(t: List<CountInfo>) {
        if (t.isEmpty())
            return
        entries1.clear()
        entries2.clear()
        mActivities.clear()
        list.clear()
        val cmList = ArrayList<String>()
        cmList.add("科目")
        cmList.add("正确率")
        cmList.add("班级均值")
        cmList.add("班级排名")
        cmList.add("年级排名")
        list.add(cmList)

        for (i in t.indices) {
            val mpRecord = t[i]

            val cList = ArrayList<String>()
            cList.add(mpRecord.CourseInfoName ?: "")
            cList.add(getFloat(mpRecord.CompletionRate * 100) + "%")
            cList.add(getFloat(mpRecord.ClassCompletionRate * 100) + "%")
            cList.add(mpRecord.CompletionRateClassOrder.toString())
            cList.add(mpRecord.CompletionRateGradeOrder.toString())
            list.add(cList)

            mActivities.add(mpRecord.CourseInfoName ?: "")
            entries1.add(RadarEntry(mpRecord.CompletionRate * 100))
            entries2.add(RadarEntry(mpRecord.ClassCompletionRate * 100))
        }

        setMemu()
        setData()
        
        adapter?.setNewData(list)
    }

    override fun setMoreData(t: List<CountInfo>) {
    }

    override fun loadError() {
    }

    override fun loadEnd() {
        entries1.clear()
        entries2.clear()
        mActivities.clear()
        list.clear()
        count_subject_rc.clear()
        adapter?.setNewData(null)
    }

    override fun loadComplete() {
    }

    private var mCountSubjectHandPresenter: CountSubjectHandPresenter? = null

    private var mTimeView: TimeView? = null

    init {
        LayoutInflater.from(context).inflate(R.layout.controller_count_subject_hand, this)
        initView()
    }

    private fun initView() {
        mTimeView = TimeView(context)
        count_subject_time_view.addView(mTimeView)
        mTimeView?.setOnTimeCheckListener(object : TimeView.OnTimeCheckListener {
            override fun confrim(startDate: String, endDate: String) {
                mCountSubjectHandPresenter?.getSubjectRightData(startDate, endDate)
            }

        })

        count_subject_rc.description.isEnabled = false
        count_subject_rc.webLineWidth = 1f
        count_subject_rc.webColor = Color.LTGRAY
        count_subject_rc.webLineWidthInner = 1f
        count_subject_rc.webColorInner = Color.LTGRAY
        count_subject_rc.webAlpha = 100
        count_subject_rc.setNoDataText(getApp().getString(R.string.null_data))
        val markView = ReaderMarkView(context, R.layout.radar_markerview)
        markView.chartView = count_subject_rc
        count_subject_rc.marker = markView
        count_subject_rv.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        adapter = CountSubjectHandAdapter()
        count_subject_rv.adapter = adapter 
    }

    fun loadData() {
        mCountSubjectHandPresenter = CountSubjectHandPresenter(this)
        mCountSubjectHandPresenter?.getSubjectRightData(getMonthStartDate(), getMonthToday())
    }

    private fun setData() {

        val set2 = RadarDataSet(entries1, "个人")
        set2.color = Color.rgb(121, 162, 175)
        set2.fillColor = Color.rgb(121, 162, 175)
        set2.setDrawFilled(true)
        set2.fillAlpha = 180
        set2.lineWidth = 2f
        set2.isDrawHighlightCircleEnabled = true
        set2.setDrawHighlightIndicators(false)

        val set1 = RadarDataSet(entries2, "班级")
        set1.color = Color.rgb(103, 110, 129)
        set1.fillColor = Color.rgb(103, 110, 129)
        set1.setDrawFilled(true)
        set1.fillAlpha = 180
        set1.lineWidth = 2f
        set1.isDrawHighlightCircleEnabled = true
        set1.setDrawHighlightIndicators(false)

        val sets = ArrayList<IRadarDataSet>()
        sets.add(set2)
        sets.add(set1)

        val data = RadarData(sets)
        data.setValueTextSize(12f)
        data.setDrawValues(false)
        data.setValueTextColor(Color.BLACK)

        count_subject_rc.clear()
        count_subject_rc.data = data
        for (set in count_subject_rc.data.dataSets)
            set.setDrawValues(!set.isDrawValuesEnabled)

        count_subject_rc.invalidate()

    }

    private fun setMemu() {
        count_subject_rc.animateY(1400)

        val xAxis = count_subject_rc.xAxis
        xAxis.textSize = 12f
        xAxis.yOffset = 0f
        xAxis.xOffset = 0f
        xAxis.valueFormatter = IAxisValueFormatter { value, _ -> mActivities[value.toInt() % mActivities.size] }
        xAxis.textColor = Color.BLACK

        val yAxis = count_subject_rc.yAxis
        yAxis.setLabelCount(5, false)
        yAxis.textSize = 12f
        yAxis.axisMinimum = 0f
        yAxis.axisMaximum = 80f
        yAxis.setDrawLabels(false)

        val l = count_subject_rc.legend
        l.verticalAlignment = Legend.LegendVerticalAlignment.TOP
        l.horizontalAlignment = Legend.LegendHorizontalAlignment.CENTER
        l.orientation = Legend.LegendOrientation.HORIZONTAL
        l.setDrawInside(false)
        l.xEntrySpace = 7f
        l.yEntrySpace = 5f
        l.textColor = Color.BLACK
    }

}
