package com.sank.demo

import com.sank.R
import com.sank.mvvm.BaseActivity

class MainActivity : BaseActivity<MainActivityViewModel>() {
    override fun getVMClass(): Class<MainActivityViewModel> {
        return MainActivityViewModel::class.java
    }

    override fun setLayoutId(): Int {
        return R.layout.activity_main
    }

    override fun create() {

    }
}