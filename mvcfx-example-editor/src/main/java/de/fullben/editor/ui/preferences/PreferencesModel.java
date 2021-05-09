package de.fullben.editor.ui.preferences;

import de.fullben.editor.Preferences;
import de.fullben.editor.ui.themes.ThemeType;
import java.util.Arrays;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class PreferencesModel {

  private final Preferences preferences;
  private final ObservableList<ThemeType> themeTypes;

  public PreferencesModel(Preferences preferences) {
    this.preferences = preferences;
    themeTypes =
        FXCollections.unmodifiableObservableList(
            FXCollections.observableList(Arrays.asList(ThemeType.values())));
  }

  public ObservableList<ThemeType> getThemeTypes() {
    return themeTypes;
  }

  public ThemeType getSelectedTheme() {
    return preferences.getTheme();
  }

  public void setSelectedTheme(ThemeType type) {
    preferences.setTheme(type);
  }
}
