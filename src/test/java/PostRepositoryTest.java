import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.stereotype.Repository;
import ru.netology.model.Post;
import ru.netology.repository.PostRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class PostRepositoryTest {
    @Test
    public void testAll() {
        PostRepository posts = new PostRepository();
        List<Post> postList = new ArrayList<>();
        for(int i = 0; i < 10; i++) {
            Post post = new Post(i, "Message post :"+i);
            postList.add(post);
            posts.save(post);
        }

        Assertions.assertEquals(postList,posts.all());
    }
    @Test
    public void testGetById() {
        PostRepository posts = new PostRepository();
        List<Post> postList = new ArrayList<>();
        for(int i = 0; i < 10; i++) {
            Post post = new Post(i, "Message post :"+i);
            postList.add(post);
            posts.save(post);
        }

        Assertions.assertEquals(posts.getById(4),Optional.of(postList.get(4)));
    }
    @Test
    public void testRemoveById() {
        PostRepository posts = new PostRepository();
        List<Post> postList = new ArrayList<>();
        for(int i = 0; i < 10; i++) {
            Post post = new Post(i+1, "Message post :"+i);
            postList.add(post);
            posts.save(post);
        }
        postList.remove(4);
        posts.removeById(5);

        Assertions.assertEquals(postList,posts.all());
    }

}
