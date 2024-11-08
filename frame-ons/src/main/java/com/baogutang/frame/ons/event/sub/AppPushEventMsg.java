package com.baogutang.frame.ons.event.sub;

import com.baogutang.frame.ons.event.EventType;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Map;
import java.util.Set;

/**
 * @author N1KO
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class AppPushEventMsg extends AbstractEvent {
    /**
     * deviceNos
     */
    private String[] deviceNos;

    /**
     * userIds should limit size
     */
    private Set<Integer> userIds;
    /**
     * title
     */
    private String title;

    public AppPushEventMsg() {
    }

    public AppPushEventMsg(Integer templateId, Map<String, String> templateParam, String[] deviceNos, String title) {
        this.templateId = templateId;
        this.templateParam = templateParam;
        this.deviceNos = deviceNos;
        this.title = title;
    }

    @Override
    public EventType getEventType() {
        return EventType.app_push;
    }


}
