package club.pineclone.gtavops.register;

import club.pineclone.gtavops.scene.SceneTemplate;
import club.pineclone.gtavops.scene._01IntroScene;
import club.pineclone.gtavops.scene._02EnhanceScene;
import club.pineclone.gtavops.scene._03DebugScene;

import java.util.ArrayList;
import java.util.List;

public class SceneRegistryHandler {

    private static final List<SceneTemplate> registry = new ArrayList<>();

    static {
        registry.add(new _01IntroScene());
        registry.add(new _02EnhanceScene());
        registry.add(new _03DebugScene());
    }

    public static List<SceneTemplate> getRegistry() {
        return registry;
    }
}
