package de.fullben.editor.ui.file;

import de.fullben.editor.ui.I18n;
import de.fullben.mvcfx.Dialogs;
import de.fullben.mvcfx.FxmlView;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Optional;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextArea;

public class FileTabView extends FxmlView<FileTabModel, FileTabController> {

  @FXML private TextArea taContents;

  public FileTabView(FileTabModel model, FileTabController controller) throws IOException {
    super(model, controller, I18n.getBundle());
    load();
  }

  @FXML
  private void initialize() {
    // tab.textProperty().bind(getModel().nameProperty());
    taContents.textProperty().bindBidirectional(getModel().contentsProperty());
  }

  @Override
  public Parent getRoot() {
    return super.getRoot();
  }

  void showFileWriteErrorDialog() {
    Dialogs.errorAlert()
        .withHeader("File Write Error")
        .withContent("Something went wrong while trying to write to a file.")
        .withOwner(getWindow())
        .showAndWait();
  }

  Path showSaveToFileChooserDialog() {
    return Dialogs.fileChooser()
        .withTitle("Select File")
        .withExtensionFilter("Text File", ".txt")
        .showSaveDialog(getWindow());
  }

  void showFileManagerErrorDialog() {
    Dialogs.errorAlert()
        .withHeader("Uh Oh")
        .withContent("Failed to open file manager of hosting operating system")
        .withOwner(getWindow())
        .showAndWait();
  }

  Boolean showFileHasUnsavedChangesSaveDialog(String name) {
    Optional<ButtonType> confirm =
        Dialogs.yesNoCancelConfirmationAlert()
            .withHeader(getString("main.confirm.save.title"))
            .withContent(getString("main.confirm.save.content", name))
            .withOwner(getWindow())
            .showAndWait();
    if (confirm.isEmpty()) {
      return null;
    }
    if (confirm.get() == ButtonType.YES) {
      return Boolean.TRUE;
    } else if (confirm.get() == ButtonType.NO) {
      return Boolean.FALSE;
    } else {
      return null;
    }
  }
}
