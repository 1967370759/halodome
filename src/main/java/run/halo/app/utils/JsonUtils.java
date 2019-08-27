package run.halo.app.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;

import java.io.IOException;
import java.util.Map;

/**
 * json 工具类
 */
public class JsonUtils {
    /**
     * 默认的json映射器
     */
    public final static ObjectMapper DEFAULT_JSON_MAPPER = createDefaultJsonMapper();

    private JsonUtils() {
    }

    /**
     * 创建默认json映射器
     * @return 对象映射器
     */
    private static ObjectMapper createDefaultJsonMapper() {
        return createDefaultJsonMapper(null);
    }

    /**
     * 创建默认json映射器
     * @param strategy 属性命名 strategy
     * @return 对象映射器
     */
    @NonNull
    public static ObjectMapper createDefaultJsonMapper(@Nullable PropertyNamingStrategy strategy) {
        //创建对象映射器
        ObjectMapper mapper = new ObjectMapper();
        //安装
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        //设置属性命名策略
        if (strategy != null) {
            mapper.setPropertyNamingStrategy(strategy);
        }
        return mapper;
    }

    /**
     * 将json转换为指定的对象类型
     * @param json 不能为空
     * @param type 对象类型不能为空
     * @param <T> 目标对象类型
     * @return 对象指定类型
     * @throws IOException 转换失败时抛出
     */
    @NonNull
    public static <T> T jsonToObject(@NonNull String json, @NonNull Class<T> type) throws IOException {
        return jsonToObject(json, type, DEFAULT_JSON_MAPPER);
    }

    /**
     * 将json转换为指定的对象类型
     * @param json 不能为空
     * @param type 对象类型不能为空
     * @param objectMapper  不能为空
     * @param <T> 目标对象类型
     * @return 对象指定类型
     * @throws IOException 转换失败时抛出
     */
    @NonNull
    private static <T> T jsonToObject(String json, Class<T> type, ObjectMapper objectMapper) throws IOException {
        Assert.hasText(json, "json 内容不能为空");
        Assert.notNull(type, " type 不能为空");
        Assert.notNull(objectMapper, "对象映射器不能为空");
        return objectMapper.readValue(json, type);

    }

    /**
     * 将对象转换为json格式
     * @param source 不能为空
     * @return  source 转换为json格式
     * @throws JsonProcessingException 转换失败时抛出
     */
    @NonNull
    public static String objectToJson(@NonNull Object source) throws JsonProcessingException {
        return objectToJson(source, DEFAULT_JSON_MAPPER);
    }

    /**
     * 将对象转换为json格式
     * @param source 不能为空
     * @param objectMapper 不能为空
     * @return source 转换为json格式
     * @throws JsonProcessingException 转换失败时抛出
     */
    @NonNull
    private static String objectToJson(@NonNull Object source, @NonNull ObjectMapper objectMapper) throws JsonProcessingException {
        Assert.notNull(source, "source 不能为空");
        Assert.notNull(objectMapper, "objectMapper 不能为空");
        return objectMapper.writeValueAsString(source);
    }

    /**
     * 将映射转换为指定的对象类型
     * @param sourceMap 不能为空
     * @param type 不能为空
     * @param <T>  目标对象类型
     * @return 对象指定的类型
     * @throws IOException 转换失败时抛出
     */
    @NonNull
    public static <T> T mapToObject(@NonNull Map<String, ?> sourceMap, @NonNull Class<T> type) throws IOException {
        return mapToObject(sourceMap, type, DEFAULT_JSON_MAPPER);
    }

    /**
     * 将映射转换为指定的对象类型
     * @param sourceMap 不能为空
     * @param type 不能为空
     * @param objectMapper 不能为空
     * @param <T> 目标对象类型
     * @return 对象指定的类型
     * @throws IOException 转换失败时抛出
     */
    public static <T> T mapToObject(@NonNull Map<String, ?> sourceMap, @NonNull Class<T> type, @NonNull ObjectMapper objectMapper) throws IOException {
        Assert.notEmpty(sourceMap, "sourceMap 不能为空");
        //序列化的map
        String json = objectToJson(sourceMap, objectMapper);
        //反序列化映射的json格式
        return jsonToObject(json, type, objectMapper);
    }

    /**
     * 将source 转换为映射
     * @param source 不能为空
     * @return 不能为空
     * @throws IOException 转换失败是抛出
     */
    public static Map<?, ?> objectToMap(@NonNull Object source) throws IOException {
        return objectToMap(source, DEFAULT_JSON_MAPPER);
    }

    /**
     * 将source 对象转为映射
     * @param source 不能为空
     * @param objectMapper 不能为空
     * @return a map
     * @throws IOException 转换失败时抛出
     */
    @NonNull
    private static Map<?, ?> objectToMap(@NonNull Object source, @NonNull ObjectMapper objectMapper) throws IOException {
        //系列化 source 对象
        String json = objectToJson(source, objectMapper);
        //反序列化 json
        return jsonToObject(json, Map.class, objectMapper);
    }
}

