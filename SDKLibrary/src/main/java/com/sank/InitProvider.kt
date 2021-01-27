package com.sank

import androidx.core.content.FileProvider

/**
 *   created by sank
 *   on 2020/9/18
 *   描述：
 */
class InitProvider : FileProvider() {
    override fun onCreate(): Boolean {
        val result = super.onCreate()
        (XSank.mContext == null && context != null).yes {
            XSank.mContext = context
            log("内部Provider初始化context：" + XSank.mContext)
        }
        return result
    }
}