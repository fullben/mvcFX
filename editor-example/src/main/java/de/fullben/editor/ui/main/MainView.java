package de.fullben.editor.ui.main;

import de.fullben.editor.ui.I18n;
import de.fullben.editor.ui.Stages;
import de.fullben.editor.ui.file.FileTabController;
import de.fullben.mvcfx.Dialogs;
import de.fullben.mvcfx.FxmlStageView;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Optional;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.stage.Stage;

public class MainView extends FxmlStageView<MainModel, MainController> {

  @FXML private TabPane tpFiles;

  public MainView(MainModel model, MainController controller) throws IOException {
    super(model, controller, I18n.getBundle());
    load();
  }

  @Override
  protected Stage initStage(Parent root) {
    Stage stage = new Stage();
    stage.setScene(new Scene(root));
    stage.setTitle(getString("editor.name"));
    stage.setOnCloseRequest(
        event -> {
          if (!isAppExitConfirmed()) {
            event.consume();
          }
        });
    Stages.setIcon(stage);
    return stage;
  }

  void addFileTab(FileTabController controller) {
    Tab tab = new Tab();
    tab.setClosable(true);
    tab.textProperty().bind(controller.getModel().nameProperty());
    tab.setContent(controller.getView().getRoot());
    tab.setOnCloseRequest(event -> getController().closeFileTab(controller));
    tab.setOnSelectionChanged(
        e -> {
          if (tab.isSelected()) {
            getController().select(controller);
          }
        });
    tpFiles.getTabs().add(tab);
    tpFiles.getSelectionModel().select(tab);
    getController().select(controller);
  }

  @FXML
  public void handleNew(ActionEvent event) {
    getController().createNewFileTab();
  }

  @FXML
  private void handleOpen(ActionEvent event) {
    getController().openFile();
  }

  @FXML
  private void handleSave(ActionEvent event) {
    getController().saveCurrentFile();
  }

  @FXML
  private void handleSaveAs(ActionEvent event) {
    getController().saveCurrentFileAs();
  }

  @FXML
  private void handleShow(ActionEvent event) {
    getController().showCurrentFileInFileManager();
  }

  @FXML
  private void handleClose(ActionEvent event) {
    Tab currentTab = tpFiles.getSelectionModel().getSelectedItem();
    if (getController().closeCurrentFileTab()) {
      tpFiles.getTabs().remove(currentTab);
    }
  }

  @FXML
  private void handlePreferences(ActionEvent event) {
    getController().openPreferencesWindow();
  }

  @FXML
  private void handleExit(ActionEvent event) {
    if (isAppExitConfirmed()) {
      hide();
    }
  }

  @FXML
  private void handleAbout(ActionEvent event) {
    getController().showAboutWindow();
  }

  private boolean isAppExitConfirmed() {
    return getController().isUnsavedChangesDiscardConfirmed();
  }

  Path showSelectFileToOpenChooser() {
    return Dialogs.fileChooser()
        .withTitle("Select File")
        .withExtensionFilter("Text File", ".txt")
        .showOpenDialog(getWindow());
  }

  void showViewLoadErrorDialog() {
    Dialogs.errorAlert()
        .withHeader("View Error")
        .withContent("Something went wrong while trying to initialize a new view.")
        .withOwner(getWindow())
        .showAndWait();
  }

  void showFileOpenErrorDialog() {
    Dialogs.errorAlert()
        .withHeader("Cannot Open File")
        .withContent("Something went wrong while trying to open a file.")
        .withOwner(getWindow())
        .showAndWait();
  }

  boolean showConfirmUnsavedChangesDiscardDialog() {
    Optional<ButtonType> res =
        Dialogs.confirmationAlert()
            .withHeader("Discard Unsaved Changes")
            .withContent("Exit and discard unsaved changes?")
            .withOwner(getWindow())
            .showAndWait();
    return res.isPresent() && res.get().equals(ButtonType.OK);
  }

  void showFileAlreadyOpenErrorDialog() {
    Dialogs.errorAlert()
        .withHeader("File Already Open")
        .withContent("A file cannot be opened multiple times.")
        .withOwner(getWindow())
        .showAndWait();
  }
}
