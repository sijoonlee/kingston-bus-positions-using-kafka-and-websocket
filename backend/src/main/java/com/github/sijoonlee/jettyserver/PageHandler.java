package com.github.sijoonlee.jettyserver;

import com.github.sijoonlee.constant.DEFAULT;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class PageHandler extends AbstractHandler {
    public void handle(String target, Request baseRequest, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        baseRequest.setHandled(true);
        response.setIntHeader("Refresh", DEFAULT.updateInterval/1000);
        response.setContentType("text/html");
        response.setStatus(HttpServletResponse.SC_OK);
        String body = "on update";
        String html = "<!DOCTYPE html><html> <head><title>Bus Table</title></head> <body>%s</body></html>";

        response.getWriter().println(String.format(html,body));
    }
}
