package src;


import java.util.*;

public class CalculateSimilarity {

    public static double computeCosineSimilarity(Map<String, Double> queryVector, Map<String, Double> docVector) {
        double dotProduct = 0.0;
        double queryNorm = 0.0;
        double docNorm = 0.0;

        for (String term : queryVector.keySet()) {
            double queryWeight = queryVector.getOrDefault(term, 0.0);
            double docWeight = docVector.getOrDefault(term, 0.0);

            dotProduct += queryWeight * docWeight;
            queryNorm += queryWeight * queryWeight;
            docNorm += docWeight * docWeight;
        }

        queryNorm = Math.sqrt(queryNorm);
        docNorm = Math.sqrt(docNorm);

        if (queryNorm > 0 && docNorm > 0) {
            return dotProduct / (queryNorm * docNorm);
        } else {
            return 0.0;
        }
    }

    public static Map<String, Double> computeSimilarities(Map<String, Double> queryVector, Map<String, Map<String, Double>> index) {
        Map<String, Double> similarityScores = new HashMap<>();

        // Comparer la requête à chaque document
        for (Map.Entry<String, Map<String, Double>> docEntry : index.entrySet()) {
            String docName = docEntry.getKey();
            Map<String, Double> docVector = docEntry.getValue();

            // Calcul de la similarité entre la requête et le document
            double similarity = computeCosineSimilarity(queryVector, docVector);

            // Ajouter le score de similarité pour ce document
            similarityScores.put(docName, similarity);
        }

        return similarityScores;
    }
}
