package de.fullben.editor.ui.preferences;

import de.fullben.editor.ui.I18n;
import de.fullben.editor.ui.Stages;
import de.fullben.editor.ui.themes.ThemeType;
import de.fullben.mvcfx.FxmlStageView;
import java.io.IOException;
import java.util.stream.Collectors;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.stage.Stage;

public class PreferencesView extends FxmlStageView<PreferencesModel, PreferencesController> {

  @FXML private ComboBox<ThemeTypeItem> cbThemes;

  public PreferencesView(PreferencesModel model, PreferencesController controller)
      throws IOException {
    super(model, controller, I18n.getBundle());
    load();
  }

  @Override
  protected Stage initStage(Parent root) {
    Stage stage = new Stage();
    stage.setResizable(false);
    stage.setTitle(getString("preferences.title"));
    stage.setScene(new Scene(root));
    Stages.setIcon(stage);
    return stage;
  }

  @FXML
  public void initialize() {
    ObservableList<ThemeTypeItem> items =
        FXCollections.observableArrayList(
            getModel().getThemeTypes().stream()
                .map(ThemeTypeItem::new)
                .collect(Collectors.toList()));
    cbThemes.setItems(items);
    ThemeType type = getModel().getSelectedTheme();
    for (ThemeTypeItem item : items) {
      if (item.type == type) {
        cbThemes.getSelectionModel().select(item);
      }
    }
    cbThemes
        .getSelectionModel()
        .selectedItemProperty()
        .addListener(
            (ChangeListener<? super ThemeTypeItem>)
                (observable, oldVal, newVal) -> {
                  if (newVal == null) {
                    return;
                  }
                  getController().setTheme(newVal.type);
                });
  }

  @FXML
  public void handleOk(ActionEvent event) {
    hide();
  }

  private class ThemeTypeItem {
    private final ThemeType type;
    private final String text;

    private ThemeTypeItem(ThemeType type) {
      this.type = type;
      text = getString(type.getKey());
    }

    @Override
    public String toString() {
      return text;
    }
  }
}
