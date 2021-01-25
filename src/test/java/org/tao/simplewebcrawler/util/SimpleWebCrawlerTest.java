package org.tao.simplewebcrawler.util;

import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.util.UUID;

public class SimpleWebCrawlerTest {
    @Test
    public void testCreatePath() {
        SimpleWebCrawler crawler = new SimpleWebCrawler();
        String randomPath = UUID.randomUUID().toString();
        try {
            File path = new File("./" + randomPath);
            crawler.createFile(path.getPath());
            Assert.assertTrue(path.exists() && path.isDirectory());
            path.delete();
        }
        catch (Exception e) {
            e.printStackTrace();
            Assert.assertTrue(false);
        }
    }
}
