package src;

import java.util.*;

public class Indexer {
    // Méthode pour calculer le TF-IDF
    public static Map<String, Map<String, Double>> computeTFIDF(Map<String, String> documents) {
        Map<String, Map<String, Double>> tfidfIndex = new HashMap<>();
        Map<String, Double> idfMap = calculateIDF(documents);

        for (Map.Entry<String, String> entry : documents.entrySet()) {
            String docName = entry.getKey();
            String[] terms = entry.getValue().split("\\s+");
            Map<String, Double> tfMap = calculateTF(terms);
            Map<String, Double> tfidfMap = new HashMap<>();

            for (String term : tfMap.keySet()) {
                double tfidf = tfMap.get(term) * idfMap.getOrDefault(term, 0.0);
                tfidfMap.put(term, tfidf);
            }

            tfidfIndex.put(docName, tfidfMap);
        }

        return tfidfIndex;
    }

    // Méthode pour calculer le TF (Term Frequency)
    private static Map<String, Double> calculateTF(String[] terms) {
        Map<String, Double> tfMap = new HashMap<>();
        int totalTerms = terms.length;

        for (String term : terms) {
            tfMap.put(term, tfMap.getOrDefault(term, 0.0) + 1.0);
        }

        for (String term : tfMap.keySet()) {
            tfMap.put(term, tfMap.get(term) / totalTerms);
        }

        return tfMap;
    }

    // Méthode pour calculer l'IDF (Inverse Document Frequency)
    private static Map<String, Double> calculateIDF(Map<String, String> documents) {
        Map<String, Double> idfMap = new HashMap<>();
        int totalDocs = documents.size();

        for (String content : documents.values()) {
            Set<String> uniqueTerms = new HashSet<>(Arrays.asList(content.split("\\s+")));
            for (String term : uniqueTerms) {
                idfMap.put(term, idfMap.getOrDefault(term, 0.0) + 1.0);
            }
        }

        for (String term : idfMap.keySet()) {
            idfMap.put(term, Math.log((double) totalDocs / (1 + idfMap.get(term))));
        }

        return idfMap;
    }
}
