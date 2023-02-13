package ru.netology.controller;

import com.google.gson.Gson;
import ru.netology.model.Post;
import ru.netology.service.PostService;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Reader;

public class PostController {
    public static final String APPLICATION_JSON = "application/json";
    private final PostService service;

    public PostController(PostService service) {
        this.service = service;
    }


    protected <T> void send(T t, HttpServletResponse response) throws IOException {
        response.setContentType(APPLICATION_JSON);
        final var gson = new Gson();
        response.getWriter().print(gson.toJson(t));
    }


    public void all(HttpServletResponse response) throws IOException {
        final var data = service.all();
        send(data, response);
    }

    public void getById(long id, HttpServletResponse response) throws IOException {
        // TODO: deserialize request & serialize response
        final var data = service.getById(id);
        send(data, response);
    }

    public void save(Reader body, HttpServletResponse response) throws IOException {
        final var gson = new Gson();
        final var post = gson.fromJson(body, Post.class);
        final var data = service.save(post);
        send(data, response);
    }

    public void removeById(long id, HttpServletResponse response) throws IOException {
        // TODO: deserialize request & serialize response
        service.removeById(id);
        response.getWriter().print("Post id: " + id + " removed");
    }
}
