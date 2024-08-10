package java_projects.demo.utils.events;

import java_projects.demo.domain.Message;

import java.util.Set;

public class MessageEvent implements UsersEvent<String> {
    private final EventType typeOfEvent;
    private final Message oldMessage;
    private final Message newMessage;

    public MessageEvent(EventType typeOfEvent, Message oldMessage, Message newMessage) {
        this.typeOfEvent = typeOfEvent;
        this.oldMessage = oldMessage;
        this.newMessage = newMessage;
    }

    @Override
    public EventType getTypeOfEvent() {
        return typeOfEvent;
    }

    public Message getOldMessage() {
        return oldMessage;
    }

    public Message getNewMessage() {
        return newMessage;
    }

    @Override
    public Set<String> getInvolved() {
        return Set.of();
    }
}
