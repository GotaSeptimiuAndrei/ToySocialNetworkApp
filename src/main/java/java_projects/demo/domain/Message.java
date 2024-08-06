package java_projects.demo.domain;

import java.time.LocalDateTime;
import java.util.Objects;

public class Message extends Entity<Long> {
    private String sender;
    private String receiver;
    private String messageContent;
    private LocalDateTime messageTime;
    private boolean received;
    private boolean seen;


    /**
     * Method for constructing a message
     * @param idMessage - Long - the id of the message
     * @param sender - Long - the user who send the message
     * @param receiver - Long - the user who is intended to receive the message
     * @param messageContent - String - the message
     */
    public Message(Long idMessage, String sender, String receiver, String messageContent) {
        super(idMessage);
        this.sender = sender;
        this.receiver = receiver;
        this.messageContent = messageContent;
        this.messageTime = LocalDateTime.now();
        this.seen = false;
        this.received = false;
    }

    /**
     * Method for constructing a message
     * @param idMessage - Long - the id of the message
     * @param sender - Long - the user who send the message
     * @param receiver - Long - the user who is intended to receive the message
     * @param messageContent - String - the message
     */
    public Message(Long idMessage, String sender, String receiver, String messageContent, Boolean received, Boolean seen,
                   LocalDateTime messageTime) {
        super(idMessage);
        this.sender = sender;
        this.receiver = receiver;
        this.messageContent = messageContent;
        this.received = received;
        this.seen = seen;
        this.messageTime = messageTime;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getMessageContent() {
        return messageContent;
    }

    public void setMessageContent(String messageContent) {
        this.messageContent = messageContent;
    }

    public LocalDateTime getMessageTime() {
        return messageTime;
    }

    public void setMessageTime(LocalDateTime messageTime) {
        this.messageTime = messageTime;
    }

    public boolean isReceived() {
        return received;
    }

    public void setReceived(boolean received) {
        this.received = received;
    }

    public boolean isSeen() {
        return seen;
    }

    public void setSeen(boolean seen) {
        this.seen = seen;
    }

    public boolean equals(Message other){
        if(other == null) return false;
        return (
                Objects.equals(super.getId(), other.getId()) &&
                        Objects.equals(this.sender, other.sender) &&
                        Objects.equals(this.receiver, other.receiver) &&
                        Objects.equals(this.messageContent, other.messageContent) &&
                        Objects.equals(this.messageTime, other.messageTime) &&
                        Objects.equals(this.received, other.received) &&
                        Objects.equals(this.seen, other.seen)
        );
    }

    public String getTheUsernameOfTheOtherUser(String username){
        if(this.sender.equals(username))
            return this.receiver;
        return this.sender;
    }

}
