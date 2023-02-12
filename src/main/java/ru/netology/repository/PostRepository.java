package ru.netology.repository;

import ru.netology.model.Post;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

// Stub
public class PostRepository {
  private ConcurrentHashMap<Long, Post> posts = new ConcurrentHashMap<Long, Post>();
  public List<Post> all() {
    List<Post> result = new ArrayList<>();
    result.addAll(posts.values());
    return result;
  }

  public Optional<Post> getById(long id) {

    return Optional.ofNullable(posts.get(id));

  }
  public void addPost(Post post) {
    post.setId(posts.size()+1);
    posts.put(post.getId(), post);
  }

  public Post save(Post post) {
    long postId = post.getId();
    if(postId == 0) {
      addPost(post);
    } else {
      if(posts.containsKey(postId)) {
//        Post oldPost = posts.get(postId);
        Post newPost = new Post();
        newPost.setId(postId);
        newPost.setContent(post.getContent());
        posts.replace(postId,newPost);
      } else {
        addPost(post);
      }
    }
    return post;
  }

  public void removeById(long id) {
    posts.remove(id);
  }
}
