package de.fullben.mvcfx;

import static java.util.Objects.requireNonNull;

import de.fullben.mvcfx.theme.PlatformDefaultTheme;
import de.fullben.mvcfx.theme.Theme;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.function.Consumer;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;

/**
 * Each application has a {@code ViewManager} which keeps track of user interface components. The
 * manager instance can be accessed by calling the {@link #get()} method. It can be used to make
 * application-wide changes, such as setting a theme.
 *
 * <p>Note that only view components initialized via framework utilities (so either by using {@link
 * View} and any of its extensions or {@link Dialogs}) will be registered with the {@code
 * ViewManager}.
 *
 * @author Benedikt Full
 */
public final class ViewManager {

  private static final ViewManager VIEW_MANAGER = new ViewManager();
  private final List<WeakReference<View<?, ?>>> viewRegistry;
  private final List<WeakReference<Alert>> alertRegistry;
  private Theme theme;

  private ViewManager() {
    viewRegistry = Collections.synchronizedList(new ArrayList<>());
    alertRegistry = Collections.synchronizedList(new ArrayList<>());
    new Timer(true)
        .scheduleAtFixedRate(
            new TimerTask() {
              @Override
              public void run() {
                cleanUpRegistries();
              }
            },
            30000,
            30000);
    theme = new PlatformDefaultTheme();
  }

  /**
   * Returns the singleton view manager instance.
   *
   * @return the instance, which will never be {@code null}
   */
  public static ViewManager get() {
    return VIEW_MANAGER;
  }

  /**
   * Applies the given theme to the views registered with this manager.
   *
   * @param theme the new theme, must not be {@code null}
   */
  public void setTheme(Theme theme) {
    requireNonNull(theme, "Theme must not be null");
    if (this.theme.equals(theme)) {
      // If given theme equals current theme, do nothing
      return;
    }
    this.theme = theme;
    forEachRegisteredView(this::applyTheme);
    forEachRegisteredAlert(this::applyTheme);
  }

  void register(View<?, ?> view) {
    synchronized (viewRegistry) {
      for (WeakReference<View<?, ?>> viewReference : viewRegistry) {
        if (view.equals(viewReference.get())) {
          return;
        }
      }
      viewRegistry.add(new WeakReference<>(view));
    }
    applyTheme(view);
  }

  void register(Alert alert) {
    synchronized (alertRegistry) {
      for (WeakReference<Alert> alertReference : alertRegistry) {
        if (alert.equals(alertReference.get())) {
          return;
        }
      }
      alertRegistry.add(new WeakReference<>(alert));
    }
    applyTheme(alert);
  }

  private void forEachRegisteredView(Consumer<View<?, ?>> viewConsumer) {
    synchronized (viewRegistry) {
      for (WeakReference<View<?, ?>> viewReference : viewRegistry) {
        View<?, ?> view = viewReference.get();
        if (view == null) {
          continue;
        }
        viewConsumer.accept(view);
      }
    }
  }

  private void forEachRegisteredAlert(Consumer<Alert> alertConsumer) {
    synchronized (alertRegistry) {
      for (WeakReference<Alert> alertReference : alertRegistry) {
        Alert alert = alertReference.get();
        if (alert == null) {
          continue;
        }
        alertConsumer.accept(alert);
      }
    }
  }

  private void applyTheme(View<?, ?> view, Theme theme) {
    if (view instanceof StageView) {
      theme.applyTo(view.getScene());
    } else {
      theme.applyTo(view.getRoot());
    }
  }

  private void applyTheme(View<?, ?> view) {
    applyTheme(view, theme);
  }

  private void applyTheme(Alert alert, Theme theme) {
    theme.applyTo(alert);
  }

  private void applyTheme(Alert alert) {
    applyTheme(alert, theme);
  }

  private void cleanUpRegistries() {
    synchronized (viewRegistry) {
      viewRegistry.removeIf(viewReference -> viewReference.get() == null);
    }
    synchronized (alertRegistry) {
      alertRegistry.removeIf(alertReference -> alertReference.get() == null);
    }
  }

  static Stage primeStage(Stage stage, Parent root) {
    requireNonNull(stage);
    requireNonNull(root);
    if (!stage.getScene().getRoot().equals(root)) {
      throw new IllegalStateException("The stage does not host the given root");
    }
    stage.initModality(Modality.WINDOW_MODAL);
    stage.initOwner(findSuitableOwner());
    stage.hide();
    return stage;
  }

  private static Window findSuitableOwner() {
    List<Window> visibleWindows = new ArrayList<>();
    for (Window window : Stage.getWindows()) {
      if (window.isShowing()) {
        visibleWindows.add(window);
      }
      if (window.isFocused()) {
        // Should be top-most window, which should usually be the best owner candidate
        return window;
      }
    }
    // If no window is focused, return the newest (?) visible window
    return visibleWindows.isEmpty() ? null : visibleWindows.get(visibleWindows.size() - 1);
  }
}
