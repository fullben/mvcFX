package de.fullben.editor.ui.main;

import de.fullben.editor.model.AlreadyHandledException;
import de.fullben.editor.ui.I18n;
import de.fullben.editor.ui.about.AboutController;
import de.fullben.editor.ui.file.FileTabController;
import de.fullben.editor.ui.file.FileTabModel;
import de.fullben.editor.ui.preferences.PreferencesController;
import de.fullben.editor.ui.preferences.PreferencesModel;
import de.fullben.mvcfx.Controller;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class MainController implements Controller {

  private final MainModel model;
  private MainView view;
  private final List<FileTabController> fileTabControllers;

  public MainController(MainModel model) {
    this.model = model;
    view = null;
    fileTabControllers = new ArrayList<>();
  }

  void openFile() {
    Path file = view.showSelectFileToOpenChooser();
    if (file == null) {
      return;
    }
    FileTabController openedFile;
    try {
      openedFile = new FileTabController(model.create(file));
    } catch (IOException e) {
      view.showFileOpenErrorDialog();
      return;
    } catch (AlreadyHandledException e) {
      view.showFileAlreadyOpenErrorDialog();
      return;
    }
    try {
      openedFile.startUp();
      fileTabControllers.add(openedFile);
      view.addFileTab(openedFile);
    } catch (IOException e) {
      view.showViewLoadErrorDialog();
    }
  }

  void createNewFileTab() {
    FileTabController controller;
    try {
      String tabName = I18n.getString("file.new.title", model.getNewTabNumber());
      controller = new FileTabController(model.create(tabName));
    } catch (IOException e) {
      view.showFileOpenErrorDialog();
      return;
    }
    try {
      controller.startUp();
      fileTabControllers.add(controller);
      view.addFileTab(controller);
      model.incrementNewTabNumber();
    } catch (IOException e) {
      view.showViewLoadErrorDialog();
    }
  }

  void select(FileTabController controller) {
    model.currentFileTabModelProperty().set(controller.getModel());
  }

  boolean closeFileTab(FileTabController controller) {
    if (!controller.isCloseConfirmed()) {
      return false;
    }
    model.closeFileTab(controller.getModel());
    int index = fileTabControllers.indexOf(controller);
    fileTabControllers.remove(controller);
    if (fileTabControllers.isEmpty()) {
      createNewFileTab();
    } else {
      if (index != 0 && index != fileTabControllers.size() - 1) {
        index--;
      }
      select(fileTabControllers.get(index));
    }
    return true;
  }

  boolean closeCurrentFileTab() {
    return closeFileTab(findByModel(model.currentFileTabModelProperty().get()));
  }

  boolean isUnsavedChangesDiscardConfirmed() {
    boolean unsavedChanges = false;
    for (FileTabController controller : fileTabControllers) {
      if (controller.fileHasUnsavedChanges()) {
        unsavedChanges = true;
        break;
      }
    }
    return !unsavedChanges || view.showConfirmUnsavedChangesDiscardDialog();
  }

  void saveCurrentFile() {
    findByModel(model.currentFileTabModelProperty().get()).saveFile();
  }

  void saveCurrentFileAs() {
    findByModel(model.currentFileTabModelProperty().get()).saveFileAs();
  }

  void showCurrentFileInFileManager() {
    findByModel(model.currentFileTabModelProperty().get()).showFileInFileManager();
  }

  void openPreferencesWindow() {
    try {
      PreferencesController controller =
          new PreferencesController(new PreferencesModel(model.getPreferences()));
      controller.startUp();
    } catch (IOException e) {
      view.showViewLoadErrorDialog();
    }
  }

  void showAboutWindow() {
    try {
      AboutController controller = new AboutController();
      controller.startUp();
    } catch (IOException e) {
      view.showViewLoadErrorDialog();
    }
  }

  @Override
  public void startUp() throws IOException {
    if (view != null) {
      return;
    }
    view = new MainView(model, this);
    createNewFileTab();
    view.show();
  }

  private FileTabController findByModel(FileTabModel model) {
    if (model == null) {
      throw new IllegalArgumentException("Model must not be null");
    }
    for (FileTabController controller : fileTabControllers) {
      if (controller.getModel().equals(model)) {
        return controller;
      }
    }
    throw new IllegalStateException("No controller for file " + model.nameProperty().get());
  }
}
