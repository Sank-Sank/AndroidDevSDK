package com.sank

import android.app.Activity
import android.app.ActivityManager
import android.content.Context
import android.net.ConnectivityManager
import android.util.Log
import android.view.View
import androidx.core.content.ContextCompat
import java.io.File
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract


/**
 *   created by sank
 *   on 2020/9/16
 *   描述：扩展
 */
/**
 * appName
 */
val Context.appName
    get() = packageManager.getPackageInfo(packageName, 0)?.applicationInfo?.loadLabel(packageManager).toString()

/**
 * 判断activity是否已经销毁
 */
/**
 * 判断Activity是否Destroy
 * @param mActivity
 * @return true:已销毁
 */
fun Activity?.isDestroy() : Boolean {
    return this == null || this.isFinishing || this.isDestroyed
}

/**
 * 全局context
 */
fun globalContext() = XSank.mContext

/**
 * 打印日志
 */
fun log(content: String?) = XSank.isDebug.yes {
    Log.e("Sank", content ?: "")
}

/**
 * 获取color
 */
fun color(color: Int) = if (globalContext() == null) 0 else ContextCompat.getColor(globalContext()!!, color)

/**
 * 获取 String
 */
fun string(string: Int) = globalContext()?.getString(string) ?: ""

/**
 * 退出app
 */
fun exitApp() {
    val manager = globalContext()!!.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
    manager.appTasks.forEach { it.finishAndRemoveTask() }
}

/**
 * view 显示隐藏
 */
fun View.visibleOrGone(show: Boolean) {
    if (show) {
        this.visibility = View.VISIBLE
    } else {
        this.visibility = View.GONE
    }
}

/**
 * 检测wifi是否连接
 */
fun Context.isWifiConnected(): Boolean {
    val cm = this.getSystemService(Context.CONNECTIVITY_SERVICE) as? ConnectivityManager
    cm ?: return false
    val networkInfo = cm.activeNetworkInfo
    return networkInfo != null && networkInfo.type == ConnectivityManager.TYPE_WIFI
}

/**
 * bool的扩展
 */
@UseExperimental(ExperimentalContracts::class)
inline fun Boolean?.yes(block: () -> Unit): Boolean? {
    contract {
        callsInPlace(block, InvocationKind.AT_MOST_ONCE)
    }
    if (this == true) block()
    return this
}

@UseExperimental(ExperimentalContracts::class)
inline fun Boolean?.no(block: () -> Unit): Boolean? {
    contract {
        callsInPlace(block, InvocationKind.AT_MOST_ONCE)
    }
    if (this != true) block()
    return this
}


/**
 * 根据文件路径删除文件
 */
fun String?.deleteFile() {
    kotlin.runCatching {
        val file = File(this ?: "")
        (file.isFile).yes {
            file.delete()
            log("删除成功")
        }
    }.onFailure {
        log(it.message)
    }
}