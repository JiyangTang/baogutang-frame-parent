package com.baogutang.frame.ons.event.ons;


import com.baogutang.frame.ons.common.TopicTagEnum;
import lombok.Data;

/**
 * @author N1KO
 */
@Data
public class QueueMsg implements IQueueMsg {

    private QueueType msgType;
    private Object msgBody;
    private String tag;
    private String msgBizKey;
    private String shardingKey;
    private long startDeliverTime;
    private int retryTimes = 0;

    public QueueMsg() {
    }

    public QueueMsg(QueueType msgType, Object msgBody, String tagName, String msgBizKey) {
        this.msgType = msgType;
        this.msgBody = msgBody;
        this.tag = tagName;
        this.msgBizKey = msgBizKey;
    }

    public QueueMsg(QueueType msgType, Object msgBody, String tagName, String msgBizKey, String shardingKey) {
        this.msgType = msgType;
        this.msgBody = msgBody;
        this.tag = tagName;
        this.msgBizKey = msgBizKey;
        this.shardingKey = shardingKey;
    }


    public QueueMsg(QueueType msgType, Object msgBody, String tag) {
        this.msgType = msgType;
        this.msgBody = msgBody;
        this.tag = tag;
    }

    /**
     * QueueMsg
     *
     * @param topicTags topicTags
     * @param msgBody   msgBody
     * @param msgBizKey msgBizKey
     */
    public QueueMsg(TopicTagEnum topicTags, Object msgBody, String msgBizKey) {
        this.msgType = QueueType.get(topicTags.getTopic());
        this.msgBody = msgBody;
        this.tag = topicTags.getTag();
        this.msgBizKey = topicTags.getMsgKeyPrefix() + msgBizKey;
    }

    public QueueMsg(TopicTagEnum topicTags, Object msgBody) {
        this.msgType = QueueType.get(topicTags.getTopic());
        this.msgBody = msgBody;
        this.tag = topicTags.getTag();
    }

    @Override
    public QueueType getMsgType() {
        return msgType;
    }

    @Override
    public String getTag() {
        return tag;
    }

    @Override
    public Object getMsgBody() {

        return msgBody;
    }

    @Override
    public String getMsgBizKey() {
        return msgBizKey;
    }

    @Override
    public String getShardingKey() {
        return shardingKey;
    }

    @Override
    public void setStartDeliverTime(long startDeliverTime) {
        this.startDeliverTime = startDeliverTime;
    }

    @Override
    public long getStartDeliverTime() {
        return startDeliverTime;
    }

    @Override
    public int getRetryTimes() {
        return retryTimes;
    }

    @Override
    public void setRetryTimes(int retryTimes) {
        this.retryTimes = retryTimes;
    }

}
