package src;

import java.io.IOException;
import java.util.*;

public class Main {
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
            Map<String, Double> similarityScores = computeSimilarities(queryVector, index);

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

        return queryVector;
    }

    private static Map<String, Double> computeSimilarities(Map<String, Double> queryVector, Map<String, Map<String, Double>> index) {
        Map<String, Double> similarityScores = new HashMap<>();

        for (Map.Entry<String, Map<String, Double>> docEntry : index.entrySet()) {
            String docName = docEntry.getKey();
            Map<String, Double> docVector = docEntry.getValue();

            // Calculer la similarité cosinus
            double dotProduct = 0.0;
            double queryNorm = 0.0;
            double docNorm = 0.0;

            // Impressions de débogage
            System.out.println("\nCalcul des similarités pour le document: " + docName);
            for (String term : queryVector.keySet()) {
                double queryWeight = queryVector.getOrDefault(term, 0.0);
                double docWeight = docVector.getOrDefault(term, 0.0);
                System.out.printf("Terme: %-10s Poids requête: %.6f Poids doc: %.6f%n", term, queryWeight, docWeight);

                dotProduct += queryWeight * docWeight;
                queryNorm += queryWeight * queryWeight;
                docNorm += docWeight * docWeight;
            }

            queryNorm = Math.sqrt(queryNorm);
            docNorm = Math.sqrt(docNorm);

            // Vérification des normes
            System.out.printf("Norme requête: %.6f Norme doc: %.6f%n", queryNorm, docNorm);

            if (queryNorm > 0 && docNorm > 0) {
                double similarity = dotProduct / (queryNorm * docNorm);
                System.out.printf("Similarité cosinus: %.6f%n", similarity);
                similarityScores.put(docName, similarity);
            } else {
                System.out.println("Similarité cosinus: 0.0 (une des normes est nulle)");
                similarityScores.put(docName, 0.0);
            }
        }

        return similarityScores;
    }
}