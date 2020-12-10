import java.util.ArrayList;

public class CustomUserCreator {

    ArrayList<User> userList = new ArrayList<User>();

    public CustomUserCreator() {

    }

    public void initialize() {
        String username1 = "maltun15";
        String password1 = "altun15";
        User user1 = new User(username1, password1);

        String username2 = "mburak15";
        String password2 = "altun15";
        User user2 = new User(username2, password2);

        String username3 = "burak15";
        String password3 = "altun15";
        User user3 = new User(username3, password3);

        userList.add(user1);
        userList.add(user2);
        userList.add(user3);
    }

    public ArrayList<User> getUserList() {
        return userList;
    }

}
