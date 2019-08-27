package run.halo.app.security.handler;

import cn.hutool.core.lang.Assert;
import cn.hutool.extra.servlet.ServletUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import run.halo.app.exception.HaloException;
import run.halo.app.model.support.BaseResponse;
import run.halo.app.utils.ExceptionUtils;
import run.halo.app.utils.JsonUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
public class DefaultAuthenticationFailureHandler implements AuthenticationFailureHandler {
    private boolean productionEnv = true;

    private ObjectMapper objectMapper = JsonUtils.DEFAULT_JSON_MAPPER;

    public DefaultAuthenticationFailureHandler() {
    }


    @Override
    public void onFailure(HttpServletRequest request, HttpServletResponse response, HaloException exception) throws IOException, ServletException {
        log.warn("处理不成功的身份 ip:[{}]", ServletUtil.getClientIP(request));
        log.error("验证失败", exception);

        BaseResponse<Object> errorDetail = new BaseResponse<>();

        errorDetail.setStatus(exception.getStatus().value());
        errorDetail.setMessage(exception.getMessage());
        errorDetail.setData(exception.getErrorData());

        if (!productionEnv) {
            errorDetail.setDevMessage(ExceptionUtils.getStackTrace(exception));
        }

        response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
        response.setStatus(exception.getStatus().value());
        response.getWriter().write(objectMapper.writeValueAsString(errorDetail));
    }

    /**
     * 设置自定义 object Mapper
     * @param objectMapper
     */
    public void setObjectMapper(ObjectMapper objectMapper) {
        Assert.notNull(objectMapper, "ObjectMapper 不能为空");
        this.objectMapper = objectMapper;
    }

    /**
     * 设置生成环境
     * @param productionEnv
     */
    public void setProductionEnv(boolean productionEnv) {
        this.productionEnv = productionEnv;
    }
}
