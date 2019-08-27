package run.halo.app.model.dto.base;

import org.springframework.lang.Nullable;
import run.halo.app.utils.BeanUtils;
import run.halo.app.utils.ReflectionUtils;

import java.lang.reflect.ParameterizedType;
import java.util.Objects;

/**
 * 用于输入DTO的转换器接口
 *
 * @param <DOMAIN>
 */
public interface InputConverter<DOMAIN> {
    /**
     * @return 具有相同值的新域（不为空
     */
    @SuppressWarnings("unchecked")
    default DOMAIN convertTo() {
//        得到参数化类型
        ParameterizedType currentType = parameterizedType();

        Objects.requireNonNull(currentType, "无法获取实际类型，因为参数化类型为null");

        Class<DOMAIN> domainClass = (Class<DOMAIN>) currentType.getActualTypeArguments()[0];

        return BeanUtils.transformFrom(this, domainClass);

    }

    /**
     * 通过dto更新
     * @param domain
     */
    default void update(DOMAIN domain) {
        BeanUtils.updateProperties(this, domain);
    }

    /**
     * 得到参数化类型
     * @return 参数化类型或null
     */
    @Nullable
    default ParameterizedType parameterizedType() {
        return ReflectionUtils.getParameterizedType(InputConverter.class, this.getClass());
    }
}
