package java_projects.demo.repository;

import java_projects.demo.domain.Message;
import java_projects.demo.utils.DataTypeConvertors;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collection;

public class MessagesRepository implements IRepository<Long, Message> {

    String url;
    String username;
    String password;

    public MessagesRepository(String url, String username, String password) {
        this.url = url;
        this.username = username;
        this.password = password;
    }

    /**
     * Get the content of the message with chosen id from database
     *
     * @param messageId - Long
     * @return - String
     */
    public String getMessageContentById(Long messageId) {
        StringBuilder message = new StringBuilder();

        String sqlQuery = "Select * from messagesContent where idMessage = ? order by partOrder";
        try (Connection connection = DriverManager.getConnection(this.url, this.username, this.password)) {
            PreparedStatement statement = connection.prepareStatement(sqlQuery);
            statement.setLong(1, messageId);

            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next())
                message.append(resultSet.getString("content"));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return message.toString();
    }


    /**
     * A method that execute a specific query on database, ignoring the result
     *
     * @param sqlQuery - String
     */
    private void executeQuery(String sqlQuery) {
        try (Connection connection = DriverManager.getConnection(this.url, this.username, this.password)) {
            PreparedStatement statement = connection.prepareStatement(sqlQuery);
            statement.executeQuery();
        } catch (Exception e) {
            if (!e.getMessage().contains("No results were returned by the query"))
                throw new RuntimeException(e);
        }
    }

    /**
     * Returns the Message created with the content of the current resultSet;
     *
     * @param resultSet - ResultSet
     * @return - Message
     * @throws SQLException - if the fields of the message are not found in db
     */
    private Message getMessageFromResultSet(ResultSet resultSet) throws SQLException {
        long idMessage = resultSet.getLong("idmessage");
        String sender = resultSet.getString("sender");
        String receiver = resultSet.getString("receiver");
        boolean received = resultSet.getBoolean("received");
        boolean seen = resultSet.getBoolean("seen");

        LocalDate messageDate = resultSet.getDate("messageDate").toLocalDate();
        LocalTime messageTime = resultSet.getTime("messageTime").toLocalTime();
        LocalDateTime messageDateTime = LocalDateTime.of(messageDate, messageTime);
        String messageContent = getMessageContentById(idMessage);

        return new Message(idMessage, sender, receiver, messageContent, received, seen, messageDateTime);

    }

