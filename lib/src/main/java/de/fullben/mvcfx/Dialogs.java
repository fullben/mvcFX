package de.fullben.mvcfx;

import java.io.File;
import java.nio.file.Path;
import java.util.Optional;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Window;

/**
 * The {@code Dialogs} class provides various static factory methods for building dialogs such as
 * alerts and file choosers.
 *
 * @author Benedikt Full
 */
public final class Dialogs {

  private Dialogs() {
    throw new AssertionError();
  }

  /**
   * Shows a directory chooser configured with the given values.
   *
   * @param title the title of the chooser dialog
   * @param initDir the initial directory of the chooser, or {@code null} for the default
   * @param owner the owning window
   * @return the directory selected by the user or {@code null} if no directory was selected
   * @see #directoryChooser()
   */
  public static Path directoryChooser(String title, Path initDir, Window owner) {
    DirectoryChooser dirChooser = new DirectoryChooser();
    dirChooser.setTitle(title);
    if (initDir != null) {
      dirChooser.setInitialDirectory(initDir.toFile());
    }
    File f = dirChooser.showDialog(owner);
    return f != null ? f.toPath() : null;
  }

  /**
   * Returns a builder for a directory chooser.
   *
   * @return the builder for the directory chooser
   * @see #directoryChooser(String, Path, Window)
   */
  public static DirectoryChooserBuilder directoryChooser() {
    return new DirectoryChooserBuilder();
  }

  /**
   * Shows a file chooser configured with the given values.
   *
   * @param title the title of the chooser dialog
   * @param initDir the initial directory of the chooser, or {@code null} for the default
   * @param extensionFilter the type of file (by extension) which can be chosen with the chooser
   * @param owner the owning window
   * @return the file selected by the user or {@code null} if no file was selected
   * @see #fileChooser()
   */
  public static Path fileChooser(
      String title, Path initDir, ExtensionFilter extensionFilter, Window owner) {
    FileChooser fileChooser = new FileChooser();
    fileChooser.setTitle(title);
    fileChooser.setSelectedExtensionFilter(extensionFilter);
    if (initDir != null) {
      fileChooser.setInitialDirectory(initDir.toFile());
    }
    File f = fileChooser.showOpenDialog(owner);
    return f != null ? f.toPath() : null;
  }

  /**
   * Returns a builder for a file chooser.
   *
   * @return the builder for the file chooser
   * @see #fileChooser(String, Path, ExtensionFilter, Window)
   */
  public static FileChooserBuilder fileChooser() {
    return new FileChooserBuilder();
  }

  /**
   * Returns an error alert populated with the given contents. The alert has one button, which
   * closes the alert.
   *
   * @param header a short description of the error
   * @param content should include possible reasons for why the error occurred
   * @param owner the owning window
   * @return the generated error alert
   * @see #errorAlert()
   */
  public static Alert errorAlert(String header, String content, Window owner) {
    return buildAlert(AlertType.ERROR, header, content, owner);
  }

  /**
   * Returns an error alert builder.
   *
   * <p>An error alert has one button, which closes the alert.
   *
   * @return the builder for the error alert
   * @see #errorAlert(String, String, Window)
   */
  public static AlertBuilder errorAlert() {
    return new AlertBuilder(AlertType.ERROR);
  }

  /**
   * Returns an information alert populated with the given contents. The alert has one button, which
   * closes the alert.
   *
   * @param header a short description
   * @param content the main information text
   * @param owner the owning window
   * @return the generated information alert
   * @see #informationAlert()
   */
  public static Alert informationAlert(String header, String content, Window owner) {
    return buildAlert(AlertType.INFORMATION, header, content, owner);
  }

  /**
   * Returns an information alert builder.
   *
   * <p>A information alert has one button, which closes the alert.
   *
   * @return the builder for the information alert
   * @see #informationAlert(String, String, Window)
   */
  public static AlertBuilder informationAlert() {
    return new AlertBuilder(AlertType.INFORMATION);
  }

