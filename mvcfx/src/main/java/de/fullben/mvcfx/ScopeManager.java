package de.fullben.mvcfx;

import java.lang.ref.WeakReference;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
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
  private final ScopeObserverRegistry registry;

  private ScopeManager() {
    registry = new ScopeObserverRegistry();
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

  void publish(Scope scope, String messageName, Object... payload) {
    final ScopeObservers observers = registry.get(scope);
    if (observers == null) {
      return;
    }
    if (isCurrentThreadFxApplicationThread()) {
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

  boolean subscribe(Scope scope, String messageName, BiConsumer<String, Object[]> observer) {
    ScopeObservers observers = registry.get(scope);
    if (observers == null) {
      observers = new ScopeObservers();
      registry.put(scope, observers);
    }
    Collection<BiConsumer<String, Object[]>> messageObservers =
        observers.computeIfAbsent(messageName, k -> new HashSet<>());
    return messageObservers.add(observer);
  }

  boolean unsubscribe(Scope scope, String messageName, BiConsumer<String, Object[]> observer) {
    final ScopeObservers scopeObservers = registry.get(scope);
    if (scopeObservers == null || scopeObservers.isEmpty()) {
      return false;
    }
    Collection<BiConsumer<String, Object[]>> messageObservers = scopeObservers.get(messageName);
    if (messageObservers == null) {
      return false;
    }
    return messageObservers.remove(observer);
  }

  boolean unsubscribe(Scope scope, BiConsumer<String, Object[]> observer) {
    final ScopeObservers scopeObservers = registry.get(scope);
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
    final Collection<BiConsumer<String, Object[]>> subscribers = observers.get(messageName);
    if (subscribers == null || subscribers.isEmpty()) {
      return;
    }
    for (BiConsumer<String, Object[]> subscriber : subscribers) {
      subscriber.accept(messageName, payload);
    }
  }

  private static boolean isCurrentThreadFxApplicationThread() {
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

  /**
   * A convenience extension of a {@code HashMap} for storing a {@link Scope}'s message observers.
   *
   * @author Benedikt Full
   */
  private static class ScopeObservers
      extends HashMap<String, Collection<BiConsumer<String, Object[]>>> {}

  /**
   * Manages a map that associates a {@link Scope} with {@link ScopeObservers ScopeObservers}.
   *
   * <p>The scopes (keys) are maintained as {@link WeakReference}s, which are periodically checked,
   * and removed if they contain {@code null}. This ensures that a scope becomes eligible for
   * garbage collection if it has become inaccessible from everywhere but the context of this
   * registry.
   *
   * @author Benedikt Full
   */
  private static class ScopeObserverRegistry {

    private final Map<WeakReference<Scope>, ScopeObservers> registry;

    private ScopeObserverRegistry() {
      this.registry = new HashMap<>();
      Timer t = new Timer(true);
      t.schedule(
          new TimerTask() {
            @Override
            public void run() {
              synchronized (registry) {
                Set<WeakReference<Scope>> emptyKeys = new HashSet<>();
                for (Entry<WeakReference<Scope>, ScopeObservers> entry : registry.entrySet()) {
                  if (entry.getKey().get() == null) {
                    emptyKeys.add(entry.getKey());
                  }
                }
                for (WeakReference<Scope> emptyKey : emptyKeys) {
                  registry.remove(emptyKey);
                }
              }
            }
          },
          30000,
          30000);
    }

    private ScopeObservers get(Scope scope) {
      synchronized (registry) {
        for (Entry<WeakReference<Scope>, ScopeObservers> entry : registry.entrySet()) {
          if (scope.equals(entry.getKey().get())) {
            return entry.getValue();
          }
        }
        return null;
      }
    }

    private void put(Scope scope, ScopeObservers observers) {
      synchronized (registry) {
        for (Entry<WeakReference<Scope>, ScopeObservers> entry : registry.entrySet()) {
          if (scope.equals(entry.getKey().get())) {
            registry.put(entry.getKey(), observers);
            return;
          }
        }
        registry.put(new WeakReference<>(scope), observers);
      }
    }
  }
}