    /**
     * A method that returns the message generated by the query
     *
     * @param sqlQuery - String
     * @return Message
     */
    private Message getMessageByQuery(String sqlQuery) {
        try (Connection connection = DriverManager.getConnection(this.url, this.username, this.password)) {
            PreparedStatement statement = connection.prepareStatement(sqlQuery);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next())
                return getMessageFromResultSet(resultSet);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    /**
     * A method that returns all messages generated by the query
     *
     * @param sqlQuery - String
     * @return - Collection<Message>
     */
    private Collection<Message> getMessagesByQuery(String sqlQuery) {
        Collection<Message> allMessages = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection(this.url, this.username, this.password)) {
            PreparedStatement statement = connection.prepareStatement(sqlQuery);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next())
                allMessages.add(getMessageFromResultSet(resultSet));
            return allMessages;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Returns the message with chosen id
     *
     * @param messageId - Long - must be not null
     * @return Message
     */
    @Override
    public Message findById(Long messageId) {
        String sqlQuery = "Select * from messages where idMessage = " + messageId;
        return getMessageByQuery(sqlQuery);
    }

    /**
     * Returns all messages from database
     *
     * @return - Iterable - Message
     */
    @Override
    public Collection<Message> findAll() {
        String sqlQuery = "Select * from messages";
        return getMessagesByQuery(sqlQuery);
    }

    /**
     * Add the content of the message in messagesContent database
     *
     * @param idMessage      - Long
     * @param messageContent - String
     */
    public void insertMessageContent(Long idMessage, String messageContent) {
        String sqlQuery = "Insert into messagesContent(idMessage, partOrder, content) values(?,?,?);";
        int packageNumber = 1;
        try (Connection connection = DriverManager.getConnection(this.url, this.username, this.password)) {
            PreparedStatement statement = connection.prepareStatement(sqlQuery);
            statement.setLong(1, idMessage);
            while (messageContent.length() > 256) {
                String currentPackage = messageContent.substring(0, 256);
                messageContent = messageContent.substring(256);

                statement.setLong(2, packageNumber++);
                statement.setString(3, messageContent);
                statement.executeQuery();
            }
            statement.setLong(2, packageNumber++);
            statement.setString(3, messageContent);
            statement.executeQuery();
        } catch (Exception e) {
            if (!e.getMessage().contains("No results were returned by the query"))
                throw new RuntimeException(e);
        }
    }

    /**
     * Add a new message in repository
     *
     * @param message - the message1 we add
     * @return - Message - if the message with chosen id already exists
     */
    @Override
    public Message add(Message message) {
        Message existingMessage = this.findById(message.getId());
        if (existingMessage != null)
            return existingMessage;

        String date = DataTypeConvertors.getDateStringFromDateTime(message.getMessageTime());
        String time = DataTypeConvertors.getTimeStringFromDateTime(message.getMessageTime());

        String sqlQuery = "Insert into Messages(idmessage, sender, receiver, messageTime, messageDate, seen, received) " +
                "values (" + message.getId() + ",'" + message.getSender() + "','" + message.getReceiver() +
                "','" + time + "','" + date + " ', " + message.isSeen() + "," + message.isReceived() + ");";
        try (Connection connection = DriverManager.getConnection(this.url, this.username, this.password)) {
            PreparedStatement statement = connection.prepareStatement(sqlQuery);
            statement.executeQuery();

        } catch (Exception e) {
            if (!e.getMessage().contains("No results were returned by the query"))
                throw new RuntimeException(e);
            else
                insertMessageContent(message.getId(), message.getMessageContent());
        }
        return null;
    }

    /**
     * Removes the message with chosen id from the database
     *
     * @param idMessage - the id we want to delete
     */
    @Override
    public void remove(Long idMessage) {
        Message message = this.findById(idMessage);
        String sqlQuery = "DELETE FROM MESSAGES WHERE idMessage = " + idMessage;
        try (Connection connection = DriverManager.getConnection(this.url, this.username, this.password)) {
            PreparedStatement statement = connection.prepareStatement(sqlQuery);
            statement.executeQuery();
        } catch (Exception e) {
            if (!e.getMessage().contains("No results were returned by the query"))
                throw new RuntimeException(e);
        }
    }

    /**
     * Updates the message with chosen id
     *
     * @param idMessage the id of the entity
     * @param entity    the new entity
     */
    @Override
    public void updateEntity(Long idMessage, Message entity) {
        this.remove(idMessage);
        this.add(entity);
    }

    /**
     * Returns if a potential message id is available in database
     *
     * @param idMessage - the id we check
     * @return - boolean
     */
    @Override
    public boolean availableId(Long idMessage) {
        Message message = findById(idMessage);
        return (message == null);
    }

    /**
     * Returns the number of messages from database
     *
     * @return int
     */
    @Override
    public int size() {
        String sqlQuery = "Select count(idMessage) from messages";
        try (Connection connection = DriverManager.getConnection(this.url, this.username, this.password)) {
            PreparedStatement statement = connection.prepareStatement(sqlQuery);
            ResultSet resultSet = statement.executeQuery();
            resultSet.next();
            return resultSet.getInt("count");
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    /**
     * A method that returns the smallest available id in database
     *
     * @return Long
     */
    public Long generateAvailableId() {
        String sqlQuery = "        SELECT MIN(m1.idMessage + 1) AS nextID" +
                "        FROM messages m1" +
                "        LEFT JOIN messages m2" +
                "        ON m1.idMessage + 1 = m2.idMessage" +
                "        WHERE m2.idMessage IS NULL;";
        try (Connection connection = DriverManager.getConnection(this.url, this.username, this.password)) {
            PreparedStatement statement = connection.prepareStatement(sqlQuery);
            ResultSet resultSet = statement.executeQuery();
            resultSet.next();
            return resultSet.getLong("nextID");
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    /**
     * A method that returns the last message from every conversation of the current user
     *
     * @param username - String - the username of the user
     * @return - Iterable < Message >
     */
    public Iterable<Message> getLastMessageFromEveryConversationOfUser(String username) {
        String sqlQuery = "Select * from messages m\n" +
                "where m.messageDate =\n" +
                "(select max(messageDate) from messages m2\n" +
                "                         where (m.sender = m2.sender and m.receiver = m2.receiver) or\n" +
                "                               (m.receiver = m2.sender and m.sender = m2.receiver))\n" +
                "and\n" +
                "    m.messagetime = (select max(m3.messageTime) from messages m3\n" +
                "                     where ((m.sender = m3.sender and m.receiver = m3.receiver) or\n" +
                "                         (m.receiver = m3.sender and m.sender = m3.receiver))\n" +
                "                        and m.messageDate = m3.messageDate) " +
                "and (sender = '" + username + "' or receiver = '" + username + "')";
        return getMessagesByQuery(sqlQuery);
    }

    /**
     * A method that returns all messages between 2 users, sorted ascending by the sending moment
     *
     * @param username1 - String
     * @param username2 - String
     * @return Iterable < Message >
     */
    public Iterable<Message> getMessagesBetween2User(String username1, String username2) {
        String usernamesTuple = "('" + username1 + "','" + username2 + "')";
        String sqlQuery = "Select * from messages where " +
                "sender in " + usernamesTuple + " and receiver in " + usernamesTuple +
                " order by messageDate,messageTime ASC";
        return getMessagesByQuery(sqlQuery);
    }

    /**
     * A method that sets all messages, older than message, that are part of the same conversation as message as
     * received
     *
     * @param message - Message
     */
    public void setMessagesReceived(Message message) {
        String stringDate = DataTypeConvertors.getDateStringFromDateTime(message.getMessageTime());
        String stringTime = DataTypeConvertors.getCompressedTimeStringFromDateTime(message.getMessageTime());
        String sqlQuery = "Update messages " +
                "set received = true " +
                "where sender = '" + message.getSender() + "' and receiver = '" + message.getReceiver() + "' " +
                "and (messages.messageDate < '" + stringDate + "' " +
                " or (messages.messageDate = '" + stringDate + "' " +
                " and messages.messageTime <= '" + stringTime + "'))";
        executeQuery(sqlQuery);
    }

    /**
     * Sets the target message as send in the database
     *
     * @param idMessage - Long
     */
    public void setMessageSeen(Long idMessage) {
        String sqlQuery = "Update messages " +
                "set received = true, seen = true " +
                "where idMessage = " + idMessage;
        executeQuery(sqlQuery);
    }
}