package de.fullben.editor.ui.themes;

import de.fullben.mvcfx.theme.OverridingStylesheetTheme;
import de.fullben.mvcfx.theme.PlatformDefaultTheme;

public class DarkTheme extends OverridingStylesheetTheme {

  public DarkTheme() {
    super("dark-theme.css", new PlatformDefaultTheme());
  }
}
