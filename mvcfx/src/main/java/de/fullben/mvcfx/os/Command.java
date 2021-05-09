package de.fullben.mvcfx.os;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Wrapper for running a command as a separate process in the environment hosting the JVM. Can be
 * used e.g. for launching programs found in the hosting environment.
 *
 * @author Benedikt Full
 */
public class Command {

  private final ProcessBuilder processBuilder;

  Command(String... command) {
    // Redirect error stream to avoid having to read both stdout and stderr
    processBuilder = new ProcessBuilder(command).redirectErrorStream(true);
  }

  public List<String> getCommand() {
    return new ArrayList<>(processBuilder.command());
  }

  /**
   * Executes the command provided to this object's constructor as a separate process in the
   * environment hosting the JVM.
   *
   * @return the output produced by the execution of the command, stripped of all leading and
   *     trailing whitespace
   * @throws IOException if the execution of the command failed, e.g. because the program to be
   *     executed was not found, access to the program was denied, or the command contained an
   *     illegal character
   */
  public String run() throws IOException {
    Process process = processBuilder.start();
    StringBuilder result = new StringBuilder();
    try (BufferedReader reader =
        new BufferedReader(new InputStreamReader(process.getInputStream()))) {
      String line;
      while ((line = reader.readLine()) != null) {
        result.append(line).append(System.lineSeparator());
      }
    }
    // Strip to remove trailing line separator (and leading/trailing whitespace added by output)
    return result.toString().strip();
  }
}
