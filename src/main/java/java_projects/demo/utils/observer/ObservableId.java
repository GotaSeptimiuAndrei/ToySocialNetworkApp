package java_projects.demo.utils.observer;

import java_projects.demo.utils.events.UsersEvent;

import java.util.HashMap;
import java.util.Map;

public class ObservableId<E extends UsersEvent<ID>, ID> {
    protected Map<ID, ObserverId<E, ID>> observers = new HashMap<>();

    public void addObserver(ObserverId<E, ID> observer, ID id) {
        observers.put(id, observer);
    }

    public void removeObserver(ID id) {
        observers.remove(id);
    }

    public void notifyObservers(E event) {
        observers.forEach((id, observer) -> {
            if (event.getInvolved().contains(id)) {
                observer.update(event);
                System.out.println(id + " notified");
            }
        });
    }
}
