package com.sank.mvvm;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;


import com.sank.BR;
import com.sank.event.EventBusListener;

import org.greenrobot.eventbus.EventBus;

/**
 * @ClassName BaseFragment
 * @Description TODO
 * @Author Sank
 * @Date 2021/1/19 17:13
 * @Version 1.0
 */
public abstract class BaseFragment<VM extends BaseViewModel> extends Fragment implements EventBusListener {
    protected VM vm;
    protected View rootView;
    protected FragmentActivity mContext;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (initEvent()) {
            register();
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mContext = requireActivity();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(setLayoutID(), container, false);
        try {
            vm = getVModelClass().newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
        vm.bind = DataBindingUtil.setContentView(mContext, setLayoutID());
        vm.bind.setVariable(BR.vm, vm);
        vm.initModel();
        vm.mActivity = requireActivity();

        //解决fragment UI重复加载
        ViewGroup parent = (ViewGroup) rootView.getParent();
        parent.removeView(rootView);
        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();
    }

    /**
     * 处理界面相关
     */
    protected abstract void initView();

    /***
     * 初始化view(加载布局)
     */
    protected abstract int setLayoutID();

    /**
     * 初始化VModel
     * @return
     */
    protected abstract Class<VM> getVModelClass();

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
