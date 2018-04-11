package com.sjkj.parent.ui.controller.count

import android.content.Context
import android.graphics.Color
import android.support.v4.content.ContextCompat
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.utils.ColorTemplate
import com.github.mikephil.charting.utils.EntryXComparator
import com.sjkj.parent.R
import com.sjkj.parent.data.server.CountInfo
import com.sjkj.parent.mvp.contract.count.CountContract
import com.sjkj.parent.mvp.presenter.count.CountSubjectRightPresenter
import com.sjkj.parent.ui.controller.base.BaseController
import com.sjkj.parent.ui.view.TimeView
import com.sjkj.parent.utils.getApp
import com.sjkj.parent.utils.getFloat
import com.sjkj.parent.utils.getMonthStartDate
import com.sjkj.parent.utils.getMonthToday
import kotlinx.android.synthetic.main.controller_count_hw_right.view.*
import java.util.*


/**
 * @author by dingl on 2017/10/19.
 * @desc CountSubjectHandController
 */
class CountHwRightController(context: Context) : BaseController(context), CountContract.View {

    private var entries1: ArrayList<Entry> = ArrayList()
    private var entries2: ArrayList<Entry> = ArrayList()
    private var mActivities: ArrayList<String> = ArrayList()
    private var list: ArrayList<List<String>> = ArrayList()
    private var courseInfoID: Int? = 0
    private var adapter: CountHwRightAdapter? = null

    override fun setNewData(t: List<CountInfo>) {
        entries1.clear()
        entries2.clear()
        mActivities.clear()
        list.clear()
        val cmList = ArrayList<String>()
        cmList.add("作业名称")
        cmList.add("正确率")
        cmList.add("班级均值")
        cmList.add("班级排名")
        list.add(cmList)

        for (i in t.indices) {
            val mpRecord = t[i]

            val cList = ArrayList<String>()
            cList.add(mpRecord.HomeworkInfoName ?: "")
            cList.add(getFloat(mpRecord.RightRate * 100) + "%")
            cList.add(getFloat(mpRecord.ClassRightRate * 100) + "%")
            cList.add(mpRecord.RightRateClassOrder.toString())
            list.add(cList)

            mActivities.add(mpRecord.HomeworkInfoName ?: "")
            entries1.add(Entry((i + 1).toFloat(), mpRecord.RightRate * 100))
            entries2.add(Entry((i + 1).toFloat(), mpRecord.ClassRightRate * 100))
        }
        Collections.sort(entries1, EntryXComparator())
        Collections.sort(entries2, EntryXComparator())
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

    private var mCountSubjectHandPresenter: CountSubjectRightPresenter? = null

    private var mTimeView: TimeView? = null

    init {
        LayoutInflater.from(context).inflate(R.layout.controller_count_hw_right, this)
        initView()
    }

    private fun initView() {
        mCountSubjectHandPresenter = CountSubjectRightPresenter(this)
        mTimeView = TimeView(context)
        count_subject_time_view.addView(mTimeView)
        mTimeView?.setOnTimeCheckListener(object : TimeView.OnTimeCheckListener {
            override fun confrim(startDate: String, endDate: String) {
                mCountSubjectHandPresenter?.getHwRightData(startDate, endDate, courseInfoID)
            }

        })

        count_subject_rc.description.isEnabled = false
        count_subject_rc.setTouchEnabled(true)
        count_subject_rc.dragDecelerationFrictionCoef = 0.9f
        count_subject_rc.isDragEnabled = true
        count_subject_rc.setScaleEnabled(true)
        count_subject_rc.setDrawGridBackground(false)
        count_subject_rc.isHighlightPerDragEnabled = true
        count_subject_rc.setNoDataText(getApp().getString(R.string.null_data))
        count_subject_rc.setPinchZoom(false)
        count_subject_rv.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        adapter = CountHwRightAdapter()
        count_subject_rv.adapter = adapter
    }

    fun loadData(courseInfoID: Int?) {
        this.courseInfoID = courseInfoID
        mCountSubjectHandPresenter?.getHwRightData(getMonthStartDate(), getMonthToday(), courseInfoID)
    }

    private fun setData() {

        val set2 = LineDataSet(entries1, "个人")
        set2.axisDependency = YAxis.AxisDependency.RIGHT
        set2.color = Color.RED
        set2.setCircleColor(Color.BLACK)
        set2.valueTextColor = Color.BLACK
        set2.lineWidth = 2f
        set2.circleRadius = 3f
        set2.fillAlpha = 30
        set2.fillColor = Color.RED
        set2.setDrawCircleHole(false)
        set2.highLightColor = Color.rgb(244, 117, 117)

        val set1 = LineDataSet(entries2, "班级")
        set1.axisDependency = YAxis.AxisDependency.LEFT
        set1.color = ColorTemplate.getHoloBlue()
        set1.setCircleColor(Color.BLACK)
        set1.valueTextColor = Color.BLACK
        set1.lineWidth = 2f
        set1.circleRadius = 3f
        set1.fillAlpha = 30
        set1.fillColor = ColorTemplate.getHoloBlue()
        set1.highLightColor = Color.rgb(244, 117, 117)
        set1.setDrawCircleHole(false)

        val data = LineData(set1, set2)
        data.setValueTextColor(Color.BLACK)
        data.setValueTextSize(9f)


        count_subject_rc.clear()
        count_subject_rc.data = data

        val sets = count_subject_rc.data.dataSets
        sets.map { it as LineDataSet }.forEach { it.setDrawValues(true) }

        count_subject_rc.invalidate()

    }

    private fun setMemu() {
        count_subject_rc.animateX(200)

        val l = count_subject_rc.legend

        l.form = Legend.LegendForm.LINE
        l.textSize = 13f
        l.textColor = ContextCompat.getColor(context, R.color.qmui_config_color_75_pure_black)
        l.verticalAlignment = Legend.LegendVerticalAlignment.BOTTOM
        l.horizontalAlignment = Legend.LegendHorizontalAlignment.LEFT
        l.orientation = Legend.LegendOrientation.HORIZONTAL
        l.setDrawInside(false)

        val xAxis = count_subject_rc.xAxis
        xAxis.position = XAxis.XAxisPosition.BOTTOM
        xAxis.setDrawGridLines(false)
        xAxis.setDrawAxisLine(true)
        xAxis.axisMinimum = 1f
        if (entries1.size <= 20) {
            xAxis.labelCount = entries1.size - 1
        } else {
            xAxis.labelCount = 20
        }
        xAxis.labelRotationAngle = -20f
        xAxis.position = XAxis.XAxisPosition.BOTTOM
        xAxis.spaceMin = 30f

        val leftAxis = count_subject_rc.axisLeft
        leftAxis.setLabelCount(5, false)
        leftAxis.axisMinimum = 0f
        leftAxis.axisMaximum = 100f

        val rightAxis = count_subject_rc.axisRight
        rightAxis.setLabelCount(5, false)
        rightAxis.setDrawGridLines(false)
        rightAxis.axisMinimum = 0f
        rightAxis.axisMaximum = 100f
    }

}

class CountHwRightAdapter : BaseQuickAdapter<List<String>, BaseViewHolder>(R.layout.adapter_count_hw_right) {

    override fun convert(helper: BaseViewHolder?, item: List<String>?) {

        helper?.setText(R.id.name1_tv, item?.get(0))
        helper?.setText(R.id.name2_tv, item?.get(1))
        helper?.setText(R.id.name3_tv, item?.get(2))
        helper?.setText(R.id.name4_tv, item?.get(3))
    }
}
