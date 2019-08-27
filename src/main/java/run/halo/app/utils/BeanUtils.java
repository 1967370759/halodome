package run.halo.app.utils;

import org.springframework.util.Assert;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.BeansException;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.util.CollectionUtils;
import run.halo.app.exception.BeanUtilsException;

import java.beans.PropertyDescriptor;
import java.util.*;
import java.util.stream.Collectors;

public class BeanUtils {
    public BeanUtils() {
    }

    /**
     * 从源对象转换（只复制相同的属性
     *
     * @param source      源数据
     * @param targetClass 不能为空
     * @param <T>         目标类的类型
     * @return 从源数据复制指定类型的实例，（如果源数据为空则为空
     * @throws BeanUtilsException 如果新建目标实例失败或复制失败 抛出
     */
    @Nullable
    public static <T> T transformFrom(@Nullable Object source, @NonNull Class<T> targetClass) {
        Assert.notNull(targetClass, "Target class 不能为空");

        if (source == null)
            return null;
//        初始化该实例
        try {
//            目标类的新实例
            T targetInstance = targetClass.newInstance();
//            复制属性
            org.springframework.beans.BeanUtils.copyProperties(source, targetInstance, getNullPropertyNames(source));
//          返回目标实例
            return targetInstance;
        } catch (Exception e) {
            throw new BeanUtilsException("没有新的" + targetClass.getName() + "实例或复制属性", e);
        }
    }

    /**
     * 从批处理中的源数据收集转换
     *
     * @param sources     源数据收集
     * @param targetClass 不能为空
     * @param <T>         目标类型
     * @return 从源数据收集转换目标收集
     * @throws BeanUtilsException 如果新建目标实例化失败或复制失败抛出
     */
    @NonNull
    public static <T> List<T> transformFromInBatch(Collection<?> sources, @NonNull Class<T> targetClass) {
        if (CollectionUtils.isEmpty(sources))
            return Collections.emptyList();
//          变换在批处理
        return sources.stream()
                .map(source -> transformFrom(sources, targetClass))
                .collect(Collectors.toList());
    }

    /**
     * 更新属性（非空
     *
     * @param source 不能为空
     * @param target 不能为空
     * @throws BeanUtilsException 如果复制失败抛出
     */
    public static void updateProperties(@NonNull Object source, @NonNull Object target) {
        Assert.notNull(source, "source object 不能Wie空");
        Assert.notNull(target, "target object 不能为空");
//    从源属性设置非空属性到目标属性
        try {
            org.springframework.beans.BeanUtils.copyProperties(source, target, getNullPropertyNames(source));
        } catch (BeansException e) {
            throw new BeanUtilsException("复制属性失败" + e);
        }
    }

    /**
     * 获取属性的空名称数组
     *
     * @param source 不能为空
     * @return 属性的空名称数组
     */
    @NonNull
    private static String[] getNullPropertyNames(@NonNull Object source) {
        return getNullPropertyNameSet(source).toArray(new String[0]);
    }

    /**
     * 获取属性的空名称集
     *
     * @param source 不能为空
     * @return 属性的空名称集
     */
    @NonNull
    private static Set<String> getNullPropertyNameSet(@NonNull Object source) {
        Assert.notNull(source, "source object 不能为空");

        BeanWrapperImpl beanWrapper = new BeanWrapperImpl(source);
        PropertyDescriptor[] propertyDescriptors = beanWrapper.getPropertyDescriptors();

        Set<String> emptyNames = new HashSet<>();

        for (PropertyDescriptor propertyDescriptor : propertyDescriptors) {
            String propertyName = propertyDescriptor.getName();

            Object propertyValue = beanWrapper.getPropertyValue(propertyName);
//            如果属性值等于null 则添加到空名称集
            if (propertyValue == null)
                emptyNames.add(propertyName);
        }
        return emptyNames;
    }
}
