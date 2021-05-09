package de.fullben.mvcfx;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.function.BiConsumer;
import javafx.application.Platform;

/**
 * Each application has a {@code ScopeManager} which represents the backbone of the
 * publish-subscribe mechanism involving the {@link Scope}s.
 *
 * <p>Note that this is very much a preview feature based on the <a
 * href="https://github.com/sialcasa/mvvmFX/wiki/Scopes">mvvmFX scopes</a> and may be altered or
 * removed in the future.
 *
 * @author Benedikt Full
 */
public final class ScopeManager {

  private static ScopeManager instance;
  private final ScopeObserverRegistry observerRegistry;

  private ScopeManager() {
    observerRegistry = new ScopeObserverRegistry();
  }

  static ScopeManager getInstance() {
    if (instance == null) {
      synchronized (ScopeManager.class) {
        if (instance == null) {
          instance = new ScopeManager();
        }
      }
    }
    return instance;
  }

  public void publish(Scope scope, String messageName, Object... payload) {
    ScopeObservers observers = observerRegistry.get(scope);
    if (observers == null) {
      return;
    }
    if (currentThreadIsFxApplicationThread()) {
      publish(messageName, payload, observers);
    } else {
      try {
        Platform.runLater(() -> publish(messageName, payload, observers));
      } catch (IllegalStateException e) {
        if (e.getMessage().equals("Toolkit not initialized")) {
          // This means we're probably in a test, so there is no FX toolkit and no need to run the
          // publishing on the JavaFX application thread
          publish(messageName, payload, observers);
        } else {
          throw e;
        }
      }
    }
  }

  public boolean subscribe(Scope scope, String messageName, BiConsumer<String, Object[]> observer) {
    ScopeObservers observers = observerRegistry.get(scope);
    if (observers == null) {
      observers = new ScopeObservers();
      observerRegistry.put(scope, observers);
    }
    Collection<BiConsumer<String, Object[]>> messageObservers =
        observers.computeIfAbsent(messageName, k -> new HashSet<>());
    return messageObservers.add(observer);
  }

  public boolean unsubscribe(
      Scope scope, String messageName, BiConsumer<String, Object[]> observer) {
    ScopeObservers scopeObservers = observerRegistry.get(scope);
    if (scopeObservers == null || scopeObservers.isEmpty()) {
      return false;
    }
    Collection<BiConsumer<String, Object[]>> messageObservers = scopeObservers.get(messageName);
    if (messageObservers == null) {
      return false;
    }
    return messageObservers.remove(observer);
  }

  public boolean unsubscribe(Scope scope, BiConsumer<String, Object[]> observer) {
    ScopeObservers scopeObservers = observerRegistry.get(scope);
    if (scopeObservers == null || scopeObservers.isEmpty()) {
      return false;
    }
    boolean unsubscribed = false;
    for (Collection<BiConsumer<String, Object[]>> messageObservers : scopeObservers.values()) {
      if (messageObservers.remove(observer)) {
        unsubscribed = true;
      }
    }
    return unsubscribed;
  }

  private static void publish(String messageName, Object[] payload, ScopeObservers observers) {
    Collection<BiConsumer<String, Object[]>> subscribers = observers.get(messageName);
    if (subscribers == null || subscribers.isEmpty()) {
      return;
    }
    for (BiConsumer<String, Object[]> subscriber : subscribers) {
      subscriber.accept(messageName, payload);
    }
  }

  private static boolean currentThreadIsFxApplicationThread() {
    try {
      return Platform.isFxApplicationThread();
    } catch (final RuntimeException e) {
      if (e.getMessage().equals("No toolkit found")) {
        // In most cases this means that we are in an environment where no JavaFX
        // application is running (probably also in a JUnit test).
        return true;
      } else {
        throw e;
      }
    }
  }

  private static class ScopeObservers
      extends HashMap<String, Collection<BiConsumer<String, Object[]>>> {}

  private static class ScopeObserverRegistry extends HashMap<Scope, ScopeObservers> {}
}
