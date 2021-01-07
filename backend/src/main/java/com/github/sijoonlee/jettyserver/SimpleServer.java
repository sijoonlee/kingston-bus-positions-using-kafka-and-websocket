package com.github.sijoonlee.jettyserver;

import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.ContextHandler;
import org.eclipse.jetty.server.handler.ContextHandlerCollection;


//https://jansipke.nl/websocket-tutorial-with-java-server-jetty-and-javascript-client/
//https://stackoverflow.com/questions/19054286/classnotfoundexception-org-eclipse-jetty-util-component-abstractlifecycle-runni
//https://stackoverflow.com/questions/15645341/how-do-i-create-an-embedded-websocket-server-jetty-9
//https://github.com/jetty-project/embedded-jetty-websocket-examples

public class SimpleServer {
    public static void main(String[] args) throws Exception {
        Server server = new Server(8080);
        ContextHandler con = new ContextHandler();
        con.setContextPath("/test");
        //https://stackoverflow.com/questions/17886865/post-request-becomes-get
        // By default a Jetty ContextHandler with a context of "/app" will actually redirect any request to "/app" to "/app/", have a look at setAllowNullPathInfo.
        // So you have 2 possible solutions, call setAllowNullPathInfo(true) on your ContextHandler, or change your post url on the client to HttpPost post = new HttpPost("http://10.0.2.2:8080/app/");
        con.setAllowNullPathInfo(true);
        con.setHandler(new SimpleHandler());
        ContextHandler con2 = new ContextHandler();
        con2.setContextPath("/");
        con2.setAllowNullPathInfo(true);
        con2.setHandler(new PageHandler());
        ContextHandlerCollection contexts = new ContextHandlerCollection();

        contexts.setHandlers(new Handler[]{con, con2});
        server.setHandler(contexts);

        server.start();
        server.join();
    }
}
