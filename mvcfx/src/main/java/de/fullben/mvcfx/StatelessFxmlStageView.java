package de.fullben.mvcfx;

import java.util.ResourceBundle;

/**
 * Base class for all stateless views which manage a JavaFX user interface component loaded from an
 * {@code fxml} file that is meant to be displayed in its own window.
 *
 * <p>In order to load the user interface and inject it into the view class, any implementing views
 * must call the {@link #load()} method. This method should always be called as the last operation
 * the constructor of any view.
 *
 * <pre>
 * public class MyView extends StatelessFxmlStageView&lt;MyController&gt; {
 *     ...
 *     public MyView(MyController c, ResourceBundle r) throws IOException {
 *       super(c, r);
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
 * <p>Note that the view expects to find the {@code fxml} file in the views resource directory (see
 * {@link FxmlView}) or in the same package as the view class. The name of the file must be equal to
 * the simple class name of the view class.
 *
 * @see StatelessFxmlView
 * @see StatelessView
 * @see StatelessStageView
 * @param <ControllerType> the type of the controller of this view
 * @author Benedikt Full
 */
public abstract class StatelessFxmlStageView<ControllerType extends Controller>
    extends FxmlStageView<Void, ControllerType> {

  /**
   * Creates a new stateless stage view with the given resources. Callers of this constructor must
   * also call {@link #load()}.
   *
   * @param controller the controller of this view
   * @param resources the resource bundle to be utilized by this view
   */
  public StatelessFxmlStageView(ControllerType controller, ResourceBundle resources) {
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
