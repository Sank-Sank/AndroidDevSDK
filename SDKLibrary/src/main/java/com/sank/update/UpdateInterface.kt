package com.sank.update


/**
 *   created by sank
 *   on 2020/9/17
 *   描述：
 */
interface OnUpdateInterface {
    fun onStartUpdate()
    fun onCompleteUpdate()
}

interface OnForMainUpdateInterface {
    fun onCompleteUpdate()
}