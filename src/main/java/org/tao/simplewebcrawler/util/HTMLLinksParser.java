package org.tao.simplewebcrawler.util;

import java.util.Set;

public interface HTMLLinksParser {
    Set<String> parse(String htmlContent);
}
