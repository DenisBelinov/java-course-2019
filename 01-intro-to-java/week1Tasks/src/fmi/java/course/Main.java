package fmi.java.course;

//import fmi.java.course.FractionSimplifier;
//
//
//public class Main {
//
//    public static void main(String[] args) {
//        //FractionSimplifier.simplify("25/50");
//        WordAnalyzer.getSharedLetters("sa", "asr");
//    }
//}

import java.io.*;
import java.util.*;


public class Main {
    public static int[] POSSIBLE_POINTS = { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 25 };

    public static void main(String[] args) {

        Scanner in = new Scanner(System.in);
        int target = 4;

        if (target > 160) {
            System.out.println(0);
            return;
        }

        int count = 0;
        for (int i : POSSIBLE_POINTS) {
            if (i == 0)
                count += howManyWith2Throws(target);
            else {
                count += howManyWith2Throws(target - i);
                count += howManyWith2Throws(target - i*2);
                count += howManyWith2Throws(target - i*3);
            }
        }

        System.out.println(count);
    }

    public static int howManyWith2Throws(int target) {
        if (target < 0)
            return 0;

        int count = 0;
        for (int i : POSSIBLE_POINTS) {
            if (i == 0)
                count += howManyWith1Throw(target);
            else if (target - i != 0){
                count += howManyWith1Throw(target - i);
                count += howManyWith1Throw(target - i*2);
                count += howManyWith1Throw(target - i*3);
            }
        }

        return count;
    }

    public static int howManyWith1Throw(int target) {
        if (target < 0)
            return 0;

        for (int i : POSSIBLE_POINTS) {
            if (i * 2 == target)
                return 1;
        }

        return 0;
    }
}
