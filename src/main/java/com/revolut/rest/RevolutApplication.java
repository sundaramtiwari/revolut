package com.revolut.rest;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RevolutApplication {

    private static final Logger logger = LoggerFactory.getLogger(RevolutApplication.class);

    private static Server jettyServer = null;

    public static void main(String[] args) throws Exception {
        start();
    }

    public static void start() throws Exception {
        logger.info("Starting jetty server");
        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.setContextPath("/");

        jettyServer = new Server(8080);
        jettyServer.setHandler(context);

        ServletHolder jerseyServlet = context.addServlet(
                org.glassfish.jersey.servlet.ServletContainer.class, "/*");
        jerseyServlet.setInitOrder(0);

        jerseyServlet.setInitParameter(
                "jersey.config.server.provider.classnames",
                TransferAPI.class.getCanonicalName() + ";" + AccountAPI.class.getCanonicalName());

        try {
            jettyServer.start();
            jettyServer.join();
        } finally {
            jettyServer.destroy();
        }
    }

    public static void shutdown() throws Exception {
        logger.info("Shutting down jetty server");
        jettyServer.stop();
        jettyServer.destroy();
    }
}
