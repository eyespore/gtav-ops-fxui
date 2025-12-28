package club.pineclone.gtavops.config;

import club.pineclone.gtavops.utils.PathUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.module.SimpleModule;
import io.vproxy.vfx.entity.input.Key;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public class ConfigHolder {

    public static final String APPLICATION_TITLE = "GTAV Ops";  /* 应用基础信息 */

    private static ObjectMapper mapper;
    private static Config config;  /* 配置 */
    private static final String VERSION_FILE = "/version.txt";

    public static String version = "unknown";

    private ConfigHolder() {}

    public static void load() throws IOException {
        /* 加载版本号信息 */
        try (InputStream in = ConfigHolder.class.getResourceAsStream(VERSION_FILE)) {
            if (in != null) {
                version = new String(in.readAllBytes(), StandardCharsets.UTF_8).trim();
            }
        }

        SimpleModule module = new SimpleModule();
        ConfigCodecs.registerAll(module);

        mapper = new ObjectMapper();
        mapper.registerModule(module);  /* 注册模块 */
        mapper.enable(SerializationFeature.INDENT_OUTPUT);  /* 美观输出 */
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);  /* 即使读取到未知属性也不会报错 */

        Config defaultConfig = new Config();  /* 默认配置 */

        Path configFilePath = PathUtils.getConfigFilePath();
        if (Files.notExists(configFilePath)) {
            /* 用户端配置不存在，直接采用默认配置，并将默认配置写入文件 */
            config = defaultConfig;
            save();
            return;
        }

        config = mapper.readerForUpdating(defaultConfig).readValue(configFilePath.toFile());
        save();  /* 若用户端配置存在无效属性，将会在这一步骤被一并移除 */
        /* 对于 Json 错误引起的异常则会被正常抛出 */
    }

    public static void overrideConfigToDefault() throws IOException {
        config = new Config();
        save();  /* 将配置写入文件 */
    }

    public static Config get() {
        return config;
    }

    /* 保存配置 */
    public static void save() throws IOException {
        Path configFilePath = PathUtils.getConfigFilePath();
        mapper.writeValue(configFilePath.toFile(), config);  /* 保存配置 */
    }
}
