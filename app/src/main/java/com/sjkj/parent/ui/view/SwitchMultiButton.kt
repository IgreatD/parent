package com.sjkj.parent.ui.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.graphics.RectF
import android.os.Bundle
import android.os.Parcelable
import android.text.TextPaint
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import com.sjkj.parent.R


class SwitchMultiButton @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : View(context, attrs, defStyleAttr) {

    private var mTabTexts = arrayOf("L", "R")
    private var mTabNum = mTabTexts.size
    private var mStrokePaint: Paint? = null
    private var mFillPaint: Paint? = null
    private var mWidth: Int = 0
    private var mHeight: Int = 0
    private var mSelectedTextPaint: TextPaint? = null
    private var mUnselectedTextPaint: TextPaint? = null
    private var onSwitchListener: OnSwitchListener? = null
    private var mStrokeRadius: Float = 0.toFloat()
    private var mStrokeWidth: Float = 0.toFloat()
    private var mSelectedColor: Int = 0
    private var mTextSize: Float = 0.toFloat()
    private var mSelectedTab: Int = 0
    private var perWidth: Float = 0.toFloat()
    private var mTextHeightOffset: Float = 0.toFloat()
    private var mFontMetrics: Paint.FontMetrics? = null

    /**
     * get default height when android:layout_height="wrap_content"
     */
    private val defaultHeight: Int
        get() = (mFontMetrics!!.bottom - mFontMetrics!!.top).toInt() + paddingTop + paddingBottom

    /**
     * get default width when android:layout_width="wrap_content"
     */
    private val defaultWidth: Int
        get() {
            val tabs = mTabTexts.size
            val tabTextWidth = mTabTexts
                    .map { mSelectedTextPaint!!.measureText(it) }
                    .max()
                    ?: 0f
            val totalTextWidth = tabTextWidth * tabs
            val totalStrokeWidth = mStrokeWidth * tabs
            val totalPadding = (paddingRight + paddingLeft) * tabs
            return (totalTextWidth + totalStrokeWidth + totalPadding.toFloat()).toInt()
        }

    init {
        initAttrs(context, attrs)
        initPaint()
    }

