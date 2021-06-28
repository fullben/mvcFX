package de.fullben.mvcfx;

import java.io.IOException;

/**
 * Base interface for all controllers to be used in conjunction with a {@link View}. A typical
 * implementation of a controller should adhere to the following structure.
 *
 * <pre>
 *   public class MyController implements Controller {
 *
 *     private final MyModel model;
 *     private MyView view;
 *
 *     public MyController(MyModel model) {
 *       this.model = model;
 *       view = null;
 *     }
 *
 *     &#064;Override
 *     public void startUp() {
 *       ...
 *     }
 *   }
 * </pre>
 *
 * @author Benedikt Full
 */
public interface Controller {

  /**
   * Implementations of this method should finalize the setup of the controller.
   *
   * <p>The primary task of this method should be the initialization of the view, which may include
   * the loading of the node hierarchy from a {@code fxml} file. If the view is hosted in a
   * dedicated window, this method should ensure that the window becomes visible.
   *
   * <p>As controllers in the wider sense are not meant to be reusable or recyclable, it is
   * important that implementations ensure that only the first call to them has any effects. This
   * can for example be done by implementing the method based on the following pattern:
   *
   * <pre>
   *   ...
   *   private MyView view;
   *   ...
   *   &#064;Override
   *   public void startUp() throws IOException {
   *     if (view != null) {
   *       return;
   *     }
   *     // Initialize view here
   *     ...
   *   }
   * </pre>
   *
   * @throws IOException if an error is encountered while loading the view from file
   */
  void startUp() throws IOException;
}
