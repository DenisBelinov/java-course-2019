package bg.sofia.uni.fmi.mjt.authroship.detection;

import java.util.Map;
import java.util.Scanner;

/**
 * This is class containing utility functions needed for processing Sentences and Words.
 */
public class TextProcessor {
    /**
     * Updates the words map with the words in the @sentence
     *
     * @param sentence
     */
    public static void processWordsInSentence(String sentence, Map<String, Integer> wordsMap) {
        String sanitized = sentence.replaceAll("\\s+", " ");

        Scanner scanner = new Scanner(sanitized);
        while (scanner.hasNext()) {
            String sanitizedWord = cleanUp(scanner.next());

            if (sanitizedWord.length() != 0) {
                wordsMap.putIfAbsent(sanitizedWord, 0);
                wordsMap.put(sanitizedWord, wordsMap.get(sanitizedWord) + 1);
            }
        }
    }

    public static int getPhrasesCountInSentence(String sentence) {
        // failfast on no phrases
        if (!sentence.contains(",") && !sentence.contains(":") && !sentence.contains(";")) {
            return 0;
        }

        String sanitized = sentence.replaceAll("\\s+", " ");
        int phrasesCount = 0;
        Scanner scanner = new Scanner(sanitized);
        scanner.useDelimiter("[,:;]");

        while (scanner.hasNext()) {
            String phrase = scanner.next();
            if (phrase.length() != 0) {
                phrasesCount++;
            }
        }

        return phrasesCount;
    }

    public static String cleanUp(String word) {
        return word.toLowerCase()
                .replaceAll( "^[_!.,:;\\-?<>#\\*\'\"\\[\\(\\]\\)\\n\\t\\\\]" +
                                    "+|[_!.,:;\\-?<>#\\*\'\"\\[\\(\\]\\)\\n\\t\\\\]+$",
                        "");
    }

    public static Double getAverageWordLength(Map<String, Integer> wordsMap) {
        int lengthSum = wordsMap.entrySet().stream()
                                           .map(e -> e.getKey().length() * e.getValue())
                                           .reduce(0, Integer::sum);

        return (double) lengthSum / getWordsCount(wordsMap);
    }

    public static Double getTypeTokenRatio(Map<String, Integer> wordsMap) {
        int wordsCount = getWordsCount(wordsMap);

        return (double) wordsMap.size() / wordsCount;
    }

    public static Double getHapaxLegomenaRatio(Map<String, Integer> wordsMap) {
        int wordsCount = getWordsCount(wordsMap);

        long wordsOnlyOnceCount = wordsMap.entrySet().stream().filter(a -> a.getValue() == 1).count();

        return (double) wordsOnlyOnceCount / wordsCount;
    }

    public static Double getAverateSentenceLength(Map<String, Integer> wordsMap, int sentenceCount) {
        int wordsCount = getWordsCount(wordsMap);

        return (double) wordsCount / sentenceCount;
    }

    private static int getWordsCount(Map<String, Integer> wordsMap) {
        return wordsMap.values().stream().mapToInt(a -> a).sum();
    }
}
