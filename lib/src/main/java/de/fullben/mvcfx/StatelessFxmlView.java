package de.fullben.mvcfx;

import java.util.ResourceBundle;

/**
 * Base class for all stateless views which manage a JavaFX user interface component loaded from an
 * {@code fxml} file.
 *
 * <p>In order to load the user interface and inject it into the view class, any implementing views
 * must call the {@link #load()} method. This method should always be called as the last operation
 * the constructor of any view.
 *
 * <pre>
 *   public class MyView extends FxmlView&lt;MyController&gt; {
 *     ...
 *     public MyView(MyController c, ResourceBundle r) throws IOException {
 *       super(c, r);
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
 *   public MyView(MyController c, ResourceBundle r) throws IOException {
 *     super(c, r);
 *     load(); // Injects tabs
 *     initialTabCount = tabs.getTabs().size();
 *   }
 *   ...
 * </pre>
 *
 * <p>Note that the view expects to find the {@code fxml} file in the views resource directory (see
 * {@link FxmlView}) or in the same package as the view class. The name of the file must be equal to
 * the simple class name of the view class.
 *
 * @see StatelessFxmlStageView
 * @see StatelessView
 * @see StatelessStageView
 * @param <ControllerType> the type of the controller of this view
 * @author Benedikt Full
 */
public class StatelessFxmlView<ControllerType extends Controller>
    extends FxmlView<Void, ControllerType> {

  /**
   * Creates a new stateless view with the given resources. Users of this constructor must ensure
   * that {@link #load()} is called, which will load the user interface components represented by
   * this view from the appropriate {@code fxml} file.
   *
   * @param controller the controller of this view
   * @param resources the resource bundle to be utilized by this view
   */
  public StatelessFxmlView(ControllerType controller, ResourceBundle resources) {
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
