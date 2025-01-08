package src;

import java.io.IOException;
import java.util.*;

public class Main {
    private static Map<String, Double> computeQueryTFIDF(String query, Map<String, Map<String, Double>> index) {
        Map<String, Double> queryVector = new HashMap<>();
        String[] terms = query.split("\\s+");
        Map<String, Integer> termFrequency = new HashMap<>();

        // Calculer la fréquence des termes (TF) pour la requête
        for (String term : terms) {
            termFrequency.put(term, termFrequency.getOrDefault(term, 0) + 1);
        }

        // Calculer le TF-IDF pour la requête en utilisant l'IDF de l'index
        for (String term : termFrequency.keySet()) {
            int tf = termFrequency.get(term);
            double idf = 0.0;
            for (Map<String, Double> docVector : index.values()) {
                if (docVector.containsKey(term)) {
                    idf = Math.log((double) index.size() / docVector.size()) + 1;
                    break;
                }
            }
            queryVector.put(term, tf * idf);
        }

        // Affichage du vecteur TF-IDF de la requête pour débogage
        System.out.println("Vecteur TF-IDF de la requête : " + queryVector);

        return queryVector;
    }

    public static void main(String[] args) {
        String corpusPath = "Text";
        String stopwordsFilePath = "Stopwords_fr.txt";

        try {
            // Étape 1 : Lire le corpus
            Map<String, String> documents = Corpus_Reader.readCorpus(corpusPath);

            // Étape 2 : Prétraitement (suppression des stopwords et stemming)
            Pre_process preprocessor = new Pre_process(stopwordsFilePath);
            for (Map.Entry<String, String> entry : documents.entrySet()) {
                String processedContent = preprocessor.processText(entry.getValue());
                documents.put(entry.getKey(), processedContent);
            }

            // Étape 3 : Indexer les documents
            Map<String, Map<String, Double>> index = Indexer.computeTFIDF(documents);

            System.out.println("Indexation terminée.");

            // Étape 4 : Effectuer une recherche pour une requête
            Scanner scanner = new Scanner(System.in);
            System.out.print("Entrez votre requête de recherche : ");
            String query = scanner.nextLine();

            // Prétraitement de la requête
            String processedQuery = preprocessor.processText(query);

            // Calculer le TF-IDF pour la requête
            Map<String, Double> queryVector = computeQueryTFIDF(processedQuery, index);

            // Calculer les similarités
            Map<String, Double> similarityScores = CalculateSimilarity.computeSimilarities(queryVector, index);

            // Trier et afficher les résultats
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