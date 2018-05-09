package com.mode.test;

import java.util.Scanner;

/*
 * 判断A是否包含B
 */
public class Main1 {

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        while (in.hasNext()) {
            String[] orign = in.nextLine().split(";");
            String a = orign[0];
            String b = orign[1];
            char[] char1 = a.toCharArray();
            char[] char2 = b.toCharArray();
            System.out.println(compareStrings(char1, char2));
        }
    }

    public static boolean compareStrings(char[] char1, char[] char2) {
        int count = 0;
        for (int i = 0; i < char2.length; i++) {
            mark: for (int j = 0; j < char1.length; j++) {
                if (char2[i] == char1[j]) {
                    count++;
                    for (int k = j; k < char1.length - 1; k++) {
                        char1[k] = char1[k + 1];
                    }
                    char1[char1.length - 1] = '0';
                    break mark;
                }
            }
        }
        if (count == char2.length) {
            return true;
        } else {
            return false;
        }
    }
}
