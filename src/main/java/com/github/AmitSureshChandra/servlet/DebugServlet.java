package com.github.AmitSureshChandra.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@WebServlet("/debug-info")
public class DebugServlet extends HttpServlet {
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        Map<String, Object> debug = new HashMap<>();
        debug.put("contextPath", req.getContextPath());
        debug.put("servletPath", req.getServletPath());
        debug.put("pathInfo", req.getPathInfo());
        debug.put("requestURL", req.getRequestURL().toString());
        debug.put("requestURI", req.getRequestURI());
        debug.put("method", req.getMethod());
        debug.put("serverName", req.getServerName());
        debug.put("serverPort", req.getServerPort());
        
        Map<String, String> headers = new HashMap<>();
        req.getHeaderNames().asIterator().forEachRemaining(name -> 
            headers.put(name, req.getHeader(name)));
        debug.put("headers", headers);

        objectMapper.writeValue(resp.getWriter(), debug);
    }
}