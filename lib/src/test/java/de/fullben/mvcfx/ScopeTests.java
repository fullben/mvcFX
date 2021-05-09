package de.fullben.mvcfx;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.function.BiConsumer;
import org.junit.jupiter.api.Test;

/**
 * Some basic smoke tests for the {@link Scope}.
 *
 * @author Benedikt Full
 */
public class ScopeTests {

  @Test
  public void publishTriggersSubscribed() {
    // Given
    TestScope scope = new TestScope();
    final Counter counter = new Counter(1);
    BiConsumer<String, Object[]> observer = (m, p) -> counter.increment();
    scope.subscribe(TestScope.MSG_1, observer);
    // When
    scope.publish(TestScope.MSG_1);
    // Then
    assertEquals(2, counter.i);
  }

  @Test
  public void publishTriggersAllSubscribers() {
    // Given
    TestScope scope = new TestScope();
    final Counter counter = new Counter(1);
    BiConsumer<String, Object[]> observer1 = (m, p) -> counter.increment();
    BiConsumer<String, Object[]> observer2 = (m, p) -> counter.incrementBy(2);
    scope.subscribe(TestScope.MSG_1, observer1);
    scope.subscribe(TestScope.MSG_1, observer2);
    // When
    scope.publish(TestScope.MSG_1);
    // Then
    assertEquals(4, counter.i);
  }

  @Test
  public void publishDoesNotTriggerUnsubscribedSubscriber() {
    // Given
    TestScope scope = new TestScope();
    final Counter counter = new Counter(1);
    BiConsumer<String, Object[]> observer = (m, p) -> counter.increment();
    scope.subscribe(TestScope.MSG_1, observer);
    // When
    scope.unsubscribe(TestScope.MSG_1, observer);
    scope.publish(TestScope.MSG_1);
    // Then
    assertEquals(1, counter.i);
  }

  @Test
  public void publishDoesNotTriggerUnspecificUnsubscribedSubscribers() {
    // Given
    TestScope scope = new TestScope();
    final Counter counter = new Counter(1);
    BiConsumer<String, Object[]> observer = (m, p) -> counter.increment();
    scope.subscribe(TestScope.MSG_1, observer);
    scope.subscribe(TestScope.MSG_2, observer);
    // When
    scope.unsubscribe(observer);
    scope.publish(TestScope.MSG_1);
    scope.publish(TestScope.MSG_2);
    // Then
    assertEquals(1, counter.i);
  }

  private static class TestScope implements Scope {
    private static final String MSG_1 = "msg_1";
    private static final String MSG_2 = "msg_2";
  }

  private static class Counter {
    int i;

    private Counter(int i) {
      this.i = i;
    }

    private void increment() {
      i++;
    }

    private void incrementBy(int i) {
      this.i += i;
    }
  }
}
