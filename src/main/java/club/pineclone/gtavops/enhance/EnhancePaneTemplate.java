package club.pineclone.gtavops.enhance;

import io.vproxy.vfx.ui.pane.FusionPane;
import io.vproxy.vfx.ui.toggle.ToggleSwitch;
import io.vproxy.vfx.ui.wrapper.ThemeLabel;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;

public abstract class EnhancePaneTemplate {

    private final FusionPane pane;

    public EnhancePaneTemplate() {
        Region spacer = new Region();
        spacer.setMinWidth(10);
        HBox.setHgrow(spacer, Priority.ALWAYS);

        ToggleSwitch toggle = new ToggleSwitch();
        ThemeLabel intro = new ThemeLabel(getTitle());

        HBox hBox = new HBox(0);
        hBox.setPadding(new Insets(12, 22, 0, 5));
        hBox.getChildren().addAll(intro, spacer, toggle.getNode());
        hBox.setPrefWidth(300);

        FusionPane pane = new FusionPane();
        pane.getNode().setPrefWidth(300);
        pane.getNode().setPrefHeight(75);
        pane.getContentPane().getChildren().add(hBox);

        ClickEventHandler handler = new ClickEventHandler(toggle);
        pane.getNode().setOnMouseClicked(handler);
        toggle.getNode().setOnMouseClicked(handler);

        this.pane = pane;
    }

    private class ClickEventHandler implements EventHandler<MouseEvent> {
        private final ToggleSwitch toggle;

        public ClickEventHandler(ToggleSwitch toggle) {
            this.toggle = toggle;
        }

        @Override
        public void handle(MouseEvent e) {
            if (e.getButton() == MouseButton.PRIMARY) {
                boolean flag = !toggle.selectedProperty().get();
                if (flag) EnhancePaneTemplate.this.onEnable();
                else EnhancePaneTemplate.this.onDisable();
                toggle.setSelected(flag);
            } else if (e.getButton() == MouseButton.SECONDARY) {
                EnhancePaneTemplate.this.onSetting();
            }
        }
    }

    public Node getNode() {
        return this.pane.getNode();
    }

    protected abstract String getTitle();

    protected void onEnable() {}

    protected void onDisable() {}

    protected void onSetting() {}
}
