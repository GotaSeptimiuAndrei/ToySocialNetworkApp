package java_projects.demo.service;

public class Services {
    private ServiceUsers serviceUsers;
    private ServiceFriendships serviceFriendships;
    private ServiceMessages serviceMessages;

    public Services(ServiceUsers serviceUsers, ServiceFriendships serviceFriendships, ServiceMessages serviceMessages) {
        this.serviceUsers = serviceUsers;
        this.serviceFriendships = serviceFriendships;
        this.serviceMessages = serviceMessages;
    }

    public ServiceUsers getServiceUsers() {
        return serviceUsers;
    }

    public void setServiceUsers(ServiceUsers serviceUsers) {
        this.serviceUsers = serviceUsers;
    }

    public ServiceFriendships getServiceFriendships() {
        return serviceFriendships;
    }

    public void setServiceFriendships(ServiceFriendships serviceFriendships) {
        this.serviceFriendships = serviceFriendships;
    }

    public ServiceMessages getServiceMessages() {
        return serviceMessages;
    }

    public void setServiceMessages(ServiceMessages serviceMessages) {
        this.serviceMessages = serviceMessages;
    }
}