  /**
   * Returns a confirmation alert populated with the given contents. The alert has two buttons, one
   * affirmation button and a cancel button.
   *
   * @param header a short description
   * @param content the main text of the confirmation
   * @param owner the owning window
   * @return the generated confirmation alert
   * @see #confirmationAlert()
   */
  public static Alert confirmationAlert(String header, String content, Window owner) {
    return buildAlert(AlertType.CONFIRMATION, header, content, owner);
  }

  /**
   * Returns a confirmation alert builder.
   *
   * <p>A confirmation alert has two buttons, one affirmation button and a cancel button.
   *
   * @return the builder for the confirmation alert
   * @see #confirmationAlert(String, String, Window)
   */
  public static AlertBuilder confirmationAlert() {
    return new AlertBuilder(AlertType.CONFIRMATION);
  }

  /**
   * Returns a confirmation alert populated with the given contents. The alert has three buttons,
   * yes, no, and cancel.
   *
   * @param header a short description
   * @param content the main text of the confirmation
   * @param owner the owning window
   * @return the generated confirmation alert
   * @see #yesNoCancelConfirmationAlert()
   */
  public static Alert yesNoCancelConfirmationAlert(String header, String content, Window owner) {
    return buildAlert(
        AlertType.CONFIRMATION,
        header,
        content,
        owner,
        ButtonType.YES,
        ButtonType.NO,
        ButtonType.CANCEL);
  }

  /**
   * Returns a yes-no-cancel confirmation alert builder.
   *
   * <p>The alert has three buttons, yes, no, and cancel.
   *
   * @return the builder for the confirmation alert
   * @see #yesNoCancelConfirmationAlert(String, String, Window)
   */
  public static AlertBuilder yesNoCancelConfirmationAlert() {
    return new AlertBuilder(
        AlertType.CONFIRMATION, ButtonType.YES, ButtonType.NO, ButtonType.CANCEL);
  }

  private static Alert buildAlert(AlertType type, String header, String content, Window owner) {
    Alert alert = new Alert(type);
    alert.setHeaderText(header);
    alert.setContentText(content);
    alert.initOwner(owner);
    ViewManager.get().register(alert);
    return alert;
  }

  private static Alert buildAlert(
      AlertType type, String header, String content, Window owner, ButtonType... buttonTypes) {
    Alert alert = new Alert(type, null, buttonTypes);
    alert.setHeaderText(header);
    alert.setContentText(content);
    alert.initOwner(owner);
    ViewManager.get().register(alert);
    return alert;
  }

  /**
   * A builder for creating {@link Alert}s.
   *
   * @author Benedikt Full
   */
  public static class AlertBuilder {

    private final AlertType type;
    private ButtonType[] buttonTypes;
    private String header;
    private String content;
    private Window owner;

    private AlertBuilder(AlertType type) {
      this.type = type;
    }

    private AlertBuilder(AlertType type, ButtonType... buttonTypes) {
      this.type = type;
      this.buttonTypes = buttonTypes;
    }

    /**
     * Sets the header of the alert.
     *
     * @param header the header of the alert
     * @return the builder instance the method was called on
     */
    public AlertBuilder withHeader(String header) {
      this.header = header;
      return this;
    }

    /**
     * Sets the content of the alert.
     *
     * @param content the content of the alert
     * @return the builder instance the method was called on
     */
    public AlertBuilder withContent(String content) {
      this.content = content;
      return this;
    }

    /**
     * Sets the owning window of the alert.
     *
     * @param owner the owner of the alert
     * @return the builder instance the method was called on
     */
    public AlertBuilder withOwner(Window owner) {
      this.owner = owner;
      return this;
    }

    /**
     * Creates an alert dialog based on the state of this builder.
     *
     * @return the new alert
     * @see #show()
     * @see #showAndWait()
     */
    public Alert build() {
      if (buttonTypes == null) {
        return buildAlert(type, header, content, owner);
      }
      return buildAlert(type, header, content, owner, buttonTypes);
    }

    /**
     * Creates an alert dialog based on the state of this builder and shows it but does not wait for
     * a user response (see {@link Alert#show()}).
     *
     * @see #build()
     * @see #showAndWait()
     */
    public void show() {
      build().show();
    }

