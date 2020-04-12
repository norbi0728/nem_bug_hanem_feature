package model;

public class Reply {
    private String user;
    private String content;
    private int id;

    //when the reply is made
    public Reply(String user, String content) {
        this.user = user;
        this.content = content;
    }
    //when it's read back from the database
    public Reply(int id, String user, String content) {
        this.id = id;
        this.user = user;
        this.content = content;
    }

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


}
