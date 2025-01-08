# Projet de Moteur de Recherche 🚀

Bienvenue dans le **Projet de Moteur de Recherche** ! Ce projet vise à créer un moteur de recherche textuel simple mais puissant en Java. Le moteur traite un corpus de documents texte, calcule leurs valeurs TF-IDF et permet aux utilisateurs d'effectuer des requêtes de recherche basées sur la similarité.

---

## **Structure du Projet** 📂

```
Répertoire du Projet
|
├── Text/                   # Dossier du corpus contenant les documents texte
├── Stopwords_fr.txt        # Fichier contenant les mots vides en français
└── src/                    # Répertoire des codes sources
    ├── Corpus_Reader.java  # Lit et charge les documents du corpus
    ├── Pre_process.java    # Nettoie et prétraite le texte (suppression des mots vides, stemming)
    ├── Indexer.java        # Construit l'index TF-IDF
    ├── CalculateSimilarity.java  # Calcule la similarité cosinus
    ├── Main.java           # Point d'entrée de l'application
```

---

## **Classes et leurs Rôles** 🛠️

### 1. **Corpus_Reader** 📄
Responsable de la lecture et du chargement des documents texte depuis le répertoire du corpus.

- **`readCorpus(String corpusPath)`**
    - Charge les fichiers `.txt` du dossier spécifié et renvoie une map contenant les noms et contenus des fichiers.

---

### 2. **Pre_process** 🛠️
Gère le prétraitement du texte.

- **Constructeur** : Charge les mots vides depuis un fichier.
- **`removeStopwords(String content)`** : Supprime les mots vides d'un texte.
- **`applyStemming(String content)`** : Réduit les mots à leur forme racine.
- **`processText(String content)`** : Combine les étapes précédentes.

---

### 3. **Indexer** 📊
Crée l'index TF-IDF pour tous les documents.

- **`calculateTF(String[] terms)`** : Calcule la fréquence des termes dans un document.
- **`calculateIDF(Map<String, String> documents)`** : Calcule l'importance globale des termes dans le corpus.
- **`computeTFIDF(Map<String, String> documents)`** : Calcule les valeurs TF-IDF pour chaque terme et document.

---

### 4. **CalculateSimilarity** 🔍
Mesure la similarité entre la requête et les documents.

- **`computeCosineSimilarity(Map<String, Double> queryVector, Map<String, Double> docVector)`** : Calcule la similarité cosinus entre un vecteur de requête et un vecteur de document.
- **`computeSimilarities(Map<String, Double> queryVector, Map<String, Map<String, Double>> index)`** : Renvoie les scores de similarité pour tous les documents.

---

### 5. **Main** 🚦
Le point d'entrée du programme.

- Charge le corpus.
- Prétraite le contenu des documents.
- Construit l'index TF-IDF.
- Accepte une requête utilisateur.
- Calcule les similarités et affiche les documents les plus pertinents.

---

## **Comment Utiliser** 🧑‍💻

1. Placez vos fichiers `.txt` dans le dossier `Text/`.
2. Créez un fichier nommé `Stopwords_fr.txt` contenant une liste de mots vides en français, un par ligne.
3. Compilez le code source :
   ```bash
   javac src/*.java
   ```
4. Lancez l'application :
   ```bash
   java src.Main
   ```
5. Entrez votre requête de recherche lorsqu'on vous le demande et visualisez les résultats.

---

## **Fonctionnalités** ✨
- **Lecture du Corpus** : Charge les documents texte depuis un répertoire.
- **Prétraitement du Texte** : Supprime les mots vides et applique le stemming.
- **Indexation TF-IDF** : Pondère les termes pour une recherche efficace.
- **Similarité Cosinus** : Compare la requête aux documents en utilisant une mesure mathématique.

---

## **Exemple d'Exécution** 📝
### Corpus Exemple
- `Text/doc1.txt` : "Le chat noir dort."
- `Text/doc2.txt` : "Le chien noir court."
- `Text/doc3.txt` : "Le chat et le chien jouent ensemble."

### Requête
Entrée : "chat noir"

### Résultat
```plaintext
Résultats de recherche :
Document: doc1.txt            Similarité: 0.876543
Document: doc3.txt            Similarité: 0.432198
Document: doc2.txt            Similarité: 0.000000
```

---



## **Remerciements** 🙌
Ce projet a été rendu possible grâce à la détermination et aux efforts de son développeur. Continuez à explorer et à améliorer les technologies de recherche ! 🚀

