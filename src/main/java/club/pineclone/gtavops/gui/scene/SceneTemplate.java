package club.pineclone.gtavops.gui.scene;

import io.vproxy.vfx.ui.scene.VScene;
import io.vproxy.vfx.ui.scene.VSceneRole;
import lombok.Setter;

public abstract class SceneTemplate extends VScene {

    @Setter private SceneLifecycleAware listener;  /* Scene 生命周期 */

    public SceneTemplate() {
        this(VSceneRole.MAIN);
    }

    public SceneTemplate(VSceneRole role) {
        super(role);
    }

    public abstract String getTitle();

    @Override
    protected void onHidden() {
        if (listener != null) listener.onSceneHidden();
    }

    @Override
    protected void beforeHiding() {
        if (listener != null) listener.beforeSceneHiding();
    }

    @Override
    protected boolean checkBeforeHiding() throws Exception {
        if (listener != null) return listener.checkBeforeSceneHiding();
        return super.checkBeforeHiding();
    }

    @Override
    protected void onShown() {
        if (listener != null) listener.onSceneShown();
    }

    @Override
    protected void beforeShowing() {
        if (listener != null) listener.beforeSceneShowing();
    }

    @Override
    protected boolean checkBeforeShowing() throws Exception {
        if (listener != null) return listener.checkBeforeSceneShowing();
        return super.checkBeforeShowing();
    }
}
