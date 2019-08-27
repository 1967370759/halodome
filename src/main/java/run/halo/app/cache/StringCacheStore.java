package run.halo.app.cache;


import com.aliyun.oss.ServiceException;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.Assert;
import run.halo.app.utils.JsonUtils;

import java.io.IOException;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

/**
 * 字符串数据缓存
 */
@Slf4j
public abstract class StringCacheStore extends AbstractCacheStore<String, String> {

    public <T> void putAny(String key, T value) {
        try {
            put(key, JsonUtils.objectToJson(value));
        } catch (JsonProcessingException e) {
           throw new ServiceException("失败转换" + value+"为 json",e);
        }
    }
    public <T> void putAny(@NonNull String key, @NonNull T value, long timeout, @NonNull TimeUnit timeUnit){
        try {
            put(key,JsonUtils.objectToJson(value),timeout,timeUnit);
        } catch (JsonProcessingException e) {
          throw new ServiceException("失败转换" +value+"为json" ,e);
        }
    }
    public <T>Optional<T> getAny(String key,Class<T> type){
        Assert.notNull(type,"type 不能为空");
        return get(key).map(value ->{
            try {
                return JsonUtils.jsonToObject(value,type);
            } catch (IOException e) {
             log.error("未能将json转换为 type"+type.getName(),e);
             return null;
            }
        });
    }

}
