package java_projects.demo.utils.observer;

import java_projects.demo.utils.events.UsersEvent;

public interface ObserverId<E extends UsersEvent<ID>, ID> {
    void update(E event);

    ID getUsername();

    void updateMessages(E event);

    void updateFriendships(E event);
}
