package de.fullben.editor.ui.themes;

public enum ThemeType {
  MODENA_LIGHT("preferences.theme.modena-light"),
  MODENA_DARK("preferences.theme.modena-dark"),
  J_METRO_LIGHT("preferences.theme.jmetro-light"),
  J_METRO_DARK("preferences.theme.jmetro-dark");

  private final String key;

  ThemeType(String key) {
    this.key = key;
  }

  public String getKey() {
    return key;
  }
}
