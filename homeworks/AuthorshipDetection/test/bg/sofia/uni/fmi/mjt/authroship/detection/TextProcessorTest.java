package bg.sofia.uni.fmi.mjt.authroship.detection;

import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

public final class TextProcessorTest {
    private static final double TEST_DELTA = 0.1;

    private Map<String, Integer> wordsMap = new HashMap<>();

    @Before
    public void setUp() {
        final int word1Count = 1;
        final int word2Count = 2;
        final int word3Count = 3;
        final int word4Count = 4;

        wordsMap.put("a", word1Count);
        wordsMap.put("ab", word2Count);
        wordsMap.put("abc", word3Count);
        wordsMap.put("abcd", word4Count);
    }

    @Test
    public void testGetPhrasesCountInSentence() {
        String sentence = "This \n" +
                "\n" +
                "last bit :::::) is also a sentence, but \n" +
                "without a terminator other than the end of the file";
        int count = TextProcessor.getPhrasesCountInSentence(sentence);

        final int expectedCount = 3;
        assertEquals(expectedCount, count);
    }

    @Test
    public void testProcessWordsInSentence() {
        String sentence = "This \n this. That? \n that : foo (bar). FOO BAR!";
        Map<String, Integer> wordsMap = new HashMap<>();

        Map<String, Integer> expectedMap = new HashMap<>();
        expectedMap.put("that", 2);
        expectedMap.put("this", 2);
        expectedMap.put("foo", 2);
        expectedMap.put("bar", 2);

        TextProcessor.processWordsInSentence(sentence, wordsMap);

        assertEquals(expectedMap, wordsMap);
    }

    @Test
    public void testGetAverageWordLength() {
        double avg = TextProcessor.getAverageWordLength(wordsMap);

        final double expected = 3;
        assertEquals(expected, avg, TEST_DELTA);
    }

    @Test
    public void testGetTypeTokenRatio() {
        double ratio = TextProcessor.getTypeTokenRatio(wordsMap);

        final double expected = 0.4;
        assertEquals(expected, ratio, TEST_DELTA);
    }

    @Test
    public void testGetHapaxLegomenaRatio() {
        double ratio = TextProcessor.getHapaxLegomenaRatio(wordsMap);

        final double expected = 0.1;
        assertEquals(expected, ratio, TEST_DELTA);
    }

    @Test
    public void testGetAverateSentenceLength() {
        final int sentenceCount = 4;
        double avg = TextProcessor.getAverateSentenceLength(wordsMap, sentenceCount);

        final double expected = 2.5;
        assertEquals(expected, avg, TEST_DELTA);
    }
}