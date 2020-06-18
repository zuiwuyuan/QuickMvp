package com.njfu.yxcmc.bus;

/**
 * 更新首页消息数量小红点
 *
 * @param <T>
 */
public class EventUpdateMsgCount<T> {

    private T event;

    private String tag;

    public EventUpdateMsgCount(T event) {
        this.event = event;
    }

    public EventUpdateMsgCount(String tag, T event) {
        this.tag = tag;
        this.event = event;
    }

    public String getTag() {
        return tag;
    }

    public T getData() {
        return event;
    }

}