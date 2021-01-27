package com.sank.event;

public interface EventBusListener {
    void register();
    void unRegister();
    boolean initEvent();
}