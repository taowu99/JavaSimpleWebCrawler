package org.tao.simplewebcrawler.util;

import org.tao.simplewebcrawler.exception.CrawleException;

public interface WebCrawler {
    void crawl(String webpage, String rootStorepath) throws CrawleException;
}
