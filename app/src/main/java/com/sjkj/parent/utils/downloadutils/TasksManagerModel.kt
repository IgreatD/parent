package com.sjkj.parent.utils.downloadutils

import org.litepal.crud.DataSupport

/**
 * @author by dingl on 2017/12/14.
 */

data class TasksManagerModel(
        var tag: Int = 0,
        var name: String? = null,
        var url: String? = null,
        var path: String? = null
) : DataSupport()
