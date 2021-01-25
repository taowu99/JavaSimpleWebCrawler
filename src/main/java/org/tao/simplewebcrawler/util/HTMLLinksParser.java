package org.tao.simplewebcrawler.util;

import org.jsoup.nodes.Element;

import java.util.Set;

public interface HTMLLinksParser {
    Set<Element> parse(String htmlContent);
}
