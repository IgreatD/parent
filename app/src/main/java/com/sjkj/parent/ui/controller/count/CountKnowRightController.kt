package com.sjkj.parent.ui.controller.count

import android.content.Context
import android.graphics.Color
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.IAxisValueFormatter
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet
import com.github.mikephil.charting.utils.EntryXComparator
import com.sjkj.parent.R
import com.sjkj.parent.data.server.CountInfo
import com.sjkj.parent.mvp.contract.count.CountContract
import com.sjkj.parent.mvp.presenter.count.CountSubjectRightPresenter
import com.sjkj.parent.ui.adapter.count.CountSubjectHandAdapter
import com.sjkj.parent.ui.controller.base.BaseController
import com.sjkj.parent.ui.view.ReaderMarkView
import com.sjkj.parent.ui.view.TimeView
import com.sjkj.parent.utils.getApp
import com.sjkj.parent.utils.getFloat
import com.sjkj.parent.utils.getMonthStartDate
import com.sjkj.parent.utils.getMonthToday
import kotlinx.android.synthetic.main.controller_count_know_right.view.*
import java.util.*

/**
 * @author by dingl on 2017/10/19.
 * @desc CountSubjectHandController
 */
class CountKnowRightController(context: Context) : BaseController(context), CountContract.View {

    private var entries1: ArrayList<BarEntry> = ArrayList()
    private var entries2: ArrayList<BarEntry> = ArrayList()
    private var mActivities: ArrayList<String> = ArrayList()
    private var list: ArrayList<List<String>> = ArrayList()
    private var courseInfoID: Int? = 0
    private var adapter: CountSubjectHandAdapter? = null

    override fun setNewData(t: List<CountInfo>) {
        entries1.clear()
        entries2.clear()
        mActivities.clear()
        list.clear()
        val cmList = ArrayList<String>()
        cmList.add("知识点名字")
        cmList.add("正确率")
        cmList.add("班级均值")
        cmList.add("个人与班级差值")
        cmList.add("班级排名")
        list.add(cmList)

        for (i in t.indices) {
            val mpRecord = t[i]

            val cList = ArrayList<String>()
            cList.add(mpRecord.KnowledgePointName ?: "")
            cList.add(getFloat(mpRecord.RightRate * 100) + "%")
            cList.add(getFloat(mpRecord.ClassRightRate * 100) + "%")
            cList.add(getFloat(mpRecord.RightRateDifference * 100) + "%")
            cList.add(mpRecord.RightRateClassOrder.toString())
            cList.add(mpRecord.RightRateGradeOrder.toString())
            list.add(cList)

            mActivities.add(mpRecord.HomeworkInfoName ?: "")
            entries1.add(BarEntry((i + 1).toFloat(), mpRecord.RightRate * 100))
            entries2.add(BarEntry((i + 1).toFloat(), mpRecord.ClassRightRate * 100))
        }
        Collections.sort(entries1, EntryXComparator())
        Collections.sort(entries2, EntryXComparator())
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
        count_subject_rc.invalidate()
        adapter?.setNewData(null)
    }

    override fun loadComplete() {
    }

    private var mCountSubjectHandPresenter: CountSubjectRightPresenter? = null

    private var mTimeView: TimeView? = null

    init {
        LayoutInflater.from(context).inflate(R.layout.controller_count_know_right, this)
        initView()
    }

    private fun initView() {
        mCountSubjectHandPresenter = CountSubjectRightPresenter(this)
        mTimeView = TimeView(context)
        count_subject_time_view.addView(mTimeView)
        mTimeView?.setOnTimeCheckListener(object : TimeView.OnTimeCheckListener {
            override fun confrim(startDate: String, endDate: String) {
                mCountSubjectHandPresenter?.getKnowRightData(startDate, endDate, courseInfoID)
            }

        })

        count_subject_rc.description.isEnabled = false
        count_subject_rc.setDrawBarShadow(false)
        count_subject_rc.setDrawValueAboveBar(false)
        count_subject_rc.setPinchZoom(false)
        count_subject_rc.setDrawGridBackground(false)
        val markView = ReaderMarkView(context, R.layout.radar_markerview)
        markView.chartView = count_subject_rc
        count_subject_rc.marker = markView
        count_subject_rc.setNoDataText(getApp().getString(R.string.null_data))

        val l = count_subject_rc.legend
        l.verticalAlignment = Legend.LegendVerticalAlignment.TOP
        l.horizontalAlignment = Legend.LegendHorizontalAlignment.RIGHT
        l.orientation = Legend.LegendOrientation.VERTICAL
        l.setDrawInside(true)
        l.yOffset = 0f
        l.xOffset = 10f
        l.yEntrySpace = 0f
        l.textSize = 8f

        count_subject_rv.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        adapter = CountSubjectHandAdapter()
        count_subject_rv.adapter = adapter
    }

    fun loadData(courseInfoID: Int?) {
        this.courseInfoID = courseInfoID
        mCountSubjectHandPresenter?.getKnowRightData(getMonthStartDate(), getMonthToday(), courseInfoID)
    }

    private fun setData() {

        val d1 = BarDataSet(entries1, "个人")
        val d2 = BarDataSet(entries2, "班级")
        val dataSets = ArrayList<IBarDataSet>()
        val green = Color.argb(150, 254, 111, 92)
        val red = Color.argb(150, 103, 110, 129)
        d1.color = green
        d2.color = red
        dataSets.add(d1)
        dataSets.add(d2)

        val xAxis = count_subject_rc.xAxis
        xAxis.isEnabled = true
        xAxis.valueFormatter = ValueFormatter()
        if (entries1.size < 20) {
            xAxis.labelCount = entries1.size
        } else {
            xAxis.labelCount = 20
        }

        val data = BarData(d1, d2)
        count_subject_rc.data = data
        count_subject_rc.invalidate()

    }

    private inner class ValueFormatter : IAxisValueFormatter {
        override fun getFormattedValue(value: Float, axis: AxisBase): String {
            val aa = (value + 1).toInt()
            return aa.toString() + ""
        }
    }

}
