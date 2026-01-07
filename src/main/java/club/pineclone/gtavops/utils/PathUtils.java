package club.pineclone.gtavops.utils;

import com.github.kwhat.jnativehook.NativeSystem;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class PathUtils {

    private static final String CONFIG_DIR_NAME = ".gtav-ops";
    private static final String DEFAULT_MACRO_CONFIG_NAME = "default-config.json";
    private static final String CLIENT_CONFIG_NAME = "config.json";

    /* 初始化宏核心家目录 */
    public static void initCoreHome() throws IOException {
        Files.createDirectories(getAppHomePath());  /* 确保应用根目录存在 */
        Files.createDirectories(getCorePath().resolve("configs"));
    }

    /* 初始化 FXUI 家目录 */
    public static void initFXUIHome() throws IOException {
        Files.createDirectories(getAppHomePath());  /* 确保应用根目录存在 */
        Files.createDirectories(getFXUIPath());
    }

    /* 应用程序锁文件 */
    public static Path getLockFilePath() {
        return getAppHomePath().resolve("singleton.lock");
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

    /* 配置文件路径( %appdata%/roaming/.gtav-ops/config.json ) */
    public static Path getMacroConfigPath() {
        return getCorePath().resolve("configs").resolve(DEFAULT_MACRO_CONFIG_NAME);
    }

    public static Path getClientConfigPath() {
        return getFXUIPath().resolve(CLIENT_CONFIG_NAME);
    }

    @Deprecated
    public static Path getFontpacksBaseDirPath() {
        return getAppHomePath().resolve("fontpacks");
    }

    /* 配置目录路径( %appdata%/roaming/.gtav-ops/* (windows) ) */
    public static Path getAppHomePath() {
        String os = getOS();
        String userHome = System.getProperty("user.home");
        Path configDir = null;
        if (os.contains("windows")) {
            configDir = Path.of(userHome, "AppData", "Roaming", CONFIG_DIR_NAME);
        } else if (os.contains("mac") || os.contains("darwin") || os.contains("nux") || os.contains("aix")) {
            configDir = Path.of(userHome, ".config", CONFIG_DIR_NAME);
        } else {
            configDir = Path.of(userHome, CONFIG_DIR_NAME);
        }
        return configDir;
    }

    public static Path getCorePath() {
        return getAppHomePath().resolve("core");
    }

    public static Path getFXUIPath() {
        return getAppHomePath().resolve("fxui");
    }
}
