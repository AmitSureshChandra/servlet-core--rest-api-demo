package org.example;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.HandlesTypes;
import javax.servlet.annotation.WebServlet;

import org.springframework.web.WebApplicationInitializer;

@HandlesTypes(WebServlet.class)
public class WebConfig implements WebApplicationInitializer {

    @Override
    public void onStartup(ServletContext servletContext) throws ServletException {
        servletContext.setInitParameter("javax.servlet.jsp.jstl.fmt.localizationContext", "messages");
    }
}