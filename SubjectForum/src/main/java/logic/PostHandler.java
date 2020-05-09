package logic;

import logic.utility.RabinKarpStringPatternMatching;
import model.Database;
import model.Post;
import model.Reply;

import java.util.*;

public class PostHandler {
    public static PostHandler postHandler = null;

    public static PostHandler getPostHandler(){
        if(postHandler == null)
            postHandler = new PostHandler();

        return postHandler;
    }

    public Map<Post, List<Integer>> searchPostsByContent(String subject, String pattern){
        Map<Post, List<Integer>> results = new HashMap<>();
        List<Post> posts = getPosts(subject);

        for(Post post: posts){
            List<Integer> matchingCharacters = RabinKarpStringPatternMatching
                    .getRabinKarpStringPatternMatching()
                    .stringPatternMatching(post.getContent(), pattern, 10, 13);
                    /*d is the base of the number system, q is the prime that we'll check the modulo*/

            if(matchingCharacters.size() != 0)
                results.put(post, matchingCharacters);
        }

        return results;
    }

    public List<Post> getPosts(String subjectName){
        List<Post> posts = new ArrayList<>();
        if(subjectName.equals("Szoftverfejlesztés")) {
            Post post1 = new Post("test1", "testUser", "testtext1", "Szoftverfejlesztés");
            ArrayList<Reply> replies1 = new ArrayList<>();
            replies1.add(new Reply(1, "testUser2", "testcomment1"));
            replies1.add(new Reply(1, "testUser2", "testcomment2"));
            replies1.add(new Reply(1, "testUser2", "testcomment3"));

            Post post2 = new Post("test2", "testUser", "testtext2", "Szoftverfejlesztés");
            ArrayList<Reply> replies2 = new ArrayList<>();
            replies2.add(new Reply(2, "testUser3", "testcomment1"));
            replies2.add(new Reply(2, "testUser3", "testcomment2"));
            replies2.add(new Reply(2, "testUser3", "testcomment3"));

            Map<Post, ArrayList<Reply>> mp1 = new HashMap<>();
            Map<Post, ArrayList<Reply>> mp2 = new HashMap<>();
            mp1.put(post1, replies1);
            mp2.put(post2, replies2);
//        List<Map<Post,ArrayList<Reply>>> everything = Database.QueryGetEverything(subjectName);
            List<Map<Post, ArrayList<Reply>>> everything = new ArrayList<>();
            everything.add(mp1);
            everything.add(mp2);

            for (Map<Post, ArrayList<Reply>> m : everything) {
                for (Map.Entry<Post, ArrayList<Reply>> entry : m.entrySet()) {
                    Post p = entry.getKey();
                    p.setReplies(entry.getValue());
                    posts.add(p);
                }
            }
        }
        else if(subjectName.equals("Szoftvermodellezés és tesztelés")){
            Post post1 = new Post("test4", "testUser4", "testtext4", "Szoftvermodellezés és tesztelés");
            ArrayList<Reply> replies1 = new ArrayList<>();
            replies1.add(new Reply(1, "testUser4", "testcomment4"));
            replies1.add(new Reply(1, "testUser4", "testcomment5"));
            replies1.add(new Reply(1, "testUser4", "testcomment6"));

            Post post2 = new Post("test5", "testUser5", "testtext5", "Szoftvermodellezés és tesztelés");
            ArrayList<Reply> replies2 = new ArrayList<>();
            replies2.add(new Reply(2, "testUser5", "testcomment7"));
            replies2.add(new Reply(2, "testUser5", "testcomment8"));
            replies2.add(new Reply(2, "testUser5", "testcomment9"));

            Map<Post, ArrayList<Reply>> mp1 = new HashMap<>();
            Map<Post, ArrayList<Reply>> mp2 = new HashMap<>();
            mp1.put(post1, replies1);
            mp2.put(post2, replies2);
//        List<Map<Post,ArrayList<Reply>>> everything = Database.QueryGetEverything(subjectName);
            List<Map<Post, ArrayList<Reply>>> everything = new ArrayList<>();
            everything.add(mp1);
            everything.add(mp2);

            for (Map<Post, ArrayList<Reply>> m : everything) {
                for (Map.Entry<Post, ArrayList<Reply>> entry : m.entrySet()) {
                    Post p = entry.getKey();
                    p.setReplies(entry.getValue());
                    posts.add(p);
                }
            }
        }
        return posts;
    }

//    public void addPost(String author, String content, String subject){
//        Database.getDatabase().addPost(new Post(author, content, subject));
//    }

//    public void addReply(int postID, String username, String content){
//        Database.getDatabase().addReply(new Reply(postID, username, content));
//    }
}
