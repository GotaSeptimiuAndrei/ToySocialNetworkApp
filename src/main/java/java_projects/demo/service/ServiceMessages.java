package java_projects.demo.service;

import java_projects.demo.domain.Message;
import java_projects.demo.repository.MessagesRepository;
import java_projects.demo.utils.events.EventType;
import java_projects.demo.utils.events.MessageEvent;
import java_projects.demo.utils.events.UsersEvent;
import java_projects.demo.utils.observer.ObservableUsers;
import java_projects.demo.validators.MessageValidator;

import java.util.ArrayList;
import java.util.stream.Collectors;

public class ServiceMessages extends ObservableUsers<UsersEvent<String>, String> {
    private final MessagesRepository messagesRepository;

    /**
     * Constructor for ServiceMessages
     *
     * @param messageRepository - the repository for messages
     */
    public ServiceMessages(MessagesRepository messageRepository) {
        this.messagesRepository = messageRepository;
    }

    /**
     * Returns all messages from repository
     *
     * @return ArrayList <Message>
     */
    public ArrayList<Message> getAllMessages() {
        return (ArrayList<Message>) messagesRepository.findAll();
    }

    /**
     * Returns the message with chosen id
     *
     * @param idMessage - Long
     * @return Message
     */
    public Message getById(Long idMessage) {
        return messagesRepository.findById(idMessage);
    }

    /**
     * Add a new message in the repository
     *
     * @param senderUsername   - String - the username of the person who sent the message
     * @param receiverUsername - String - the username of the person expected to receive the message
     * @param messageContent   - String - the content of the message
     * @throws Exception - if the message content violates the security rules
     */
    public void addNewMessage(String senderUsername, String receiverUsername, String messageContent) throws Exception {
        MessageValidator.validate(senderUsername, receiverUsername, messageContent);
        Message message = new Message(this.messagesRepository.generateAvailableId(),
                senderUsername, receiverUsername, messageContent);
        messagesRepository.add(message);
        super.notifyObserversMessage(new MessageEvent(EventType.ADD, null, message));
    }

    public static ArrayList<Message> getArraySortedDESCByDateTime(ArrayList<Message> messages) {
        return (ArrayList<Message>) messages.stream().sorted((message1, message2) -> {
            if (message1.getMessageTime().isAfter(message2.getMessageTime()))
                return -1;
            if (message1.getMessageTime().isBefore(message2.getMessageTime()))
                return 1;
            return 0;
        }).collect(Collectors.toList());
    }

    /**
     * Returns a list with all messages from user A to user B sorted chronological
     *
     * @param username1 - String
     * @param username2 - String
     * @return - ArrayList <Message>
     */
    public ArrayList<Message> getAllMessagesBetween2Users(String username1, String username2) {
        return (ArrayList<Message>) this.messagesRepository.getMessagesBetween2User(username1, username2);
    }

    /**
     * Every message that has sender = message.Sender and receiver = message.Receiver and sending time before
     * message.sendingTime, will be set as received
     *
     * @param message - Message
     */
    public void setMessagesReceived(Message message) {
        this.messagesRepository.setMessagesReceived(message);
        message.setReceived(true);
        super.notifyObserversMessage(new MessageEvent(EventType.UPDATE, null, message));
    }

    /**
     * Sets all messages from sender to receiver as seen
     *
     * @param message - Message
     */
    public void setMessageSeen(Message message) {
        this.messagesRepository.setMessageSeen(message.getId());
        message.setSeen(true);
        message.setReceived(true);
        super.notifyObserversMessage(new MessageEvent(EventType.UPDATE, null, message));
    }

    /**
     * Returns the last message from every conversation descending by the moment of sending
     *
     * @param username - String
     * @return - Iterable <Message>
     */
    public Iterable<Message> getLastMessageFromEveryConversation(String username) {
        return getArraySortedDESCByDateTime((ArrayList<Message>) this.messagesRepository.getLastMessageFromEveryConversationOfUser(username));
    }
}
