package src;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.Set;
import org.tartarus.snowball.ext.PorterStemmer;

public class Pre_process {
    private final Set<String> stopwords;

    public Pre_process(String stopwordsFilePath) throws IOException {
        stopwords = new HashSet<>(Files.readAllLines(Path.of(stopwordsFilePath)));
    }

    public String removeStopwords(String content) {
        StringBuilder cleanedText = new StringBuilder();
        String[] words = content.split("\\s+");
        for (String word : words) {
            if (!stopwords.contains(word.toLowerCase())) {
                cleanedText.append(word).append(" ");
            }
        }
        return cleanedText.toString().trim();
    }

    public String applyStemming(String content) {
        StringBuilder stemmedText = new StringBuilder();
        String[] words = content.split("\\s+");
        PorterStemmer stemmer = new PorterStemmer();
        for (String word : words) {
            stemmer.setCurrent(word.toLowerCase());
            stemmer.stem();
            stemmedText.append(stemmer.getCurrent()).append(" ");
        }
        return stemmedText.toString().trim();
    }

    public String processText(String content) {
        String withoutStopwords = removeStopwords(content);
        return applyStemming(withoutStopwords);
    }
}
