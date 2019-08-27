package run.halo.app.cache;

import org.springframework.lang.NonNull;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

/**
 * 缓存储存接口
 * @param <K> 缓存键类型
 * @param <V>缓存值类型
 */
public interface CacheStore<K,V> {

    /**
     *按缓存键获取
     * @param key 不能为空
     * @return 缓存值
     */
    @NonNull
    Optional<V>get(@NonNull K key);

    /**
     *放置一个将过期的缓存
     * @param key key 不能为空
     * @param value value 不能为空
     * @param timeout 密匙不能为空
     * @param timeUnit 超时单位
     */

    void put(@NonNull K key, @NonNull V value, long timeout, @NonNull TimeUnit timeUnit);

    /**
     *放置一个缓存 ，如果密匙不存在，缓存将过期
     * @param key 不能为空
     * @param value 不能为空
     * @param timeout 密匙不能为1
     * @param timeUnit 超时单位不能为空
     * @return如果键不存在且设置了值， 则为true 如果键之前存在，则为false 如果有其他原因 则为null
     */
    Boolean putIfAbsent(@NonNull K key, @NonNull V value, long timeout, @NonNull TimeUnit timeUnit);

    /**
     * 放置一个未过期的缓存
     * @param key 不能为空
     * @param value 不能为空
     */
    void put(@NonNull K key,@NonNull V value);

    /**
     * 删除一个键值
     * @param key 不能为空
     */
    void delete (@NonNull K key);


}
