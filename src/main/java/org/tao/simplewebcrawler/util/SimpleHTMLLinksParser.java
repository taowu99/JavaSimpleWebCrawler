package org.tao.simplewebcrawler.util;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.tao.simplewebcrawler.main.SpringBootConsoleApplication;

import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

@Service
public class SimpleHTMLLinksParser implements HTMLLinksParser{
    private static Logger LOG = LoggerFactory.getLogger(SimpleHTMLLinksParser.class);

    final static String[] TAGS = new String[] {"href", "src"};

    @Override
    public Set<String> parse(String htmlContent) {
        return parseHTMLRefs(htmlContent);
    }

    private Set<String> parseHTMLRefs(String content) {
        final Set<String> refs = new HashSet<>();
        try {
            final Document elements = Jsoup.parse(content);
            elements.getAllElements().stream()
                    .filter(s -> {
                        for (String tag : TAGS)
                            if (!s.attr(tag).isBlank())
                                return true;
                        return false;
                    })
                    .forEach(s -> {
                        for (String tag : TAGS) {
                            final String stag = s.attr(tag);
                            if (!stag.isBlank() && !stag.toLowerCase(Locale.ROOT).startsWith("javascript")) {
                                refs.add(stag);
//                            System.out.println(tag + "=" + stag);
                            }
                        }
                    });
        }
        catch (Exception e) {
            LOG.error("Error: "+ e.getMessage());
        }
        return refs;
    }
}
