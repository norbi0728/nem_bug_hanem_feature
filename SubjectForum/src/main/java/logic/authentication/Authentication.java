package logic.authentication;

import model.User;

public class Authentication {
    public static Authentication authentication = null;

    public static Authentication getAuthentication(){
        if(authentication == null)
            authentication = new Authentication();

        return authentication;
    }
//    public String register(String userName, String password, String firstName, String lastName){
//        //if(Database.getDatabase().userExists(userName))
//            return "Felhasználónév " + userName + " már foglalt!";
//        else{
//            /*check if the password meets the requirements*/
//            if(password.length() >= 6){
//                //Database.getDatabase().addUser(new User(userName, hash(password), firstName, lastName))
//                return "Sikeres regisztráció!";
//            }
//            else {
//                return "A jelszó hossza nem éri el a 6 karaktert!";
//            }
//        }
//    }

//    public String login(String userName, String password){
//        if(Database.getDatabase().getPasswordHash(userName) == hash(password))
//            return "Sikeres bejelentkezés!";
//        else
//            return "Hibás felhasználónév vagy jelszó";
//    }

    private String hash(String password){
        return String.valueOf(password.hashCode());
    }
}
