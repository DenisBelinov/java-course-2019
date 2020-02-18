package bg.sofia.uni.fmi.mjt.authroship.detection;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class AuthorshipDetectorImpl implements AuthorshipDetector {
    private Map<String, LinguisticSignature> signatures = new HashMap<>();
    private Map<FeatureType, Double> weights;

    public Map<String, LinguisticSignature> getSignatures() {
        return signatures;
    }

    public Map<FeatureType, Double> getWeights() {
        return weights;
    }

    public AuthorshipDetectorImpl(InputStream signaturesDataset, double[] weights) {
        FeatureType[] types = FeatureType.values();
        this.weights = IntStream.range(0, weights.length).boxed()
                .collect(Collectors.toMap(i -> types[i], i -> weights[i]));

        try (BufferedReader br = new BufferedReader(new InputStreamReader(signaturesDataset))) {
            while (br.ready()) {
                String line = br.readLine();

                // get the values
                String[] values = line.split(", ");

                if (values.length != FeatureType.values().length + 1) {
                    // we have a faulty line, skip it
                    continue;
                }

                // generate the features map
                Map<FeatureType, Double> features;
                features = IntStream.range(0, types.length).boxed()
                        .collect(Collectors.toMap(i -> types[i], i -> Double.parseDouble(values[i + 1])));

                // store the signature
                signatures.put(values[0], new LinguisticSignature(features));
            }
        } catch (IOException e) {
            throw new RuntimeException("Error while reading the signature Dataset.");
        }
    }

    @Override
    public LinguisticSignature calculateSignature(InputStream mysteryText) {
        // setup
        Map<String, Integer> wordsMap = new HashMap<>();
        int sentenceCount = 0;
        int phrasesCount = 0;

        try (Scanner scanner = new Scanner(new BufferedReader(new InputStreamReader(mysteryText)))) {
            scanner.useDelimiter("[.?!]");

            while (scanner.hasNext()) {
                String sentence = scanner.next();

                // avoid thinking of a better regex with this if
                if (sentence.replaceAll("\\s+", "").length() == 0) {
                    continue;
                }

                sentenceCount++;

                TextProcessor.processWordsInSentence(sentence, wordsMap);
                phrasesCount += TextProcessor.getPhrasesCountInSentence(sentence);
            }
        }

        Map<FeatureType, Double> features = getFeaturesMap(wordsMap, sentenceCount, phrasesCount);

        return new LinguisticSignature(features);
    }

    @Override
    public double calculateSimilarity(LinguisticSignature firstSignature, LinguisticSignature secondSignature) {
        Map<FeatureType, Double> firstFeatures = firstSignature.getFeatures();
        Map<FeatureType, Double> secondFeatures = secondSignature.getFeatures();

        FeatureType[] types = FeatureType.values();

        return Arrays.stream(types).map(t -> (firstFeatures.get(t) - secondFeatures.get(t)) * weights.get(t))
                                   .map(Math::abs)
                                   .mapToDouble(a -> a)
                                   .sum();
    }

    @Override
    public String findAuthor(InputStream mysteryText) {
        LinguisticSignature testSignature = calculateSignature(mysteryText);

        // This print is for testing purpouses. It souts the Signatures sorted by similarity with the given text
//        signatures.entrySet()
//                .stream().collect(Collectors.toList()).stream()
//                .sorted((s1, s2) -> { Double sim1 = calculateSimilarity(s1.getValue(), testSignature);
//                                      Double sim2 = calculateSimilarity(s2.getValue(), testSignature);
//                                      return sim1.compareTo(sim2); })
//                .forEachOrdered(s -> System.out.println(s.getKey() +
//                        " -> " +
//                        calculateSimilarity(s.getValue(), testSignature)));

        return signatures.entrySet()
                .stream()
                .min((s1, s2) -> compareSimilarityWithSignature(s1.getValue(), s2.getValue(), testSignature))
                .get()
                .getKey();
    }

    /**
     * Generates a map with features that can be later passed to LinguisticSignature constructor
     * @param wordsMap a map containing word : occurrenceInText
     */
    private Map<FeatureType, Double> getFeaturesMap(Map<String, Integer> wordsMap,
                                                    int sentenceCount,
                                                    int phrasesCount) {

        Map<FeatureType, Double> features = new HashMap<>();

        features.put(FeatureType.AVERAGE_WORD_LENGTH, TextProcessor.getAverageWordLength(wordsMap));
        features.put(FeatureType.TYPE_TOKEN_RATIO, TextProcessor.getTypeTokenRatio(wordsMap));
        features.put(FeatureType.HAPAX_LEGOMENA_RATIO, TextProcessor.getHapaxLegomenaRatio(wordsMap));
        features.put(FeatureType.AVERAGE_SENTENCE_LENGTH,
                     TextProcessor.getAverateSentenceLength(wordsMap, sentenceCount));
        features.put(FeatureType.AVERAGE_SENTENCE_COMPLEXITY, (double) phrasesCount / sentenceCount);

        return features;
    }

    private int compareSimilarityWithSignature(LinguisticSignature s1,
                                               LinguisticSignature s2,
                                               LinguisticSignature compareToSignature) {

        Double sim1 = calculateSimilarity(s1, compareToSignature);
        Double sim2 = calculateSimilarity(s2, compareToSignature);

        return sim1.compareTo(sim2);
    }
}
