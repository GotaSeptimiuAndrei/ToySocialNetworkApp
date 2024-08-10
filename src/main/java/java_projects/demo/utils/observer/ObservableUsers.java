package java_projects.demo.utils.observer;

import java_projects.demo.utils.events.EventType;
import java_projects.demo.utils.events.MessageEvent;
import java_projects.demo.utils.events.UsersEvent;

public class ObservableUsers<E extends UsersEvent<ID>, ID> extends ObservableId<E, ID> {
    private void notifySender(E event) {
        observers.forEach((id, observer) -> {
            if (event.getInvolved().contains(id) && ((MessageEvent) event).getNewMessage().getSender().equals(id)) {
                observer.updateMessages(event);
                System.out.println(id + " notified " + event.getTypeOfEvent());
            }
        });
    }

    private void notifyReceiver(E event) {
        observers.forEach((id, observer) -> {
            if (event.getInvolved().contains(id) && ((MessageEvent) event).getNewMessage().getReceiver().equals(id)) {
                observer.updateMessages(event);
                System.out.println(id + " notified " + event.getTypeOfEvent());
            }
        });
    }

    public void notifyObserversMessage(E event) {
        if (event.getTypeOfEvent() == EventType.ADD) {
            notifySender(event);
            notifyReceiver(event);
            return;
        }
        observers.forEach((id, observer) -> {
            if (event.getInvolved().contains(id)) {
                observer.updateMessages(event);
                System.out.println(id + " notified " + event.getTypeOfEvent());
            }
        });
    }

    public void notifyObserversFriendship(E event) {
        observers.forEach((id, observer) -> {
            if (event.getInvolved().contains(id)) {
                observer.updateFriendships(event);
                System.out.println(id + " notified" + event.getTypeOfEvent());
            }
        });
    }
}
