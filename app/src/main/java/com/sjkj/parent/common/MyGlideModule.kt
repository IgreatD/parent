package com.sjkj.parent.common

import com.bumptech.glide.annotation.GlideModule
import com.bumptech.glide.module.AppGlideModule

/**
 * @author by dingl on 2017/9/19.
 * @desc MyGlideModule
 */
@GlideModule
open class MyGlideModule : AppGlideModule() {
    override fun isManifestParsingEnabled(): Boolean {
        return false
    }
}
