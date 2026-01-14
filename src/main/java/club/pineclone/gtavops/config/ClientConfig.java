package club.pineclone.gtavops.config;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 客户端配置文件，定义例如语言等配置项
 */
@Data
@NoArgsConstructor
public class ClientConfig {

    public String lang = "enUS";  /* 用户端使用的本地化 */

}
