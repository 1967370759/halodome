package run.halo.app.model.enums;

import org.springframework.util.Assert;

import java.util.stream.Stream;

public interface ValueEnum<T> {

    static <V,E extends ValueEnum<V>> E valueToEnum(Class<E> enumType,V value){
        Assert.notNull(enumType,"enum type 不能为空");
        Assert.notNull(value,"value 不能为空");
        Assert.isTrue(enumType.isEnum(),"类型必须是枚举类型");

        return Stream.of(enumType.getEnumConstants())
                .filter(item->item.getValue().equals(value))
                .findFirst()
                .orElseThrow(()->new IllegalArgumentException("未知的数据库值"+value));
    }
    T getValue();
}
