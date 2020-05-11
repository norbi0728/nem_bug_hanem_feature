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

    public List<Post> getPosts(String subjectCode){
        List<Post> posts = Database.QueryGetEverything(subjectCode);

        return posts;
    }

}
