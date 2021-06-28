package de.fullben.mvcfx.theme;

import java.util.Objects;
import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;

/**
 * Can be used to represent themes that provide a style sheet defining theming instructions for all
 * user interface components, also known as user agent stylesheet.
 *
 * @see OverridingStylesheetTheme
 * @author Benedikt Full
 */
public class UserAgentStylesheetTheme implements Theme {

  private final String stylesheet;

  /**
   * Constructs a new application theme.
   *
   * @param stylesheet the resource which defines the application theme
   */
  public UserAgentStylesheetTheme(String stylesheet) {
    this.stylesheet = stylesheet;
  }

  @Override
  public void applyTo(Scene scene) {
    ObservableList<String> stylesheets = scene.getStylesheets();
    stylesheets.clear();
    Application.setUserAgentStylesheet(stylesheet);
  }

  @Override
  public void applyTo(Parent parent) {
    ObservableList<String> stylesheets = parent.getStylesheets();
    stylesheets.clear();
    Application.setUserAgentStylesheet(stylesheet);
  }

  @Override
  public void applyTo(Alert alert) {
    ObservableList<String> stylesheets = alert.getDialogPane().getStylesheets();
    stylesheets.clear();
    Application.setUserAgentStylesheet(stylesheet);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    UserAgentStylesheetTheme that = (UserAgentStylesheetTheme) o;
    return Objects.equals(stylesheet, that.stylesheet);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(stylesheet);
  }
}
