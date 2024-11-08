package com.baogutang.frame.redis.utils;

import com.google.common.collect.Sets;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisStringCommands;
import org.springframework.data.redis.connection.ReturnType;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.data.redis.core.types.Expiration;
import org.springframework.integration.redis.util.RedisLockRegistry;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;

/**
 * RedisUtils
 *
 */
@Component
@Slf4j
public class RedisUtils {

  // 默认过期时间
  private static final long DEFAULT_EXPIRE_UNUSED = 60000L;

  @Resource private RedisTemplate<String, Object> redisTemplate;

  @Resource private RedisLockRegistry redisLockRegistry;

  private static RedisTemplate<String, Object> staticRedisTemplate;

  private static RedisLockRegistry staticRedisLockRegistry;

  @PostConstruct
  public void init() {
    RedisUtils.staticRedisTemplate = redisTemplate;
    RedisUtils.staticRedisLockRegistry = redisLockRegistry;
  }

  private static class SingletonHolder {
    private static final RedisUtils factory = new RedisUtils();
  }

  public static RedisUtils getInstance() {
    return SingletonHolder.factory;
  }

  /**
   * 在Redis中放入值
   *
   * @param key
   * @param value
   */
  public void setRedis(String key, Object value) {
    staticRedisTemplate.opsForValue().set(key, value);
    staticRedisTemplate.expire(key, 30, TimeUnit.SECONDS);
  }

  /**
   * 给指定的key对应的key-value设置: 多久过时
   *
   * 注:过时后，redis会自动删除对应的key-value。
   * 注:若key不存在，那么也会返回false。
   *
   * @param key
   *            指定的key
   * @param timeout
   *            过时时间
   * @param unit
   *            timeout的单位
   * @return  操作是否成功
   * @date 2020/3/8 12:18:58
   */
  public  boolean expire(String key, long timeout, TimeUnit unit) {
    Boolean result = staticRedisTemplate.expire(key, timeout, unit);
    return result;
  }

  /**
   * 在Redis中放入值，并指定有效时间
   *
   * @param key
   * @param value
   * @param expire
   * @param timeUnit
   */
//  public void setRedis(String key, Object value, long expire, TimeUnit timeUnit) {
//    staticRedisTemplate.opsForValue().set(key, value);
//    if (expire > 0) {
//      staticRedisTemplate.expire(key, expire, timeUnit);
//    }
//  }

  /**
   * 在Redis中放入值，并指定有效时间
   *
   * @param key
   * @param value
   * @param expire
   * @param timeUnit
   */
  public <T> void setRedis(String key, T value, long expire, TimeUnit timeUnit) {
    staticRedisTemplate.opsForValue().set(key, value);
    if (expire > 0) {
      staticRedisTemplate.expire(key, expire, timeUnit);
    }
  }

  public void setHashMap(String key, Map<String, Object> map) {
    HashOperations hashOper = staticRedisTemplate.opsForHash();
    hashOper.putAll(key, map);
  }

  public void setHashMap(String key, Map<String, Object> map, long expire, TimeUnit timeUnit) {
    HashOperations hashOper = staticRedisTemplate.opsForHash();
    hashOper.putAll(key, map);
    if (expire > 0) {
      staticRedisTemplate.expire(key, expire, timeUnit);
    }
  }

  public void increHashKey(String key, String hashKey, long value) {
    HashOperations hashOper = staticRedisTemplate.opsForHash();
    hashOper.increment(key, hashKey, value);
  }

  /**
   * 删除Redis中的数据
   *
   * @param key
   */
  public void removeRedis(String key) {
    staticRedisTemplate.delete(key);
  }

  /**
   * 获取Redis中值的有效期
   *
   * @param key
   */
  public Long getRedisExpire(String key) {
    return staticRedisTemplate.getExpire(key, TimeUnit.SECONDS);
  }

  /**
   * 从Redis中获取值
   *
   * @param key
   * @return
   */
//  public Object getRedis(String key) {
//    return staticRedisTemplate.opsForValue().get(key);
//  }

  public <T> T getRedis(String key) {
    return (T) staticRedisTemplate.opsForValue().get(key);
  }

  /**
   * 从Redis中获取值，并重置有效期
   *
   * @param key
   * @return
   */
  public Object updateRedisResetExpire(String key, int expireTime, TimeUnit timeUnit) {
    Object sessionObj = getRedis(key);
    if (sessionObj != null) {
      // 重置有效时间
      staticRedisTemplate.expire(key, expireTime, timeUnit);
    }
    return sessionObj;
  }

