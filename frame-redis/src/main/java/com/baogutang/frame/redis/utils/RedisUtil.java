package com.baogutang.frame.redis.utils;

import lombok.extern.slf4j.Slf4j;
import org.assertj.core.util.Lists;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.data.redis.core.types.Expiration;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.Serializable;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * @Description 基于redisson的工具类 + string和hash类型泛型获取
 * 没有的方法可直接调用redisTemplate方法获取redisTemplate对象进行操作或者自行添加
 * 有问题概不负责，使用前自行调试 *^_^*
 */

@Slf4j
@Component
@AutoConfigureAfter({RedissonClient.class, RedisTemplate.class})
@SuppressWarnings("unused")
public class RedisUtil implements InitializingBean {

    @Autowired
    private RedissonClient redissonClient;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    public RedisTemplate<String, Object> redisTemplate() {
        return redisTemplate;
    }

    /**
     * 自旋锁、未获取锁会一直自旋尝试获取、直到获取为止
     *
     * @param lockKey 加锁key
     *                默认加锁时间30s、每隔10s会检查任务是否执行完成、没有完成自动延长加锁时间
     */
    public void lock(String lockKey) {
        RLock lock = redissonClient.getLock(lockKey);
        lock.lock();
    }

    /**
     * 自旋锁、可设置过期时间、无论任务是否执行完成、时间过期后放弃锁
     *
     * @param lockKey   加锁key
     * @param leaseTime key过期时间
     */
    public void lock(String lockKey, long leaseTime) {
        RLock lock = redissonClient.getLock(lockKey);
        lock.lock(leaseTime, TimeUnit.SECONDS);
    }

    /**
     * 独占锁、尝试获取锁、获取失败则放弃
     *
     * @param lockKey 加锁key
     */
    public boolean tryLock(String lockKey) {
        RLock lock = redissonClient.getLock(lockKey);
        return lock.tryLock();
    }

