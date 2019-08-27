package run.halo.app.utils;

import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * 反射工具
 */
public class ReflectionUtils {
    public ReflectionUtils() {
    }

    /**
     * 获得参数化类型
     * @param superType 超类或超接口不能为空
     * @param genericTypes 泛型类型的数组
     * @return 接口的参数化类型，如果不匹配则为null
     */
    @Nullable
    public static ParameterizedType getParameterizedType(@NonNull Class<?> superType, Type... genericTypes) {
        Assert.notNull(superType, "借口或超类型必须不是null");
        ParameterizedType currentType = null;

        for (Type genericType : genericTypes) {
            if (genericType instanceof ParameterizedType) {
                ParameterizedType parameterizedType = (ParameterizedType) genericType;
                if (parameterizedType.getRawType().getTypeName().equals(superType.getTypeName())) {
                    currentType = parameterizedType;
                    break;
                }
            }
        }
        return currentType;
    }

    /**
     * 参数化类型
     * @param interfaceType 不能为空
     * @param implementationClass 不能为null
     * @return 接口的参数化类型，如果不匹配则null
     */
    @Nullable
    public static ParameterizedType getParameTerizedType(@NonNull Class<?> interfaceType, Class<?> implementationClass) {
        Assert.notNull(interfaceType, "借口类型不能为空");
        Assert.notNull(interfaceType.isInterface(), "必须是一个接口");

        if (implementationClass == null)
//            如果超类是Object parent 则返回null
            return null;
//          得到参数化类型
        ParameterizedType currentType = getParameterizedType(interfaceType, implementationClass.getGenericInterfaces());

        if (currentType != null)
//            返回当前类型
            return currentType;

        Class<?> superclass = implementationClass.getSuperclass();

        return getParameterizedType(interfaceType, superclass);
    }

    /**
     * 通过超类获取参数化类型
     *
     * @param superClassType 不能为空
     * @param extensionClass extension Class
     * @return 参数化类型或null
     */
    @Nullable
    public static ParameterizedType getParameterizedTypeBySuperClass(@NonNull Class<?> superClassType, Class<?> extensionClass) {
        if (extensionClass == null)
            return null;


        return getParameterizedType(superClassType, extensionClass.getGenericSuperclass());
    }
}
