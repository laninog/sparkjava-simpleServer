package com.sparkjava.test;

import static spark.Spark.before;
import static spark.Spark.get;
import static spark.Spark.options;
import static spark.Spark.path;
import static spark.Spark.post;
import static spark.Spark.put;
import static spark.Spark.port;

import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;

import spark.RouteGroup;

/**
 * Created by alejandro on 9/4/17.
 */
public class SimpleServer {

    private static Logger log = Logger.getLogger(SimpleServer.class);

    public static void main(String args[]) {

        initLog();

        log.info("Starting spark...");

        port(8080);

        enableCORS("*", "OPTIONS, GET, POST, PUT", "Accept, Accept-Language, Content-Language, Content-Type");

        get("/hello", (req, res) -> {

            log.info("Request -> " + req.params());

            return "Hello World!!";
        });

        createUserResource();

    }

    private static void initLog() {
        ConsoleAppender console = new ConsoleAppender();

        String PATTERN = "%d [%p|%c|%C{1}] %m%n";
        console.setLayout(new PatternLayout(PATTERN));
        console.setThreshold(Level.DEBUG);
        console.activateOptions();

        Logger.getRootLogger().addAppender(console);
    }

    private static void createUserResource() {

        path("/user", new RouteGroup() {

            @Override
            public void addRoutes() {

                get("", (req, res) -> {
                    log.info("Request -> " + req.params());
                    return "All users!";
                });

                get("/:id", (req, res) -> {
                    log.info("Request -> " + req.params());
                    return "Find a user by id";
                });

                post("", (req, res) -> {
                    log.info("Request -> " + req.params());
                    return "Create a user";
                });

                put("/:id", (req, res) -> {
                    log.info("Request -> " + req.params());
                    return "Update a user";
                });

            }

        });

    }

    private static void enableCORS(final String origin, final String methods, final String headers) {

        options("/*", (request, response) -> {

            String accessControlRequestHeaders = request.headers("Access-Control-Request-Headers");
            if (accessControlRequestHeaders != null) {
                response.header("Access-Control-Allow-Headers", accessControlRequestHeaders);
            }

            String accessControlRequestMethod = request.headers("Access-Control-Request-Method");
            if (accessControlRequestMethod != null) {
                response.header("Access-Control-Allow-Methods", accessControlRequestMethod);
            }

            return "OK";
        });

        before((request, response) -> {
            response.header("Access-Control-Allow-Origin", origin);
            response.header("Access-Control-Request-Method", methods);
            response.header("Access-Control-Allow-Headers", headers);
            response.type("text/plain");
        });
    }

}
