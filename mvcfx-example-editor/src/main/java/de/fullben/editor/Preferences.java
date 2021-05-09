package de.fullben.editor;

import de.fullben.editor.ui.themes.ThemeType;

public class Preferences {

  private ThemeType theme;

  public Preferences() {
    theme = ThemeType.MODENA_LIGHT;
  }

  public ThemeType getTheme() {
    return theme;
  }

  public void setTheme(ThemeType theme) {
    this.theme = theme;
  }
}
