package com.javarush.shadrin;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * реализация дешифровки статистическим анализом
 */
public class StatisticalAnalyzer {

    /**
     * метод для компиляции массива(генерация префикс-функции), вспомогательный метод,
     * который используется алгоритмом дальше
     */
    private int[] compilePatternArray(String pattern) {

        int patternLength = pattern.length();
        int[] compiledPatternArray = new int[patternLength];
        compiledPatternArray[0] = 0;//базовый случай

        int len = 0;// длина предыдущего наибольшего префикса-суффикса
        int i = 1;

        while (i < patternLength) {
            if (pattern.charAt(i) == pattern.charAt(len)) {
                len++;
                compiledPatternArray[i] = len;
                i++;
            } else {
                if (len != 0) {
                    len = compiledPatternArray[len - 1];
                } else {
                    compiledPatternArray[i] = len;
                    i++;
                }
            }
        }
        System.out.println("Скомпилированный массив шаблонов: " + Arrays.toString(compiledPatternArray));
        return compiledPatternArray;
    }

    /**
     * поиск текста по заданному шаблону, использую алгоритм Кнута–Морриса–Пратта
     * путем поиска всех вхождений поттерна в текст
     */
    public List<Integer> findMostLikelyShiftByKMP(String text, String pattern) {
        List<Integer> foundIndexes = new ArrayList<>();
        if (pattern.isEmpty()) {
            return foundIndexes;
        }

        // приводим к одному регистру чтобы исключить ошибки
        text = text.toLowerCase();
        pattern = pattern.toLowerCase();

        int[] compiledPatternArray = compilePatternArray(pattern);
        int textIndex = 0;
        int patternIndex = 0;

        while (textIndex < text.length()) {
            if (pattern.charAt(patternIndex) == text.charAt(textIndex)) {
                textIndex++;
                patternIndex++;
            }

            if (patternIndex == pattern.length()) {
                foundIndexes.add(textIndex - patternIndex);
                patternIndex = compiledPatternArray[patternIndex - 1];
            } else if (textIndex < text.length() && pattern.charAt(patternIndex) != text.charAt(textIndex)) {
                if (patternIndex != 0)
                    patternIndex = compiledPatternArray[patternIndex - 1];
                else
                    textIndex++;
            }
        }
        return foundIndexes;
    }
}
