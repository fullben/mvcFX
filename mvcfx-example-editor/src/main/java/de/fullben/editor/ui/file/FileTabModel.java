package de.fullben.editor.ui.file;

import de.fullben.editor.model.FileHandler;
import java.io.IOException;
import java.nio.file.Path;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class FileTabModel {

  private final FileHandler fileHandler;
  private final StringProperty name;
  private final StringProperty contents;
  private final ReadOnlyObjectWrapper<Path> file;

  public FileTabModel(FileHandler fileHandler) throws IOException {
    this.fileHandler = fileHandler;
    name = new SimpleStringProperty(fileHandler.getFilename());
    contents = new SimpleStringProperty(fileHandler.loadFileContents());
    this.file = new ReadOnlyObjectWrapper<>(fileHandler.getFile());
  }

  public void save() throws IOException {
    fileHandler.saveFileContents(contents.get());
  }

  public boolean hasUnsavedChanges() {
    try {
      return !fileHandler.matchesCurrentFileContents(contents.get());
    } catch (IOException e) {
      return false;
    }
  }

  public void saveAs(Path file) throws IOException {
    Path oldFile = fileHandler.getFile();
    fileHandler.setFile(file);
    try {
      fileHandler.saveFileContents(contents.get());
    } catch (IOException e) {
      fileHandler.setFile(oldFile);
      throw e;
    }
    this.file.set(file);
    name.set(fileHandler.getFilename());
  }

  public FileHandler getFileHandler() {
    return fileHandler;
  }

  public Path getFile() {
    return fileHandler.getFile();
  }

  public ReadOnlyObjectProperty<Path> fileProperty() {
    return file.getReadOnlyProperty();
  }

  public StringProperty contentsProperty() {
    return contents;
  }

  public StringProperty nameProperty() {
    return name;
  }
}
