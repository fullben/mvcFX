package de.fullben.mvcfx.theme;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;

/**
 * {@code Theme}s an be used to replace or change the style of an application.
 *
 * @see OverridingStylesheetTheme
 * @see UserAgentStylesheetTheme
 * @author Benedikt Full
 */
public interface Theme {

  /**
   * Applies the theme to the given scene.
   *
   * @param scene a scene to which the theme will be applied, must be non-{@code null}
   */
  void applyTo(Scene scene);

  /**
   * Applies the theme to the given parent.
   *
   * @param parent a parent to which the theme will be applied, must be non-{@code null}
   */
  void applyTo(Parent parent);

  /**
   * Applies the theme to the given alert.
   *
   * @param alert an alert to which the theme will be applied, must be non-{@code null}
   */
  void applyTo(Alert alert);
}
