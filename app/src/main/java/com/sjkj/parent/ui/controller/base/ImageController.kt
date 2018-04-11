package com.sjkj.parent.ui.controller.base

import android.content.Context
import android.view.LayoutInflater
import android.widget.FrameLayout
import com.sjkj.parent.R
import com.sjkj.parent.common.GlideApp
import kotlinx.android.synthetic.main.controller_image.view.*

/**
 * @author by dingl on 2017/9/20.
 * @desc ImageController
 */
class ImageController(context: Context) : FrameLayout(context) {

    init {
        LayoutInflater.from(context).inflate(R.layout.controller_image, this)
    }

    fun loadImage(imgPath: String?) {
        GlideApp.with(context)
                .load(imgPath)
                .error(R.drawable.ic_png_error)
                .into(image_img)
    }

}
