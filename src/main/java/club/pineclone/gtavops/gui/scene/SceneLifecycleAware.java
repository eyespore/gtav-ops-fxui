package club.pineclone.gtavops.gui.scene;

public interface SceneLifecycleAware {

    default void onSceneHidden() {}

    default void beforeSceneHiding() {}

    default boolean checkBeforeSceneHiding() throws Exception {
        return true;
    }

    default void onSceneShown() {}

    default void beforeSceneShowing() {}

    default boolean checkBeforeSceneShowing() throws Exception {
        return true;
    }
}
