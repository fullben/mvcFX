package de.fullben.mvcfx.os;

import java.io.IOException;
import java.nio.file.Path;

/**
 * {@code OperatingSystem} implementation for supporting operations on macOS operating systems.
 *
 * @author Benedikt Full
 */
public class MacOs extends OperatingSystem {

  MacOs(String name) {
    super(name);
  }

  /**
   * Opens the macOS Finder at the given directory.
   *
   * @param directory the initial directory of the Finder
   * @throws IOException if the given directory does not exist or something goes wrong while trying
   *     to open the Finder
   */
  @Override
  public void openFileManager(Path directory) throws IOException {
    openFileManager("open", directory);
  }

  /**
   * Opens the Terminal.app command-line interpreter at the given directory.
   *
   * @param directory the initial directory of the Terminal
   * @throws IOException if the given directory does not exist or something goes wrong while trying
   *     to open the Terminal
   */
  @Override
  public void openCommandLineInterface(Path directory) throws IOException {
    assertDirectoryExists(directory);
    new Command("/usr/bin/open", "-a", "Terminal", "cd", quoteAbsolutePath(directory)).run();
  }
}
