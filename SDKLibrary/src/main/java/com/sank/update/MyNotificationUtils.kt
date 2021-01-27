package com.sank.update

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.content.FileProvider
import java.io.File

/**
 * created by sank
 * on 2020/9/18
 * 描述：
 */
object MyNotificationUtils {
    private var manager: NotificationManager? = null
    private fun getManager(context: Context): NotificationManager? {
        if (manager == null) {
            manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        }
        return manager
    }

    private fun getNotificationBuilder(mContext: Context, title: String
                                       , content: String, channelId: String, iconID: Int): NotificationCompat.Builder {
        //大于8.0
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            //id随便指定
            val channel = NotificationChannel(channelId
                    , mContext.packageName, NotificationManager.IMPORTANCE_DEFAULT)
            channel.canBypassDnd() //可否绕过，请勿打扰模式
            channel.enableLights(true) //闪光
            channel.lockscreenVisibility = Notification.VISIBILITY_SECRET //锁屏显示通知
            channel.lightColor = Color.RED //指定闪光是的灯光颜色
            channel.canShowBadge() //桌面laucher消息角标
            channel.enableVibration(true) //是否允许震动
            channel.setSound(null, null)
            //channel.getAudioAttributes();//获取系统通知响铃声音配置
            channel.group //获取通知渠道组
            channel.setBypassDnd(true) //设置可以绕过，请勿打扰模式
            channel.vibrationPattern = longArrayOf(100, 100, 200) //震动的模式，震3次，第一次100，第二次100，第三次200毫秒
            channel.shouldShowLights() //是否会闪光
            //通知管理者创建的渠道
            getManager(mContext)!!.createNotificationChannel(channel)
        }
        return NotificationCompat.Builder(mContext, channelId).setAutoCancel(true)
                .setContentTitle(title)
                .setContentText(content).setSmallIcon(iconID)
    }

    /**
     * @param title
     * @param content
     * @param channelId
     */
    fun showNotificationProgress(mContext: Context, title: String
                                 , content: String, channelId: String, iconID: Int, path: String?) {
        val builder = getNotificationBuilder(mContext, title, content, channelId, iconID)
        val i = Intent(Intent.ACTION_VIEW)
        i.addCategory(Intent.CATEGORY_DEFAULT)
        i.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            i.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            val contentUri = FileProvider.getUriForFile(mContext, mContext.packageName + ".provider", File(path))
            //打开APK
            i.setDataAndType(contentUri, "application/vnd.android.package-archive")
        } else {
            i.setDataAndType(Uri.fromFile(File(path)), "application/vnd.android.package-archive")
        }
        val pendingIntent = PendingIntent.getActivity(mContext, 0, i, 0)
        builder.setContentIntent(pendingIntent)
        builder.setOnlyAlertOnce(true)
        builder.setDefaults(Notification.FLAG_ONLY_ALERT_ONCE)
        //        builder.setProgress(maxProgress, progress, false);
        builder.setWhen(System.currentTimeMillis())
        getManager(mContext)!!.notify(iconID, builder.build())
    }

    fun showNotificationProgressApkDown(mContext: Context
                                        , progress: Int, iconID: Int, channelID: String) {
        val builder = getNotificationBuilder(mContext, "正在下载", "$progress%", channelID, iconID)
        builder.setOnlyAlertOnce(true)
        builder.setDefaults(Notification.FLAG_ONLY_ALERT_ONCE)
        builder.setProgress(100, progress, false)
        builder.setWhen(System.currentTimeMillis())
        getManager(mContext)!!.notify(iconID, builder.build())
    }

    fun cancleNotification(mContext: Context) {
        getManager(mContext)!!.cancelAll()
    }
}