package club.pineclone.gtavops.client.config;

import club.pineclone.gtavops.config.ConfigCodecs;
import club.pineclone.gtavops.common.JsonConfigLoader;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Path;

public class ClientConfigLoader {

    private static ObjectMapper mapper;
    private static final Logger log = LoggerFactory.getLogger(ClientConfigLoader.class);

    private ClientConfigLoader() {
        SimpleModule module = new SimpleModule();
        ConfigCodecs.registerAll(module);
        mapper = new ObjectMapper();
        mapper.registerModule(module);  /* 注册模块 */
        mapper.enable(SerializationFeature.INDENT_OUTPUT);  /* 美观输出 */
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);  /* 即使读取到未知属性也不会报错 */
    }

    @Getter private static final ClientConfigLoader instance = new ClientConfigLoader();

    /* 加载客户端配置 */
    public ClientConfig load(Path path) throws IOException {
        ClientConfig config = JsonConfigLoader.load(path, ClientConfig::new, mapper);
        JsonConfigLoader.save(path, config, mapper);
        log.debug("Client using locale: {}", config.lang);
        return config;
    }

    /* 保存配置 */
    public void save(ClientConfig config, Path path) throws IOException {
        JsonConfigLoader.save(path, config, mapper);
    }

}
