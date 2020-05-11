package model;

import java.util.Date;

public class Reply {
    public void setDate(Date date) {
        this.date = date;
    }

    private Date date;
    private String user;
    private String content;
    private int id;
    private int postID;

    //when the reply is made
    public Reply(int postID, String user, String content) {
        this.date = new Date(System.currentTimeMillis());
        this.postID = postID;
        this.user = user;
        this.content = content;
    }
    //when it's read back from the database
    public Reply(int id, int postID, String user, String content, Date date) {
        this.id = id;
        this.postID = postID;
        this.user = user;
        this.content = content;
        this.date = date;
    }

    public Reply(){}

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPostID() {return postID; }

    public void setPostID(int postID) { this.postID = postID; }


    public Date getDate() {
        return date;
    }
}
