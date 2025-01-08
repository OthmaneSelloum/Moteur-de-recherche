package src;

import java.io.IOException;
import java.util.*;

public class Main {
    private static Map<String, Double> computeQueryTFIDF(String query, Map<String, Map<String, Double>> index) {
        Map<String, Double> queryVector = new HashMap<>();
        String[] terms = query.split("\\s+");
        Map<String, Integer> termFrequency = new HashMap<>();

        for (String term : terms) {
            termFrequency.put(term, termFrequency.getOrDefault(term, 0) + 1);
        }

        for (String term : termFrequency.keySet()) {
            int tf = termFrequency.get(term);
            double idf = 0.0;
            int df = 0;
            for (Map<String, Double> docVector : index.values()) {
                if (docVector.containsKey(term)) {
                    df++;
                }
            }
            idf = Math.log((double) index.size() / df) + 1;
            queryVector.put(term, tf * idf);
        }
       // System.out.println("Vecteur TF-IDF de la requête : " + queryVector);
        return queryVector;
    }

    public static void main(String[] args) {
        String corpusPath = "Text";
        String stopwordsFilePath = "Stopwords_fr.txt";
        try {
            Map<String, String> documents = Corpus_Reader.readCorpus(corpusPath);
            Pre_process preprocessor = new Pre_process(stopwordsFilePath);
            for (Map.Entry<String, String> entry : documents.entrySet()) {
                String processedContent = preprocessor.processText(entry.getValue());
                documents.put(entry.getKey(), processedContent);
            }
            Map<String, Map<String, Double>> index = Indexer.computeTFIDF(documents);
            System.out.println("Indexation terminée.");

            Scanner scanner = new Scanner(System.in);
            System.out.print("Entrez votre requête de recherche : ");
            String query = scanner.nextLine();
            String processedQuery = preprocessor.processText(query);
            Map<String, Double> queryVector = computeQueryTFIDF(processedQuery, index);

            Map<String, Double> similarityScores = CalculateSimilarity.computeSimilarities(queryVector, index);
            List<Map.Entry<String, Double>> sortedResults = new ArrayList<>(similarityScores.entrySet());
            sortedResults.sort((a, b) -> Double.compare(b.getValue(), a.getValue()));

            System.out.println("\nRésultats de recherche :");
            for (Map.Entry<String, Double> result : sortedResults) {
                System.out.printf("Document: %-20s Similarité: %.6f%n", result.getKey(), result.getValue());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}