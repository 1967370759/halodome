package run.halo.app.config;

import com.fasterxml.classmate.TypeResolver;
import io.swagger.models.auth.In;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.lang.NonNull;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMethod;
import run.halo.app.config.properties.HaloProperties;
import run.halo.app.model.entity.User;
import run.halo.app.security.filter.AdminAuthenticationFilter;
import run.halo.app.security.filter.ApiAuthenticationFilter;
import run.halo.app.security.support.UserDetail;
import springfox.documentation.builders.*;
import springfox.documentation.schema.AlternateTypeRule;
import springfox.documentation.schema.AlternateTypeRuleConvention;
import springfox.documentation.service.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger.web.SecurityConfiguration;
import springfox.documentation.swagger.web.SecurityConfigurationBuilder;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.lang.reflect.Array;
import java.lang.reflect.Type;
import java.time.temporal.Temporal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static run.halo.app.model.support.HaloConst.HALO_VERSION;
import static springfox.documentation.schema.AlternateTypeRules.newRule;

@EnableSwagger2
@Configuration
@Slf4j
public class swaggerConfiguration {

    private final HaloProperties haloProperties;

    private final List<ResponseMessage> globalResponses = Arrays.asList(
            new ResponseMessageBuilder().code(200).message("成功").build(),
            new ResponseMessageBuilder().code(400).message("错误请求").build(),
            new ResponseMessageBuilder().code(401).message("非法的").build(),
            new ResponseMessageBuilder().code(403).message("被禁止的").build(),
            new ResponseMessageBuilder().code(404).message("为找到").build(),
            new ResponseMessageBuilder().code(500).message("内部服务器错误").build());

    public swaggerConfiguration(HaloProperties haloProperties) {
        this.haloProperties = haloProperties;
    }

    @Bean
    public Docket haloDefaultApi() {
        if (haloProperties.isDocDisabled()) {
            log.debug("doc已被禁用");
        }
        return buildApiDocket("run.halo.app.admin",
                "run.halo.app.controller.admin", "/api/admin/**")
                .securitySchemes(adminApiKeys())
                .securityContexts(portalSecurityContext())
                .enable(!haloProperties.isDocDisabled());
    }

    private Docket buildApiDocket(@NonNull String groupName, @NonNull String basePackage, @NonNull String antPattern) {
        Assert.hasText(groupName, "组名不能为空");
        Assert.hasText(basePackage, "basePackage 不能为空");
        Assert.hasText(antPattern, "antPattern 不能为空");

        return new Docket(DocumentationType.SWAGGER_2)
                .groupName(groupName)
                .select()
                .apis(RequestHandlerSelectors.basePackage(basePackage))
                .paths(PathSelectors.ant(antPattern))
                .build()
                .apiInfo(apiInfo())
                .useDefaultResponseMessages(false)
                .globalResponseMessage(RequestMethod.GET, globalResponses)
                .globalResponseMessage(RequestMethod.POST, globalResponses)
                .globalResponseMessage(RequestMethod.DELETE, globalResponses)
                .globalResponseMessage(RequestMethod.PUT, globalResponses)
                .directModelSubstitute(Temporal.class, String.class);
    }

    private List<ApiKey> adminApiKeys() {
        return Arrays.asList(
                new ApiKey("令牌头", AdminAuthenticationFilter.ADOMIN_TOKEN_HEADER_NAME, In.HEADER.name()),
                new ApiKey("令牌尾", AdminAuthenticationFilter.ADOMIN_TOKEN_QUERY_NAME, In.QUERY.name())

        );
    }

    private List<SecurityContext> portalSecurityContext() {
        return Collections.singletonList(
                SecurityContext.builder()
                        .securityReferences(defaultAuth())
                        .forPaths(PathSelectors.regex("/api/context/.*"))
                        .build()
        );
    }
    private List<ApiKey> portalApiKeys(){
        return Arrays.asList(
                new ApiKey("令牌头",apiAuthenticationFilter.API_TOKEN_HEADER_NAME,In.HEADER.name());
                new ApiKey("令牌尾",apiAuthenticationFilter.API_TOKEN_QUERY_NAME,In.QUERY.name());
        )
    }

    private List<SecurityReference> defaultAuth() {
        AuthorizationScope[] authorizationScopes = {new AuthorizationScope("global", "accessEverything")};
        return Arrays.asList(new SecurityReference("令牌头", authorizationScopes),
                new SecurityReference("令牌尾", authorizationScopes));
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder().title("halo API 文档")
                .description("halo API 文档")
                .version(HALO_VERSION)
                .termsOfServiceUrl("https://github.com/halo-dev")
                .contact(new Contact("RYAN0UP", "https://ryanc.cc/", "i#ryanc.cc"))
                .build();
    }

    @Bean
    public AlternateTypeRuleConvention customizeConvention(TypeResolver resolver) {
        return new AlternateTypeRuleConvention() {
            @Override
            public List<AlternateTypeRule> rules() {
                return Arrays.asList(
                        newRule(User.class, emptyMixin(User.class)),
                        newRule(UserDetail.class, emptyMixin(UserDetail.class)),
                        newRule(resolver.resolve(Pageable.class), resolver.resolve(pageableMixin())),
                        newRule(resolver.resolve(Sort.class), resolver.resolve(sortMixin()))
                );
            }


            @Override
            public int getOrder() {
                return Ordered.LOWEST_PRECEDENCE;
            }
        };
    }

    /**
     * 用于控制器参数（例如 HttpServletRequest,ModelView...
     * @param clazz 控制器参数类型不能为空
     * @return
     */
    private Type emptyMixin(Class<?> clazz) {
        Assert.notNull(clazz, "类类型不能为空");
        return new AlternateTypeBuilder()
                .fullyQualifiedClassName(String.format("%S.GENERATED.%S", clazz.getPackage().getName(), clazz.getSimpleName()))
                .withProperties(Collections.emptyList())
                .build();
    }

    private Type sortMixin() {
        return new AlternateTypeBuilder()
                .fullyQualifiedClassName(String.format("%s.generated.%s", Sort.class.getPackage().getName(), Sort.class.getSimpleName()))
                .withProperties(Collections.emptyList())
                .build();
    }

    private Type pageableMixin() {
        return new AlternateTypeBuilder()
                .fullyQualifiedClassName(String.format("%s.generated.%s",Pageable.class.getPackage().getName(),Pageable.class.getSimpleName()))
                .withProperties(Arrays.asList(property(Integer.class,"page"),property(Integer.class,"size"),property(String[].class,"sort")))
                .build();


    }

    private AlternateTypePropertyBuilder property(Class<?> type, String name) {
        return  new AlternateTypePropertyBuilder()
                .withName(name)
                .withType(type)
                .withCanRead(true)
                .withCanWrite(true);
    }


}
