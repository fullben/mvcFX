package de.fullben.mvcfx;

import java.util.ResourceBundle;

/**
 * Base class for views that do not have any mutable state (usually encapsulated in a model), and
 * are thus <i>stateless</i>.
 *
 * <p>In order to build the user interface represented by an instance, any implementing views must
 * override the {@link #initRoot()} method. This method is called automatically by the constructor
 * of this class.
 *
 * <pre>
 *   public class MyView extends StatelessView&lt;MyController&gt; {
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
 *     ...
 * </pre>
 *
 * @see StatelessStageView
 * @see StatelessFxmlView
 * @see StatelessFxmlStageView
 * @param <ControllerType> the type of the controller of this view
 * @author Benedikt Full
 */
public abstract class StatelessView<ControllerType extends Controller>
    extends View<Void, ControllerType> {

  /**
   * Creates a new stateless view with the given resources and calls {@link #initRoot()} to
   * initialize the JavaFX user interface components represented by this {@code View} instance.
   *
   * @param controller the controller of this view
   * @param resources the resource bundle to be utilized by this view
   */
  public StatelessView(ControllerType controller, ResourceBundle resources) {
    super(null, false, controller, resources, true);
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
