package club.pineclone.gtavops.scene;

import club.pineclone.gtavops.enhance.EnhancePaneTemplate;
import club.pineclone.gtavops.enhance._01ToggleWeaponSpeedHackEnhance;
import io.vproxy.vfx.ui.scene.VSceneRole;
import io.vproxy.vfx.ui.wrapper.ThemeLabel;
import io.vproxy.vfx.util.FXUtils;
import javafx.scene.layout.*;

import java.util.ArrayList;
import java.util.List;

public class _02EnhanceScene extends SceneTemplate {

    private final List<EnhancePaneTemplate> panes = new ArrayList<>();

    public _02EnhanceScene() {
        super(VSceneRole.MAIN);
        enableAutoContentWidthHeight();

        var msgLabel = new ThemeLabel(
                "右击某一项功能来更进一步配置！"
        );
        FXUtils.observeWidthCenter(getContentPane(), msgLabel);
        msgLabel.setLayoutY(100);

        GridPane gridPane = new GridPane();
        gridPane.setLayoutY(200);
        gridPane.setHgap(50);
        gridPane.setVgap(50);
        FXUtils.observeWidthCenter(getContentPane(), gridPane);

        panes.add(new _01ToggleWeaponSpeedHackEnhance());

        int hLimit = 3;
        int hIndex = 0;
        int vIndex = 0;
        for (EnhancePaneTemplate enhance : panes) {
            gridPane.add(enhance.getNode(), hIndex, vIndex);
            hIndex++;
            if (hIndex >= hLimit) {
                vIndex++;
                hIndex = 0;
            }
        }

        getContentPane().getChildren().addAll(
                msgLabel,
                gridPane
        );
    }

    @Override
    public String getTitle() {
        return "Enhance";
    }

}
