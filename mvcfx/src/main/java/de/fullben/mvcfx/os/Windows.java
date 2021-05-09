package de.fullben.mvcfx.os;

import java.io.IOException;
import java.nio.file.Path;

/**
 * {@code OperatingSystem} implementation for supporting operations on Windows operating systems.
 *
 * @author Benedikt Full
 */
public class Windows extends OperatingSystem {

  Windows(String name) {
    super(name);
  }

  /**
   * Opens the Windows File Explorer at the given directory.
   *
   * @param directory the initial directory of the File Explorer
   * @throws IOException if the given directory does not exist or something goes wrong while trying
   *     to open the File Explorer
   */
  @Override
  public void openFileManager(Path directory) throws IOException {
    openFileManager("explorer", directory);
  }

  /**
   * Opens the cmd.exe command-line interpreter at the given directory.
   *
   * @param directory the initial directory of the interface
   * @throws IOException if the given directory does not exist or something goes wrong while trying
   *     to open the command-line interpreter
   */
  @Override
  public void openCommandLineInterface(Path directory) throws IOException {
    assertDirectoryExists(directory);
    new Command("cmd.exe", "/c", "start", "cd", quoteAbsolutePath(directory)).run();
  }
}
