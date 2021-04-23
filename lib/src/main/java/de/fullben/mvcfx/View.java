package de.fullben.mvcfx;

import static java.util.Objects.requireNonNull;

import java.text.MessageFormat;
import java.util.ResourceBundle;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Window;

/**
 * Base class for all view classes which manage JavaFX user interface components.
 *
 * <p>In order to build the user interface represented by an instance, any implementing views must
 * override the {@link #initRoot()} method. This method is called automatically by the constructor
 * of this class.
 *
 * <pre>
 *   public class MyView extends View&lt;MyModel, MyController&gt; {
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
 *     ...
 * </pre>
 *
 * @see StageView
 * @see FxmlView
 * @see FxmlStageView
 * @param <ModelType> the type of the model of this view
 * @param <ControllerType> the type of the controller of this view
 * @author Benedikt Full
 */
public abstract class View<ModelType, ControllerType extends Controller> {

  private final ModelType model;
  private final ControllerType controller;
  private final ResourceBundle resources;
  private Parent root;

  /**
   * Creates a new view with the given resources and calls {@link #initRoot()} to initialize the
   * JavaFX user interface components represented by this {@code View} instance.
   *
   * @param model the model associated with this view
   * @param controller the controller of this view
   * @param resources the resource bundle to be utilized by this view
   */
  public View(ModelType model, ControllerType controller, ResourceBundle resources) {
    this(model, controller, resources, true);
  }

  /**
   * Constructor for internal usage. Can be used to create a {@code View} that has an uninitialized
   * root.
   *
   * <p>If the constructor is called with {@code false} for {@code initRoot}, it becomes the
   * caller's responsibility to ensure that the root element of the view is initialized and that the
   * view is registered with the {@link ViewManager}.
   *
   * @param model the model associated with this view
   * @param controller the controller of this view
   * @param resources the resource bundle to be utilized by this view
   * @param initRoot {@code true} if the user interface elements represented by this instance should
   *     be initialized by calling {@link #initRoot()}, {@code false} if the root should remain
   *     uninitialized
   */
  View(ModelType model, ControllerType controller, ResourceBundle resources, boolean initRoot) {
    this.model = requireNonNull(model);
    this.controller = requireNonNull(controller);
    this.resources = requireNonNull(resources);
    if (initRoot) {
      root = requireNonNull(initRoot());
      ViewManager.get().register(this);
    } else {
      root = null;
    }
  }

  /**
   * Implementations must initialize the user interface elements which this view represents. This
   * method is called automatically by the constructor of this class.
   *
   * <p>The following snippet shows an example implementation of this method which creates a simple
   * user interface that contains a button.
   *
   * <pre>
   *   &#064;Override
   *   protected Parent initRoot() {
   *     StackPane root = new StackPane();
   *     Button btn = new Button(getString("btn.text"));
   *     root.getChildren().add(btn);
   *     return root;
   *   }
   * </pre>
   *
   * @return the root element of the user interface elements represented by this view, must not be
   *     {@code null}
   */
  protected abstract Parent initRoot();

  /**
   * Can be used to set the user interface components to be represented by this view instance. Note
   * that this method can only be used if the instance's root node has not been assigned yet.
   *
   * @param root the root element of the user interface graph to be represented by this view
   * @throws NullPointerException if the given {@code root} is {@code null}
   * @throws IllegalStateException if the root node of this view already has a non-{@code null}
   *     value
   */
  final void setRoot(Parent root) {
    requireNonNull(root);
    if (this.root != null) {
      throw new IllegalStateException("Root node can only be set once");
    }
    this.root = root;
  }

  /**
   * Returns the root node of the view.
   *
   * @return the root element of the view
   */
  protected Parent getRoot() {
    return root;
  }

  /**
   * Returns the scene of the root node (initialized by {@link #initRoot()} of the view.
   *
   * @return the scene hosting the root node
   */
  protected Scene getScene() {
    return root.getScene();
  }

  /**
   * Returns the window of the root node (initialized by {@link #initRoot()} of the view.
   *
   * @return the window hosting the root node
   */
  protected Window getWindow() {
    return root.getScene().getWindow();
  }

  /**
   * Returns the model instance associated with the view. This method is meant for view-internal use
   * only.
   *
   * @return the model of the view, never {@code null}
   */
  protected final ModelType getModel() {
    return model;
  }

  /**
   * Returns the controller instance associated with the view. This method is meant for
   * view-internal use only.
   *
   * @return the controller of the view, never {@code null}
   */
  protected final ControllerType getController() {
    return controller;
  }

  /**
   * Returns the resource bundle associated with the view. This method is meant for view-internal
   * use only.
   *
   * @return the resource bundle of the view, never {@code null}
   */
  protected final ResourceBundle getResources() {
    return resources;
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
  protected final String getString(String key) {
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
  protected final String getString(String key, Object... arguments) {
    if (arguments == null || arguments.length == 0) {
      return getString(key);
    }
    return MessageFormat.format(getString(key), arguments);
  }
}
