package de.fullben.editor;

/**
 * Indicates that a specific file is already handled by a known {@link FileHandler}.
 *
 * @author Benedikt Full
 */
public class AlreadyHandledException extends Exception {

  public AlreadyHandledException(String message) {
    super(message);
  }

  public AlreadyHandledException() {
    super();
  }
}
