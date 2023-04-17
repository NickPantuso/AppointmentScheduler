package group.qam2.model;

/**
 * Creates a User.
 * @author Nick Pantuso
 */
public class User {

    private int userId;
    private String userName;
    private String pass;

    public User(int userId, String userName, String pass) {
        this.userId = userId;
        this.userName = userName;
        this.pass = pass;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public String getPass() {
        return pass;
    }
}
