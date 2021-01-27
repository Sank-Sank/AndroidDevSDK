package com.sank

import android.content.Context
import com.sank.update.XUpdate


/**
 *   created by sank
 *   on 2020/9/17
 */
public open class XSank{
    companion object {
        const val UPDATE = 0

        //是否是调试模式
        internal var isDebug: Boolean = true

        //全局Context
        internal var mContext: Context? = null

        @JvmStatic
        fun getModel(type: Int): XSank? {
            when (type) {
                UPDATE -> {
                    return XUpdate()
                }
                else -> {
                    return null
                }
            }
        }

        @JvmStatic
        fun init(context: Context, isDebug: Boolean = false) {
            Companion.isDebug = isDebug
            mContext = context.applicationContext
            log("外部初始化context")
        }
    }


}