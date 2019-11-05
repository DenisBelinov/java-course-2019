package fmi.java.course;

import java.util.Arrays;

public class WordAnalyzer {
    public static String getSharedLetters(String word1, String word2) {
        word1 = word1.toLowerCase();
        word2 = word2.toLowerCase();

        String common = "";

        for (char c : word1.toCharArray()) {
            if (!common.contains(Character.toString(c)) && word2.contains(Character.toString(c))) {
                common += c;
            }
        }

        // do this just so I stick to the information from the lecture as much as possible
        char[] commonArr = common.toCharArray();
        Arrays.sort(commonArr);

        common = new String(commonArr);

        return common;
    }
}
