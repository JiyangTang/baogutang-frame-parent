package com.baogutang.frame.ons.event;

import java.io.Serializable;
import java.util.Date;
import java.util.Map;

/**
 * @author N1KO
 */
public interface IEvent extends Serializable {

    /**
     * getId
     *
     * @return getId
     */
    String getId();

    /**
     * getEventType
     *
     * @return EventType
     */
    EventType getEventType();

    /**
     * isSyncEvent
     *
     * @return isSyncEvent
     */
    boolean isSyncEvent();

    /**
     * getEventTime
     *
     * @return Date
     */
    Date getEventTime();

    /**
     * getTemplateId
     *
     * @return Integer
     */
    Integer getTemplateId();

    /**
     * getTemplateParam
     *
     * @return map
     */
    Map<String, String> getTemplateParam();


}
