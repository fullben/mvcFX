package de.fullben.mvcfx;

import java.util.ResourceBundle;
import javafx.scene.Parent;

/**
 * Base class for all stateless views which manage JavaFX user interface components that are meant
 * to be displayed in their own window.
 *
 * <p>In order to build the user interface represented by an instance, any implementing views must
 * override the {@link #initRoot()} and {@link #initStage(Parent)} methods. These methods are called
 * automatically by the constructor of this class. The {@code Parent} returned by the {@code
 * initRoot()} method will be provided as parameter to {@code initStage(Parent root)}.
 *
 * <pre>
 * public class MyView extends StatelessStageView&lt;MyController&gt; {
 *     ...
 *     public MyView(MyController c, ResourceBundle r) {
 *       super(c, r);
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
 * @see StatelessView
 * @see StatelessFxmlView
 * @see StatelessFxmlStageView
 * @param <ControllerType> the type of the controller of this view
 * @author Benedikt Full
 */
public abstract class StatelessStageView<ControllerType extends Controller>
    extends StageView<Void, ControllerType> {

  /**
   * Creates a new stateless view with the given resources and calls {@link
   * StageView#initStage(Parent)} to initialize the window represented by this {@code StageView}
   * instance.
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
   * @param controller the controller of this view
   * @param resources the resource bundle to be utilized by this view
   */
  public StatelessStageView(ControllerType controller, ResourceBundle resources) {
    super(null, false, controller, resources);
  }

  /**
   * Throws an {@link UnsupportedOperationException}.
   *
   * @return throws an exception
   */
  @Override
  protected final Void getModel() {
    throw new UnsupportedOperationException();
  }
}
