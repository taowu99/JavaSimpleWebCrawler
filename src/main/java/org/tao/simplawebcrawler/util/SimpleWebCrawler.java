package org.tao.simplawebcrawler.util;

import org.springframework.stereotype.Service;

import java.io.*;
import org.apache.commons.io.FilenameUtils;
import java.net.URL;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
public class SimpleWebCrawler implements WebCrawler{
    @Override
    public void crawle(String webpage, String rootStorepath) throws CrawleException {
        createPath(rootStorepath);
        Set<String> visited = new HashSet<>();
        crawleLocal(webpage, visited, rootStorepath);
    }

    protected void crawleLocal(String webpage, Set<String> visited, String targetFolder) {
        visited.add(webpage);

        try {
            final URL url = new URL(webpage);
            final String name = FilenameUtils.getName(url.getPath());
            String content = downloadWebPage(webpage);
            File fl = new File(targetFolder, Optional.ofNullable(name).filter(s -> !s.isEmpty()).orElse("default.htm"));
//            fl.createNewFile();
            FileWriter writer = new FileWriter(fl);
            writer.write(content);
            writer.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected String downloadWebPage(String webpage) throws Exception {
        URL url = new URL(webpage);
        InputStream is = url.openStream();  // throws an IOException
        BufferedReader br = new BufferedReader(new InputStreamReader(is));

        StringBuilder res = new StringBuilder();
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
