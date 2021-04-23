package de.fullben.mvcfx;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
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
 *   public class MyView extends FxmlView&lt;MyModel, MyController&gt; {
 *     ...
 *     public MyView(MyModel m, MyController c, ResourceBundle r) throws IOException {
 *       super(m, c, r);
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
 *   public MyView(MyModel m, MyController c, ResourceBundle r) throws IOException {
 *     super(m, c, r);
 *     load(); // Injects tabs
 *     initialTabCount = tabs.getTabs().size();
 *   }
 *   ...
 * </pre>
 *
 * <p>Note that the view expects to find the {@code fxml} file in the views resource directory
 * ({@link #FXML_DIR}) or in the same package as the view class. The name of the file must be equal
 * to the simple class name of the view class.
 *
 * @see View
 * @see StageView
 * @see FxmlStageView
 * @param <ModelType> the type of the model of this view
 * @param <ControllerType> the type of the controller of this view
 * @author Benedikt Full
 */
public abstract class FxmlView<ModelType, ControllerType extends Controller>
    extends View<ModelType, ControllerType> {

  private static final String FXML_DIR = "/views/";
  private static final String FXML_EXTENSION = ".fxml";

  /**
   * Creates a new view with the given resources. Users of this constructor must ensure that {@link
   * #load()} is called, which will load the user interface components represented by this view from
   * the appropriate {@code fxml} file.
   *
   * @param model the model associated with this view
   * @param controller the controller of this view
   * @param resources the resource bundle to be utilized by this view
   */
  public FxmlView(ModelType model, ControllerType controller, ResourceBundle resources) {
    super(model, controller, resources, false);
  }

  /**
   * Throws an {@link UnsupportedOperationException}.
   *
   * @return throws an exception
   */
  @Override
  protected final Parent initRoot() {
    throw new UnsupportedOperationException();
  }

  /**
   * Loads the view from the {@code fxml} file and injects all relevant member fields before
   * registering the view with the view manager.
   *
   * <p>This method should be called by the constructor of any extending class.
   *
   * @throws IOException if an error is encountered while attempting to read from the view file
   * @throws IllegalStateException if the method is called more than once during the object's life
   *     cycle
   */
  protected void load() throws IOException {
    setRoot(loadFxml());
    ViewManager.get().register(this);
  }

  /**
   * Loads the view from the {@code fxml} file and injects all relevant member fields.
   *
   * @throws IOException if an error is encountered while attempting to read from the view file
   * @throws IllegalStateException if the method is called more than once during the object's life
   *     cycle
   */
  final Parent loadFxml() throws IOException {
    if (super.getRoot() != null) {
      throw new IllegalStateException("Cannot load view, has been loaded already");
    }
    final FXMLLoader loader = new FXMLLoader();
    loader.setController(this);
    loader.setLocation(findFxmlResource());
    loader.setResources(getResources());
    return loader.load();
  }

  private URL findFxmlResource() {
    // Fxml file in fxml views directory
    String filename = FXML_DIR + getClass().getSimpleName() + FXML_EXTENSION;
    URL fxml = getClass().getResource(filename);
    if (fxml != null) {
      return fxml;
    }
    // Fxml file in same package as views class
    filename = "/" + getClass().getName().replace(".", "/") + FXML_EXTENSION;
    fxml = getClass().getResource(filename);
    if (fxml != null) {
      return fxml;
    }
    throw new IllegalStateException("Cannot find FXML view file");
  }

  /**
   * Returns the root node of the view.
   *
   * @return the root element of the view
   * @throws IllegalStateException if this is method is called before {@link #load()} has been
   *     called
   */
  @Override
  protected Parent getRoot() {
    assertViewLoaded();
    return super.getRoot();
  }

  /**
   * Returns the scene of the root node (accessible through {@link #getRoot()}) of the view.
   *
   * @return the scene hosting the root node
   * @throws IllegalStateException if this is method is called before {@link #load()} has been
   *     called
   */
  @Override
  protected Scene getScene() {
    assertViewLoaded();
    return super.getScene();
  }

  /**
   * Returns the window associated with the root element (accessible through {@link #getRoot()}) of
   * the view.
   *
   * @return the window hosting the view
   * @throws IllegalStateException if this is method is called before {@link #load()} has been
   *     called
   */
  @Override
  protected Window getWindow() {
    assertViewLoaded();
    return super.getWindow();
  }

  /**
   * This method can be used to check whether the user interface components represented by this view
   * have been initialized yet.
   *
   * @return {@code true} if the components have been initialized already, {@code false} if not
   */
  public final boolean isLoaded() {
    return super.getRoot() != null;
  }

  private void assertViewLoaded() {
    if (!isLoaded()) {
      throw new IllegalStateException(
          "View has not been loaded yet (Did you call load() in the view constructor?)");
    }
  }
}
