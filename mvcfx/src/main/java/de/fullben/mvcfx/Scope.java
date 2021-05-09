package de.fullben.mvcfx;

import java.util.function.BiConsumer;

/**
 * A {@code Scope} is a data context shared between different types of models.
 *
 * <p>Scopes can be used to communicate data or that an event has happened between model instances
 * in a decoupled manner via a publish-subscribe mechanism.
 *
 * <pre>
 *   public class DetailsScope implements Scope {
 *     public static final String UPDATE = "DetailsScope.update";
 *     ...
 *   }
 *
 *   ...
 *   // In a model class, perform some actions which necessitate
 *   // reactions by other components
 *   // Inform them by publishing the appropriate message
 *   detailsScope.publish(DetailsScope.UPDATE);
 *   ...
 * </pre>
 *
 * <p>Note that this is very much a preview feature based on the <a
 * href="https://github.com/sialcasa/mvvmFX/wiki/Scopes">mvvmFX scopes</a> and may be altered or
 * removed in the future.
 *
 * @author Benedikt Full
 */
public interface Scope {

  /**
   * Publishes the given message and payload to all subscribers.
   *
   * @param messageName the message identifier
   * @param payload the payload, may be empty
   */
  default void publish(String messageName, Object... payload) {
    ScopeManager.getInstance().publish(this, messageName, payload);
  }

  /**
   * Subscribes this scope to the message identified by the given name. This will cause the given
   * {@code observer} code to be executed everytime the message is published.
   *
   * @param messageName the message identifier
   * @param observer the code to be executed whenever the message is published
   * @return {@code true} if a new subscription was added, {@code false} in any other case
   */
  default boolean subscribe(String messageName, BiConsumer<String, Object[]> observer) {
    return ScopeManager.getInstance().subscribe(this, messageName, observer);
  }

  /**
   * Unsubscribes the given {@code observer}.
   *
   * @param messageName the message identifier
   * @param observer the observer to be unsubscribed
   * @return {@code true} if the given {@code observer} could be unsubscribed, {@code false} in any
   *     other case
   */
  default boolean unsubscribe(String messageName, BiConsumer<String, Object[]> observer) {
    return ScopeManager.getInstance().unsubscribe(this, messageName, observer);
  }

  /**
   * Unsubscribes the given {@code observer} from all messages of this scope.
   *
   * @param observer the observer to be unsubscribed
   * @return {@code true} if the given {@code observer} could be unsubscribed, {@code false} in any
   *     other case
   */
  default boolean unsubscribe(BiConsumer<String, Object[]> observer) {
    return ScopeManager.getInstance().unsubscribe(this, observer);
  }
}
