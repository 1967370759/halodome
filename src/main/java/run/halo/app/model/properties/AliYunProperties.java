package run.halo.app.model.properties;

public enum AliYunProperties implements PropertyEnum {
    /**
     * 阿里云 oss断点
     */
    OSS_ENDPOINT("oss_aliyun_endpoint", String.class, ""),

    OSS_BUCKET_NAME("oss_aliyun_bucket_name", String.class, ""),
    OSS_ACCESS_KEY("oss_aliyun_sccess_key", String.class, ""),
    OSS_ACCESS_SECRET("oss_aliyun_access_secret", String.class, "");


    AliYunProperties(String value, Class<?> type, String defaultValue) {
        this.type = type;
        this.value = value;
        this.defaultValue = defaultValue;
        if (!PropertyEnum.isSupportedType(type)) {
            throw new IllegalArgumentException("不支持的博客属性类型" + type);
        }
    }

    private final Class<?> type;

    private final String value;
    private final String defaultValue;

    @Override
    public Class<?> getType() {
        return type;
    }

    @Override
    public String defaultValue() {
        return defaultValue;
    }

    @Override
    public String getValue() {
        return value;
    }
}
