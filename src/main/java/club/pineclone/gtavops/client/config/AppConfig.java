package club.pineclone.gtavops.client.config;

import club.pineclone.gtavops.common.PathUtils;
import lombok.Getter;

import java.nio.file.Path;

/**
 * 客户端内嵌配置
 */
@Getter
public class AppConfig {

    /* FX 客户端家路径 */
    private final Path clientHomePath;

    /* FX 客户端配置文件路径 */
    private final Path clientConfigPath;

    private final FontPackSettings fontPackSettings;

    private final Path singletonLockPath;

    public AppConfig(Path appHomePath) {
        this.clientHomePath = appHomePath.resolve("fxui");
        this.clientConfigPath = clientHomePath.resolve("config.json");
        this.singletonLockPath = clientHomePath.resolve("singleton.lock");

        this.fontPackSettings = new FontPackSettings(clientHomePath);
    }

    @Getter
    public static class FontPackSettings {
        private final Path homePath;

        public FontPackSettings(Path clientHomePath) {
            this.homePath = clientHomePath.resolve("fontpack");  /* 字体包家目录 */
        }
    }

}