    /**
     * Creates an alert dialog based on the state of this builder and shows it, waiting for a user
     * response (see {@link Alert#showAndWait()}).
     *
     * @return the button pressed by the user
     * @see #build()
     * @see #showAndWait()
     */
    public Optional<ButtonType> showAndWait() {
      return build().showAndWait();
    }
  }

  /**
   * A builder for creating {@link DirectoryChooser}s.
   *
   * @author Benedikt Full
   */
  public static class DirectoryChooserBuilder {

    private String title;
    private Path initialDirectory;

    private DirectoryChooserBuilder() {
      title = null;
      initialDirectory = null;
    }

    /**
     * Sets the title of the directory chooser dialog.
     *
     * @param title the title of the directory chooser
     * @return the builder instance the method was called on
     */
    public DirectoryChooserBuilder withTitle(String title) {
      this.title = title;
      return this;
    }

    /**
     * Sets the directory that will be displayed by the directory chooser dialog.
     *
     * @param initialDirectory the initial directory of the directory chooser
     * @return the builder instance the method was called on
     */
    public DirectoryChooserBuilder withInitialDirectory(Path initialDirectory) {
      this.initialDirectory = initialDirectory;
      return this;
    }

    /**
     * Creates a directory chooser dialog based on the state of this builder.
     *
     * @return the new directory chooser
     * @see #showDialog(Window)
     */
    public DirectoryChooser build() {
      if (title == null) {
        throw new IllegalStateException("Title is required");
      }
      DirectoryChooser directoryChooser = new DirectoryChooser();
      directoryChooser.setTitle(title);
      if (initialDirectory != null) {
        directoryChooser.setInitialDirectory(initialDirectory.toFile());
      }
      return directoryChooser;
    }

    /**
     * Builds a directory chooser dialog based on the state of this builder and shows it.
     *
     * @param owner the owning window
     * @return the directory chosen by the user or {@code null} if no directory was chosen
     */
    public Path showDialog(Window owner) {
      File file = build().showDialog(owner);
      return file != null ? file.toPath() : null;
    }
  }

  /**
   * A builder for creating {@link FileChooser}s.
   *
   * @author Benedikt Full
   */
  public static class FileChooserBuilder {

    private String title;
    private Path initialDirectory;
    private ExtensionFilter extensionFilter;

    private FileChooserBuilder() {}

    /**
     * Sets the title of the file chooser dialog.
     *
     * @param title the title of the file chooser
     * @return the builder instance the method was called on
     */
    public FileChooserBuilder withTitle(String title) {
      this.title = title;
      return this;
    }

    /**
     * Sets the directory that will be displayed by the file chooser directory.
     *
     * @param initialDirectory the initial directory of the file chooser
     * @return the builder instance the method was called on
     */
    public FileChooserBuilder withInitialDirectory(Path initialDirectory) {
      this.initialDirectory = initialDirectory;
      return this;
    }

    /**
     * Sets the filter for what type of file can be selected using the file chooser.
     *
     * @param extensionFilter the type of file (by extension) which can be chosen with the chooser
     *     resulting from this builder
     * @return the builder instance the method was called on
     */
    public FileChooserBuilder withExtensionFilter(ExtensionFilter extensionFilter) {
      this.extensionFilter = extensionFilter;
      return this;
    }

    /**
     * Creates a file chooser dialog based on the state of this builder.
     *
     * @return the new file chooser
     * @see #showOpenDialog(Window)
     */
    public FileChooser build() {
      FileChooser fileChooser = new FileChooser();
      fileChooser.setTitle(title);
      fileChooser.setSelectedExtensionFilter(extensionFilter);
      if (initialDirectory != null) {
        fileChooser.setInitialDirectory(initialDirectory.toFile());
      }
      return fileChooser;
    }

    /**
     * Builds a file chooser dialog based on the state of this builder and shows it.
     *
     * @param owner the owning window
     * @return the file chosen by the user or {@code null} if no file was chosen
     */
    public Path showOpenDialog(Window owner) {
      File file = build().showOpenDialog(owner);
      return file != null ? file.toPath() : null;
    }
  }
}
