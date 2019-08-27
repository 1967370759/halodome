package run.halo.app.cache;

import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;
import run.halo.app.utils.DateUtils;


import java.util.Date;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

/**
 * 抽象的数据缓存
 *
 * @param <K>
 * @param <V>
 */
@Slf4j
public abstract class AbstractCacheStore<K, V> implements CacheStore<K, V> {

    /**
     * 按键获取缓存包装器
     * @param key key 不能为空
     * @return 一个可选择的缓存包装器
     */
    @NonNull
    abstract Optional<CacheWrapper<V>> getInternal(@NonNull K key);

    /**
     * 放置缓存包装器
     * @param key key 不能为空
     * @param cacheWrapper 缓存包装器不能为空
     */
    abstract void putInternal(@NonNull K key, @NonNull CacheWrapper<V> cacheWrapper);

    /**
     * 如果没有 key ，则放置缓存包装器
     * @param key key 不能为空
     * @param cacheWrapper 缓存包装器不能为空
     * @return 如果key不存在且设置了值，则为true 如果key之前存在，则为false 如果有其他原因，则为null
     */
    abstract Boolean putInternalIfAbsent(@NonNull K key, @NonNull CacheWrapper<V> cacheWrapper);

    @Override
    public Optional<V> get(K key) {
        Assert.notNull(key, "缓存键不能为空");
        return getInternal(key).map(cacheWrapper -> {
            if (cacheWrapper.getExpireAt() != null && cacheWrapper.getExpireAt().before(DateUtils.now())) {
                //删除 键值
                delete(key);
                //返回为空
                return null;
            }
            return cacheWrapper.getData();
        });
    }

    @Override
    public void put(K key, V value, long timeout, TimeUnit timeUnit) {
        putInternal(key, buildCacheWrapper(value, timeout, timeUnit));

    }

    @Override
    public Boolean putIfAbsent(K key, V value, long timeout, TimeUnit timeUnit) {
        return putInternalIfAbsent(key, buildCacheWrapper(value, timeout, timeUnit));
    }

    @Override
    public void put(K key, V value) {
        putInternal(key, buildCacheWrapper(value, 0, null));
    }

    /**
     * 包装器构建缓存
     * @param value 缓存值不能为空
     * @param timeout 关键过期时间，如果过去时间小于1，缓存将不会过期
     * @param timeUnit 超时单位
     * @return 缓存包装
     */
    @NonNull
    private CacheWrapper<V> buildCacheWrapper(@NonNull V value, long timeout, @Nullable TimeUnit timeUnit) {
        Assert.notNull(value, "缓存值不能为空");
        Assert.isTrue(timeout >= 0, "缓存过期超时必须不能小于1");
        Date now = DateUtils.now();
        Date expireAt = null;
        if (timeout > 0 && timeUnit != null) {
            expireAt = DateUtils.add(now, timeout, timeUnit);

        }
        //建立缓存包装
        CacheWrapper<V> cacheWrapper = new CacheWrapper<>();
        cacheWrapper.setCreateAt(now);
        cacheWrapper.setExpireAt(expireAt);
        cacheWrapper.setData(value);
        return cacheWrapper;
    }
}
