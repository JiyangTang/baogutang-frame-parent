
package com.baogutang.frame.ons.event.sub;

import com.baogutang.frame.ons.event.EventType;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Map;

/**
 * SmsEventMsg
 * @author N1KO
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class SmsEventMsg extends AbstractEvent {

    /**
     * phoneNumbers
     */
    private String[] phoneNumbers;

    public SmsEventMsg() {
    }

    public SmsEventMsg(Integer templateId, Map<String, String> templateParam, String[] phoneNumbers) {
        this(templateId, templateParam, phoneNumbers, false);
    }

    public SmsEventMsg(Integer templateId, Map<String, String> templateParam, String[] phoneNumbers, boolean syncEvent) {
        this.templateId = templateId;
        this.templateParam = templateParam;
        this.phoneNumbers = phoneNumbers;
        this.syncEvent = syncEvent;
    }

    @Override
    public EventType getEventType() {
        return EventType.sms;
    }


}
