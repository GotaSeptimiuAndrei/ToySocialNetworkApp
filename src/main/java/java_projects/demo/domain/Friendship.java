package java_projects.demo.domain;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

public class Friendship extends Entity<ArrayList<String>>{
    private LocalDateTime friendshipMoment;

    private boolean isPending;

    private boolean isFromFirstFriend;

    /**
     * Create a new friendship between two users and will store the current time as the moment of friendship start
     * @param idFriend1 the id of the first user
     * @param idFriend2 the id of the second user
     */
    public Friendship(String idFriend1, String idFriend2){
        super(new ArrayList<>(Arrays.asList(idFriend1, idFriend2)));
        this.friendshipMoment = LocalDateTime.now();
        this.isPending = true;
        this.isFromFirstFriend = true;
    }

    /**
     * Create a new friendship between two users and will store the current time as the moment of friendship start
     * @param idFriend1 the id of the first user
     * @param idFriend2 the id of the second user
     */
    public Friendship(String idFriend1, String idFriend2, LocalDateTime friendshipMoment, boolean isPending,
                      boolean isFromFirstFriend){
        super(new ArrayList<>(Arrays.asList(idFriend1, idFriend2)));
        this.friendshipMoment = friendshipMoment;
        this.isPending = isPending;
        this.isFromFirstFriend = isFromFirstFriend;
    }

    /**
     * Return the id of the first friend
     * @return the id of the friend
     */
    public String getIdFriend1() {
        ArrayList<String> users = new ArrayList<>(this.getId());
        return users.getFirst();
    }

    /**
     * Update the id of the second friend
     * @param idFriend1 - the new id
     */
    public void setIdFriend1(String idFriend1) {
        String secondUser = this.getIdFriend2();
        this.setId(new ArrayList<>(Arrays.asList(idFriend1, secondUser)));
    }

    /**
     * Return the id of the second friend
     * @return the id of the friend
     */
    public String getIdFriend2() {
        ArrayList<String> users = new ArrayList<>(this.getId());
        return users.get(1);
    }

    /**
     * Update the id of the second friend
     * @param idFriend2 - the new friend
     */
    public void setIdFriend2(String idFriend2) {
        String firstUser = this.getIdFriend2();
        this.setId(new ArrayList<>(Arrays.asList(firstUser, idFriend2)));
    }

    /**
     * Return the moment when the friendship has started
     * @return the time
     */
    public LocalDateTime getFriendshipMoment() {
        return friendshipMoment;
    }

    /**
     * Update the time when the friendship has started
     * @param friendshipMoment the new time
     */
    public void setFriendshipMoment(LocalDateTime friendshipMoment) {
        this.friendshipMoment = friendshipMoment;
    }

    /**
     * Return true if the friend request is on pending and false if is accepted
     * @return boolean
     */
    public boolean getIsPending() {
        return isPending;
    }

    /**
     * Set the state of the friendship. True means the request is in pending. False means the request has been accepted
     * @param pending - boolean - true or false
     */
    public void setPending(boolean pending) {
        isPending = pending;
    }

    public boolean isFromFirstFriend() {
        return isFromFirstFriend;
    }

    public void setFromFirstFriend(boolean fromFirstFriend) {
        isFromFirstFriend = fromFirstFriend;
    }

    public boolean isFromUsername(String username){
        if(Objects.equals(username, this.getIdFriend1()) && this.isFromFirstFriend)
            return true;
        return Objects.equals(username, this.getIdFriend2()) && !this.isFromFirstFriend;
    }

    public String getOtherFriend(String username){
        if(this.getIdFriend1().equals(username))
            return getIdFriend2();
        return getIdFriend1();
    }
}
