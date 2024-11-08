package com.baogutang.frame.ons.event.ons;

/**
 * @author N1KO
 */

public enum QueueType {

    /**
     * HAPPY_CASH
     */
    HAPPY_CASH("happyCash", "happyCash"),
    ADAPUNDI("adapundi", "adapundi"),

    ;

    private String topicName;
    private String topicDesc;

    QueueType(String topicName, String topicDesc) {
        this.topicName = topicName;
        this.topicDesc = topicDesc;
    }

    public String getTopicName() {
        return topicName;
    }

    public void setTopicName(String topicName) {
        this.topicName = topicName;
    }

    public String getTopicDesc() {
        return topicDesc;
    }

    public void setTopicDesc(String topicDesc) {
        this.topicDesc = topicDesc;
    }

    public static QueueType[] allTypes() {

        return values();
    }

    public static QueueType get(String topicName) {
        for (QueueType value : values()) {
            if (value.getTopicName().equals(topicName)) {
                return value;
            }
        }

        return null;
    }

    public static boolean isOrderMsg(QueueType queueType) {
        return false;
    }

}
