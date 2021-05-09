package de.fullben.editor.ui.preferences;

import de.fullben.editor.ui.themes.DarkTheme;
import de.fullben.editor.ui.themes.JMetroTheme;
import de.fullben.editor.ui.themes.ThemeType;
import de.fullben.mvcfx.Controller;
import de.fullben.mvcfx.ViewManager;
import de.fullben.mvcfx.theme.PlatformDefaultTheme;
import java.io.IOException;
import jfxtras.styles.jmetro.Style;

public class PreferencesController implements Controller {

  private final PreferencesModel model;
  private PreferencesView view;

  public PreferencesController(PreferencesModel model) {
    this.model = model;
    view = null;
  }

  @Override
  public void startUp() throws IOException {
    if (view != null) {
      return;
    }
    view = new PreferencesView(model, this);
    view.show();
  }

  void setTheme(ThemeType type) {
    switch (type) {
      case MODENA_LIGHT:
        ViewManager.get().setTheme(new PlatformDefaultTheme());
        break;
      case MODENA_DARK:
        ViewManager.get().setTheme(new DarkTheme());
        break;
      case J_METRO_LIGHT:
        ViewManager.get().setTheme(new JMetroTheme(Style.LIGHT));
        break;
      case J_METRO_DARK:
        ViewManager.get().setTheme(new JMetroTheme(Style.DARK));
        break;
      default:
        throw new IllegalArgumentException("Unknown theme type: " + type);
    }
    model.setSelectedTheme(type);
  }
}
