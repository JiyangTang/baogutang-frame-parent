package com.baogutang.frame.ons.event.sub;

import com.baogutang.frame.ons.event.EventType;
import com.baogutang.frame.ons.event.ons.IQueueMsg;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * @author N1KO
 */
@EqualsAndHashCode(callSuper = true)
@Data
@ToString
public class QueueEvent extends AbstractEvent {

    /**
     * msg
     */
    private IQueueMsg msg;

    public QueueEvent(IQueueMsg msg) {
        this.msg = msg;
    }

    @Override
    public EventType getEventType() {
        return EventType.queue;
    }


}
