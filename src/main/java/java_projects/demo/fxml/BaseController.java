package java_projects.demo.fxml;

import java_projects.demo.fxml.widgets_generators.VBoxesGenerator;
import java_projects.demo.service.ServiceFriendships;
import java_projects.demo.service.ServiceMessages;
import java_projects.demo.service.ServiceUsers;
import java_projects.demo.service.Services;

public class BaseController {
    protected ServiceUsers serviceUsers;
    protected ServiceFriendships serviceFriendships;
    protected ServiceMessages serviceMessages;
    protected VBoxesGenerator vBoxesGenerator;

    public void setServices(Services services) {
        this.serviceUsers = services.getServiceUsers();
        this.serviceFriendships = services.getServiceFriendships();
        this.serviceMessages = services.getServiceMessages();
        vBoxesGenerator = new VBoxesGenerator(services);
    }

    /**
     * Set the serviceUsers for the current controller
     *
     * @param serviceUsers - ServiceUser - the service for the users
     */
    public void setServiceUsers(ServiceUsers serviceUsers) {
        this.serviceUsers = serviceUsers;
    }

    /**
     * Set the serviceFriendships for the current controller
     *
     * @param serviceFriendships - ServiceFriendships - the service for the users
     */
    public void setServiceFriendships(ServiceFriendships serviceFriendships) {
        this.serviceFriendships = serviceFriendships;
    }

}
