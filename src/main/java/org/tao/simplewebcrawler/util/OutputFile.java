package org.tao.simplewebcrawler.util;

import java.io.IOException;
import java.util.Map;
import java.util.Set;

public interface OutputFile {
    OutputFile addContent(Map<String, Set<String>> content);
    OutputFile save(String outputFile) throws IOException;
}
