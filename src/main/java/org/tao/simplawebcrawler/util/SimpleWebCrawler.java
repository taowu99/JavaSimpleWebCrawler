package org.tao.simplawebcrawler.util;

import org.springframework.stereotype.Service;

import java.io.File;

@Service
public class SimpleWebCrawler implements WebCrawler{
    @Override
    public void crawle(String webpage, String rootStorepath) throws CrawleException {
        createPath(rootStorepath);

    }

    protected void createPath(String pathname) throws CrawleException {
        File folder = new File(pathname);
        try {
            if (!folder.exists())
                folder.mkdir();
        } catch (Exception e) {
            e.printStackTrace();
            throw new CrawleException(e.getMessage());
        } finally {
            if (!folder.exists() || !folder.isDirectory())
                throw new CrawleException("failed to create " + pathname);
        }
    }
}
