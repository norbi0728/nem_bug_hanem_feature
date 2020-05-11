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
            return "Felhaszn�l�n�v " + userName + " m�r foglalt!";
        else{
            /*check if the password meets the requirements*/
            if(password.length() >= 6 && userName.length() > 3){
                Database.InsertQueryUsers(userName, password, firstName, lastName);
                return "Sikeres regisztr�ci�!";
            }
            else {
                return "A jelsz� hossza nem �ri el a 6 karaktert, vagy a felhaszn�l�n�v hossza kisebb mint 4!";
            }
        }
    }

    public static String login(String userName, String password){
        if(Database.LoginQuery(userName, password)) {
            username = userName;
            return "Sikeres bejelentkez�s!";
        }
        else
            return "Hib�s felhaszn�l�n�v vagy jelsz�";
    }

    public static String getUsername() {
        return username;
    }

    public static String getUsersFullName(String username) throws SQLException {
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
