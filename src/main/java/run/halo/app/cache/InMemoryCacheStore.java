package run.halo.app.cache;

import lombok.extern.slf4j.Slf4j;
import org.hibernate.cache.CacheException;import org.springframework.util.Assert;

import javax.annotation.PreDestroy;
import java.util.Optional;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 内存缓存存键
 */
@Slf4j
public class InMemoryCacheStore extends StringCacheStore{
    /**
     * 清除时间表（ms)
     */
    private final static long PERIOD=60*1000;
    /**
     *缓存的容器
     */
    private final static ConcurrentHashMap<String ,CacheWrapper<String>> cacheContainer=new ConcurrentHashMap<>();

    /**
     *
     */
    private final Timer timer;
    /**
     * lock
     */
    private Lock lock=new ReentrantLock();

    public InMemoryCacheStore(){
        timer=new Timer();
        timer.scheduleAtFixedRate(new CacheExpiryCleaner(),0,PERIOD);
    }
    @Override
    Optional<CacheWrapper<String>> getInternal(String key) {
        Assert.hasText(key,"key 不能为空");
        return Optional.ofNullable(cacheContainer.get(key));
    }

    @Override
    void putInternal(String key, CacheWrapper<String> cacheWrapper) {
        Assert.hasText(key,"key 不能为空");
        Assert.notNull(cacheWrapper,"cacheWrapper 不能为空");
        //放入缓存包装器
        CacheWrapper<String> putCacheWrapper=cacheContainer.put(key,cacheWrapper);
        log.debug("Put [{}] cache result: [{}], original cache wrapper: [{}]",key,putCacheWrapper,cacheWrapper);

    }

    @Override
    Boolean putInternalIfAbsent(String key, CacheWrapper<String> cacheWrapper) {
        Assert.hasText(key,"key 不能为空");
        Assert.notNull(cacheWrapper,"cacheWrapper 不能为空");
        log.debug("Preparing to put key: [{}], value: [{}]",key,cacheWrapper);
try {


    lock.lock();
    Optional<String> valueOptional = get(key);
    if (valueOptional.isPresent()) {
        log.warn("Failed to put the cache, because the key: [{}] has been present already", key);
        return false;
    }
    //放入缓存包装器
    putInternal(key,cacheWrapper);
    log.debug("Put successfully");
    return true;
} finally {
    lock.unlock();
}

    }

    @Override
    public void delete(String key) {
    Assert.hasText(key,"key 不能为空");
    cacheContainer.remove(key);
    log.debug("删除 key :[{}]",key);
    }

    @PreDestroy
    public void preDestroy(){
        log.debug("取消所有定时任务");
        timer.cancel();
    }

    /**
     * 缓存清除
     */
    private class CacheExpiryCleaner extends TimerTask {

        @Override
        public void run() {
            cacheContainer.keySet().forEach(key->{
                if (!InMemoryCacheStore.this.get(key).isPresent()){
                    log.debug("删除过期键:[{}]",key);
                }
            });
        }
    }
}
