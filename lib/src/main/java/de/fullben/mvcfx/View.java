package de.fullben.mvcfx;

import static java.util.Objects.requireNonNull;

import java.io.IOException;
import java.net.URL;
import java.text.MessageFormat;
import java.util.ResourceBundle;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.stage.Window;

/**
 * Base class for all view classes which manage a JavaFX user interface component loaded from an
 * {@code fxml} file.
 *
 * <p>In order to load the user interface and inject it into the view class, any implementing views
 * must call the {@link #load()} method. This method should always be called as the last operation
 * the constructor of any view.
 *
 * <pre>
 *   public class MyView extends View&lt;MyModel, MyController&gt; {
 *     ...
 *     public MyView(MyModel model, MyController controller, ResourceBundle res) throws IOException {
 *       super(model, controller, res);
 *       load();
 *     }
 *
 *     &#064;FXML
 *     public void initialize() {
 *       // Your own initialization code here, called automatically after the constructor
 *     }
 *     ...
 * </pre>
 *
 * Alternatively, {@code load()} may also be called before some setup operations are performed on
 * user interface components which are injected by {@code load()}.
 *
 * <pre>
 *   ...
 *   &#064;FXML private TabPane tabs;
 *   private final int initialTabCount;
 *
 *   public MyView(MyModel model, MyController controller, ResourceBundle res) throws IOException {
 *     super(model, controller, res);
 *     load(); // Will inject tabs
 *     initialTabCount = tabs.getTabs().size();
 *   }
 *   ...
 * </pre>
 *
 * Note that the view expects to find the {@code fxml} source in a resource file found in the {@link
 * #FXML_DIR} directory. The name of the file must be equal to the simple class name of the view
 * class.
 *
 * @see StageView
 * @see Controller
 * @param <ModelType> the type of the model of this view
 * @param <ControllerType> the type of the controller of this view
 * @author Benedikt Full
 */
public abstract class View<ModelType, ControllerType extends Controller> {

  private static final String FXML_DIR = "/views/";
  private static final String FXML_EXTENSION = ".fxml";
  private final ModelType model;
  private final ControllerType controller;
  private final URL fxmlViewUrl;
  private final ResourceBundle resources;
  private Parent root;

  /**
   * Creates a new view with the given resources. Callers of this constructor must also call {@link
   * #load()}.
   *
   * @param model the model associated with this view
   * @param controller the controller of this view
   * @param resources the resource bundle to be utilized by this view
   */
  public View(ModelType model, ControllerType controller, ResourceBundle resources) {
    this.model = requireNonNull(model, "Model must not be null");
    this.controller = requireNonNull(controller, "Controller must not be null");
    fxmlViewUrl = View.class.getResource(FXML_DIR + getClass().getSimpleName() + FXML_EXTENSION);
    this.resources = requireNonNull(resources, "Resources must not be null");
    root = null;
  }

  /**
   * Loads the view from the {@code fxml} file and injects all relevant member fields.
   *
   * @throws IOException if an error is encountered while attempting to read from the view file
   * @throws IllegalStateException if the method is called more than once during the object's life
   *     cycle
   */
  protected void load() throws IOException {
    if (root != null) {
      throw new IllegalStateException("Cannot load view, has been loaded already");
    }
    final FXMLLoader loader = new FXMLLoader();
    loader.setController(this);
    loader.setLocation(fxmlViewUrl);
    loader.setResources(resources);
    root = loader.load();
  }

  /**
   * Returns the root node of the view.
   *
   * @return the root element of the view
   * @throws IllegalStateException if this is method is called before {@link #load()} has been
   *     called
   */
  public Parent getRoot() {
    assertViewLoaded();
    return root;
  }

  /**
   * Returns the window associated with the root element of the view.
   *
   * @return the window hosting the view
   * @throws IllegalStateException if this is method is called before {@link #load()} has been
   *     called
   */
  protected Window getWindow() {
    assertViewLoaded();
    return root.getScene().getWindow();
  }

  /**
   * Returns the string resource identified by the given {@code key}.
   *
   * <p>The main purpose of this method is the retrieval of localized user interface messages.
   *
   * @param key a valid resource key
   * @return the resource value
   * @throws NullPointerException if the given {@code key} is {@code null}
   * @throws java.util.MissingResourceException if no string for the given key can be found
   * @see #getString(String, Object...)
   */
  protected String getString(String key) {
    return resources.getString(key);
  }

  /**
   * Returns the string resource identified by the given {@code key} after having inserted the given
   * arguments.
   *
   * <p>The main purpose of this method is the retrieval of localized user interface messages.
   *
   * @param key a valid resource key
   * @param arguments values for filling the placeholders in the resource string identified by the
   *     given key
   * @return the resource value
   * @throws NullPointerException if the given {@code key} is {@code null}
   * @throws java.util.MissingResourceException if no string for the given key can be found
   * @see #getString(String)
   */
  protected String getString(String key, Object... arguments) {
    if (arguments == null || arguments.length == 0) {
      return getString(key);
    }
    return MessageFormat.format(getString(key), arguments);
  }

  /**
   * Returns the model instance associated with the view. This method is meant for view-internal use
   * only.
   *
   * @return the model of the view
   */
  protected ModelType getModel() {
    return model;
  }

  /**
   * Returns the controller instance associated with the view. This method is meant for
   * view-internal use only.
   *
   * @return the controller of the view
   */
  protected ControllerType getController() {
    return controller;
  }

  public boolean isLoaded() {
    return root != null;
  }

  private void assertViewLoaded() {
    if (!isLoaded()) {
      throw new IllegalStateException(
          "View has not been loaded yet (Missing call to load() in the view constructor?)");
    }
  }
}
