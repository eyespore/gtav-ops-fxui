package club.pineclone.gtavops.config;

import com.github.kwhat.jnativehook.NativeSystem;
import io.vproxy.base.util.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

public class AppConfig {

    static {
        try {
            initializeConfig();
        } catch (IOException e) {
            Logger.lowLevelDebug("failed in initializing application config");
            throw new RuntimeException(e);
        }
    }

    public static final String APPLICATION_TITLE = "GTAV Marco By Pineclone";
    public static final String APPLICATION_VERSION = "build00001-alpha1";

    public static final String CONFIG_DIR_NAME = ".gtav";
    public static final String CONFIG_FILE_NAME = "config.toml";
    private static final String DEFAULT_CONFIG_RESOURCE = "default-config.toml";

    private AppConfig() {}

    private static void initializeConfig() throws IOException {
        Path userHome = Path.of(System.getProperty("user.home"));
        Path configDir = userHome.resolve(CONFIG_DIR_NAME);
        Path configFile = configDir.resolve(CONFIG_FILE_NAME);

        if (Files.notExists(configFile)) {
            Files.createDirectories(configDir); // 确保目录存在

            try (InputStream in = AppConfig.class.getClassLoader().getResourceAsStream(DEFAULT_CONFIG_RESOURCE)) {
                if (in == null) {
                    Logger.lowLevelDebug("could not found default application file" + DEFAULT_CONFIG_RESOURCE);
                    throw new IOException();
                }
                Files.copy(in, configFile, StandardCopyOption.REPLACE_EXISTING);
            }
        }
    }

    public static String getArch() {
        return NativeSystem.getArchitecture().toString().toLowerCase();
    }

    public static String getOS() {
        return NativeSystem.getFamily().toString().toLowerCase();
    }

    public static String getJHome() {
        return System.getProperty("java.home");
    }

}
