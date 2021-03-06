package org.tao.simplewebcrawler.util;

import org.jsoup.nodes.Element;
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
            new JsonOutputFile().addContent(res).save(outputFile);

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
//        visited.put(wbp.toString(), null);
//        Set<String> refs = null;
        try {
            final Queue<String> urls = new LinkedList<>();
            urls.add(wbp.toString());
            while (!urls.isEmpty()) {
                final String s = urls.poll();
                LOG.debug("url:{}", s);
                final URL webpage;
                try {
                    webpage = new URL(s);
                    visited.put(webpage.toString(), null);
                }
                catch (Exception e) {
                    LOG.error("{} error: {}", s, e.getMessage());
                    continue;
                }
                final Set<Element> refs = linksParser.parse(downloadWebPage(webpage.toString()));
                Set<String> links = new HashSet<>();
                for (Element element : refs) {
                    for (String tag : new String[]{"href", "src"}) {
                        String ref = element.attr(tag);
                        if (ref.isBlank())
                            continue;

                        links.add(ref);
                        if (ref.toLowerCase(Locale.ROOT).startsWith("http")) {
                            try {
                                URL tmp = new URL(ref);
                                if (!tmp.getHost().equals(wbp.getHost()) || tmp.getPort() != wbp.getPort())
                                    continue;
                            } catch (Exception e) {
                                // ref is not a full url, ignore error
                            }
                        }
                        if (ref.startsWith("//") && ref.indexOf(wbp.getHost()) != 2)
                            continue;

                        final URL link;
                        if (ref.toLowerCase(Locale.ROOT).startsWith("http"))
                            link = new URL(ref);
                        else if (ref.startsWith("//"))
                            link = new URL(wbp.getProtocol() + ":" +ref);
                        else
                            link = new URL(wbp.getProtocol(), wbp.getHost(), wbp.getPort(), ref);
                        if (!visited.containsKey(link.toString()) && !urls.contains(link.toString()) && tag.equals("href"))
                            urls.add(link.toString());
                    }
                }
                visited.put(webpage.toString(), links);
                LOG.debug("{}: {}", webpage.toString(), links);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            LOG.debug(visited.toString());
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
