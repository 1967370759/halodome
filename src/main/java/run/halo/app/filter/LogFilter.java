package run.halo.app.filter;

import cn.hutool.extra.servlet.ServletUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 过滤器日志
 */
@Slf4j
public class LogFilter extends OncePerRequestFilter {


    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        String remoteAddr = ServletUtil.getClientIP(request);
        log.debug("");
        log.debug("Starting url:[()],method:[()]", request.getRequestURI(), request.getMethod(), remoteAddr);
        //设置过滤时间
        long startTime = System.currentTimeMillis();
        //做的过滤器
        filterChain.doFilter(request, response);
        log.debug("Ending   url: [{}], method: [{}], ip: [{}], status: [{}], usage: [{}] ms", request.getRequestURL(), request.getMethod(), remoteAddr, response.getStatus(), (System.currentTimeMillis() - startTime));
        log.debug("");
    }
}
