package com.sank.mvvm;

import android.app.Activity;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LifecycleRegistry;

import com.sank.BR;
import com.sank.event.EventBusListener;

import org.greenrobot.eventbus.EventBus;

/**
 * @ClassName BaseActivity
 * @Description TODO
 * @Author Sank
 * @Date 2021/1/19 17:07
 * @Version 1.0
 */
public abstract class BaseActivity<VM extends BaseViewModel> extends Activity implements EventBusListener, LifecycleOwner {
    protected VM vm;
    protected Activity mContext;
    protected LifecycleRegistry mLifecycleRegistry = new LifecycleRegistry(this);

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (initEvent()) {
            register();
        }
        try {
            vm = getVMClass().newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
        mContext = this;
        vm.mActivity = this;
        vm.bind = DataBindingUtil.setContentView(mContext, setLayoutId());
        vm.bind.setVariable(BR.vm, vm);
        vm.initModel();
        mLifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_CREATE);
        create();
    }

    @NonNull
    @Override
    public Lifecycle getLifecycle() {
        return mLifecycleRegistry;
    }

    protected abstract Class<VM> getVMClass();

    protected abstract int setLayoutId();

    protected abstract void create();

    @Override
    public void onStart() {
        super.onStart();
        mLifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_START);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mLifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_RESUME);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mLifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_PAUSE);
    }

    @Override
    protected void onStop() {
        super.onStop();
        mLifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_STOP);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mLifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_DESTROY);
    }

    @Override
    public void register() {
        EventBus.getDefault().register(this);
    }

    @Override
    public void unRegister() {
        EventBus.getDefault().unregister(this);
    }

    @Override
    public boolean initEvent() {
        return false;
    }
}
