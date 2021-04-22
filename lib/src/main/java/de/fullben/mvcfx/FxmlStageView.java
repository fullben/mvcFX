package de.fullben.mvcfx;

import java.io.IOException;
import java.util.ResourceBundle;
import javafx.scene.Parent;
import javafx.stage.Stage;

/**
 * Base class for all views which manage a JavaFX user interface component loaded from an {@code
 * fxml} file that is meant to be displayed in its own window.
 *
 * <p>In order to load the user interface and inject it into the view class, any implementing views
 * must call the {@link #load()} method. This method should always be called as the last operation
 * the constructor of any view.
 *
 * <pre>
 * public class MyView extends FxmlStageView&lt;MyModel, MyController&gt; {
 *     ...
 *     public MyView(MyModel m, MyController c, ResourceBundle r) throws IOException {
 *       super(m, c, r);
 *       load();
 *     }
 *
 *     &#064;FXML
 *     public void initialize() {
 *       // Your own initialization code here, called automatically after the constructor
 *       ...
 *     }
 *
 *     &#064;Override
 *     protected void initStage(Parent root) {
 *       Stage stage = new Stage();
 *       stage.setTitle(getString("stage.title");
 *       stage.setScene(new Scene(root));
 *       return stage;
 *     }
 *     ...
 * </pre>
 *
 * This class is an extension of the {@link FxmlView} class that adds functionality for defining and
 * managing a user interface window which hosts the user interface nodes of this view.
 *
 * @see View
 * @see StageView
 * @see FxmlView
 * @param <ModelType> the type of the model of this controller
 * @param <ControllerType> the type of the controller of this view
 * @author Benedikt Full
 */
public abstract class FxmlStageView<ModelType, ControllerType extends Controller>
    extends FxmlView<ModelType, ControllerType> {

  private Stage stage;

  /**
   * Creates a new stage view with the given resources. Callers of this constructor must also call
   * {@link #load()}.
   *
   * @param model the model associated with this view
   * @param controller the controller of this view
   * @param resources the resource bundle to be utilized by this view
   */
  public FxmlStageView(ModelType model, ControllerType controller, ResourceBundle resources) {
    super(model, controller, resources);
    stage = null;
  }

  /**
   * Implementations must initialize the {@code Stage} which this view represents.
   *
   * <p>The following snippet shows an example implementation of this method which creates a simple
   * user interface that contains a button.
   *
   * <pre>
   *   &#064;Override
   *   protected Stage initStage(Parent root) {
   *     Stage stage = new Stage();
   *     stage.setTitle(getString("stage.title"));
   *     stage.setScene(new Scene(root));
   *     return stage;
   *   }
   * </pre>
   *
   * <p>This method is called automatically after the view associated with this class has been
   * loaded from the corresponding {@code fxml} file, with the return value of {@link #initRoot()}
   * as parameter.
   *
   * @param root the root of the view loaded from {@code fxml}
   * @return the window hosting the user interface elements represented by this view, must not be
   *     {@code null}
   * @see #load()
   */
  protected abstract Stage initStage(Parent root);

  /**
   * Loads the view from the {@code fxml} file and injects all relevant member fields before
   * registering the view with the view manager. This is followed by initializing the the stage of
   * the view, as defined by {@link #initStage(Parent)}.
   *
   * <p>Note that this method ensures that the stage
   *
   * <ul>
   *   <li>hosts the root element defined by {@link #initRoot()},
   *   <li>is hidden,
   *   <li>is window modal (no events delivered to other windows of the same hierarchy),
   *   <li>and is owned by one of the currently visible windows.
   * </ul>
   *
   * @throws IOException if an error is encountered while attempting to read from the view file
   * @throws IllegalStateException if the method is called more than once during the object's life
   *     cycle
   */
  @Override
  protected final void load() throws IOException {
    Parent root = loadFxml();
    setRoot(root);
    stage = ViewManager.finalizeStage(initStage(root), root);
    ViewManager.get().register(this);
  }

  /**
   * Returns the window initialized by {@link #initStage(Parent)}.
   *
   * @return the {@code Stage} represented by this view
   * @throws IllegalStateException if the method is called before the stage has been initialized
   */
  @Override
  protected Stage getWindow() {
    assertStageLoaded();
    return stage;
  }

  /**
   * Shows the stage of this view.
   *
   * @throws IllegalStateException if the method is called before the stage has been initialized
   * @see #hide()
   */
  public final void show() {
    assertStageLoaded();
    stage.show();
  }

  /**
   * Hides the stage of this view.
   *
   * @throws IllegalStateException if the method is called before the stage has been initialized
   * @see #show()
   */
  public final void hide() {
    assertStageLoaded();
    stage.hide();
  }

  private void assertStageLoaded() {
    if (stage == null) {
      throw new IllegalStateException(
          "Stage has not been loaded yet (Did you call load() in your view constructor?)");
    }
  }
}
