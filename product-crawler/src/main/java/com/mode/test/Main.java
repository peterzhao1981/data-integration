package com.mode.test;

import java.util.Random;

public class Main {

    public static int randomArr(int[] arr) {
        int value;
        Random intgerRandom = new Random();
        value = arr[intgerRandom.nextInt(arr.length - 1)];
        return value;
    }

    public static void main(String[] args) {
        int[] arr = { 1, 2, 3, 4, 5, 60, 78, 999 };
        int value = randomArr(arr);
        System.out.printf("�۰���ң�Ԫ��ֵΪ��%d������λ��Ϊ��%d%n", value,
                SearchUtils.binarySearch(arr, value));
        System.out.printf("������ң�Ԫ��ֵΪ��%d������λ��Ϊ��%d", value,
                SearchUtils.insertSearch(arr, value));
    }

}

class SearchUtils {
    // ���ֲ��ң��ݹ�ʵ�֣�=ʱ�临�Ӷȡ�n��
    public static int binarySearch(int[] array, int value) {
        int low = 0;
        int high = array.length - 1;
        return binarySearch(array, low, high, value);
    }

    public static int binarySearch(int[] array, int low, int high, int value) {
        int mid = low + (high - low) / 2;
        if (mid >= array.length) {
            return -1;
        } else if (low > high) {
            return -1;
        } else if (array[mid] == value) {
            return mid;
        }

        if (array[mid] > value) {
            return binarySearch(array, low, mid - 1, value);
        } else {
            return binarySearch(array, mid + 1, high, value);
        }
    }

    // 插入法查找
    public static int insertSearch(int[] arr, int value) {
        int low = 0;
        int high = arr.length - 1;
        int mid = 0;
        while (low < high) {
            mid = low + (high - low) * (value - arr[low]) / (arr[high] - arr[low]);
            if (mid >= arr.length) {
                return -1;
            } else if (arr[mid] == value) {
                return mid;
            } else if (arr[mid] < value) {
                low++;
            } else {
                high--;
            }
        }
        return -1;
    }
}
