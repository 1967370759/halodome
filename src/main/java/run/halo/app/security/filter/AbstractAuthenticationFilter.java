package run.halo.app.security.filter;

import cn.hutool.core.lang.Assert;
import io.micrometer.core.lang.NonNull;
import io.micrometer.core.lang.Nullable;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;
import run.halo.app.config.properties.HaloProperties;
import run.halo.app.security.handler.AuthenticationFailureHandler;
import run.halo.app.security.handler.DefaultAuthenticationFailureHandler;
import run.halo.app.service.OptionService;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

public abstract class AbstractAuthenticationFilter extends OncePerRequestFilter {
    protected final AntPathMatcher antPathMatcher;
    protected final HaloProperties haloProperties;
    protected final OptionService optionService;
    private AuthenticationFailureHandler failureHandler;

    private Set<String> excludeUrlpatterns = new HashSet<>(2);

    /**
     * 排除url模式
     *
     * @param haloProperties
     * @param optionService
     */
    public AbstractAuthenticationFilter(HaloProperties haloProperties, OptionService optionService) {
        this.haloProperties = haloProperties;
        this.optionService = optionService;
        antPathMatcher = new AntPathMatcher();
    }

    @Nullable
    protected abstract String getTokenFromRequest(@NonNull HttpServletRequest request);

    protected abstract void doAuthenticate(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException;

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        Assert.notNull(request, "Http Servlet request 不能为空");
        return excludeUrlpatterns.stream().anyMatch(p -> antPathMatcher.match(p, request.getServletPath()));
    }

    /**
     * 添加排除url方式
     *
     * @param excludeUrlPatterns
     */
    public void addExcludeUrlPatterns(@NonNull String... excludeUrlPatterns) {
        Assert.notNull(excludeUrlPatterns, "Exclude url Patterns 不能为空");
        Collections.addAll(this.excludeUrlpatterns, excludeUrlPatterns);
    }

    /**
     * 获取 excludeUrlpatterns
     *
     * @return excludeUrlpatterns
     */
    @NonNull
    public Set<String> getExcludeUrlpatterns() {
        return excludeUrlpatterns;
    }

    /**
     * set Sets exclude url patterns.
     * @param excludeUrlPatterns
     */
    public void setExcludeUrlpatterns(@NonNull Collection<String> excludeUrlPatterns) {
        Assert.notNull(excludeUrlpatterns, "excludeUrlPatterns 不能为空")
        ;
        this.excludeUrlpatterns = new HashSet<>(excludeUrlPatterns);
    }

    /**
     * 获取身份验证失败处理程序（默认值 @DefaultAuthenticationFailureHandler
     * @return
     */
    @NonNull
    protected AuthenticationFailureHandler getFailureHandler(){
        if (failureHandler==null)
        {
            synchronized (this){
                if (failureHandler==null){
                    DefaultAuthenticationFailureHandler failureHandler=new DefaultAuthenticationFailureHandler();
                    failureHandler.setProductionEnv(haloProperties.isProductionEnv());
                    this.failureHandler=failureHandler;
                }
            }
        }
        return failureHandler;
    }

    /**
     * 设置身份验证失败处理程序
     * @param failureHandler
     */
    public void setFailureHandler(@NonNull AuthenticationFailureHandler failureHandler){
        Assert.notNull(failureHandler,"AuthenticationFailureHandler 不能为空");
        this.failureHandler=failureHandler;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {
Boolean isInstalled=optionService.getByPropertyOr
    }
}
