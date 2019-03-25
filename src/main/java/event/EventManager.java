package event;

import java.util.Queue;
import java.util.concurrent.*;

/**
 * Event manager : consume in a queue or immediately events, given event listeners.
 */
public class EventManager {

    private final Queue<Event> events = new ConcurrentLinkedQueue();
    private boolean running;
    // TODO find better than BlockingQueue for concurrency ?
    private ConcurrentMap<Class<?>, BlockingQueue<EventListener<Event>>> eventListeners;

    private static EventManager instance = new EventManager();

    public static EventManager getInstance() {
        return instance;
    }

    /**
     * Private constructor
     */
    private EventManager() {
        super();
    }

    /**
     * Start up the event manager
     */
    public void startup() {
        eventListeners = new ConcurrentHashMap<>();
        running = true;
        final Thread thread = new Thread(() -> {
            while (running) {
                consume(events, eventListeners);
                // TODO sleep ? wait for new event ?
            }
        });
        thread.start();
    }

    /**
     * Shutdown the event manager
     */
    public void shutdown() {
        running = false;
        consume(events, eventListeners);
    }

    /**
     * Offer an event to be consumed.
     *
     * @param event the event.
     */
    public void offer(final Event event) {
        final Class<?> clazz = event.getClass();
        if (eventListeners.containsKey(clazz)) {
            events.offer(event);
        } else {
            // TODO log
        }
    }

    /**
     * Consumed immediately an event.
     *
     * @param event the event.
     */
    public void trigger(final Event event) {
        consume(eventListeners, event);
    }

    /**
     * Add an event eventListener
     *
     * @param eventListener the eventListener.
     */
    public void addListener(final EventListener<? extends Event> eventListener) {
        final BlockingQueue<EventListener<Event>> listeners = eventListeners.computeIfAbsent(eventListener.getListenedEvent(), collection -> new LinkedBlockingQueue<>());
        listeners.add((EventListener<Event>) eventListener);
    }

    /**
     * Remove an event eventListener.
     *
     * @param eventListener the eventListener.
     */
    public void removeListener(final EventListener<? extends Event> eventListener) {
        final BlockingQueue<EventListener<Event>> listeners = eventListeners.get(eventListener.getListenedEvent());
        if (listeners != null) {
            listeners.remove(eventListener);
        } else {
            // TODO log
        }
    }

    private void consume(final Queue<Event> events, final ConcurrentMap<Class<?>, BlockingQueue<EventListener<Event>>> eventListeners) {
        Event event;
        while ((event = events.poll()) != null) {
            consume(eventListeners, event);
        }
    }

    private void consume(final ConcurrentMap<Class<?>, BlockingQueue<EventListener<Event>>> eventListeners, final Event event) {
        final Class<?> clazz = event.getClass();
        if (eventListeners.containsKey(clazz)) {
            final BlockingQueue<EventListener<Event>> listeners = eventListeners.get(clazz);
            for (final EventListener<Event> listener : listeners) {
                listener.consume(event);
                if (listener.isOnce()) {
                    listeners.remove(listener);
                }
            }
        } else {
            // TODO log
        }
    }
}