    private fun initAttrs(context: Context, attrs: AttributeSet?) {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.SwitchMultiButton)
        mStrokeRadius = typedArray.getDimension(R.styleable.SwitchMultiButton_strokeRadius, STROKE_RADIUS)
        mStrokeWidth = typedArray.getDimension(R.styleable.SwitchMultiButton_strokeWidth, STROKE_WIDTH)
        mTextSize = typedArray.getDimension(R.styleable.SwitchMultiButton_switchTextSize, TEXT_SIZE)
        mSelectedColor = typedArray.getColor(R.styleable.SwitchMultiButton_selectedColor, SELECTED_COLOR)
        mSelectedTab = typedArray.getInteger(R.styleable.SwitchMultiButton_selectedTab, SELECTED_TAB)
        val mSwitchTabsResId = typedArray.getResourceId(R.styleable.SwitchMultiButton_switchTabs, 0)
        if (mSwitchTabsResId != 0) {
            mTabTexts = resources.getStringArray(mSwitchTabsResId)
            mTabNum = mTabTexts.size
        }
        typedArray.recycle()
    }

    private fun initPaint() {
        mStrokePaint = Paint()
        mStrokePaint!!.color = mSelectedColor
        mStrokePaint!!.style = Paint.Style.STROKE
        mStrokePaint!!.isAntiAlias = true
        mStrokePaint!!.strokeWidth = mStrokeWidth
        mFillPaint = Paint()
        mFillPaint!!.color = mSelectedColor
        mFillPaint!!.style = Paint.Style.FILL_AND_STROKE
        mStrokePaint!!.isAntiAlias = true
        mSelectedTextPaint = TextPaint(Paint.ANTI_ALIAS_FLAG)
        mSelectedTextPaint!!.textSize = mTextSize
        mSelectedTextPaint!!.color = -0x1
        mStrokePaint!!.isAntiAlias = true
        mUnselectedTextPaint = TextPaint(Paint.ANTI_ALIAS_FLAG)
        mUnselectedTextPaint!!.textSize = mTextSize
        mUnselectedTextPaint!!.color = mSelectedColor
        mStrokePaint!!.isAntiAlias = true
        mTextHeightOffset = -(mSelectedTextPaint!!.ascent() + mSelectedTextPaint!!.descent()) * 0.5f
        mFontMetrics = mSelectedTextPaint!!.fontMetrics
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val defaultWidth = defaultWidth
        val defaultHeight = defaultHeight
        setMeasuredDimension(getExpectSize(defaultWidth, widthMeasureSpec), getExpectSize(defaultHeight, heightMeasureSpec))
    }

    private fun getExpectSize(size: Int, measureSpec: Int): Int {
        var result = size
        val specMode = View.MeasureSpec.getMode(measureSpec)
        val specSize = View.MeasureSpec.getSize(measureSpec)
        when (specMode) {
            View.MeasureSpec.EXACTLY -> result = specSize
            View.MeasureSpec.UNSPECIFIED -> result = size
            View.MeasureSpec.AT_MOST -> result = Math.min(size, specSize)
        }
        return result
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val left = mStrokeWidth * 0.5f
        val top = mStrokeWidth * 0.5f
        val right = mWidth - mStrokeWidth * 0.5f
        val bottom = mHeight - mStrokeWidth * 0.5f

        //draw rounded rectangle
        canvas.drawRoundRect(RectF(left, top, right, bottom), mStrokeRadius, mStrokeRadius, mStrokePaint!!)

        //draw line
        for (i in 0 until mTabNum - 1) {
            canvas.drawLine(perWidth * (i + 1), top, perWidth * (i + 1), bottom, mStrokePaint!!)
        }
        //draw tab and line
        for (i in 0 until mTabNum) {
            val tabText = mTabTexts[i]
            val tabTextWidth = mSelectedTextPaint!!.measureText(tabText)
            if (i == mSelectedTab) {
                //draw selected tab
                if (i == 0) {
                    drawLeftPath(canvas, left, top, bottom)

                } else if (i == mTabNum - 1) {
                    drawRightPath(canvas, top, right, bottom)

                } else {
                    canvas.drawRect(perWidth * i, top, perWidth * (i + 1), bottom, mFillPaint!!)
                }
                // draw selected text
                canvas.drawText(tabText, 0.5f * perWidth * (2 * i + 1).toFloat() - 0.5f * tabTextWidth, mHeight * 0.5f + mTextHeightOffset, mSelectedTextPaint!!)

            } else {
                //draw unselected text
                canvas.drawText(tabText, 0.5f * perWidth * (2 * i + 1).toFloat() - 0.5f * tabTextWidth, mHeight * 0.5f + mTextHeightOffset, mUnselectedTextPaint!!)
            }
        }
    }

    private fun drawLeftPath(canvas: Canvas, left: Float, top: Float, bottom: Float) {
        val leftPath = Path()
        leftPath.moveTo(left + mStrokeRadius, top)
        leftPath.lineTo(perWidth, top)
        leftPath.lineTo(perWidth, bottom)
        leftPath.lineTo(left + mStrokeRadius, bottom)
        leftPath.arcTo(RectF(left, bottom - 2 * mStrokeRadius, left + 2 * mStrokeRadius, bottom), 90f, 90f)
        leftPath.lineTo(left, top + mStrokeRadius)
        leftPath.arcTo(RectF(left, top, left + 2 * mStrokeRadius, top + 2 * mStrokeRadius), 180f, 90f)
        canvas.drawPath(leftPath, mFillPaint!!)
    }

    private fun drawRightPath(canvas: Canvas, top: Float, right: Float, bottom: Float) {
        val rightPath = Path()
        rightPath.moveTo(right - mStrokeRadius, top)
        rightPath.lineTo(right - perWidth, top)
        rightPath.lineTo(right - perWidth, bottom)
        rightPath.lineTo(right - mStrokeRadius, bottom)
        rightPath.arcTo(RectF(right - 2 * mStrokeRadius, bottom - 2 * mStrokeRadius, right, bottom), 90f, -90f)
        rightPath.lineTo(right, top + mStrokeRadius)
        rightPath.arcTo(RectF(right - 2 * mStrokeRadius, top, right, top + 2 * mStrokeRadius), 0f, -90f)
        canvas.drawPath(rightPath, mFillPaint!!)
    }


    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        mWidth = measuredWidth
        mHeight = measuredHeight
        perWidth = (mWidth / mTabNum).toFloat()
        checkAttrs()
    }

    private fun checkAttrs() {
        if (mStrokeRadius > 0.5f * mHeight) {
            mStrokeRadius = 0.5f * mHeight
        }
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (event.action == MotionEvent.ACTION_UP) {
            val x = event.x
            for (i in 0 until mTabNum) {
                if (x > perWidth * i && x < perWidth * (i + 1)) {
                    if (mSelectedTab == i) {
                        return true
                    }
                    mSelectedTab = i
                    if (onSwitchListener != null) {
                        onSwitchListener!!.onSwitch(i, mTabTexts[i])
                    }
                }
            }
            invalidate()
        }
        return true
    }

    /**
     * called when swtiched
     */
    interface OnSwitchListener {
        fun onSwitch(position: Int, tabText: String)
    }

    fun setOnSwitchListener(onSwitchListener: OnSwitchListener): SwitchMultiButton {
        this.onSwitchListener = onSwitchListener
        return this
    }

    /**
     * get position of selected tab
     */
    fun getSelectedTab(): Int {
        return mSelectedTab
    }

    fun setSelectedTab(mSelectedTab: Int): SwitchMultiButton {
        this.mSelectedTab = mSelectedTab
        invalidate()
        if (onSwitchListener != null) {
            onSwitchListener!!.onSwitch(mSelectedTab, mTabTexts[mSelectedTab])
        }
        return this
    }

    override fun onSaveInstanceState(): Parcelable? {
        val bundle = Bundle()
        bundle.putParcelable("View", super.onSaveInstanceState())
        bundle.putFloat("StrokeRadius", mStrokeRadius)
        bundle.putFloat("StrokeWidth", mStrokeWidth)
        bundle.putFloat("TextSize", mTextSize)
        bundle.putInt("SelectedColor", mSelectedColor)
        bundle.putInt("SelectedTab", mSelectedTab)
        return bundle
    }

    override fun onRestoreInstanceState(state: Parcelable) {
        if (state is Bundle) {
            mStrokeRadius = state.getFloat("StrokeRadius")
            mStrokeWidth = state.getFloat("StrokeWidth")
            mTextSize = state.getFloat("TextSize")
            mSelectedColor = state.getInt("SelectedColor")
            mSelectedTab = state.getInt("SelectedTab")
            super.onRestoreInstanceState(state.getParcelable("View"))
        } else {
            super.onRestoreInstanceState(state)
        }
    }

    companion object {
        private val STROKE_RADIUS = 0f
        private val STROKE_WIDTH = 2f
        private val TEXT_SIZE = 14f
        private val SELECTED_COLOR = -0x148500
        private val SELECTED_TAB = 0
    }
}
