package com.github.AmitSureshChandra.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.AmitSureshChandra.annotation.Delete;
import com.github.AmitSureshChandra.annotation.Get;
import com.github.AmitSureshChandra.annotation.Post;
import com.github.AmitSureshChandra.annotation.Put;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

@WebServlet("/api/*")
public class DispatcherServlet extends HttpServlet {
    private static final Logger logger = Logger.getLogger(DispatcherServlet.class.getName());
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final Map<String, Object> controllers = new HashMap<>();

    @Override
    public void init() throws ServletException {
        logger.info("Initializing DispatcherServlet...");
        try {
            Object userController = Class.forName("com.github.AmitSureshChandra.controller.UserRestController").newInstance();
            controllers.put("/users", userController);
            logger.info("Successfully initialized UserRestController");
            logger.info("Available controllers: " + controllers.keySet());
        } catch (Exception e) {
            logger.severe("Failed to initialize controllers: " + e.getMessage());
            throw new ServletException("Failed to initialize controllers", e);
        }
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String pathInfo = req.getPathInfo();
        String method = req.getMethod();
        String requestURL = req.getRequestURL().toString();
        
        logger.info(String.format("Request: %s %s (PathInfo: %s)", method, requestURL, pathInfo));
        
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        try {
            handleRequest(pathInfo, method, req, resp);
        } catch (Exception e) {
            logger.severe("Error handling request: " + e.getMessage());
            StringWriter sw = new StringWriter();
            e.printStackTrace(new PrintWriter(sw));
            logger.severe("Stack trace: " + sw.toString());
            
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            Map<String, Object> error = new HashMap<>();
            error.put("error", e.getMessage());
            error.put("type", e.getClass().getSimpleName());
            error.put("path", pathInfo);
            error.put("method", method);
            objectMapper.writeValue(resp.getWriter(), error);
        }
    }

    private void handleRequest(String pathInfo, String httpMethod, HttpServletRequest req, HttpServletResponse resp) throws Exception {
        if (pathInfo == null) {
            logger.warning("PathInfo is null");
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        
        String[] pathParts = pathInfo.split("/");
        logger.info("Path parts: " + java.util.Arrays.toString(pathParts));
        
        if (pathParts.length < 2) {
            logger.warning("Invalid path structure: " + pathInfo);
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        
        String controllerPath = "/" + pathParts[1];
        logger.info("Looking for controller: " + controllerPath);
        
        Object controller = controllers.get(controllerPath);
        if (controller == null) {
            logger.warning("Controller not found for path: " + controllerPath);
            logger.info("Available controllers: " + controllers.keySet());
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return;
        }
        
        logger.info("Found controller: " + controller.getClass().getSimpleName());

        Method[] methods = controller.getClass().getDeclaredMethods();
        logger.info("Controller has " + methods.length + " methods");
        
        for (Method method : methods) {
            logger.info("Checking method: " + method.getName() + " for HTTP method: " + httpMethod);
            if (matchesHttpMethod(method, httpMethod)) {
                logger.info("Invoking method: " + method.getName());
                Object result = method.invoke(controller, req, resp);
                if (result != null) {
                    logger.info("Method returned result, writing JSON response");
                    objectMapper.writeValue(resp.getWriter(), result);
                } else {
                    logger.info("Method returned null");
                }
                return;
            }
        }
        
        logger.warning("No matching method found for HTTP method: " + httpMethod);
        resp.setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
    }

    private boolean matchesHttpMethod(Method method, String httpMethod) {
        boolean matches = (httpMethod.equals("GET") && method.isAnnotationPresent(Get.class)) ||
                         (httpMethod.equals("POST") && method.isAnnotationPresent(Post.class)) ||
                         (httpMethod.equals("PUT") && method.isAnnotationPresent(Put.class)) ||
                         (httpMethod.equals("DELETE") && method.isAnnotationPresent(Delete.class));
        
        if (matches) {
            logger.info("Method " + method.getName() + " matches HTTP " + httpMethod);
        }
        return matches;
    }
}