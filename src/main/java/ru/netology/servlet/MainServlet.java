package ru.netology.servlet;

import ru.netology.controller.PostController;
import ru.netology.repository.PostRepository;
import ru.netology.service.PostService;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class MainServlet extends HttpServlet {

    private static final String PATH = "/api/posts";
    private static final String GET = "GET";
    private static final String POST = "POST";
    private static final String DELETE = "DELETE";

    private PostController controller;
    volatile HttpServletResponse resp;
    volatile HttpServletRequest req;

    @Override
    public void init() {
        final var repository = new PostRepository();
        final var service = new PostService(repository);
        controller = new PostController(service);
    }

    protected void handleGet(String path, HttpServletResponse resp) throws IOException {
        if (path.equals(PATH)) {
            controller.all(resp);
        } else if (path.matches(PATH + "/\\d+")) {
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
            switch (method) {
                case GET:
                    handleGet(path, resp);
                    break;
                case POST:
                    controller.save(req.getReader(), resp);
                    break;
                case DELETE:
                    final var id = Long.parseLong(path.substring(path.lastIndexOf("/") + 1));
                    controller.removeById(id, resp);
                    break;
                default:
                    resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }

    }


}

