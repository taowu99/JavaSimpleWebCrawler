package org.tao.simplewebcrawler.util;

import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.tao.simplewebcrawler.exception.CrawleException;
import org.tao.simplewebcrawler.main.SpringBootConsoleApplication;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;

@Service
public class SimpleWebCrawler implements WebCrawler{
    private static Logger LOG = LoggerFactory.getLogger(SimpleWebCrawler.class);

    @Autowired
    HTMLLinksParser linksParser;

    @Override
    public Map<String, Set<String>> crawl(String webpage, String rootStorepath) throws CrawleException {
        createPath(rootStorepath);
        try {
            final URL url = new URL(webpage);

            Map<String, Set<String>> visited = new HashMap<>();
            return crawlLocal(url, visited);
        }
        catch (MalformedURLException e) {
            e.printStackTrace();
            throw new CrawleException(e.getMessage());
        }
    }

    protected Map<String, Set<String>> crawlLocal(URL wbp, Map<String, Set<String>> visited) {
//        visited.put(webpage.toString(), null);
//        Set<String> refs = null;
        try {
            Queue<String> urls = new LinkedList<>();
            urls.add(wbp.toString());
            while (!urls.isEmpty()) {
                URL webpage = new URL(urls.poll());
//            final URL url = new URL(webpage);
                String content = downloadWebPage(webpage.toString());
                Set<String> refs = linksParser.parse(content);
                for (String ref : refs) {
                    if (ref.startsWith("http") && !ref.startsWith(webpage.getHost()))
                        continue;
                    String link = new URL(wbp, ref).toString();
                    if (!visited.containsKey(link))
                        urls.add(link);
                }
                visited.put(webpage.toString(), refs);
                System.out.println(refs);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            return visited;
        }
    }

    protected String downloadWebPage(String webpage) {
        final StringBuilder res = new StringBuilder();
        try {
            final BufferedReader br = new BufferedReader(
                    new InputStreamReader(
                            new URL(webpage).openStream()
                    ));

            do {
                String line = br.readLine();
                if (line == null)
                    break;
                res.append(line);
            } while (true);
        }
        catch (Exception e) {
            LOG.error(e.getMessage());
        }
        finally {
            return res.toString();
        }
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
