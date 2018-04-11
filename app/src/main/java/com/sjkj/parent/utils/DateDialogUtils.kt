package com.sjkj.parent.utils

import android.app.DatePickerDialog
import android.content.Context
import android.widget.TextView
import com.sjkj.parent.R
import com.sjkj.parent.ui.view.TimeView.Companion.mCalendar
import org.jetbrains.anko.sdk25.coroutines.onClick
import java.sql.Date
import java.util.*

/**
 * @author by dingl on 2017/12/29.
 * @desc DateDialogUtils
 */
class DateDialogUtils private constructor() {

    companion object {
        fun getInstance() = Holder.instance
    }

    private object Holder {
        val instance = DateDialogUtils()
    }
    
    fun initData(context: Context, view_start: TextView, view_end: TextView) {

        view_start.text = getMonthToday()
        
        view_end.text = getMonthToday()

        view_end.onClick {

            val endDialog = createDatePickDialog(context, view_end)
            endDialog.datePicker.maxDate = System.currentTimeMillis()
            endDialog.datePicker.minDate = Date.valueOf(view_start.text.toString()).time
            endDialog.show()

        }

        view_start.onClick {
            val startDialog = createDatePickDialog(context, view_start)
            startDialog.datePicker.maxDate = Date.valueOf(view_end.text.toString()).time
            startDialog.show()
        }

    }

    private fun createDatePickDialog(context: Context, btn: TextView): DatePickerDialog {

        val dialog: DatePickerDialog?

        if (btn.text.toString().trim({ it <= ' ' }).isEmpty()) {
            dialog = DatePickerDialog(context, R.style.Translucent_NoTitle, DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->

                btn.text = String.format("%d-%d-%d", year, month + 1, dayOfMonth)

            }, mCalendar.get(Calendar.YEAR), mCalendar.get(Calendar.MONTH), mCalendar.get(Calendar.DAY_OF_MONTH))

        } else {
            val date = btn.text.toString().trim({ it <= ' ' }).split("-".toRegex()).dropLastWhile({ it.isEmpty() }).toTypedArray()
            dialog = DatePickerDialog(context, R.style.Translucent_NoTitle, DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->

                btn.text = String.format("%d-%d-%d", year, month + 1, dayOfMonth)
            }, Integer.parseInt(date[0]), Integer.parseInt(date[1]) - 1, Integer.parseInt(date[2]))

        }
        return dialog
    }
}
