package src;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;

public class Corpus_Reader {
    public static Map<String, String> readCorpus(String corpusPath) throws IOException {
        Map<String, String> documents = new HashMap<>();
        File folder = new File(corpusPath);

        if (!folder.exists() || !folder.isDirectory()) {
            throw new IllegalArgumentException("Invalid corpus path");
        }

        // Parcours des fichiers dans le dossier racine
        for (File file : folder.listFiles()) {
            if (file.isFile() && file.getName().endsWith(".txt")) { // Vérifier l'extension .txt
                String content = Files.readString(file.toPath());
                documents.put(file.getName(), content); // Ajoute le nom du fichier et son contenu
            }
        }

        return documents;
    }
}
