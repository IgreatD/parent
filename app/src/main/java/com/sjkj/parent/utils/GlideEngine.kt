package com.sjkj.parent.utils

import android.content.Context
import android.graphics.drawable.Drawable
import android.net.Uri
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.Priority
import com.bumptech.glide.request.RequestOptions
import com.zhihu.matisse.engine.ImageEngine

/**
 * @author by dingl on 2017/9/21.
 * @desc GlideEngine
 */
class GlideEngine : ImageEngine {

    override fun loadAnimatedGifThumbnail(context: Context?, resize: Int, placeholder: Drawable?, imageView: ImageView?, uri: Uri?) {
    }

    override fun loadImage(context: Context?, resizeX: Int, resizeY: Int, imageView: ImageView?, uri: Uri?) {
        Glide.with(context)
                .load(uri)
                .apply(RequestOptions().centerCrop().override(resizeX, resizeY).priority(Priority.HIGH))
                .into(imageView)

    }

    override fun loadAnimatedGifImage(context: Context?, resizeX: Int, resizeY: Int, imageView: ImageView?, uri: Uri?) {
    }

    override fun supportAnimatedGif(): Boolean = true

    override fun loadThumbnail(context: Context?, resize: Int, placeholder: Drawable?, imageView: ImageView?, uri: Uri?) {

        Glide.with(context)
                .asBitmap()
                .load(uri)
                .apply(RequestOptions()
                        .centerCrop()
                        .override(resize, resize))
                .into(imageView)

    }
}
