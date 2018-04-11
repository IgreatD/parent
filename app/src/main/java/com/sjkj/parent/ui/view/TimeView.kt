package com.sjkj.parent.ui.view

import android.app.DatePickerDialog
import android.content.Context
import android.view.LayoutInflater
import android.widget.Button
import android.widget.LinearLayout
import com.sjkj.parent.R
import com.sjkj.parent.utils.getMonthStartDate
import com.sjkj.parent.utils.getMonthToday
import kotlinx.android.synthetic.main.view_time.view.*
import org.jetbrains.anko.sdk25.coroutines.onClick
import java.sql.Date
import java.util.*

/**
 * @author by dingl on 2017/9/22.
 * @desc TimeView
 */
class TimeView(context: Context) : LinearLayout(context) {

    init {
        LayoutInflater.from(context).inflate(R.layout.view_time, this)
        initData()
    }
    
    companion object {
        val mCalendar: Calendar = Calendar.getInstance()
    }

    private fun initData() {

        view_assign_bt_left.text = getMonthStartDate()
        view_assign_bt_right.text = getMonthToday()

        view_assign_bt_right.onClick {

            val endDialog = createDatePickDialog(view_assign_bt_right)
            endDialog.datePicker.maxDate = System.currentTimeMillis()
            endDialog.datePicker.minDate = Date.valueOf(view_assign_bt_left.text.toString()).time
            endDialog.show()
        }

        view_assign_bt_left.onClick {
            val startDialog = createDatePickDialog(view_assign_bt_left)
            startDialog.datePicker.maxDate = Date.valueOf(view_assign_bt_right.text.toString()).time
            startDialog.show()
        }

    }

    private fun createDatePickDialog(btn: Button): DatePickerDialog {

        val dialog: DatePickerDialog?

        if (btn.text.toString().trim({ it <= ' ' }).isEmpty()) {
            dialog = DatePickerDialog(context, R.style.Translucent_NoTitle, DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
                btn.text = String.format("%d-%d-%d", year, month + 1, dayOfMonth)
                onTimeCheckListener?.confrim(view_assign_bt_left.text.toString(), view_assign_bt_right.text.toString())
            }, mCalendar.get(Calendar.YEAR), mCalendar.get(Calendar.MONTH), mCalendar.get(Calendar.DAY_OF_MONTH))

        } else {
            val date = btn.text.toString().trim({ it <= ' ' }).split("-".toRegex()).dropLastWhile({ it.isEmpty() }).toTypedArray()
            dialog = DatePickerDialog(context, R.style.Translucent_NoTitle, DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
                btn.text = String.format("%d-%d-%d", year, month + 1, dayOfMonth)
                onTimeCheckListener?.confrim(view_assign_bt_left.text.toString(), view_assign_bt_right.text.toString())
            }, Integer.parseInt(date[0]), Integer.parseInt(date[1]) - 1, Integer.parseInt(date[2]))

        }
        return dialog
    }

    interface OnTimeCheckListener {
        fun confrim(startDate: String, endDate: String)
    }

    private var onTimeCheckListener: OnTimeCheckListener? = null

    fun setOnTimeCheckListener(onTimeCheckListener: OnTimeCheckListener?) {
        this.onTimeCheckListener = onTimeCheckListener
    }

}
