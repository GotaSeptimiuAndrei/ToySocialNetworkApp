package java_projects.demo.fxml.homepage;

public class FeedController extends MainController {
    public void setUp() {
        serviceMessages.addObserver(this, this.username);
        serviceFriendships.addObserver(this, this.username);
    }
}
