package com.mode.test;

import java.util.Scanner;

public class MainRGB {

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        while (in.hasNext()) {
            String str = in.nextLine();
            char[] charStr = str.toCharArray();
            covertRGB(charStr);
        }
    }

    public static void covertRGB(char[] charStr) {
        int len = charStr.length;
        quickSort(charStr, 0, len - 1);
        for (int i = len - 1; i >= 0; i--) {
            System.out.print(charStr[i]);
        }
    }

    public static void quickSort(char[] charStr) {
        if (charStr.length > 0) {
            quickSort(charStr, 0, charStr.length - 1);
        }
    }

    private static void quickSort(char[] charStr, int low, int high) {

        if (low > high) {
            return;
        }
        int i = low;
        int j = high;
        int label = charStr[low];
        while (i < j) {
            while (i < j && charStr[j] > label) {
                j--;
            }
            while (i < j && charStr[i] <= label) {
                i++;
            }
            if (i < j) {
                char p = charStr[i];
                charStr[i] = charStr[j];
                charStr[j] = p;
            }
        }
        char p = charStr[i];
        charStr[i] = charStr[low];
        charStr[low] = p;
        quickSort(charStr, low, i - 1);
        quickSort(charStr, i + 1, high);
    }

}
