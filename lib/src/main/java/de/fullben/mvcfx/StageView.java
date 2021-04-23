package de.fullben.mvcfx;

import java.util.ResourceBundle;
import javafx.scene.Parent;
import javafx.stage.Stage;

/**
 * Base class for all views which manage JavaFX user interface components that is meant to be
 * displayed in their own window.
 *
 * <p>In order to build the user interface represented by an instance, any implementing views must
 * override the {@link #initRoot()} and {@link #initStage(Parent)} methods. These methods are called
 * automatically by the constructor of this class. The {@code Parent} returned by the {@code
 * initRoot()} method will be provided as parameter to {@code initStage(Parent root)}.
 *
 * <pre>
 * public class MyView extends StageView&lt;MyModel, MyController&gt; {
 *     ...
 *     public MyView(MyModel m, MyController c, ResourceBundle r) {
 *       super(m, c, r);
 *     }
 *
 *     &#064;Override
 *     protected Parent initRoot() {
 *       StackPane root = new StackPane();
 *       Button btn = new Button(getString("btn.text"));
 *       root.getChildren().add(btn);
 *       return root;
 *     }
 *
 *     &#064;Override
 *     protected Stage initStage(Parent root) {
 *       Stage stage = new Stage();
 *       stage.setTitle(getString("stage.title"));
 *       stage.setScene(new Scene(root));
 *       return stage;
 *     }
 *     ...
 * </pre>
 *
 * This class is an extension of the {@link View} class that adds functionality for defining and
 * managing a user interface window which hosts the user interface nodes of this view.
 *
 * @see View
 * @see FxmlView
 * @see FxmlStageView
 * @param <ModelType> the type of the model of this controller
 * @param <ControllerType> the type of the controller of this view
 * @author Benedikt Full
 */
public abstract class StageView<ModelType, ControllerType extends Controller>
    extends View<ModelType, ControllerType> {

  private final Stage stage;

  /**
   * Creates a new view with the given resources and calls {@link #initStage(Parent)} to initialize
   * the window represented by this {@code StageView} instance.
   *
   * <p>Note that the constructor ensures that the window:
   *
   * <ul>
   *   <li>hosts the root element defined by {@link #initRoot()},
   *   <li>is hidden,
   *   <li>is window modal (no events delivered to other windows of the same hierarchy),
   *   <li>and is owned by one of the currently visible windows.
   * </ul>
   *
   * @param model the model associated with this view
   * @param controller the controller of this view
   * @param resources the resource bundle to be utilized by this view
   */
  public StageView(ModelType model, ControllerType controller, ResourceBundle resources) {
    super(model, controller, resources);
    stage = ViewManager.primeStage(initStage(getRoot()), getRoot());
    ViewManager.get().register(this);
  }

  /**
   * Implementations must initialize the {@code Stage} which this view represents. This method is
   * called automatically by the constructor of this class, with the return value of {@link
   * #initRoot()} as parameter.
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
   * @param root the root element of the view
   * @return the window hosting the user interface elements represented by this view, must not be
   *     {@code null}
   */
  protected abstract Stage initStage(Parent root);

  /**
   * Returns the window initialized by {@link #initStage(Parent)}.
   *
   * @return the {@code Stage} represented by this view
   */
  @Override
  protected Stage getWindow() {
    return stage;
  }

  /**
   * Shows the stage of this view.
   *
   * @see #hide()
   */
  public final void show() {
    stage.show();
  }

  /**
   * Hides the stage of this view.
   *
   * @see #show()
   */
  public final void hide() {
    stage.hide();
  }
}
