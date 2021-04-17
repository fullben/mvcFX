package de.fullben.mvcfx;

import java.io.IOException;
import java.util.ResourceBundle;
import javafx.scene.Parent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;

/**
 * Base class for all views which manage a JavaFX user interface component loaded from an {@code
 * fxml} file that is meant to be displayed in its own window.
 *
 * <pre>
 * public class MyView extends StageView&lt;MyModel, MyController&gt; {
 *     ...
 *     public MyView(MyModel model, MyController controller, ResourceBundle res) throws IOException {
 *       super(model, controller, res);
 *       load();
 *     }
 *
 *     &#064;FXML
 *     public void initialize() {
 *       // Your own initialization code here, called automatically after the constructor
 *       ...
 *     }
 *
 *     &#064;FXML
 *     public void configureStage(Parent root) {
 *       // Create and configure the stage represented by this view
 *       Stage stage = new Stage();
 *       stage.setTitle(getString("stage.title");
 *       stage.setScene(new Scene(root));
 *       return stage;
 *     }
 *     ...
 * </pre>
 *
 * This class is an extension of the {@link View} class that adds functionality for defining and
 * managing a user interface window which hosts the user interface nodes of this view.
 *
 * @author Benedikt Full
 * @see Controller
 * @param <ModelType> the type of the model of this controller
 * @param <ControllerType> the type of the controller of this view
 */
public abstract class StageView<ModelType, ControllerType extends Controller>
    extends View<ModelType, ControllerType> {

  private Stage stage;

  /**
   * Creates a new stage view with the given resources. Callers of this constructor must also call
   * {@link #load()}.
   *
   * @param model the model associated with this view
   * @param controller the controller of this view
   * @param resources the resource bundle to be utilized by this view
   */
  public StageView(ModelType model, ControllerType controller, ResourceBundle resources) {
    super(model, controller, resources);
    stage = null;
  }

  /**
   * Loads the view from the {@code fxml} file and injects all relevant member fields. This is
   * followed by initializing the the stage of the view, as defined by {@link
   * #configureStage(Parent)}.
   *
   * <p>Note that this method ensures that the stage
   *
   * <ul>
   *   <li>is hidden,
   *   <li>window modal (no events delivered to other windows of the same hierarchy),
   *   <li>and owned by one of the currently visible windows (see {@link #findVisibleWindow()}).
   * </ul>
   *
   * @throws IOException if an error is encountered while attempting to read from the view file
   * @throws IllegalStateException if the method is called more than once during the object's life
   *     cycle
   */
  @Override
  protected void load() throws IOException {
    if (stage != null) {
      throw new IllegalStateException("Cannot load stage, has been loaded already");
    }
    super.load();
    stage = configureStage(getRoot());
    stage.initModality(Modality.WINDOW_MODAL);
    stage.initOwner(findVisibleWindow());
    // Ensure stage is not visible, even if configured to be so
    stage.hide();
  }

  /**
   * Implementations of this method should create and configure a window which displays the user
   * interface defined by the given {@code root}.
   *
   * <p>This method is called automatically after the view associated with this class has been
   * loaded from the corresponding {@code fxml} file. The root element of the loaded view will be
   * provided to this method as parameter {@code root}.
   *
   * @param root the root of the view loaded from {@code fxml}
   * @return the new window of the view
   * @see #load()
   */
  protected abstract Stage configureStage(Parent root);

  @Override
  protected Window getWindow() {
    assertStageLoaded();
    return stage;
  }

  /**
   * Shows the stage of this view.
   *
   * @throws IllegalStateException if the method is called before the stage has been initialized
   * @see #hide()
   */
  public void show() {
    assertStageLoaded();
    stage.show();
  }

  /**
   * Hides the stage of this view.
   *
   * @throws IllegalStateException if the method is called before the stage has been initialized
   * @see #show()
   */
  public void hide() {
    assertStageLoaded();
    stage.hide();
  }

  private void assertStageLoaded() {
    if (stage == null) {
      throw new IllegalStateException(
          "Stage has not been loaded yet (Did you call load() in your view's constructor?)");
    }
  }

  /**
   * @return the first application window where the {@link Window#isShowing()} method returns true
   *     or {@code null} if there is no window satisfying this criteria
   */
  private static Window findVisibleWindow() {
    // Use with care, getWindows() returns list of all windows that have had their show() called
    for (Window window : Stage.getWindows()) {
      if (window.isShowing()) {
        return window;
      }
    }
    return null;
  }
}
