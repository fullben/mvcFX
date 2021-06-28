package de.fullben.mvcfx.theme;

import static java.util.Objects.requireNonNull;

import java.util.List;
import java.util.Objects;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;

/**
 * Can be used to represent a theme that overrides (modifies) an existing theme.
 *
 * @see UserAgentStylesheetTheme
 * @author Benedikt Full
 */
public class OverridingStylesheetTheme implements Theme {

  private final String stylesheet;
  private final Theme baseTheme;

  /**
   * Creates a new overriding theme, modifying the given {@code baseTheme}.
   *
   * @param stylesheet the resource which defines the overrides of this theme
   * @param baseTheme the theme that will be overridden by the stylesheet, must not {@code null} or
   *     an instance of this class
   */
  public OverridingStylesheetTheme(String stylesheet, Theme baseTheme) {
    this.stylesheet = requireNonNull(stylesheet);
    this.baseTheme = requireNotOverridingStyleSheetTheme(requireNonNull(baseTheme));
  }

  @Override
  public void applyTo(Scene scene) {
    List<String> stylesheets = scene.getStylesheets();
    stylesheets.clear();
    baseTheme.applyTo(scene);
    stylesheets.add(stylesheet);
  }

  @Override
  public void applyTo(Parent parent) {
    List<String> stylesheets = parent.getStylesheets();
    stylesheets.clear();
    baseTheme.applyTo(parent);
    stylesheets.add(stylesheet);
  }

  @Override
  public void applyTo(Alert alert) {
    List<String> stylesheets = alert.getDialogPane().getStylesheets();
    stylesheets.clear();
    baseTheme.applyTo(alert);
    stylesheets.add(stylesheet);
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

  private static Theme requireNotOverridingStyleSheetTheme(Theme theme) {
    if (theme instanceof OverridingStylesheetTheme) {
      throw new IllegalArgumentException(
          "Theme must not be an " + OverridingStylesheetTheme.class.getSimpleName());
    }
    return theme;
  }
}
