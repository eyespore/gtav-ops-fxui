package club.pineclone.gtavops.enhance;

import io.vproxy.vfx.ui.alert.SimpleAlert;
import io.vproxy.vfx.ui.button.FusionButton;
import io.vproxy.vfx.ui.layout.HPadding;
import io.vproxy.vfx.ui.layout.VPadding;
import io.vproxy.vfx.ui.stage.VStage;
import io.vproxy.vfx.ui.stage.VStageInitParams;
import io.vproxy.vfx.ui.toggle.ToggleSwitch;
import io.vproxy.vfx.ui.wrapper.ThemeLabel;
import io.vproxy.vfx.util.FXUtils;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.geometry.Insets;
import javafx.scene.control.Alert;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;

/* 切枪偷速 */
public class _01ToggleWeaponSpeedHackEnhance extends EnhancePaneTemplate {

    private final StringProperty keymap = new SimpleStringProperty("BUTTON4");

    @Override
    protected String getTitle() {
        return "切枪偷速";
    }

    @Override
    protected void onEnable() {
        super.onEnable();
    }

    @Override
    protected void onDisable() {
        super.onDisable();
    }

    @Override
    protected void onSetting() {
        var vStage = new VStage(new VStageInitParams()
                .setMaximizeAndResetButton(false)
                .setIconifyButton(false)
        );
        vStage.getInitialScene().enableAutoContentWidthHeight();

        vStage.getStage().setWidth(800);
        vStage.getStage().setHeight(400);
        vStage.getStage().initModality(Modality.APPLICATION_MODAL);
        vStage.setTitle(getTitle());

        HBox hBox1 = createButtonConfig("激活快捷键");
        FXUtils.observeWidth(vStage.getInitialScene().getScrollPane().getNode(), hBox1, -40);

        HBox hBox2 = createButtonConfig("激活方式");
        HBox hBox3 = createToggleConfig("切入偷速时尝试切换近战武器");
        HBox hBox4 = createToggleConfig("切出偷速时尝试切换武器");

        var box = new HBox(
                new HPadding(10),
                new VBox(
                        new VPadding(20),
                        hBox1,
                        hBox2,
                        new VPadding(15),
                        hBox3,
                        new VPadding(20),
                        hBox4
                )
        );
        vStage.getInitialScene().getScrollPane().setContent(box);
        vStage.show();
    }

    private HBox createToggleConfig(String info) {
        HBox hBox = getConfigBox(new Insets(12, 0, 0, 20));
        Region spacer = getSpacer();

        ToggleSwitch toggle = new ToggleSwitch();
        ThemeLabel intro = new ThemeLabel(info);

        intro.setOnMouseClicked(e -> {
            toggle.setSelected(!toggle.isSelected());
        });

        hBox.getChildren().addAll(intro, spacer, toggle.getNode());
        return hBox;
    }

    private HBox createButtonConfig(String info) {
        HBox hBox = getConfigBox(new Insets(24, 0, 0, 20));
        Region spacer = getSpacer();

        var keyChooserBtn = new FusionButton(keymap.get()) {{
            setPrefWidth(100);
            setPrefHeight(40);
        }};

        keyChooserBtn.setOnAction(e -> SimpleAlert.showAndWait(Alert.AlertType.INFORMATION,
                "call SimpleAlert.showAndWait(...)"));

        ThemeLabel intro = new ThemeLabel(info);
        hBox.getChildren().addAll(intro, spacer, keyChooserBtn);
        return hBox;
    }

    private Region getSpacer() {
        Region spacer = new Region();
        spacer.setMinWidth(10);
        HBox.setHgrow(spacer, Priority.ALWAYS);
        return spacer;
    }

    private HBox getConfigBox(Insets insets) {
        HBox hBox = new HBox(0);
        hBox.setPadding(insets);
        hBox.setPrefWidth(750);
        return hBox;
    }
}
