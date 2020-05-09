package logic.authentication;

import model.Database;
import model.User;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Authentication {
    private static String username;
    public static Authentication authentication = null;

    public static Authentication getAuthentication(){
        if(authentication == null)
            authentication = new Authentication();

        return authentication;
    }
    public static String register(String userName, String password, String firstName, String lastName){
        if(Database.ExistQuery("SELECT Username FROM users", userName, "Username"))
            return "Felhasználónév " + userName + " már foglalt!";
        else{
            /*check if the password meets the requirements*/
            if(password.length() >= 6){
                Database.InsertQueryUsers(userName, password, firstName, lastName);
                return "Sikeres regisztráció!";
            }
            else {
                return "A jelszó hossza nem éri el a 6 karaktert!";
            }
        }
    }

    public static String login(String userName, String password){
        if(Database.LoginQuery(userName, password)) {
            username = userName;
            return "Sikeres bejelentkezés!";
        }
        else
            return "Hibás felhasználónév vagy jelszó";
    }

    public static String getUsername() {
        return username;
    }

    public static String getUsersFullName() throws SQLException {
        String firstName;
        String lastName;
        ResultSet rs = Database.Query("SELECT * FROM users WHERE USERNAME="+
                "'"+username+"'");
        rs.first();
        firstName = rs.getString("Firstname");
        lastName = rs.getString("Lastname");

        return lastName + " " + firstName;
    }

    private String hash(String password){
        return String.valueOf(password.hashCode());
    }

}
