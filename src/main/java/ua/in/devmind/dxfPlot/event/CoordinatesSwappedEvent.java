package ua.in.devmind.dxfPlot.event;

import javafx.beans.NamedArg;
import javafx.event.Event;
import javafx.event.EventTarget;
import javafx.event.EventType;

public class CoordinatesSwappedEvent extends Event {

    public static final EventType<CoordinatesSwappedEvent> COORDINATEST_SWAPPED;
    public static final EventType<CoordinatesSwappedEvent> ANY;
    private final boolean coordinatesSwapped;

    public CoordinatesSwappedEvent(@NamedArg("coordinatesSwapped") boolean coordinatesSwapped) {
        super(COORDINATEST_SWAPPED);
        this.coordinatesSwapped = coordinatesSwapped;
    }

    public CoordinatesSwappedEvent(Object source, EventTarget target, @NamedArg("coordinatesSwapped") boolean coordinatesSwapped) {
        super(source, target, COORDINATEST_SWAPPED);
        this.coordinatesSwapped = coordinatesSwapped;
    }

    public CoordinatesSwappedEvent copyFor(Object var1, EventTarget var2) {
        return (CoordinatesSwappedEvent)super.copyFor(var1, var2);
    }

    public final boolean isCoordinatesSwapped() {
        return coordinatesSwapped;
    }

    static {
        COORDINATEST_SWAPPED = new EventType(Event.ANY, "COORDINATES_SWAPPED");
        ANY = COORDINATEST_SWAPPED;
    }
}
