package org.tao.simplewebcrawler.util;

import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class TextOutputFile implements OutputFile {
    final Map<String, Set<String>> content;

    public TextOutputFile() {
        content = new HashMap<>();
    }

    @Override
    public TextOutputFile addContent(Map<String, Set<String>> newContent) {
        newContent.entrySet().forEach(
                entry -> {
                    final Set<String> values = content.getOrDefault(entry.getKey(), new HashSet<String>());
                    values.addAll(entry.getValue());
                    this.content.put(entry.getKey(), values);
                });
        return this;
    }

    @Override
    public OutputFile save(String outputFile) throws IOException {
        final FileWriter writer = new FileWriter(outputFile);
        writer.write("site=[links]\n");
        for (Map.Entry<String, Set<String>> entry : this.content.entrySet())
            writer.write(entry.toString()+ "\n");

        writer.close();
        return this;
    }
}
