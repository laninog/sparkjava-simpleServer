package com.sparkjava.test;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

import static spark.Spark.*;

public class Server {

    private static Logger log = Logger.getLogger(Server.class);

    public static void main(String args[]) {

        BasicConfigurator.configure();

        log.info("Starting spark...");

        port(8080);

        get("/**", (req, res) -> {

            log.info("Path -> " + req.pathInfo());

            String response;

            String path = req.pathInfo();

            File file = new File("public" + path);

            res.type("text/html");

            if (!file.exists()) {
                res.status(404);
                response = "<!DOCTYPE html><html><body><h1>Not found</h1></body></html>";
            } else if(file.isFile()) {
                res.status(200);
                response = readFile(file.getPath(), StandardCharsets.UTF_8);
            } else if(file.isDirectory()) {
                res.status(302);
                response = readFile("public/index.html", StandardCharsets.UTF_8);
            } else {
                res.status(500);
                response = "<!DOCTYPE html><html><body><h1>Internal Error</h1></body></html>";
            }

            return response;
        });

    }

    private static String readFile(String path, Charset encoding)
            throws IOException {
        byte[] encoded = Files.readAllBytes(Paths.get(path));
        return new String(encoded, encoding);
    }

}
