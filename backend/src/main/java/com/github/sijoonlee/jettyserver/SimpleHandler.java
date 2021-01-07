package com.github.sijoonlee.jettyserver;

import java.io.IOException;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;
import org.eclipse.jetty.server.handler.ContextHandler;

public class SimpleHandler extends AbstractHandler {

    public void handle(String target, Request baseRequest, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        baseRequest.setHandled(true);
        response.setContentType("application/json;charset=utf-8");
        response.setStatus(HttpServletResponse.SC_OK);
        String body = "";
        if(baseRequest.getMethod().equalsIgnoreCase("POST")){
            String line = null;
            while( (line = baseRequest.getReader().readLine()) != null){
                body += line + "\n";
            }
        } else if(baseRequest.getMethod().equalsIgnoreCase("GET")){
            Map<String,String[]> params = baseRequest.getParameterMap();
            for(Map.Entry<String,String[]> param : params.entrySet()){
                body += "[KEY: " + param.getKey() + "]\n";
                for(String val : param.getValue()){
                    body += "[VAL: " + val + "]\n";
                }
            }
        }

        response.getWriter().println(body);
    }
}