  /**
   * 根据表达式，获取所有键的集合
   *
   * @param pattern 表达式格式，如：key_*
   * @return
   */
  public Set<String> getRedisKeys(String pattern) {
    Set<String> keys = Sets.newConcurrentHashSet();
    Set<byte[]> byteKeys =
        staticRedisTemplate.getConnectionFactory().getConnection().keys(pattern.getBytes());
    Iterator<byte[]> it = byteKeys.iterator();
    while (it.hasNext()) {
      byte[] data = it.next();
      keys.add(new String(data, 0, data.length));
    }
    return keys;
  }

  public boolean hasKey(String key) {

    return Boolean.TRUE.equals(staticRedisTemplate.hasKey(key));
  }

  /**
   * 一次获取多个值
   *
   * @param keys
   * @return
   */
  public Collection<Object> getRedisMulti(String... keys) {
    if (keys == null && keys.length == 0) {
      Collections.emptySet();
    }
    Collection<String> collection = new ArrayList<>();
    for (int i = 0; i < keys.length; i++) {
      collection.add(keys[i]);
    }
    return staticRedisTemplate.opsForValue().multiGet(collection);
  }

  /**
   * 根据Key获取已经保存到Redis中所有的值,Session级别
   *
   * @param key 键
   * @return map数据
   */
  public Map<String, Object> getAllMap(String key) {
    if ("".equals(key) || (null == key)) {
      return null;
    }
    Set<Object> keys = Collections.singleton(staticRedisTemplate.keys("*_" + key));
    Map<String, Object> map = new ConcurrentHashMap<>();
    if (keys != null && keys.size() > 0) {
      for (Object hashKey : keys) {
        Object text = getHashValue(String.valueOf(hashKey), key);
        map.put(String.valueOf(hashKey), text);
      }
    }
    return map;
  }

  /**
   * 根据Key和HashKey获取一个值，返回String
   *
   * @param key 键
   * @param hashKey has键
   * @return String 缓存数据
   */
  public <T extends Serializable> T getHashValue(String key, String hashKey) {
    //Object obj = staticRedisTemplate.opsForHash().get(key, hashKey);
    return staticRedisTemplate.execute(new RedisCallback<T>() {
      @Override
      @SneakyThrows
      public T doInRedis(@NonNull RedisConnection connection) throws DataAccessException {
        byte[] keyByte = staticRedisTemplate.getStringSerializer().serialize(key);
        byte[] hashKeyByte = staticRedisTemplate.getStringSerializer().serialize(hashKey);
        byte[] bytes = connection.hGet(keyByte, hashKeyByte);
        Object deserialize = staticRedisTemplate.getHashValueSerializer().deserialize(bytes);
        return (T) deserialize;
      }
    });
  }

  /**
   * 根据Key和HashKey删除一个值
   *
   * @param key 键
   * @param hashKey has键
   */
  public void deleteHashValue(String key, String hashKey) {
    staticRedisTemplate.opsForHash().delete(key, hashKey);
  }

  public Map<Object, Object> getHashMap(String hashKey) {
    if (StringUtils.isBlank(hashKey)) {
      return new HashMap();
    }
    return staticRedisTemplate.boundHashOps(hashKey).entries();
  }

  /**
   * 对象加锁；无过期时间，需要手动释放锁
   *
   * @param lockKey
   */
  public void lock(String lockKey) {
    Lock lock = obtainLock(lockKey);
    lock.lock();
  }

  public boolean tryLock(String lockKey) {
    Lock lock = obtainLock(lockKey);
    return lock.tryLock();
  }

  /**
   * 对指定的key加锁，并在指定的时间之后过期
   *
   * @param lockKey
   * @param seconds
   * @return
   */
  public boolean tryLock(String lockKey, long seconds) {
    Lock lock = obtainLock(lockKey);
    try {
      return lock.tryLock(seconds, TimeUnit.SECONDS);
    } catch (InterruptedException e) {
      return false;
    }
  }

  /**
   * 给指定的key加锁；锁定的Key将于默认时间（60s)之后自动过期；
   *
   * @param lockKey
   */
  public void unlock(String lockKey) {
    try {
      Lock lock = obtainLock(lockKey);
      lock.unlock();
      staticRedisLockRegistry.expireUnusedOlderThan(DEFAULT_EXPIRE_UNUSED);
    } catch (Exception e) {
      log.error("Distributed lock [{}] release exception", lockKey, e);
    }
  }

  private Lock obtainLock(String lockKey) {
    return staticRedisLockRegistry.obtain(lockKey);
  }

  public boolean lock(String key, String value, long timeout, TimeUnit timeUnit) {
    // 加锁，也可以使用 stringRedisTemplate.opsForValue().setIfAbsent(key, value, 15, TimeUnit.SECONDS);
    // Expiration.from(timeout, timeUnit) 过期时间和单位
    // RedisStringCommands.SetOption.SET_IF_ABSENT ,等同于 NX 当 key 不存在时候才可以
    Boolean lockStat =
        staticRedisTemplate.execute(
            (RedisCallback<Boolean>)
                connection ->
                    connection.set(
                        key.getBytes(StandardCharsets.UTF_8),
                        value.getBytes(StandardCharsets.UTF_8),
                        Expiration.from(timeout, timeUnit),
                        RedisStringCommands.SetOption.SET_IF_ABSENT));
    return lockStat != null && lockStat;
  }

