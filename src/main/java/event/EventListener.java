package event;

import java.util.function.Consumer;

/**
 * An event listener.
 *
 * @param <T> the event type to listen to.
 */
public class EventListener<T extends Event> {

    private final Class<T> listenedEvent;
    private final Consumer<T> eventConsumer;
    private final boolean once;

    /**
     * Constructor.
     *
     * @param listenedEvent the event to be listen to.
     * @param eventConsumer the event consumer.
     * @param once          whether or not the listener must listen to only once event.
     */
    public EventListener(final Class<T> listenedEvent, final Consumer<T> eventConsumer, final boolean once) {
        this.listenedEvent = listenedEvent;
        this.eventConsumer = eventConsumer;
        this.once = once;
    }

    /**
     * Constructor.
     *
     * @param listenedEvent the event to be listen to.
     * @param eventConsumer the event consumer.
     */
    public EventListener(final Class<T> listenedEvent, final Consumer<T> eventConsumer) {
        this(listenedEvent, eventConsumer, false);
    }

    /**
     * @return the event to be listen to.
     */
    public Class<T> getListenedEvent() {
        return listenedEvent;
    }

    /**
     * @return whether or not the listener must listen to only once event.
     */
    public boolean isOnce() {
        return once;
    }

    /**
     * Consume an event
     *
     * @param event the event.
     */
    public void consume(final T event) {
        eventConsumer.accept(event);
    }

}
