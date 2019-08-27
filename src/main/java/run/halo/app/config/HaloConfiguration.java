    package run.halo.app.config;


    import com.fasterxml.jackson.databind.ObjectMapper;
    import org.apache.catalina.filters.CorsFilter;
    import org.jdom.filter.ContentFilter;
    import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
    import org.springframework.boot.context.properties.EnableConfigurationProperties;
    import org.springframework.boot.web.client.RestTemplateBuilder;
    import org.springframework.boot.web.servlet.FilterRegistrationBean;
    import org.springframework.context.annotation.Bean;
    import org.springframework.context.annotation.Configuration;
    import org.springframework.core.Ordered;
    import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
    import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
    import org.springframework.web.client.RestTemplate;
    import run.halo.app.cache.InMemoryCacheStore;
    import run.halo.app.cache.StringCacheStore;
    import run.halo.app.config.properties.HaloProperties;
    import run.halo.app.filter.LogFilter;
    import run.halo.app.utils.HttpClientUtils;

    import java.security.KeyManagementException;
    import java.security.KeyStoreException;
    import java.security.NoSuchAlgorithmException;

    /**
     * halo 配置
     */
    @Configuration
    @EnableConfigurationProperties(HaloProperties.class)
    public class HaloConfiguration {

        private final static int TIMEOUT = 5000;

        @Bean
        public ObjectMapper objectMapper(Jackson2ObjectMapperBuilder builder) {
            builder.failOnEmptyBeans(false);
            return builder.build();
        }

        @Bean
        public RestTemplate httpsRestTemplate(RestTemplateBuilder builder) throws NoSuchAlgorithmException, KeyStoreException, KeyManagementException {
            RestTemplate httpsRestTemplate = builder.build();
            httpsRestTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory(HttpClientUtils.createHttpsClient(TIMEOUT)));
            return httpsRestTemplate;
        }

        @Bean
        @ConditionalOnMissingBean
        public StringCacheStore stringCacheStore() {
            return new InMemoryCacheStore();
        }

        /**
         * 创建一个 CorsFilter
         * @return corsfilter 注册bean
         */
        @Bean
        public FilterRegistrationBean<CorsFilter> corsFilter() {
            FilterRegistrationBean<CorsFilter> corsFilter = new FilterRegistrationBean<>();
            corsFilter.setOrder(Ordered.HIGHEST_PRECEDENCE + 10);
            corsFilter.setFilter(new CorsFilter());
            corsFilter.addUrlPatterns("/api/*");
            return corsFilter;
        }

        /**
         * 创建一个日志过滤器
         * @return 日志过滤器登记bean
         */
        @Bean
        public FilterRegistrationBean<LogFilter> contentFilter(){
            FilterRegistrationBean<LogFilter> logfilter=new FilterRegistrationBean<>();

            logfilter.setOrder(Ordered.HIGHEST_PRECEDENCE+9);
            logfilter.setFilter(new LogFilter());
            logfilter.addUrlPatterns("/*");
            return logfilter;
        }
        public FilterRegistrationBean<ContentFilter> contentFilter(HaloProperties haloProperties,
                                                                   OptionService){}
    }
