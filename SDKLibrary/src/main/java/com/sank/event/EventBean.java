package com.sank.event;

/**
 * @ClassName EventBean
 * @Description TODO
 * @Author Sank
 * @Date 2021/1/19 17:02
 * @Version 1.0
 */
public class EventBean {
    private int what;
    private String msg;
    private Object data;
    private int positionChange;

    public int getWhat() {
        return what;
    }

    public void setWhat(int what) {
        this.what = what;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public int getPositionChange() {
        return positionChange;
    }

    public void setPositionChange(int positionChange) {
        this.positionChange = positionChange;
    }
}
