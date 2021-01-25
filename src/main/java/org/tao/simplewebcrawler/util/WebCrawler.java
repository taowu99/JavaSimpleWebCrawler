package org.tao.simplewebcrawler.util;

import org.tao.simplewebcrawler.exception.CrawleException;

import java.util.Map;
import java.util.Set;

public interface WebCrawler {
    Map<String, Set<String>> crawl(String webpage, String rootStorepath) throws CrawleException;
}