  public boolean unlock(String key, String value) {
    try {
      // 释放锁使用 Lua 脚本，验证传入的 key--value 跟 Redis 中是否一样
      String script =
          "if redis.call('get', KEYS[1]) == ARGV[1] then return redis.call('del', KEYS[1]) else return 0 end";
      Long unLockStat =
          staticRedisTemplate.execute(
              (RedisCallback<Long>)
                  connection ->
                      connection.eval(
                          script.getBytes(),
                          ReturnType.INTEGER,
                          1,
                          key.getBytes(StandardCharsets.UTF_8),
                          value.getBytes(StandardCharsets.UTF_8)));
      return "1".equals(null == unLockStat ? "0" : unLockStat.toString());
    } catch (Exception e) {
      log.error("Unlock failed key = {}", key);
      return false;
    }
  }

  /**
   * 增加
   *
   * @param key
   * @param
   * @return
   */
  public static long increment(String key) {
    if (StringUtils.isBlank(key)) {
      return 0;
    }
    return staticRedisTemplate.opsForValue().increment(key);
  }

  public static long decrement(String key) {
    if (StringUtils.isBlank(key)) {
      return 0;
    }
    return staticRedisTemplate.opsForValue().decrement(key);
  }

  /**
   * 原子自增
   *
   * @param key
   * @param value
   * @param timeout
   * @param unit
   * @return
   */
  public long increment(String key, long value, long timeout, TimeUnit unit) {
    Long increment = redisTemplate.opsForValue().increment(key, value);
    if (timeout > 0) {
      redisTemplate.expire(key, timeout, unit);
    }
    return increment;
  }

  public long decrement(String key, long value, long timeout, TimeUnit unit) {
    Long decrement = redisTemplate.opsForValue().decrement(key, value);
    if (timeout > 0) {
      redisTemplate.expire(key, timeout, unit);
    }
    return decrement;
  }

  /**
   * 刷新redis中hash中属性的值
   * @param key 键值
   * @param hashKey hashkey
   * @param value 值
   */
  public void flushHashRedis(String key,String hashKey,Object value,long timeout, TimeUnit unit){
    if (redisTemplate.hasKey(key)){
      redisTemplate.opsForHash().put(key, hashKey,value);
      redisTemplate.expire(key, timeout, unit);
    }
  }

  /**
   * @Description 获取多个hash-key对应的值
   * @Date 2021-12-22 15:50
   */
  public <T> List<T> getHashValueByHashKeys(@NonNull String key, List<String> hashKeys){
    byte[][] rawHashKeys = new byte[hashKeys.size()][];
    int counter = 0;
    for (String hashKey : hashKeys) {
      rawHashKeys[counter++] = staticRedisTemplate.getStringSerializer().serialize(hashKey);
    }
    List<byte[]> bytes = staticRedisTemplate.execute((RedisCallback<List<byte[]>>) connection -> {
      byte[] keyByte = staticRedisTemplate.getStringSerializer().serialize(key);
      return connection.hMGet(keyByte, rawHashKeys);
    });
    List<T> list = new ArrayList<>();
    bytes.forEach(valueByte -> {
      T deserialize = (T)staticRedisTemplate.getHashValueSerializer().deserialize(valueByte);
      list.add(deserialize);
    });
    return list;
  }

  /**
   * @Description 请勿使用此方法，此方法因序列化问题会导致存入redis的值取不出来。
   * @Date 2021-12-22 15:50
   */
  public Long deleteAndSetHashMap(String key,Map<String, Object> map,Long expireTime){
    List<String> keys = new ArrayList<>();
    keys.add(key);
    Object[] values = new Object[map.size()*2 + 1];
    int index = 0;
    for (Map.Entry<String, Object> entry : map.entrySet()) {
      values[index++] = entry.getKey();
      values[index++] = entry.getValue();
    }
    values[values.length-1] = expireTime;
    String script = "redis.call('del',KEYS[1])  for i=1, #ARGV do if i%2 == 0 then redis.call('HSET',KEYS[1],string.gsub(ARGV[i-1],'\"',''),ARGV[i]) end end redis.call('EXPIRE',KEYS[1],ARGV[#ARGV]) return #ARGV/2";
    DefaultRedisScript<Long> redisScript = new DefaultRedisScript<>();
    redisScript.setResultType(Long.class);
    redisScript.setScriptText(script);
    return staticRedisTemplate.execute(redisScript, keys, values);
  }
}
