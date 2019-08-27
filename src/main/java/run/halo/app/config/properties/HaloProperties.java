package run.halo.app.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import run.halo.app.model.support.HaloConst;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * halo 属性配置
 */
@Data
@ConfigurationProperties("halo")
@Component
public class HaloProperties {
    /**
     * Doc api 禁用(默认是 true)
     */
    private boolean docDisabled = true;

    /**
     * 生产环境  默认为 true
     */
    private boolean productionEnv = true;

    /**
     * 启用身份验证
     */
    private boolean authEnabled = true;

    /**
     *
     */
    private String workDir = HaloConst.USER_HOME + "/.halo/";

    public HaloProperties() throws IOException {
        Files.createDirectories(Paths.get(workDir));
    }
}
