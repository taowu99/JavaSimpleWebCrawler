package org.tao.simplewebcrawler.main;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.tao.simplewebcrawler.util.WebCrawler;

import java.util.Calendar;

@SpringBootApplication
@ComponentScan(basePackages = "org.tao.simplewebcrawler.util")
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

        if (args == null || args.length < 2) {
            LOG.error("Wrong syntax format:\nPlease run the application as following: java SpringBootConsoleApplication [webpage to crawle] [output file]");
            System.exit(1);
        }

        try {
            crawler.crawl(args[0], args[1]);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
