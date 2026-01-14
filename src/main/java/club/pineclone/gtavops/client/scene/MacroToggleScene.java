package club.pineclone.gtavops.client.scene;

import club.pineclone.gtavops.client.forked.ForkedThemeLabel;
import club.pineclone.gtavops.client.i18n.I18nContext;
import club.pineclone.gtavops.client.i18n.I18nText;
import club.pineclone.gtavops.client.scene.macrotoggle.*;
import io.vproxy.vfx.util.FXUtils;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.layout.*;

import java.util.List;

public class MacroToggleScene extends SceneTemplate {

    private final List<MacroToggle> macroToggles;

    public MacroToggleScene(I18nContext i18nContext) {
        super(i18nContext);
        enableAutoContentWidthHeight();
        ForkedThemeLabel headerLabel = new ForkedThemeLabel();
        headerLabel.textProperty().bind(I18nText.of(i -> i.macroToggleScene.header).binding(i18nContext));
        FXUtils.observeWidthCenter(getContentPane(), headerLabel);
        headerLabel.setLayoutY(40);

        HBox gameVersionPane = new HBox(10);
        gameVersionPane.setPadding(new Insets(24, 0, 0, 0));

        gameVersionPane.getChildren().addAll(headerLabel);
        gameVersionPane.setAlignment(Pos.CENTER);
        gameVersionPane.setLayoutY(10);
        FXUtils.observeWidthCenter(getContentPane(), gameVersionPane);

        GridPane gridPane = new GridPane();
        gridPane.setLayoutY(80);
        gridPane.setHgap(10);
        gridPane.setVgap(10);

        FXUtils.observeWidthCenter(getContentPane(), gridPane);

        int hLimit = 4;
        int hIndex = 0;
        int vIndex = 0;

        /* 注册所有宏功能开关 */

        macroToggles = List.of(
                new SwapGlitchToggle(i18nContext),
                new RouletteSnakeToggle(i18nContext),
                new ADSwingToggle(i18nContext),
                new MeleeGlitchToggle(i18nContext),
                new BetterMMenuToggle(i18nContext),
                new BetterLButtonToggle(i18nContext),
                new QuickSwapToggle(i18nContext),
                new DelayClimbToggle(i18nContext),
                new BetterPMenuToggle(i18nContext),
                new AutoFireFeatureToggle(i18nContext)
        );

        for (MacroToggle toggle : macroToggles) {
            gridPane.add(toggle.getNode(), hIndex, vIndex);
            hIndex++;
            if (hIndex >= hLimit) {
                vIndex++;
                hIndex = 0;
            }
        }

        getContentPane().getChildren().addAll(
//                headerLabel,
                gameVersionPane,
                gridPane
        );
    }

    @Override
    public void onUIInit() {
        macroToggles.forEach(MacroToggle::onUIInit);
    }

    @Override
    public void onUIDispose() {
        macroToggles.forEach(MacroToggle::onUIDispose);
    }
}
