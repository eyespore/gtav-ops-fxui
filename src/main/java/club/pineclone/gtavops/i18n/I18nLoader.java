package club.pineclone.gtavops.i18n;

import club.pineclone.gtavops.utils.JsonConfigLoader;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import io.vproxy.commons.util.Singleton;
import io.vproxy.vfx.manager.internal_i18n.InternalI18n;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;
import java.util.stream.Stream;

public class I18nLoader {

    @Getter private static final I18nLoader instance = new I18nLoader();
    @Getter private final Map<String, I18nItem> i18nLoaderMap = new HashMap<>();  /* 本地化映射表 */
    private final Logger log = LoggerFactory.getLogger(getClass());
    private final ObjectMapper mapper;
    private volatile boolean vfxI18nRegistered = false;

    private I18nLoader() {
        mapper = new ObjectMapper();
        mapper.enable(SerializationFeature.INDENT_OUTPUT);  /* 美观输出 */
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);  /* 忽略不存在的属性 */
    }

    /* I18n本地化对象 */
    @AllArgsConstructor
    public static class I18nItem {
        @Getter private final String name;  /* 本地化显示名称 */
        private final Supplier<ExtendedI18n> loader;  /* 本地化懒加载 */

        public ExtendedI18n load() {
            return loader.get();
        }
    }

    /**
     * 初始化本地化，通过扫描指定的位置，将本地化映射记录到Map当中
     */
    public void init() {
        loadI18nItemFromJarFile();  /* 通过 JarFile 资源加载可用的本地化 */
    }

    /**
     * 该类仅负责读取I18n配置文件，不更改任何界面本地化，通常需要通过{@link club.pineclone.gtavops.scene.ConfigScene}当中的
     * 本地化变更组件修改之后，调用该方法获取新的ExtendedI18n，并重新注入到MainFX的ObjectProperty当中
     *
     * @param lang 首次加载使用的本地化，该本地化通常会被记录在客户端配置当中，并在初始化时传入
     * @throws RuntimeException 本地化资源基于闭包延迟加载，若客户端传入了一个不受支持的本地化，那么在load阶段将会
     * 抛出IOException，异常在闭包中会被转为RuntimeException继续抛出，最终应当由客户端捕获处理
     */
    public ExtendedI18n load(String lang) {
        ExtendedI18n i18n = i18nLoaderMap.get(lang).load();

        if (!vfxI18nRegistered) {
            /* 由于 VFX 对本地化具有直接依赖，因此仍然应该保留将 ExtendedI18n 注册到全局 */
            Singleton.register(InternalI18n.class, i18n);  /* 将本地化加载到 VFX UI 资源 */
            Singleton.register(ExtendedI18n.class, i18n);
            vfxI18nRegistered = true;
        }

        return i18n;
    }

    private ExtendedI18n loadI18n(String lang) {
        /* 建立 Lang 到 I18nItem 的映射 */
        try (InputStream in = I18nLoader.class.getResourceAsStream("/i18n/" + lang + ".json")) {
            return JsonConfigLoader.load(in, ExtendedI18n::new, mapper);
        } catch (IOException e) {
            throw new RuntimeException(e);  /* 加载失败，若本地化加载失败会向上层抛出 */
        }
    }

    private void loadI18nItemFromJarFile() {
        log.debug("Loading I18n Item from JarFile");
        Stream.of("zhCN", "enUS").forEach(lang -> {
            ExtendedI18n i18n = loadI18n(lang);
            i18nLoaderMap.put(i18n.lang, new I18nItem(i18n.name, () -> loadI18n(lang)));
        });
    }

    private static void createI18nTemplate(String lang) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        ExtendedI18n i18n = getInstance().loadI18n(lang);
        mapper.enable(SerializationFeature.INDENT_OUTPUT);
        Path path = Path.of("target/" + lang + ".json");  /* 创建本地化文件 */
        JsonConfigLoader.save(path, i18n, mapper);
    }

    /* 这个方法用于导出本地化 pojo 到json，从而创建更多的本地化配置 */
    public static void main(String[] args) throws IOException {
        createI18nTemplate("zhCN");
        createI18nTemplate("enUS");
    }
}
