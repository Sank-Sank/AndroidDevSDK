package com.sank.update

import java.io.Serializable


/**
 *   created by sank
 *   on 2020/9/17
 *   描述：
 */
data class UpdateBean(
        //更新相关
        internal var url : String?,
        internal var versionName : String? = null,
        internal var title : String?,
        internal var Description : String?,
        internal var iconId : Int? = null,
        //是否强制更新
        internal var force : Boolean = false
) : Serializable