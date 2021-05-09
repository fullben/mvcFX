package de.fullben.editor.ui;

import javafx.scene.image.Image;
import javafx.stage.Stage;

public final class Stages {

  private static final Image APP_ICON = new Image("editor-icon.png");

  private Stages() {
    throw new AssertionError();
  }

  public static void setIcon(Stage stage) {
    stage.getIcons().clear();
    stage.getIcons().add(APP_ICON);
  }
}
