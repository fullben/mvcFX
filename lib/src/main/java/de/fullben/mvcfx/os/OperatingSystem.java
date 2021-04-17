package de.fullben.mvcfx.os;

import com.sun.javafx.PlatformUtil;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * An {@code OperatingSystem} represents an interface to the operating system hosting the
 * application. It provides convenient access to certain operations on the host, such as opening a
 * file manager. The current host can be obtained from the {@link #hosting()} method.
 *
 * @author Benedikt Full
 */
public abstract class OperatingSystem {

  private static OperatingSystem host;
  private final String name;

  OperatingSystem(String name) {
    this.name = name;
  }

  /**
   * Returns an {@code OperatingSystem} implementation representing the operating system hosting the
   * JVM. If the hosting system is unknown or not supported, a no-op implementation will be returned
   * (see {@link UnsupportedOperatingSystem}).
   *
   * <p>Note that this method provides access to a lazily initialized singleton. This means that the
   * first call to the method will create the instance, while all subsequent calls to the method
   * will return a reference to this instance.
   *
   * @return the hosting operating system, never {@code null}
   */
  public static OperatingSystem hosting() {
    if (host == null) {
      synchronized (OperatingSystem.class) {
        if (host == null) {
          host = getHostingOperatingSystem();
        }
      }
    }
    return host;
  }

  /**
   * Opens the file manager of the operating system hosting the JVM at the specified directory.
   *
   * @param directory the initial directory of the file manager
   * @throws IOException if the given directory does not exist or something goes wrong while trying
   *     to open the file manager
   */
  public abstract void openFileManager(Path directory) throws IOException;

  /**
   * Opens the command-line interface of the operating system hosting the JVM at the specified
   * directory.
   *
   * @param directory the initial directory of the interface
   * @throws IOException if the given directory does not exist or something goes wrong while trying
   *     to open the interface
   */
  public abstract void openCommandLineInterface(Path directory) throws IOException;

  /**
   * Returns the name of the hosting operating system.
   *
   * @return the name of the hosting operating system, which will be the same as the value returned
   *     by {@code System.getProperty("os.name")}
   */
  public String getName() {
    return name;
  }

  static String openFileManager(String fileManagerCommand, Path directory) throws IOException {
    assertDirectoryExists(directory);
    return new Command(fileManagerCommand, quoteAbsolutePath(directory)).run();
  }

  static void assertDirectoryExists(Path path) throws IOException {
    if (path == null || Files.notExists(path) || !Files.isDirectory(path)) {
      throw new IOException("Path does not exist or is not a directory: " + path);
    }
  }

  static String quoteAbsolutePath(Path p) {
    return "\"" + p.toAbsolutePath() + "\"";
  }

  private static OperatingSystem getHostingOperatingSystem() {
    String osName = System.getProperty("os.name");
    if (PlatformUtil.isWindows()) {
      return new Windows(osName);
    } else if (PlatformUtil.isMac()) {
      return new MacOs(osName);
    } else {
      return new UnsupportedOperatingSystem(osName);
    }
  }
}
