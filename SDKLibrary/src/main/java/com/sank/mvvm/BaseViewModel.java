package com.sank.mvvm;

import android.app.Activity;

import androidx.databinding.ViewDataBinding;
import androidx.lifecycle.ViewModel;

/**
 * @ClassName BaseViewModel
 * @Description TODO
 * @Author Sank
 * @Date 2021/1/19 17:04
 * @Version 1.0
 */
public class BaseViewModel<B extends ViewDataBinding> extends ViewModel implements ModelListener {
    protected B bind;
    protected Activity mActivity;

    @Override
    public void initModel() {

    }
}
