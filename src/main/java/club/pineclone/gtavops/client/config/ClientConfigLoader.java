package club.pineclone.gtavops.client.config;

import club.pineclone.gtavops.client.utils.JsonConfigLoader;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Path;

public class ClientConfigLoader {

    private ObjectMapper mapper;
    private final Logger log = LoggerFactory.getLogger(getClass());

    private final Path clientConfigPath;

    public ClientConfigLoader(Path clientConfigPath) {
        this.clientConfigPath = clientConfigPath;

        mapper = new ObjectMapper();
        mapper.enable(SerializationFeature.INDENT_OUTPUT);  /* 美观输出 */
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);  /* 即使读取到未知属性也不会报错 */
    }

    /* 加载客户端配置 */
    public ClientConfig load() throws IOException {
        ClientConfig config = JsonConfigLoader.load(clientConfigPath, ClientConfig::new, mapper);
        JsonConfigLoader.save(clientConfigPath, config, mapper);
        log.debug("Client using locale: {}", config.lang);
        return config;
    }

    /* 保存配置 */
    public void save(ClientConfig config) throws IOException {
        JsonConfigLoader.save(clientConfigPath, config, mapper);
    }

}
