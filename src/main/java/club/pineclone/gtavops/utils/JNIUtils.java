package club.pineclone.gtavops.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Files;
import java.nio.file.Path;

public class JNIUtils {

    public static native String getForegroundWindowTitle();
    private static final Logger log = LoggerFactory.getLogger(JNIUtils.class);

    static {
        System.load(loadDll().toAbsolutePath().toString());
    }

    private static Path loadDll() {
        String os = PathUtils.getOS();  // 操作系统
        String arch = PathUtils.getArch();  // 系统架构
        String jHome = PathUtils.getJHome();  // Java家目录
        String libName = System.mapLibraryName("PlatformFocusMonitor");

        Path dllPath = Path.of(jHome, "bin", "native", os, arch, libName);

        // ./bin/native/window/x86/PlatformFocusMonitor.dll
        if(Files.notExists(dllPath)){  // 在 ide 内环境时则采用默认位置
            dllPath = Path.of("assets", "native", os, arch, libName);
        }

        log.debug("Load native: {}", dllPath);
        return dllPath;
    }

    public static void main(String[] args) {
        System.out.println(getForegroundWindowTitle());
    }

}