    /**
     * 在waitTime时间内一直尝试获取锁、时间过期未获取到则放弃
     *
     * @param lockKey  加锁key
     * @param waitTime 自旋等待时间
     */
    public boolean tryLock(String lockKey, long waitTime) {
        RLock lock = redissonClient.getLock(lockKey);
        try {
            return lock.tryLock(waitTime, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            return false;
        }
    }

    /**
     * 在waitTime时间内一直尝试获取锁、时间过期未获取到则放弃、leaseTime为加锁时间、时间过期无论任务是否完成则释放锁
     *
     * @param lockKey   加锁key
     * @param waitTime  自旋等待时间
     * @param leaseTime key过期时间  默认秒
     */
    public boolean tryLock(String lockKey, long waitTime, long leaseTime) {
        RLock lock = redissonClient.getLock(lockKey);
        try {
            return lock.tryLock(waitTime, leaseTime, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            log.error("tryLock error, msg:{}", e.getMessage(), e);
        }
        return false;
    }

    public <T> T tryLockAndUnlock(String key, Supplier<T> supplier) {
        if (tryLock(key)) {
            try {
                return supplier.get();
            } finally {
                unlock(key);
            }
        }
        log.warn("tryLockAndUnlockFail:{}", key);
        return null;
    }

    public <T> T tryLockAndUnlock(String key, Supplier<T> supplier, long waitTime, long leaseTime) {
        if (tryLock(key, waitTime, leaseTime)) {
            try {
                return supplier.get();
            } finally {
                unlock(key);
            }
        }
        log.warn("tryLockAndUnlockFail:{}", key);
        return null;
    }

    public boolean tryLockAndExecute(String key, int waitTime, int leaseTime, Runnable runnable) {
        if (waitTime <= 0 || leaseTime <= 0) {
            log.error("waitTime and leaseTime can not less than 0!");
            throw new IllegalArgumentException();
        }
        if (waitTime < leaseTime) {
            log.warn("{},waitTime less than leaseTime !!!", key);
        }
        if (tryLock(key, waitTime, leaseTime)) {
            try {
                runnable.run();
            } finally {
                // 释放锁
                unlock(key);
            }
            return true;
        }
        return false;
    }

    public void unlock(String lockKey) {
        try {
            RLock lock = redissonClient.getLock(lockKey);
            if (lock.isLocked() && lock.isHeldByCurrentThread()) {
                lock.unlock();
            }
        } catch (Exception e) {
            log.warn("unlock error", e);
        }
    }

    /**
     * 释放锁
     *
     * @param lockKey 加锁的key
     */
    public void unLock(String lockKey) {
        if (StringUtils.isEmpty(lockKey)) {
            log.info("RedissonLockUtil -> unLock -> info: lockKey is null");
            return;
        }
        RLock lock = redissonClient.getLock(lockKey);
        if (lock.isLocked() && lock.isHeldByCurrentThread()) {
            lock.unlock();
            log.info("RedissonLockUtil -> unLock -> info: lock released,key -> {}", lockKey);
        }
    }

    /**
     * @Description 设置key的过期时间
     */
    public Boolean setExpire(String key, Long expire, TimeUnit timeUnit) {
        return redisTemplate.expire(key, expire, timeUnit);
    }

    /**
     * @Description 指定key的过期时间
     */
    public Boolean setExpire(String key, Date date) {
        return redisTemplate.expireAt(key, date);
    }

    /**
     * @return 获取key的过期时间
     */
    public Long getExpire(String key) {
        return redisTemplate.getExpire(key);
    }

    /**
     * @Description 删除key
     */
    public Boolean delKey(String key) {
        return redisTemplate.delete(key);
    }

    /**
     * @Description string类型取值
     */
    public <T extends Serializable> T get(@NonNull String key) {
        return redisTemplate.execute((RedisCallback<T>) redisConnection -> {
            byte[] bytes = redisConnection.get(rawKey(key));
            return bytes == null ? null : deserializeValue(bytes, valueSerializer());
        });
    }

    /**
     * @Description string类型批量取值
     */
    public <T extends Serializable> List<T> get(@NonNull Collection<String> keys) {
        List<byte[]> bytes = redisTemplate.execute((RedisCallback<List<byte[]>>) connection -> connection.mGet(rawKeys(keys)));
        return bytes == null ? null : deserializeValues(bytes, valueSerializer());
    }

    /**
     * @Description string类型赋值
     */
    public void set(String key, String value, long timeout, TimeUnit timeUnit) {
        redisTemplate.opsForValue().set(key, value, timeout, timeUnit);
    }

    /**
     * @Description map类型取值
     */
    public <HV extends Serializable, HK extends Serializable> HV get(@NonNull String key, @NonNull HK hk) {
        return redisTemplate.execute((RedisCallback<HV>) redisConnection -> {
            byte[] hGet = redisConnection.hGet(rawKey(key), rawHashKey(hk));
            return hGet == null ? null : deserializeValue(hGet, hashValueSerializer());
        });
    }

    /**
     * @Description map类型批量取值
     */
    public <HK extends Serializable, HV extends Serializable> List<HV> get(@NonNull String key, @NonNull Collection<HK> hks) {
        List<byte[]> valueBytes = redisTemplate.execute((RedisCallback<List<byte[]>>) redisConnection -> redisConnection.hMGet(rawKey(key), rawHashKeys(hks)));
        return valueBytes == null ? new ArrayList<>() : deserializeValues(valueBytes, hashValueSerializer());
    }

    public <HK extends Serializable, HV extends Serializable> Map<HK, HV> getAll(@NonNull String key) {
        Map<byte[], byte[]> map = redisTemplate.execute((RedisCallback<Map<byte[], byte[]>>) redisConnection -> redisConnection.hGetAll(rawKey(key)));
        if (map == null || map.size() == 0) {
            return new HashMap<>();
        }
        return map.entrySet().stream().collect(Collectors.toMap(entry -> deserializeValue(entry.getKey(), hashKeySerializer()), entry -> deserializeValue(entry.getValue(), hashValueSerializer())));
    }


    /**
     * @Description map类型赋值
     */
    public void set(@NonNull String key, @NonNull Object hk, Object value) {
        redisTemplate.opsForHash().put(key, hk, value);
    }

    /**
     * @Description map类型赋值
     */
    public <HK extends Serializable, HV extends Serializable> void set(@NonNull String key, @NonNull Map<HK, HV> map) {
        redisTemplate.opsForHash().putAll(key, map);
    }

    /**
     * @Description map先删 再put 再设置过期时间
     */
    public <HV extends Serializable, HK extends Serializable> Boolean set(@NonNull String key, @NonNull Map<HK, HV> map, long timeout, TimeUnit timeUnit) {
        if (map.isEmpty()) {
            return false;
        }
        Expiration expiration = Expiration.from(timeout, timeUnit);
        String script = "redis.call('del',KEYS[1])  for i=1, #ARGV do if i%2 == 0 then redis.call('HSET',KEYS[1],string.gsub(ARGV[i-1],'\"',''),ARGV[i]) end end redis.call('EXPIRE',KEYS[1],ARGV[#ARGV]) return #ARGV/2";
        DefaultRedisScript<Integer> redisScript = new DefaultRedisScript<>(script, Integer.class);
        List<Object> argv = handle(map);
        argv.add(expiration.getExpirationTimeInSeconds());
        Integer execute = redisTemplate.execute(redisScript, Lists.newArrayList(key), argv.toArray());
        return execute != null && execute == 1;
    }

    private static <HV extends Serializable, HK extends Serializable> List<Object> handle(Map<HK, HV> map) {
        List<Object> list = new ArrayList<>();
        map.forEach((k, v) -> {
            list.add(k);
            list.add(v);
        });
        return list;
    }

    public <T> T getDataFromCache(String key, Supplier<T> supplier, Long cacheTime) {
        Object o = redisTemplate.opsForValue().get(key);
        if (Objects.nonNull(o)) {
            return (T) o;
        }
        T t = supplier.get();
        if (cacheTime == null) {
            redisTemplate.opsForValue().set(key, t);
        } else {
            redisTemplate.opsForValue().set(key, t, cacheTime, TimeUnit.SECONDS);
        }
        return t;
    }

    /**
     * @Description list根据下标取值
     */
    public <L extends Serializable> L get(@NonNull String key, long index) {
        return redisTemplate.execute((RedisCallback<L>) redisConnection -> {
            byte[] bytes = redisConnection.lIndex(rawKey(key), index);
            return bytes == null ? null : deserializeValue(bytes, valueSerializer());
        });
    }

    /**
     * @Description list类型批量取值 end = -1 则取全部
     */
    public <L extends Serializable> List<L> get(@NonNull String key, long start, long end) {
        List<byte[]> valueBytes = redisTemplate.execute((RedisCallback<List<byte[]>>) redisConnection -> redisConnection.lRange(rawKey(key), start, end));
        return valueBytes == null ? new ArrayList<>() : deserializeValues(valueBytes, valueSerializer());
    }

    /**
     * @Description list类型取值 根据 ListOption 类型取值
     */
    public <L extends Serializable> L get(@NonNull String key, @NonNull ListOption option) {
        return redisTemplate.execute((RedisCallback<L>) redisConnection -> {
            byte[] bytes = new byte[0];
            if (option == ListOption.lPop()) {
                bytes = redisConnection.lPop(rawKey(key));
            }
            if (option == ListOption.rPop()) {
                bytes = redisConnection.rPop(rawKey(key));
            }
            return bytes == null ? null : deserializeValue(bytes, valueSerializer());
        });
    }

    @SuppressWarnings("unchecked")
    private byte[] rawKey(String key) {
        return keySerializer().serialize(key);
    }

    @SuppressWarnings("unchecked")
    private byte[][] rawKeys(Collection<String> keys) {
        byte[][] rawKeys = new byte[keys.size()][];
        int i = 0;
        String key;
        for (Iterator<String> var4 = keys.iterator(); var4.hasNext(); rawKeys[i++] = keySerializer().serialize(key)) {
            key = var4.next();
        }
        return rawKeys;
    }

    @SuppressWarnings("unchecked")
    private <HK> byte[] rawHashKey(HK hk) {
        return hashKeySerializer().serialize(hk);
    }

    @SuppressWarnings({"all"})
    private <HK> byte[][] rawHashKeys(Collection<HK> hks) {
        byte[][] rawKeys = new byte[hks.size()][];
        int i = 0;
        Object hashKey;
        for (Iterator var6 = hks.iterator(); var6.hasNext(); rawKeys[i++] = hashKeySerializer().serialize(hashKey)) {
            hashKey = var6.next();
        }
        return rawKeys;
    }

    @SuppressWarnings("unchecked")
    static <T> T deserializeValue(@NonNull byte[] rawValue, @NonNull RedisSerializer<?> redisSerializer) {
        return (T) redisSerializer.deserialize(rawValue);
    }

    @SuppressWarnings("unchecked")
    static <T> List<T> deserializeValues(@NonNull List<byte[]> rawValues, @NonNull RedisSerializer<?> redisSerializer) {
        List<T> values = new ArrayList<>(rawValues.size());
        for (byte[] bs : rawValues) {
            values.add((T) redisSerializer.deserialize(bs));
        }
        return values;
    }

    @SuppressWarnings("rawtypes")
    RedisSerializer keySerializer() {
        return redisTemplate.getKeySerializer();
    }

    @SuppressWarnings("rawtypes")
    RedisSerializer hashKeySerializer() {
        return redisTemplate.getHashKeySerializer();
    }

    @SuppressWarnings("rawtypes")
    RedisSerializer valueSerializer() {
        return redisTemplate.getValueSerializer();
    }

    @SuppressWarnings("rawtypes")
    RedisSerializer hashValueSerializer() {
        return redisTemplate.getHashValueSerializer();
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        log.info("====== RedisUtil init success ======");
    }


    enum ListOption {

        /**
         * 移出并获取列表的第一个元素
         */
        L_POP,
        /**
         * 移出并获取列表的最后一个元素
         */
        R_POP;

        ListOption() {
        }

        public static ListOption lPop() {
            return L_POP;
        }

        public static ListOption rPop() {
            return R_POP;
        }
    }

}
