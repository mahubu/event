package event;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

public class EventManagerTest {

    private static EventManager manager;

    @BeforeClass
    public static void setUp() {
        manager = EventManager.getInstance();
        manager.startup();
    }

    @Test(timeout = 200)
    public void offerToExistingListener() throws InterruptedException {
        final EventTracer tracer = new EventTracer(false);
        final EventListener<SimpleEvent> listener = new EventListener<>(SimpleEvent.class, event -> {
            tracer.setTriggered(true);
        });
        manager.addListener(listener);
        manager.offer(new SimpleEvent());
        Thread.sleep(100);
        Assert.assertTrue(tracer.isTriggered());
    }

    @Test(timeout = 200)
    public void offerToRemovedListener() throws InterruptedException {
        final EventTracer tracer = new EventTracer(false);
        final EventListener<SimpleEvent> listener = new EventListener<>(SimpleEvent.class, event -> {
            tracer.setTriggered(true);
        });
        manager.addListener(listener);
        manager.removeListener(listener);
        manager.offer(new SimpleEvent());
        Thread.sleep(100);
        Assert.assertFalse(tracer.isTriggered());
    }

    @Test
    public void triggerToExistingListener() {
        final EventTracer tracer = new EventTracer(false);
        final EventListener<SimpleEvent> listener = new EventListener<>(SimpleEvent.class, event -> {
            tracer.setTriggered(true);
        });
        manager.addListener(listener);
        manager.trigger(new SimpleEvent());
        Assert.assertTrue(tracer.isTriggered());
    }

    @Test
    public void triggerToRemovedListener() {
        final EventTracer tracer = new EventTracer(false);
        final EventListener<SimpleEvent> listener = new EventListener<>(SimpleEvent.class, event -> {
            tracer.setTriggered(true);
        });
        manager.addListener(listener);
        manager.removeListener(listener);
        manager.trigger(new SimpleEvent());
        Assert.assertFalse(tracer.isTriggered());
    }

    @AfterClass
    public static void tearDown() {
        manager.shutdown();
    }

    public static class SimpleEvent implements Event {
    }

    private static class EventTracer {

        public boolean triggered;

        public EventTracer(boolean triggered) {
            this.triggered = triggered;
        }

        public boolean isTriggered() {
            return triggered;
        }

        public void setTriggered(boolean triggered) {
            this.triggered = triggered;
        }
    }

}
