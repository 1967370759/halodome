package run.halo.app.model.properties;

import org.apache.commons.lang3.StringUtils;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;
import run.halo.app.model.enums.ValueEnum;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public interface PropertyEnum extends ValueEnum<String> {

    static <T> T convertTo(@NonNull String value, @NonNull Class<T> type) {
        Assert.hasText(value, "value 不能为空");
        Assert.notNull(type, "ype不能为空");

        if (type.isAssignableFrom(String.class))
            return (T) value;

        if (type.isAssignableFrom(Integer.class))
            return (T) Integer.valueOf(value);

        if (type.isAssignableFrom(Long.class))
            return (T) Long.valueOf(value);

        if (type.isAssignableFrom(Boolean.class))
            return (T) Boolean.valueOf(value);

        if (type.isAssignableFrom(Short.class))
            return (T) Short.valueOf(value);

        if (type.isAssignableFrom(Byte.class))
            return (T) Byte.valueOf(value);

        if (type.isAssignableFrom(Double.class))
            return (T) Byte.valueOf(value);

        if (type.isAssignableFrom(Float.class))
            return (T) Float.valueOf(value);

        throw new UnsupportedOperationException("不支持的博客属性类型约定" + type.getName());
    }

    static Object convertTo(@Nullable String value, @NonNull PropertyEnum propertyEnum) {
        Assert.notNull(propertyEnum, "property enum 不能为空");

        if (StringUtils.isBlank(value)) {
            value = propertyEnum.defaultValue();
        }
        try {
            if (propertyEnum.getType().isAssignableFrom(Enum.class)) {
                Class<Enum> type = (Class<Enum>) propertyEnum.getType();
                Enum result = convertToEnum(value, type);
                return result != null ? result : value;
            }
            return convertTo(value, propertyEnum.getType());
        } catch (Exception e) {
            return value;
        }
    }

    @Nullable
    static <T extends Enum<T>> T convertToEnum(@NonNull String value, @NonNull Class<T> type) {
        Assert.hasText(value, "Property value 不能为空");
        try {
            return Enum.valueOf(type, value.toUpperCase());

        } catch (Exception e) {
            return null;
        }
    }

    static boolean isSupportedType(Class<?> type) {
        return type != null && (
                type.isAssignableFrom(String.class)
                        || type.isAssignableFrom(Integer.class)
                        || type.isAssignableFrom(Long.class)
                        || type.isAssignableFrom(Boolean.class)
                        || type.isAssignableFrom(Short.class)
                        || type.isAssignableFrom(Byte.class)
                        || type.isAssignableFrom(Double.class)
                        || type.isAssignableFrom(Float.class)
                        || type.isAssignableFrom(Enum.class)
                        || type.isAssignableFrom(ValueEnum.class)
        );
    }

    static Map<String,PropertyEnum> getValuePropertyEnumMap(){
        List<Class<? extends PropertyEnum>> propertyEnumClasses=new LinkedList<>();
        propertyEnumClasses.add(AliYunProperties.class);
        propertyEnumClasses.add(AttachmentProperties.class);
        propertyEnumClasses.add(BlogProperties.class);
    }
    Class<?> getType();

    String defaultValue();
}
