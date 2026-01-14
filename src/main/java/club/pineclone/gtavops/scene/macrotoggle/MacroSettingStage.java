package club.pineclone.gtavops.scene.macrotoggle;

import club.pineclone.gtavops.i18n.I18nContext;
import club.pineclone.gtavops.i18n.I18nText;
import io.vproxy.vfx.ui.layout.HPadding;
import io.vproxy.vfx.ui.stage.VStage;
import io.vproxy.vfx.ui.stage.VStageInitParams;
import io.vproxy.vfx.util.FXUtils;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class MacroSettingStage {

    private final VStage vStage;
    protected Pane content;
    protected final I18nContext i18nContext;  /* 本地化 */
    protected final Logger log = LoggerFactory.getLogger(getClass());

    public MacroSettingStage(I18nContext i18nContext, I18nText introTitle) {
        this.i18nContext = i18nContext;
        content = new VBox();

        vStage = new VStage(new VStageInitParams().setMaximizeAndResetButton(false).setIconifyButton(false));
        vStage.getInitialScene().enableAutoContentWidthHeight();
        vStage.getStage().setWidth(700);
        vStage.getStage().setHeight(500);
        vStage.getStage().initModality(Modality.APPLICATION_MODAL);
        this.i18nContext.i18nProperty().addListener((obs, oldVal, newVal) -> {
            vStage.setTitle(introTitle.resolve(newVal));
        });
        /* 由于设置面板并非应用启动后第一时间加载，因此不会得到 i18n 更新的触发，此处需要手动更新一次I18n */
        vStage.setTitle(introTitle.resolve(this.i18nContext.get()));
    }

    protected Pane getContent() {
        return content;
    }

    private VStage getStage() {
        return vStage;
    }

    @Deprecated
    private void show() {
        FXUtils.observeWidth(getStage().getInitialScene().getNode(), getContent(), -30);
        HBox hbox = new HBox(new HPadding(10), content);
        vStage.getInitialScene().getScrollPane().setContent(hbox);
        vStage.show();
    }

    public void showAndWait() {
        FXUtils.observeWidth(getStage().getInitialScene().getNode(), getContent(), -30);
        HBox hbox = new HBox(new HPadding(10), content);
        vStage.getInitialScene().getScrollPane().setContent(hbox);
        vStage.showAndWait();
    }

    protected void onVSettingStageInit() {}

    protected void onVSettingStageExit() {}

    public final void initVSettingStage() {
        onVSettingStageInit();
    }

    public final void exitVSettingStage() {
        onVSettingStageExit();
    }
}
