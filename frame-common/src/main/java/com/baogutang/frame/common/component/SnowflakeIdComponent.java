package com.baogutang.frame.common.component;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;

/**
 * 订单号生成规则 日期+long 数据中心和服务器序号 各减少4为二进制, sequenceBits-6 long 0- 0000000000- 00000000- 00000-
 * 00000000-000000-0000000000-0000-00 1保留位置 17位单天秒数 8位数据中心 10位服务器序号 26位订单号 2位保留位数
 *
 * @author N1KO
 */
@Slf4j
@Component
public class SnowflakeIdComponent implements ApplicationRunner {

    private final String KEY_DATACENTER = "snowflake.dataCenter";

    private final String KEY_SERVICE_ID = "snowflake.serviceId";

    private static long dataCenter = 10;

    private static long serviceId = 10;

    private final long workerIdBits = 7L;

    private final long datacenterIdBits = 5L;

    private final long maxWorkerId = ~(-1L << workerIdBits);

    private final long maxDatacenterId = ~(-1L << datacenterIdBits);

    private long workerId;

    private long datacenterId;

    private long sequence = 0L;

    private long lastTimestamp = -1L;

    private static class SingletonHolder {
        private static final SnowflakeIdComponent FACTORY =
                new SnowflakeIdComponent(serviceId, dataCenter);
    }

    public static SnowflakeIdComponent getInstance() {
        return SingletonHolder.FACTORY;
    }

    private SnowflakeIdComponent() {
    }

    private SnowflakeIdComponent(long workerId, long datacenterId) {
        if (workerId > maxWorkerId || workerId < 0) {
            throw new IllegalArgumentException(
                    String.format("serviceId 生成值: %d, 上限: %d", workerId, maxWorkerId));
        }
        if (datacenterId > maxDatacenterId || datacenterId < 0) {
            throw new IllegalArgumentException(
                    String.format("datacenterId 生成值: %d, 上限: %d", datacenterId, maxDatacenterId));
        }
        this.workerId = workerId;
        this.datacenterId = datacenterId;
    }

    public synchronized String nextId() {
        long timestamp = timeGen();
        if (timestamp < lastTimestamp) {
            // 服务器时钟被调整了,ID生成器停止服务.
            throw new RuntimeException(
                    String.format(
                            "Clock moved backwards.  Refusing to generate id for %d milliseconds",
                            lastTimestamp - timestamp));
        }
        long sequenceBits = 14L;
        if (lastTimestamp == timestamp) {
            long sequenceMask = ~(-1L << sequenceBits);
            sequence = (sequence + 1) & sequenceMask;
            if (sequence == 0) {
                timestamp = tilNextMillis(lastTimestamp);
            }
        } else {
            sequence = 0L;
        }
        lastTimestamp = timestamp;
        SimpleDateFormat dateformat = new SimpleDateFormat("yyyyMMdd");
        long holdBits = 1L;
        long workerIdShift = sequenceBits + holdBits;
        long datacenterIdShift = sequenceBits + workerIdBits + holdBits;
        long timestampLeftShift = sequenceBits + workerIdBits + datacenterIdBits + holdBits;
        return dateformat.format(timestamp)
                + (((getCurrentDaySecond(timestamp)) << timestampLeftShift)
                | (datacenterId << datacenterIdShift)
                | (workerId << workerIdShift)
                | (sequence << holdBits));
    }

    protected long tilNextMillis(long lastTimestamp) {
        long timestamp = timeGen();
        while (timestamp <= lastTimestamp) {
            timestamp = timeGen();
        }
        return timestamp;
    }

    protected long getCurrentDaySecond(long timestamp) {
        return (timestamp % 100000000)
                + 100000000;
    }

    protected long timeGen() {
        return System.currentTimeMillis();
    }

    @Override
    public void run(ApplicationArguments applicationArguments) {
        int dataCenterRandom = (int) (Math.ceil(Math.random() * maxDatacenterId));
        int serviceIdRandom = (int) (Math.ceil(Math.random() * maxWorkerId));
        try {
            log.debug(">>>>>>>>>>SnowflakeIdFactory.run<<<<<<<<<<");
            dataCenter = dataCenterRandom;
            serviceId = serviceIdRandom;
            log.debug(">>>>>>>>>>dataCenter: {}, serviceId:{}<<<<<<<<<<", dataCenter, serviceId);
        } catch (Exception e) {
            dataCenter = dataCenterRandom;
            serviceId = serviceIdRandom;
        }
    }
}
