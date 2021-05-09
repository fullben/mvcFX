package de.fullben.editor.ui.themes;

import de.fullben.mvcfx.theme.Theme;
import java.util.Objects;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import jfxtras.styles.jmetro.JMetro;
import jfxtras.styles.jmetro.Style;

public class JMetroTheme implements Theme {

  private final Style style;

  public JMetroTheme(Style style) {
    this.style = style;
  }

  private JMetro newJMetro() {
    JMetro jMetro = new JMetro(style);
    jMetro.setAutomaticallyColorPanes(true);
    return jMetro;
  }

  @Override
  public void applyTo(Scene scene) {
    newJMetro().setScene(scene);
  }

  @Override
  public void applyTo(Parent parent) {
    newJMetro().setParent(parent);
  }

  @Override
  public void applyTo(Alert alert) {
    newJMetro().setScene(alert.getDialogPane().getScene());
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    JMetroTheme that = (JMetroTheme) o;
    return style == that.style;
  }

  @Override
  public int hashCode() {
    return Objects.hash(style);
  }
}
