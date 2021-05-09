package de.fullben.mvcfx.os;

import java.nio.file.Path;

/**
 * No-op {@code OperatingSystem} implementation. All methods implemented by this class have no
 * effect.
 *
 * @author Benedikt Full
 */
public class UnsupportedOperatingSystem extends OperatingSystem {

  UnsupportedOperatingSystem(String name) {
    super(name);
  }

  /**
   * No-op implementation.
   *
   * @param directory the initial directory of the file manager
   */
  @Override
  public void openFileManager(Path directory) {}

  /**
   * No-op implementation.
   *
   * @param directory the initial directory of the interface
   */
  @Override
  public void openCommandLineInterface(Path directory) {}
}
