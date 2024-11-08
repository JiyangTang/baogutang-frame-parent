package com.baogutang.frame.ons.event.sub;


import com.baogutang.frame.ons.event.IEvent;
import lombok.Data;

import java.sql.Timestamp;
import java.util.Map;
import java.util.UUID;

/**
 * @author N1KO
 */
@Data
public abstract class AbstractEvent implements IEvent {

    /**
     * id
     */
    protected String id;
    /**
     * syncEvent
     */
    protected boolean syncEvent;
    /**
     * eventTime
     */
    protected Timestamp eventTime;
    /**
     * templateId
     */
    protected Integer templateId;
    /**
     * templateParam
     */
    protected Map<String, String> templateParam;

    /**
     * appType
     */
    private String appType;

    @Override
    public String getId() {
        return UUID.randomUUID().toString();
    }

    /**
     * isSyncEvent
     *
     * @return isSyncEvent
     */
    @Override
    public boolean isSyncEvent() {
        return false;
    }

    /**
     * getEventTime
     *
     * @return Timestamp
     */
    @Override
    public Timestamp getEventTime() {
        return new Timestamp(System.currentTimeMillis());
    }

}
