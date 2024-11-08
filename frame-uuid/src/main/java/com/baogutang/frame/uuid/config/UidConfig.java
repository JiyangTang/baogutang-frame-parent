package com.baogutang.frame.uuid.config;

import com.baogutang.frame.uuid.impl.CachedUidGenerator;
import com.baogutang.frame.uuid.impl.DefaultUidGenerator;
import com.baogutang.frame.uuid.worker.DisposableWorkerIdAssigner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;

@Configuration
public class UidConfig {

    @Resource
    private UidConfigProperties uidConfigProperties;


    @Bean
    public DefaultUidGenerator defaultUidGenerator(DisposableWorkerIdAssigner workerIdAssigner) {

        DefaultUidGenerator defaultUidGenerator = new DefaultUidGenerator();

        defaultUidGenerator.setWorkerIdAssigner(workerIdAssigner);

        if (uidConfigProperties.getTimeBits() != null) {
            defaultUidGenerator.setTimeBits(uidConfigProperties.getTimeBits());
        }

        if (uidConfigProperties.getWorkerBits() != null) {
            defaultUidGenerator.setWorkerBits(uidConfigProperties.getWorkerBits());
        }

        if (uidConfigProperties.getSeqBits() != null) {
            defaultUidGenerator.setSeqBits(uidConfigProperties.getSeqBits());
        }

        if (uidConfigProperties.getEpochStr() != null) {
            defaultUidGenerator.setEpochStr(uidConfigProperties.getEpochStr());
        }

        return defaultUidGenerator;
    }

    @Bean
    public CachedUidGenerator cachedUidGenerator(DisposableWorkerIdAssigner workerIdAssigner) {
        CachedUidGenerator cachedUidGenerator = new CachedUidGenerator();
        cachedUidGenerator.setWorkerIdAssigner(workerIdAssigner);

        /**
         * 一系列默认配置
         *      <!-- 以下为可选配置, 如未指定将采用默认值 -->
         * 		<!-- RingBuffer size扩容参数, 可提高UID生成的吞吐量. -->
         * 		<!-- 默认:3， 原bufferSize=8192, 扩容后bufferSize= 8192 << 3 = 65536 -->
         * 		<!--<property name="boostPower" value="3"></property>-->
         *
         * 		<!-- 指定何时向RingBuffer中填充UID, 取值为百分比(0, 100), 默认为50 -->
         * 		<!-- 举例: bufferSize=1024, paddingFactor=50 -> threshold=1024 * 50 / 100 = 512. -->
         * 		<!-- 当环上可用UID数量 < 512时, 将自动对RingBuffer进行填充补全 -->
         * 		<!--<property name="paddingFactor" value="50"></property>-->
         *
         * 		<!-- 另外一种RingBuffer填充时机, 在Schedule线程中, 周期性检查填充 -->
         * 		<!-- 默认:不配置此项, 即不实用Schedule线程. 如需使用, 请指定Schedule线程时间间隔, 单位:秒 -->
         * 		<!--<property name="scheduleInterval" value="60"></property>-->
         *
         * 		<!-- 拒绝策略: 当环已满, 无法继续填充时 -->
         * 		<!-- 默认无需指定, 将丢弃Put操作, 仅日志记录. 如有特殊需求, 请实现RejectedPutBufferHandler接口(支持Lambda表达式) -->
         * 		<!--<property name="rejectedPutBufferHandler" ref="XxxxYourPutRejectPolicy"></property>-->
         *
         * 		<!-- 拒绝策略: 当环已空, 无法继续获取时 -->
         * 		<!-- 默认无需指定, 将记录日志, 并抛出UidGenerateException异常. 如有特殊需求, 请实现RejectedTakeBufferHandler接口(支持Lambda表达式) -->
         * 		<!--<property name="rejectedTakeBufferHandler" ref="XxxxYourTakeRejectPolicy"></property>-->
         *
         */
        return cachedUidGenerator;
    }

}
