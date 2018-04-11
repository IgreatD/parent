package com.sjkj.parent.ui.widget

import android.net.Uri
import android.support.v7.widget.AppCompatImageView
import android.text.Editable
import android.text.TextWatcher
import android.view.MotionEvent
import android.view.View
import android.widget.*
import com.qmuiteam.qmui.util.QMUIKeyboardHelper
import com.sjkj.parent.R
import com.sjkj.parent.common.Common
import com.sjkj.parent.ui.activity.ChatActivity
import com.sjkj.parent.ui.fragment.BaseFragment
import com.sjkj.parent.utils.KeyBoard
import com.sjkj.parent.utils.audio.AudioRecordManager
import com.sjkj.parent.utils.audio.IAudioRecordListener
import com.sjkj.parent.utils.floder.MaterialFilePicker
import com.sjkj.parent.utils.getAudioPath
import org.jetbrains.anko.sdk25.coroutines.onClick
import org.jetbrains.anko.sdk25.coroutines.onTouch
import org.jetbrains.anko.support.v4.act
import java.io.File

/**
 * @author by dingl on 2017/9/28.
 * @desc ChatInputFragment
 */
class ChatInputFragment : BaseFragment() {

    private var view_chat_voice: ImageView? = null
    private var view_chat_input_et: EditText? = null
    private var view_chat_input_tv: TextView? = null
    private var view_chat_add: AppCompatImageView? = null
    private var view_chat_send: Button? = null
    private var add_ll: LinearLayout? = null
    private var plug: LinearLayout? = null
    private var view_chat_img: AppCompatImageView? = null
    private var view_chat_brush: AppCompatImageView? = null
    private var view_chat_file: AppCompatImageView? = null

    private var contentView: View? = null
    
    override fun initView(view: View?) {
        view_chat_voice = view?.findViewById(R.id.view_chat_voice)
        view_chat_input_et = view?.findViewById(R.id.view_chat_input_et)
        view_chat_input_tv = view?.findViewById(R.id.view_chat_input_tv)
        view_chat_add = view?.findViewById(R.id.view_chat_add)
        view_chat_send = view?.findViewById(R.id.view_chat_send)
        add_ll = view?.findViewById(R.id.add_ll)
        plug = view?.findViewById(R.id.plug)
        view_chat_img = view?.findViewById(R.id.view_chat_img)
        view_chat_brush = view?.findViewById(R.id.view_chat_brush)
        view_chat_file = view?.findViewById(R.id.view_chat_file)
        view_chat_input_et?.requestFocus()
        QMUIKeyboardHelper.hideKeyboard(view)
        initVisibleListener()
        initPlugListener()
        initAudioRecordManager()
    }

    override fun getLayoutId(): Int = R.layout.view_chat_input

    private fun initAudioRecordManager() {

        AudioRecordManager.getInstance(context).maxVoiceDuration = Common.DEFAULT_MAX_AUDIO_RECORD_TIME_SECOND

        val audioDir = File(getAudioPath())
        if (!audioDir.exists()) {
            audioDir.mkdirs()
        }
        AudioRecordManager.getInstance(context).setAudioSavePath(audioDir.absolutePath)
        AudioRecordManager.getInstance(context).audioRecordListener = object : IAudioRecordListener {

            private var mTimerTV: TextView? = null
            private var mStateTV: TextView? = null
            private var mStateIV: ImageView? = null
            private var mRecordWindow: PopupWindow? = null

            override fun initTipView() {
                val view = View.inflate(context, R.layout.popup_audio_wi_vo, null)
                mStateIV = view?.findViewById(R.id.rc_audio_state_image)
                mStateTV = view?.findViewById(R.id.rc_audio_state_text)
                mTimerTV = view?.findViewById(R.id.rc_audio_timer)
                mRecordWindow = PopupWindow(view, -1, -1)
                mRecordWindow?.showAtLocation(this@ChatInputFragment.view, 17, 0, 0)
                mRecordWindow?.isFocusable = true
                mRecordWindow?.isOutsideTouchable = false
                mRecordWindow?.isTouchable = false
            }

            override fun setTimeoutTipView(counter: Int) {
                if (this.mRecordWindow != null) {
                    this.mStateIV?.visibility = View.GONE
                    this.mStateTV?.visibility = View.VISIBLE
                    this.mStateTV?.setText(R.string.voice_rec)
                    this.mStateTV?.setBackgroundResource(R.drawable.bg_voice_popup)
                    this.mTimerTV?.text = String.format("%s", Integer.valueOf(counter))
                    this.mTimerTV?.visibility = View.VISIBLE
                }
            }

            override fun setRecordingTipView() {
                if (this.mRecordWindow != null) {
                    this.mStateIV?.visibility = View.VISIBLE
                    this.mStateIV?.setImageResource(R.drawable.ic_volume_1)
                    this.mStateTV?.visibility = View.VISIBLE
                    this.mStateTV?.setText(R.string.voice_rec)
                    this.mStateTV?.setBackgroundResource(R.drawable.bg_voice_popup)
                    this.mTimerTV?.visibility = View.GONE
                }
            }

            override fun setAudioShortTipView() {
                if (this.mRecordWindow != null) {
                    mStateIV?.setImageResource(R.drawable.ic_volume_wraning)
                    mStateTV?.setText(R.string.voice_short)
                }
            }

            override fun setCancelTipView() {
                if (this.mRecordWindow != null) {
                    this.mTimerTV?.visibility = View.GONE
                    this.mStateIV?.visibility = View.VISIBLE
                    this.mStateIV?.setImageResource(R.drawable.ic_volume_cancel)
                    this.mStateTV?.visibility = View.VISIBLE
                    this.mStateTV?.setText(R.string.voice_cancel)
                    this.mStateTV?.setBackgroundResource(R.drawable.corner_voice_style)
                }
            }

            override fun destroyTipView() {
                if (this.mRecordWindow != null) {
                    this.mRecordWindow?.dismiss()
                    this.mRecordWindow = null
                    this.mStateIV = null
                    this.mStateTV = null
                    this.mTimerTV = null
                }
            }

            override fun onStartRecord() {

            }

            override fun onFinish(audioPath: Uri, duration: Int) {
                val file = File(audioPath.path)
                if (file.exists()) {
                    onSendListener?.sendAudio(file, duration)
                }
            }

            override fun onAudioDBChanged(db: Int) {
                when (db / 5) {
                    0 -> this.mStateIV?.setImageResource(R.drawable.ic_volume_1)
                    1 -> this.mStateIV?.setImageResource(R.drawable.ic_volume_2)
                    2 -> this.mStateIV?.setImageResource(R.drawable.ic_volume_3)
                    3 -> this.mStateIV?.setImageResource(R.drawable.ic_volume_4)
                    4 -> this.mStateIV?.setImageResource(R.drawable.ic_volume_5)
                    5 -> this.mStateIV?.setImageResource(R.drawable.ic_volume_6)
                    6 -> this.mStateIV?.setImageResource(R.drawable.ic_volume_7)
                    else -> this.mStateIV?.setImageResource(R.drawable.ic_volume_8)
                }
            }
        }
    }

