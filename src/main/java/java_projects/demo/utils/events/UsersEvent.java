package java_projects.demo.utils.events;

import java.util.Set;

public interface UsersEvent<ID> {
    Set<ID> getInvolved();

    EventType getTypeOfEvent();
}
