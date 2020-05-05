package model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Post {
    private Date date;
    private String author;
    private String content;
    private String title;
    private int id;
    private List<Reply> replies;
    private String subject; //the name of the class, e.g. Programming I.

    //when it's just have made
    public Post(String title, String author, String content, String subject) {
        this.date = new Date(System.currentTimeMillis());
        this.title = title;
        this.author = author;
        this.content = content;
        this.subject = subject;
        replies = new ArrayList<>();
    }
    //when it's read back from the database
    public Post(Date date, String title, String author, String content, int id, List<Reply> replies, String subject) {
        this.date = date;
        this.title = title;
        this.author = author;
        this.content = content;
        this.id = id;
        this.replies = replies;
        this.subject = subject;
    }

    public Post(){}

    public void reply(String user, String content){
        replies.add(new Reply(id, user, content));
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) { this.content = content; }

    public String getAuthor() { return author; }

    public void setAuthor(String author) { this.author = author; }

    public String getSubject() { return subject; }

    public void setSubject(String subject) { this.subject = subject; }

    public String getTitle() { return title; }

    public void setTitle(String title) {this.title = title; }

    public int getID() { return id; }

    public void setID(int id) { this.id = id; }
}
