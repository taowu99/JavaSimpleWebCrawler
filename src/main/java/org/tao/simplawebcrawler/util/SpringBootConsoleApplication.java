package org.tao.simplawebcrawler.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SpringBootConsoleApplication
        implements CommandLineRunner {

    @Autowired
    WebCrawler crawler;

    private static Logger LOG = LoggerFactory
            .getLogger(SpringBootConsoleApplication.class);

    public static void main(String[] args) {
        LOG.info("STARTING APPLICATION ...");
        SpringApplication.run(SpringBootConsoleApplication.class, args);
        LOG.info("... APPLICATION FINISHED");
    }

    @Override
    public void run(String... args) {
        LOG.info("EXECUTING : command line runner");

        if (args == null || args.length < 1) {
            LOG.error("Wrong format:\nPlease run the application as following: java SpringBootConsoleApplication [webpage to crawle]");
            System.exit(1);
        }

        try {
            crawler.crawle(args[0], ".");
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
