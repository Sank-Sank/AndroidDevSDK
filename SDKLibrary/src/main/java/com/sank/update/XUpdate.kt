package com.sank.update

import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import com.sank.XSank
import com.sank.globalContext
import com.sank.log
import com.sank.yes
import java.io.File


/**
 *   created by sank
 *   on 2020/9/16
 *   描述：主类，链式调用
 */
public class XUpdate : XSank(){

    private var mContext: AppCompatActivity? = null
    private var updateBean: UpdateBean? = null
    private var updateFragment: UpdateFragment? = null

    private val dirPath: String = "${globalContext()?.cacheDir?.absolutePath}"
    private val fileName: String = "dwonload.apk"

    private val channelIDLoading = "loading"
    private val channelIDComplete = "complete"

    //更新完成后对main的回调
    private lateinit var updateInterface: OnForMainUpdateInterface

    fun setBean(mContext: AppCompatActivity, updateBean: UpdateBean, updateInterface: OnForMainUpdateInterface): XUpdate {
        this.mContext = mContext
        this.updateInterface = updateInterface
        this.updateBean = updateBean
        updateFragment = UpdateFragment.newInstance(updateBean)
        updateFragment?.setClickListener(object : OnUpdateInterface {
            override fun onStartUpdate() {
                downloadByHttpUrlConnection()
            }

            override fun onCompleteUpdate() {
                installApk()
            }
        })
        return this
    }

    fun show(): XUpdate {
        mContext?.supportFragmentManager?.let { it1 ->
            updateFragment?.showNow(it1, UpdateFragment::class.java.simpleName)
        }
        return this
    }

    /**
     * 使用 HttpUrlConnection 下载
     */
    private fun downloadByHttpUrlConnection() {
        FileDownloadUtil.download(
                updateBean?.url!!,
                dirPath,
                fileName,
                onStart = { downloadStart() },
                onProgress = { current, total -> downloading(current, total) },
                onComplete = { downloadComplete() },
                onError = { downloadError(it) }
        )
    }

    private fun downloadError(it: Throwable) {
        log("下载错误")
    }

    private fun downloadComplete() {
        updateInterface.onCompleteUpdate()
        MyNotificationUtils.cancleNotification(globalContext()!!)
        MyNotificationUtils.showNotificationProgress(
            globalContext()!!,
            updateBean?.title!!,
            "更新完成，点击安装",
            channelIDComplete,
            updateBean?.iconId!!,
            "${dirPath}${File.separator}csbao.apk"
        )
        installApk()
        updateFragment?.setComplete()
    }

    private fun downloading(current: Long, total: Long) {
        val progress = (current * 100.0 / total).toInt()
        log("progress == $progress")
        updateBean?.force.yes {
            updateFragment?.setProgress(progress)
        }
        MyNotificationUtils.showNotificationProgressApkDown(
            globalContext()!!,
            progress,
            updateBean?.iconId!!,
            channelIDLoading
        )
    }

    private fun downloadStart() {
        log("下载开始")
    }

    /**
     * 安装apk
     */
    private fun installApk() {
        // 获取当前sdcard存储路径
        val apkFile = File("${dirPath}${File.separator}csbao.apk")
        if (!apkFile.exists()) {
            return
        }
        val i = Intent(Intent.ACTION_VIEW)
        i.addCategory(Intent.CATEGORY_DEFAULT)
        i.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            val contentUri = globalContext()?.let { FileProvider.getUriForFile(it, it.packageName + ".fileprovider", apkFile) }
            //打开APK
            i.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            i.setDataAndType(contentUri, "application/vnd.android.package-archive")
        } else {
            i.setDataAndType(Uri.fromFile(apkFile), "application/vnd.android.package-archive")
        }
        globalContext()?.startActivity(i)
    }
}