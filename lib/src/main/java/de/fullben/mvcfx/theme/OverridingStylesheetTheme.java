package de.fullben.mvcfx.theme;

import java.util.List;
import java.util.Objects;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;

/**
 * Can be used to represent themes that override (modify) an existing theme.
 *
 * @see UserAgentStylesheetTheme
 * @author Benedikt Full
 */
public class OverridingStylesheetTheme implements Theme {

  private final String stylesheet;

  /**
   * Creates a new overriding theme.
   *
   * @param stylesheet the resource which defines the overrides of this theme
   */
  public OverridingStylesheetTheme(String stylesheet) {
    this.stylesheet = stylesheet;
  }

  @Override
  public void applyTo(Scene scene) {
    List<String> stylesheets = scene.getStylesheets();
    if (stylesheets.contains(stylesheet)) {
      return;
    }
    stylesheets.add(stylesheet);
  }

  @Override
  public void applyTo(Parent parent) {
    List<String> stylesheets = parent.getStylesheets();
    if (stylesheets.contains(stylesheet)) {
      return;
    }
    stylesheets.add(stylesheet);
  }

  @Override
  public void applyTo(Alert alert) {
    List<String> stylesheets = alert.getDialogPane().getStylesheets();
    if (stylesheets.contains(stylesheet)) {
      return;
    }
    stylesheets.add(stylesheet);
  }

  /**
   * Removes the stylesheet of this theme from the given scene.
   *
   * @param scene a scene , must be non-{@code null}
   */
  public void removeFrom(Scene scene) {
    scene.getStylesheets().remove(stylesheet);
  }

  /**
   * Removes the stylesheet of this theme from the given parent.
   *
   * @param parent a parent , must be non-{@code null}
   */
  public void removeFrom(Parent parent) {
    parent.getStylesheets().remove(stylesheet);
  }

  /**
   * Removes the stylesheet of this theme from the given alert.
   *
   * @param alert an alert , must be non-{@code null}
   */
  public void removeFrom(Alert alert) {
    alert.getDialogPane().getStylesheets().remove(stylesheet);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    OverridingStylesheetTheme that = (OverridingStylesheetTheme) o;
    return Objects.equals(stylesheet, that.stylesheet);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(stylesheet);
  }
}
