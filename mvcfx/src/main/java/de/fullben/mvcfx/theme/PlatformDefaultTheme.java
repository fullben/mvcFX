package de.fullben.mvcfx.theme;

/**
 * A theme implementation of the JavaFX default theme. This theme is the same as the one
 * automatically applied to any JavaFX application if no overriding stylesheets are defined.
 *
 * @author Benedikt Full
 */
public class PlatformDefaultTheme extends UserAgentStylesheetTheme {

  /** Creates a new platform-specific default theme. */
  public PlatformDefaultTheme() {
    super(null);
  }
}
