package de.fullben.editor.ui;

import de.fullben.editor.FileManager;
import de.fullben.editor.Preferences;
import de.fullben.editor.ui.main.MainController;
import de.fullben.editor.ui.main.MainModel;
import javafx.application.Application;
import javafx.stage.Stage;

public class Editor extends Application {

  public static void main(String[] args) {
    Application.launch(args);
  }

  @Override
  public void start(Stage primaryStage) throws Exception {
    FileManager fileManager = new FileManager();
    Preferences preferences = new Preferences();
    MainController controller = new MainController(new MainModel(fileManager, preferences));
    controller.startUp();
  }
}
