
package com.baogutang.frame.ons.event;

/**
 * @author N1KO
 */
public enum EventType {

    /**
     * 短信
     */
    sms(1, "短信事件"),
    /**
     * 邮件
     */
    email(2, "邮件事件"),
    /**
     * 队列
     */
    queue(3, "队列事件"),
    /**
     * app 推送
     */
    app_push(4, "app通知事件"),
    ;


    private final int value;
    private final String desc;

    EventType(int value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    public int getValue() {
        return value;
    }

    public String getDesc() {
        return desc;
    }

}
