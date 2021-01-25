package org.tao.simplewebcrawler.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.tao.simplewebcrawler.exception.CrawleException;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;

@Service
public class SimpleWebCrawler implements WebCrawler{
    private static Logger LOG = LoggerFactory.getLogger(SimpleWebCrawler.class);

    @Autowired
    HTMLLinksParser linksParser;

    @Override
    public Map<String, Set<String>> crawl(String webpage, String outputFile) throws CrawleException {
        try {
            final Map<String, Set<String>> res = crawlLocal(new URL(webpage), new HashMap<>());
            final FileWriter writer = new FileWriter(outputFile);
            writer.write("site=[links]\n");
            for (Map.Entry<String, Set<String>> entry : res.entrySet())
                writer.write(entry.toString()+ "\n");
            
            writer.close();
            return res;
        }
        catch (MalformedURLException e) {
            e.printStackTrace();
            throw new CrawleException(e.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    protected Map<String, Set<String>> crawlLocal(URL wbp, Map<String, Set<String>> visited) {
//        visited.put(webpage.toString(), null);
//        Set<String> refs = null;
        try {
            final Queue<String> urls = new LinkedList<>();
            urls.add(wbp.toString());
            while (!urls.isEmpty()) {
                final URL webpage = new URL(urls.poll());
                final Set<String> refs = linksParser.parse(downloadWebPage(webpage.toString()));
                for (String ref : refs) {
                    if (ref.startsWith("http") && !ref.startsWith(webpage.getHost()))
                        continue;
                    final String link = new URL(wbp, ref).toString();
                    if (!visited.containsKey(link))
                        urls.add(link);
                }
                visited.put(webpage.toString(), refs);
//                System.out.println(refs);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            LOG.info(visited.toString());
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

    protected void createFile(String pathname) throws CrawleException {
        File folder = new File(pathname);
        try {
            if (!folder.exists())
                folder.createNewFile();
        } catch (Exception e) {
            e.printStackTrace();
            throw new CrawleException(e.getMessage());
        } finally {
            if (!folder.exists() || !folder.isDirectory())
                throw new CrawleException("failed to create " + pathname);
        }
    }
}
