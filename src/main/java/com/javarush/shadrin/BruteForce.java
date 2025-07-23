package com.javarush.shadrin;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Класс дешифровки(метод грубой силы) с использованием линейного алгоритма, частнотного анализа
 */
public class BruteForce {
    private final Cipher cipher;
    private final Map<Character, Integer> alphabetIndexMap;
    private final char[] alphabet;
    private List<Character> frequentChars;

    public BruteForce(String referenceTextPath) throws IOException {
        this.cipher = new Cipher(new Validator());
        this.alphabet = Cipher.ALPHABET;
        this.alphabetIndexMap = createAlphabetIndexMap();
        this.frequentChars = analyzeCharInFile(referenceTextPath);
    }

    /**
     * Создает карту символов алфавита с их индексами
     * @return Map где ключ - символ, значение - его индекс в алфавите
     */
    private Map<Character, Integer> createAlphabetIndexMap() {
        Map<Character, Integer> map = new HashMap<>();
        for (int i = 0; i < alphabet.length; i++) {
            map.put(alphabet[i], i);
        }
        return map;
    }

    /**
     * Находит наиболее вероятный ключ шифрования
     * @param encryptedText    зашифрованный текст
     * @param useFrequentChars true - использовать частотный анализ, false - искать пробелы
     * @return найденный ключ
     */
    public int findBestKey(String encryptedText, boolean useFrequentChars) {
        int bestKey = 0;
        double bestScore = -1;

        for (int key = 0; key < alphabet.length; key++) {
            double score = useFrequentChars
                    ? countFrequentCharsWithKey(encryptedText, key)
                    : countSpacesWithKey(encryptedText, key);

            if (score > bestScore) {
                bestScore = score;
                bestKey = key;
            }
        }
        return bestKey;
    }

    /**
     * Подсчитывает количество пробелов при расшифровке с заданным ключом
     * @return количество найденных пробелов
     */
    private double countSpacesWithKey(String text, int key) {
        int spaceCount = 0;
        for (char c : text.toCharArray()) {
            if (alphabetIndexMap.containsKey(c)) {
                int index = (alphabetIndexMap.get(c) - key + alphabet.length) % alphabet.length;
                if (alphabet[index] == ' ') {
                    spaceCount++;
                }
            }
        }
        return spaceCount;
    }

    /**
     * Подсчитывает частоту встречаемости эталонных символов
     * @return отношение совпадений к общему числу символов
     */
    private double countFrequentCharsWithKey(String text, int key) {
        int matches = 0;
        int totalLetters = 0;

        for (char c : text.toCharArray()) {
            if (alphabetIndexMap.containsKey(c)) {
                int index = (alphabetIndexMap.get(c) - key + alphabet.length) % alphabet.length;
                char decryptedChar = alphabet[index];

                if (frequentChars.contains(decryptedChar)) {
                    matches++;
                }
                totalLetters++;
            }
        }
        return totalLetters > 0 ? (double) matches / totalLetters : 0;
    }

    /**
     * метод для анализа эталонного текста и возврат отсортированного списка символов
     * по частоте (от самых частых к самым редким)
     */
    public List<Character> analyzeCharInFile(String filePath) throws IOException {
        Path path = Paths.get(filePath);
        if (!Files.exists(path)) {
            throw new IOException("Файл эталонного текста не найден: " + path.toAbsolutePath());
        }

        Map<Character, Integer> frequencyMap = new HashMap<>();
        String content = Files.readString(path);

        for (char c : content.toCharArray()) {
            if (alphabetIndexMap.containsKey(c)) {
                frequencyMap.put(c, frequencyMap.getOrDefault(c, 0) + 1);
            }
        }

        return frequencyMap.entrySet().stream()
                .sorted(Map.Entry.<Character, Integer>comparingByValue().reversed())
                .limit(10)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }

    /**
     * Расшифровывает текст с автоматическим подбором ключа
     * @param useFrequentChars true - использовать частотный анализ, false - искать пробелы
     * @return расшифрованный текст
     */
    public String decryptWithBestKey(String encryptedText, boolean useFrequentChars) {
        int bestKey = findBestKey(encryptedText, useFrequentChars);
        return cipher.decrypt(encryptedText, bestKey);
    }
}

