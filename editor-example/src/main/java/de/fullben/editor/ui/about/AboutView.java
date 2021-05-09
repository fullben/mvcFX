package de.fullben.editor.ui.about;

import de.fullben.editor.ui.I18n;
import de.fullben.editor.ui.Stages;
import de.fullben.mvcfx.StatelessFxmlStageView;
import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class AboutView extends StatelessFxmlStageView<AboutController> {

  public AboutView(AboutController controller) throws IOException {
    super(controller, I18n.getBundle());
    load();
  }

  @Override
  protected Stage initStage(Parent root) {
    Stage stage = new Stage();
    stage.setTitle(getString("about.title"));
    stage.setResizable(false);
    stage.setScene(new Scene(root));
    Stages.setIcon(stage);
    return stage;
  }

  @FXML
  public void handleClose(ActionEvent event) {
    hide();
  }
}
