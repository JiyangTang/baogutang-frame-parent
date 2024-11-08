package com.baogutang.frame.common.component;

import com.baogutang.frame.common.exception.BusinessException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.transaction.support.TransactionTemplate;

import javax.annotation.Resource;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

/**
 * @author N1KO
 */
@Slf4j
public abstract class BaseSupport {

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    @Resource
    private TransactionTemplate transactionTemplate;

    /**
     * tryExecuteWithInLimit
     *
     * @param key      key
     * @param limit    limit
     * @param supplier supplier
     * @param <T>      t
     * @return res
     */
    public <T> T tryExecuteWithInLimit(String key, int limit, Integer expire, TimeUnit timeUnit, Supplier<T> supplier) {
        try {
            Long val = redisTemplate.opsForValue().increment(key);
            if (Objects.nonNull(val) && val > limit) {
                log.info("Key:{}, reachLimit:{}", key, limit);
                throw new BusinessException("Request reached upper limit!");
            }
            return supplier.get();
        } finally {
            redisTemplate.expire(key, expire, timeUnit);
        }
    }

    /**
     * tryExecuteWithInLimit
     *
     * @param key      key
     * @param limit    limit
     * @param executor executor
     */
    public void tryExecuteWithInLimit(String key, int limit, Integer expire, TimeUnit timeUnit, Executor executor) {
        try {
            Long val = redisTemplate.opsForValue().increment(key);
            if (Objects.nonNull(val) && val > limit) {
                log.info("Key:{}, reachLimit:{}", key, limit);
                throw new BusinessException("Request reached upper limit!");
            }
            executor.exec();
        } finally {
            redisTemplate.expire(key, expire, timeUnit);
        }
    }


    /**
     * transactionExec
     *
     * @param executor executor
     */
    public void transactionExec(Executor executor) {
        transactionTemplate.executeWithoutResult(status -> {
            try {
                executor.exec();
            } catch (Exception e) {
                status.setRollbackOnly();
                log.warn("exec transaction fail", e);
                throw e;
            }
        });
    }

    /**
     * transactionExec
     *
     * @param supplier supplier
     * @param <T>      t
     * @return res
     */
    public <T> T transactionExec(Supplier<T> supplier) {
        return transactionTemplate.execute(status -> {
            try {
                return supplier.get();
            } catch (Exception e) {
                status.setRollbackOnly();
                log.warn("exec transaction fail", e);
                throw e;
            }
        });
    }

}