    private fun initPlugListener() {
        view_chat_img?.onClick {
            onSendListener?.sendImg()
            add_ll?.visibility = View.GONE
        }

        view_chat_brush?.onClick {
            onSendListener?.sendBrush()
            add_ll?.visibility = View.GONE
        }

        view_chat_input_et?.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus)
                onSendListener?.scrollToBottom()
        }

        view_chat_input_et?.addTextChangedListener(object : TextWatcher {

            override fun afterTextChanged(p0: Editable?) {
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                onSendListener?.scrollToBottom()
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (p0.isNullOrEmpty()) {
                    view_chat_add?.visibility = View.VISIBLE
                    view_chat_send?.visibility = View.GONE
                } else {
                    view_chat_add?.visibility = View.GONE
                    view_chat_send?.visibility = View.VISIBLE
                }
            }

        })

        view_chat_send?.onClick {
            onSendListener?.sent(view_chat_input_et?.text.toString(), 0)
            view_chat_input_et?.text = null
        }

        view_chat_input_tv?.onTouch { v, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> AudioRecordManager.getInstance(getContext()).startRecord()
                MotionEvent.ACTION_MOVE -> {
                    if (isCancelled(v, event))
                        AudioRecordManager.getInstance(getContext()).willCancelRecord()
                    else
                        AudioRecordManager.getInstance(getContext()).continueRecord()
                }
                MotionEvent.ACTION_UP -> {
                    AudioRecordManager.getInstance(getContext()).stopRecord()
                    AudioRecordManager.getInstance(getContext()).destroyRecord()
                }
            }
        }

        view_chat_file?.onClick {
            MaterialFilePicker()
                    .withActivity(act as ChatActivity)
                    .withRequestCode(Common.REQUEST_FILE)
                    .withHiddenFiles(true)
                    .withTitle(resources.getString(R.string.choose_file))
                    .start()
        }

        view_chat_voice?.onClick {
            if (view_chat_input_tv?.isShown == false) {
                QMUIKeyboardHelper.hideKeyboard(view_chat_input_et)
                view_chat_input_tv?.visibility = View.VISIBLE
                view_chat_input_et?.visibility = View.GONE
            } else {
                view_chat_input_tv?.visibility = View.GONE
                view_chat_input_et?.visibility = View.VISIBLE
                QMUIKeyboardHelper.showKeyboard(view_chat_input_et, false)
            }
            add_ll?.visibility = View.GONE
        }

    }

    private fun initVisibleListener() {
        val keyBoard = KeyBoard.with(act)
                .bindToContent(contentView)
                .bindToEditText(view_chat_input_et)
                .bindToEmotionButton(view_chat_add, view_chat_input_tv)
                .build()
        keyBoard.setmEmotionLayout(add_ll)
        QMUIKeyboardHelper.setVisibilityEventListener(act, {
            if (it) {
                add_ll?.visibility = View.GONE
            }
        })

    }

    interface OnSendListener {
        fun sent(msg: String?, msgType: Int)
        fun scrollToBottom()
        fun sendImg()
        fun sendBrush()
        fun sendAudio(file: File, duration: Int)
    }

    private var onSendListener: OnSendListener? = null

    fun setOnSendListener(onSendListener: OnSendListener) {
        this.onSendListener = onSendListener
    }

    private fun isCancelled(view: View, event: MotionEvent): Boolean {
        val location = IntArray(2)
        view.getLocationOnScreen(location)

        if (event.rawX < location[0] || event.rawX > location[0] + view.width
                || event.rawY < location[1] - 40) {
            return true
        }

        return false
    }

    fun closeInput() {
        QMUIKeyboardHelper.hideKeyboard(view_chat_input_et)
        add_ll?.visibility = View.GONE
        view_chat_input_et?.clearFocus()
    }

    fun bindToContent(view: View?) {
        contentView = view
    }
}
