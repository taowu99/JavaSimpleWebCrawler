package org.tao.simplewebcrawler.util;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class JsonOutputFile implements OutputFile {
    final Map<String, Set<String>> content;

    public JsonOutputFile() {
        content = new HashMap<>();
    }

    @Override
    public JsonOutputFile addContent(Map<String, Set<String>> newContent) {
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

        Gson gsonObj = new Gson();
        Type gsonTypeObj = new TypeToken<>(){}.getType();

        String jsonString = gsonObj.toJson(this.content, gsonTypeObj);
        writer.write(jsonString);
        writer.close();
        return this;
    }

    public OutputFile save1(String outputFile) throws IOException {
        final FileWriter writer = new FileWriter(outputFile);
        writer.write("{\n");
        for (Map.Entry<String, Set<String>> entry : this.content.entrySet()) {
            writer.write("{\n");
            writer.write("\"Site\": \""+entry.getKey() + "\"\n");
            writer.write("\"Links\" [: ");
            entry.getValue().forEach( v -> {
                try {
                    writer.write("\""+v+"\",");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            writer.write( "]\n");
            writer.write("},\n");
        }
        writer.write("}");
        writer.close();
        return this;
    }
}
