package org.tao.simplewebcrawler.util;

import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.tao.simplewebcrawler.exception.CrawleException;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.HashSet;
import java.util.Set;

@Service
public class SimpleWebCrawler implements WebCrawler{
    @Autowired
    HTMLLinksParser linksParser;

    @Override
    public void crawl(String webpage, String rootStorepath) throws CrawleException {
        createPath(rootStorepath);
        Set<String> visited = new HashSet<>();
        crawlLocal(webpage, visited, rootStorepath);
    }

    protected Set crawlLocal(String webpage, Set<String> visited, String targetFolder) {
        visited.add(webpage);
        Set<String> refs = null;
        try {
            final URL url = new URL(webpage);
            final String name = FilenameUtils.getName(url.getPath());
            String content = downloadWebPage(webpage);
            refs = linksParser.parse(content);
            System.out.println(refs);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            return refs;
        }
    }

    protected String downloadWebPage(String webpage) throws Exception {
        final BufferedReader br = new BufferedReader(
                new InputStreamReader(
                        new URL(webpage).openStream()
                ));
        final StringBuilder res = new StringBuilder();
        do {
            String line = br.readLine();
            if (line == null)
                break;
            res.append(line);
        } while (true);

        return res.toString();
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
