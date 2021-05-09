package de.fullben.editor.ui.main;

import de.fullben.editor.AlreadyHandledException;
import de.fullben.editor.FileHandler;
import de.fullben.editor.FileManager;
import de.fullben.editor.Preferences;
import de.fullben.editor.ui.file.FileTabModel;
import java.io.IOException;
import java.nio.file.Path;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

public class MainModel {

  private final FileManager fileManager;
  private final Preferences preferences;
  private final ObjectProperty<FileTabModel> currentFileTabModel;
  private int newTabNumber;

  public MainModel(FileManager fileManager, Preferences preferences) {
    this.fileManager = fileManager;
    this.preferences = preferences;
    currentFileTabModel = new SimpleObjectProperty<>();
    newTabNumber = 1;
  }

  int getNewTabNumber() {
    return newTabNumber;
  }

  void incrementNewTabNumber() {
    newTabNumber++;
  }

  ObjectProperty<FileTabModel> currentFileTabModelProperty() {
    return currentFileTabModel;
  }

  void closeFileTab(FileTabModel model) {
    fileManager.removeFileHandler(model.getFileHandler());
    if (model.equals(currentFileTabModel.get())) {
      currentFileTabModel.set(null);
    }
  }

  FileTabModel create(Path file) throws IOException, AlreadyHandledException {
    FileHandler handler = fileManager.createFileHandler(file);
    if (handler == null) {
      throw new IllegalStateException("TODO cannot open same file twice");
    }
    FileTabModel tabModel = new FileTabModel(handler);
    currentFileTabModel.set(tabModel);
    return tabModel;
  }

  FileTabModel create(String name) throws IOException {
    FileTabModel tabModel = new FileTabModel(fileManager.createFileHandler(name));
    currentFileTabModel.set(tabModel);
    return tabModel;
  }

  public Preferences getPreferences() {
    return preferences;
  }
}
