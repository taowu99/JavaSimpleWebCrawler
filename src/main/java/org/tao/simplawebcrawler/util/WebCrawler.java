package org.tao.simplawebcrawler.util;

import java.nio.file.Path;

public interface WebCrawler {
    void crawle(String webpage, String rootStorepath) throws CrawleException;
}
