package ru.netology.servlet;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import ru.netology.JavaConfig;
import ru.netology.controller.PostController;
import ru.netology.repository.PostRepository;
import ru.netology.service.PostService;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class MainServlet extends HttpServlet {

    AnnotationConfigApplicationContext context;
    private PostController controller;
    volatile HttpServletResponse resp;
    volatile HttpServletRequest req;

    @Override
    public void init() {
        context = new AnnotationConfigApplicationContext(JavaConfig.class);
        controller = (PostController)  context.getBean("postController");
    }

    protected void handleGet(String path, HttpServletResponse resp) throws IOException {
        if (path.equals("/api/posts")) {
            controller.all(resp);
        } else if (path.matches("/api/posts/\\d+")) {
            final var id = Long.parseLong(path.substring(path.lastIndexOf("/") + 1));
            controller.getById(id, resp);
        } else {
            resp.getWriter().print("Path : " + path + " not found");
        }

    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) {

        // если деплоились в root context, то достаточно этого
        this.resp = resp;
        this.req = req;
        try {
            final var path = req.getRequestURI();
            final var method = req.getMethod();

            if (method.equals("GET")) {
                handleGet(path, resp);
                return;
            }


            if (method.equals("POST") && path.equals("/api/posts")) {
                controller.save(req.getReader(), resp);
                return;
            }
            if (method.equals("DELETE") && path.matches("/api/posts/\\d+")) {
                // easy way
                final var id = Long.parseLong(path.substring(path.lastIndexOf("/") + 1));
                controller.removeById(id, resp);
                return;
            }
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
        } catch (Exception e) {
            e.printStackTrace();
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }

    }


}

