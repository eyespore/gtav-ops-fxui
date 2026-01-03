package club.pineclone.gtavops.gui.feature;

import club.pineclone.gtavops.common.AbstractRegistry;
import io.vproxy.vfx.control.globalscreen.GlobalScreenUtils;
import lombok.Getter;

public class FeatureTogglePaneRegistry extends AbstractRegistry<FeatureTogglePaneTemplate> {

    @Getter
    private static final FeatureTogglePaneRegistry instance = new FeatureTogglePaneRegistry();

    private FeatureTogglePaneRegistry() {
        register(new _01SwapGlitchFeatureTogglePane());
        register(new _02RouletteSnakeFeatureTogglePane());
        register(new _03ADSwingFeatureTogglePane());
        register(new _04MeleeGlitchFeatureTogglePane());
        register(new _05BetterMMenuFeatureTogglePane());
        register(new _06BetterLButtonFeatureTogglePane());
        register(new _07QuickSwapFeatureTogglePane());
        register(new _08DelayClimbFeatureTogglePane());
        register(new _09BetterPMenuFeatureTogglePane());
        register(new _10AutoFireFeatureTogglePane());
    }

    public synchronized void initAll() {
        GlobalScreenUtils.enable(this);
        values().forEach(FeatureTogglePaneTemplate::doInit);
    }

    public synchronized void stopAll() {
        values().forEach(FeatureTogglePaneTemplate::doStop);
        GlobalScreenUtils.disable(this);
    }

}
