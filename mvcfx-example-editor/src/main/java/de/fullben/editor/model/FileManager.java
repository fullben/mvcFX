package de.fullben.editor.model;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class FileManager {

  private final List<FileHandler> fileHandlers;

  public FileManager() {
    fileHandlers = new ArrayList<>();
  }

  public FileHandler createFileHandler(Path file) throws AlreadyHandledException {
    if (hasFileHandler(file)) {
      throw new AlreadyHandledException();
    }
    FileHandler handler = new FileHandler(file);
    fileHandlers.add(handler);
    return handler;
  }

  public FileHandler createFileHandler(String name) {
    FileHandler handler = new FileHandler(name);
    fileHandlers.add(handler);
    return handler;
  }

  public boolean removeFileHandler(FileHandler handler) {
    return fileHandlers.remove(handler);
  }

  public List<FileHandler> getFileHandlers() {
    return fileHandlers;
  }

  private boolean hasFileHandler(Path file) {
    for (FileHandler handler : fileHandlers) {
      Path handlerFile = handler.getFile();
      if (handlerFile == null || !handlerFile.equals(file)) {
        continue;
      }
      return true;
    }
    return false;
  }
}
