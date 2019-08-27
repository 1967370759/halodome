package run.halo.app.security.filter;

import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpHeaders;
import run.halo.app.config.properties.HaloProperties;
import run.halo.app.service.OptionService;


@Slf4j
public class ApiAuthenticationFilter extends AbstractAuthenticationFilter {
    public final static String API_IOKEN_HEADER_NAME = "API-" + HttpHeaders.AUTHORIZATION;

    public final static String API_TOKEN_QUERY_NAME = "api_token";

    private final OptionService optionService;

    public ApiAuthenticationFilter(HaloProperties haloProperties,
                                   OptionService optionService) {
        super(haloProperties, optionService);
    }
}
