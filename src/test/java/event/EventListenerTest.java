package event;

import org.junit.Assert;
import org.junit.Test;

public class EventListenerTest {

    @Test
    public void createMultipleListener() {
        final EventListener<SimpleEvent> listener = new EventListener<>(SimpleEvent.class, event -> {
        });
        Assert.assertEquals(SimpleEvent.class, listener.getListenedEvent());
        Assert.assertFalse(listener.isOnce());
    }

    @Test
    public void createOnceListener() {
        final EventListener<SimpleEvent> listener = new EventListener<>(SimpleEvent.class, event -> {
        }, true);
        Assert.assertEquals(SimpleEvent.class, listener.getListenedEvent());
        Assert.assertTrue(listener.isOnce());
    }

    @Test
    public void consumeEvent() {
        final EventTracer tracer = new EventTracer(false);
        final EventListener<SimpleEvent> listener = new EventListener<>(SimpleEvent.class, event -> {
            tracer.setTriggered(true);
        });
        listener.consume(new SimpleEvent());
        Assert.assertEquals(SimpleEvent.class, listener.getListenedEvent());
        Assert.assertTrue(tracer.isTriggered());
    }


    private static class SimpleEvent implements Event {
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
