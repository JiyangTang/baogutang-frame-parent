package com.baogutang.frame.ons.event;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;


/**
 * @author N1KO
 */
@Data
@ToString
public class OnsMessage<T extends IEvent> implements Serializable {

    private T msgBody;

    private String eventSource;

    public OnsMessage() {
    }

    public OnsMessage(T msgBody, String eventSource) {
        this();
        this.msgBody = msgBody;
        this.eventSource = eventSource;
    }

    public OnsMessage(T msgBody) {
        this();
        this.msgBody = msgBody;
    }

}
