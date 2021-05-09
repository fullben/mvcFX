package de.fullben.editor.ui;

import java.text.MessageFormat;
import java.util.ResourceBundle;

public final class I18n {

  private static ResourceBundle bundle;

  private I18n() {
    throw new AssertionError();
  }

  /**
   * Returns the resource bundle. The bundle is loaded during the first call to this method. The
   * method uses the default locale to load a bundle version.
   *
   * @return the resource bundle containing the user interface text
   */
  public static ResourceBundle getBundle() {
    if (bundle == null) {
      bundle = ResourceBundle.getBundle("messages");
    }
    return bundle;
  }

  /**
   * Calling this method is equal to calling {@link ResourceBundle#getString(String)} on the bundle
   * returned by {@link #getBundle()}
   *
   * @param key the key of the desired string
   * @return the string for the given key
   */
  public static String getString(String key) {
    return getBundle().getString(key);
  }

  public static String getString(String key, Object... arguments) {
    if (arguments == null || arguments.length == 0) {
      return getString(key);
    }
    return MessageFormat.format(getString(key), arguments);
  }
}
