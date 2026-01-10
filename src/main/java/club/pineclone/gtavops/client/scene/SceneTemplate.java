package club.pineclone.gtavops.client.scene;

import club.pineclone.gtavops.client.UILifecycleAware;
import club.pineclone.gtavops.client.i18n.I18nContext;
import io.vproxy.vfx.ui.scene.VScene;
import io.vproxy.vfx.ui.scene.VSceneRole;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class SceneTemplate extends VScene implements UILifecycleAware {

    protected Logger log = LoggerFactory.getLogger(getClass());
    protected I18nContext i18nContext;
    private final BooleanProperty readyProperty = new SimpleBooleanProperty(false);

    public SceneTemplate(I18nContext i18nContext) {
        super(VSceneRole.MAIN);
        this.i18nContext = i18nContext;
    }

    /* 供 SceneGroup 查询某个Scene是否加载完毕 */
    public BooleanProperty readyProperty() {
        return readyProperty;
    }

    @Override
    protected final void onShown() {  /* 加载完成 */
        readyProperty.set(true);
    }

    @Override
    protected final void onHidden() {  /* 未加载 */
        readyProperty.set(false);
    }
}
