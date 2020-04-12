package model;

public class User {
    private String firstName;
    private String lastName;
    private String passwordHash;//the hash of the password which he/she gives after registration, not the neptun's pass
    private String userName;

    public User() {
    }

    public User(String userName, String firstName, String lastName, String passwordHash) {
        this.userName = userName;
        this.firstName = firstName;
        this.lastName = lastName;
        this.passwordHash = passwordHash;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String neptun) {
        this.userName = neptun;
    }
}
