package java_projects.demo.service;

import java_projects.demo.domain.Friendship;
import java_projects.demo.repository.IRepository;
import java_projects.demo.utils.events.EventType;
import java_projects.demo.utils.events.FriendshipChangeEvent;
import java_projects.demo.utils.events.UsersEvent;
import java_projects.demo.utils.observer.ObservableUsers;
import java_projects.demo.validators.FriendshipValidator;

import java.time.LocalDateTime;
import java.util.*;

public class ServiceFriendships extends ObservableUsers<UsersEvent<String>, String> {
    private final IRepository<ArrayList<String>, Friendship> friendshipsRepo;

    /**
     * Constructor for creating a service
     *
     * @param friendshipsRepo - repository of friends
     */
    public ServiceFriendships(IRepository<ArrayList<String>, Friendship> friendshipsRepo) {
        this.friendshipsRepo = friendshipsRepo;
    }

    /**
     * Check if two users are friends
     *
     * @param username1 - String - the username of the first user
     * @param username2 - String - the username of the second user
     * @return true if the users are friends and false otherwise
     */
    public boolean friends(String username1, String username2) {
        Friendship friendship = friendshipsRepo.findById(new ArrayList<>(List.of(username1, username2)));

        if (friendship == null)
            return false;

        return !friendship.getIsPending();
    }

    /**
     * Check if there is a pending friend request from user id1 to user id2
     *
     * @param username1 - String - the id of the first user
     * @param username2 - String - the id of the second user
     * @return 1 if there is a pending request from username1 to username2
     * -1 if there is a pending request from username2 to username1
     * 0 otherwise
     */
    public int pendingFriendRequest(String username1, String username2) {
        Friendship friendship = friendshipsRepo.findById(new ArrayList<>(List.of(username1, username2)));
        if (friendship == null || !friendship.getIsPending())
            return 0;

        if (Objects.equals(friendship.getIdFriend1(), username1)) {
            if (friendship.isFromFirstFriend())
                return 1;
            return -1;
        }

        return friendship.isFromFirstFriend() ? -1 : 1;
    }

    /**
     * Add a new friendship in the repo
     *
     * @param username1 - the id of one friend
     * @param username2 - the id of the other friend
     * @throws Exception if there is no user with an id or if the users are already friends
     */
    public void addFriendship(String username1, String username2) throws Exception {
        FriendshipValidator.validate(username1, username2);
        Friendship friendship = new Friendship(username1, username2);
        if (username1.compareTo(username2) > 0)
            friendship.setFromFirstFriend(false);
        friendshipsRepo.add(friendship);

        super.notifyObserversFriendship(new FriendshipChangeEvent(EventType.ADD, null, friendship));
    }

    /**
     * Transform an Iterable object into a collection
     *
     * @param iter the iterable object we transform
     * @return the collection object we just created
     */
    public static <E> Collection<E> makeCollection(Iterable<E> iter) {
        Collection<E> list = new ArrayList<>();
        for (E item : iter) {
            list.add(item);
        }
        return list;
    }

    /**
     * Returns the friendship between id1 and id2
     *
     * @param username1 - String - the id of the first friend
     * @param username2 - String - the id of the second friend
     * @return - Friendship - the friendship between 2 users
     */
    public Friendship getFriendshipById(String username1, String username2) {
        return friendshipsRepo.findById(new ArrayList<>(Arrays.asList(username1, username2)));
    }

    /**
     * Returns all friendships from repository
     *
     * @return - ArrayList < Friendship >
     */
    public ArrayList<Friendship> getAllFriendships() {
        Collection<Friendship> iterable = makeCollection(friendshipsRepo.findAll());
        return new ArrayList<>(iterable);
    }

    /**
     * Remove the friendship between 2 users
     *
     * @param usernames - ArrayList < String > with size = 2 that has the ids of the users that are no longer friends
     */
    public void removeFriendship(ArrayList<String> usernames) {
        Friendship friendship = friendshipsRepo.remove(usernames);
        super.notifyObserversFriendship(new FriendshipChangeEvent(EventType.DELETE, friendship, null));
    }

    /**
     * Set the friend request between user with id1 and user with id2 as accepted
     *
     * @param username1 - String - the first user from friend request
     * @param username2 - String - the second user from friend request
     */
    public void acceptFriendRequest(String username1, String username2) {
        Friendship friendship = friendshipsRepo.findById(new ArrayList<>(Arrays.asList(username1, username2)));
        Friendship oldFriendship = friendship; // Friendship class has only primitive types, so it's safe to copy it
        friendship.setPending(false);
        friendship.setFriendshipMoment(LocalDateTime.now());
        friendshipsRepo.updateEntity(new ArrayList<>(Arrays.asList(username1, username2)), friendship);
        super.notifyObserversFriendship(new FriendshipChangeEvent(EventType.UPDATE, oldFriendship, friendship));
    }

    /**
     * Set the friend request between user1 and user2 as declined
     *
     * @param username1 - String - the first user from friend request
     * @param username2 - String - the second user from friend request
     */
    public void declineFriendRequest(String username1, String username2) {
        Friendship friendship = friendshipsRepo.remove(new ArrayList<>(Arrays.asList(username1, username2)));
        super.notifyObserversFriendship(new FriendshipChangeEvent(EventType.DELETE, friendship, null));
    }

    /**
     * Returns a list of all users that have requested friendship from current user
     *
     * @param username - String - the current user
     * @return - ArrayList <Friendship>
     */
    public ArrayList<Friendship> getAllFriendRequestsReceived(String username) {
        ArrayList<Friendship> friendRequests = new ArrayList<>();
        friendshipsRepo.findAll().forEach(friendship -> {
            if (friendship.getIdFriend1().equals(username) || friendship.getIdFriend2().equals(username))
                if (friendship.getIsPending() && !friendship.isFromUsername(username))
                    friendRequests.add(friendship);
        });
        return friendRequests;
    }

    /**
     * Returns the usernames of all friends of the chosen user
     *
     * @param username - String
     * @return Iterable <String>
     */
    public Iterable<String> getAllFriendsOfUser(String username) {
        ArrayList<String> friends = new ArrayList<>();
        friendshipsRepo.findAll().forEach(friendship -> {
            if (friendship.getIdFriend1().equals(username))
                friends.add(friendship.getIdFriend2());
            else if (friendship.getIdFriend2().equals(username))
                friends.add(friendship.getIdFriend1());
        });
        return friends;
    }

    /**
     * Returns the usernames of all friends of chosen user that are matching the pattern (the pattern is included in
     * their username)
     *
     * @param username - String
     * @param pattern  - String
     * @return Iterable<String>
     */
    public Iterable<String> getAllFriendsMatchingPattern(String username, String pattern) {
        ArrayList<String> friendsMatching = new ArrayList<>();
        this.getAllFriendsOfUser(username).forEach(friend -> {
            if (friend.contains(pattern))
                friendsMatching.add(friend);
        });
        return friendsMatching;
    }
}
