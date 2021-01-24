package org.tao.simplewebcrawler.util;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class SimpleHTMLLinksParser implements HTMLLinksParser{
    @Override
    public Set<String> parse(String htmlContent) {
        return parseHTMLRefs(htmlContent);
    }

    private Set<String> parseHTMLRefs(String content) {
        final Set<String> refs = new HashSet<>();
        final Document elements = Jsoup.parse(content);
//        int cnt =0;
//        elements.getAllElements().stream().forEach(e -> System.out.println("*****" + e.attr("href")));
//        elements.select("[src]").forEach(e -> System.out.println(e.toString()));
//        elements.select("link[href]").forEach(e -> System.out.println(e.toString()));
        elements.getAllElements().stream()
                .filter(s -> !s.attr("href").isEmpty() || !s.attr("src").isEmpty())
                .forEach(s-> {
                    if (!s.attr("href").isEmpty())
                        refs.add(s.attr("href"));
                    if (!s.attr("src").isEmpty())
                        refs.add(s.attr("src"));
                });
//        System.out.println(elements.getAllElements().size());
        return refs;
    }
}
