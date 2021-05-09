package de.fullben.editor.ui.file;

import de.fullben.mvcfx.Controller;
import de.fullben.mvcfx.os.OperatingSystem;
import java.io.IOException;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;

public class FileTabController implements Controller {

  private final FileTabModel model;
  private FileTabView view;

  public FileTabController(FileTabModel model) {
    this.model = model;
    view = null;
  }

  @Override
  public void startUp() throws IOException {
    if (view != null) {
      return;
    }
    view = new FileTabView(model, this);
  }

  public boolean saveFile() {
    try {
      model.save();
      return true;
    } catch (NoSuchFileException e) {
      return saveFileAs();
    } catch (IOException e) {
      view.showFileWriteErrorDialog();
      return false;
    }
  }

  public boolean saveFileAs() {
    Path file = view.showSaveToFileChooserDialog();
    if (file == null) {
      return false;
    }
    try {
      model.saveAs(file);
      return true;
    } catch (IOException e) {
      view.showFileWriteErrorDialog();
      return false;
    }
  }

  public void showFileInFileManager() {
    Path file = model.getFile();
    if (file == null) {
      return;
    }
    try {
      OperatingSystem.hosting().openFileManager(file.getParent());
    } catch (IOException e) {
      view.showFileManagerErrorDialog();
    }
  }

  public boolean isCloseConfirmed() {
    if (fileHasNoUnsavedChanges()) {
      return true;
    }
    Boolean saveChanges = view.showFileHasUnsavedChangesSaveDialog(model.nameProperty().get());
    if (saveChanges == null) {
      // User wants to cancel close
      return false;
    }
    if (saveChanges) {
      return saveFile();
    }
    return true;
  }

  private boolean fileHasNoUnsavedChanges() {
    return !fileHasUnsavedChanges();
  }

  public boolean fileHasUnsavedChanges() {
    return model.hasUnsavedChanges();
  }

  public String getFilename() {
    return model.nameProperty().get();
  }

  @Deprecated
  public FileTabModel getModel() {
    return model;
  }

  @Deprecated
  public FileTabView getView() {
    return view;
  }
}
