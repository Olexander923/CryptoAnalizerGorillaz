package com.javarush.shadrin;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * реализация шифровки, дешифровки, валидации
 */
public class Cipher {

    public static final char[] ALPHABET = {'а', 'б', 'в', 'г', 'д', 'е', 'ж', 'з', 'и', 'й', 'к', 'л', 'м', 'н', 'о', 'п',
            'р', 'с', 'т', 'у', 'ф', 'х', 'ц', 'ч', 'ш', 'щ', 'ъ', 'ы', 'ь', 'э', 'ю', 'я',
            'А', 'Б', 'В', 'Г', 'Д', 'Е', 'Ж', 'З', 'И', 'Й', 'К', 'Л', 'М', 'Н', 'О', 'П',
            'Р', 'С', 'Т', 'У', 'Ф', 'Х', 'Ц', 'Ч', 'Ш', 'Щ', 'Ъ', 'Ы', 'Ь', 'Э', 'Ю', 'Я',
            '.', ',', '«', '»', '"', '\'', ':', '!', '?', ' '};

    private final Validator validator;

    public Cipher(Validator validator) {
        this.validator = validator;
    }

    /**
     * метод шифрования
     * @param text  входной текст
     * @param shift сдвиг(ключ)
     * @return если символ есть в алфивите, вычиляем новую позицию и добавляем в результат, если нет,
     * добавляем в результат как есть
     */
    public String encrypt(String text, int shift) {
        Objects.requireNonNull(text, "Текст не может быть пустым");

        if (!validator.isValidKey(shift, ALPHABET)) {
            throw new IllegalStateException("Ошибка!, недопустимый ключ " + shift);
        }
        StringBuilder result = new StringBuilder();
        HashMap<Character, Integer> charCountMap = new HashMap<>();
        for (int i = 0; i < ALPHABET.length; i++) {
            charCountMap.put(ALPHABET[i], i);
        }

        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);

            if (charCountMap.containsKey(c)) {

                //вычисляю новый индекс с учетом сдвига
                int originalPosition = charCountMap.get(c);
                int newPosition = (originalPosition + shift) % ALPHABET.length;

                //если сдвиг отрицательный
                if (newPosition < 0) {
                    newPosition += ALPHABET.length;
                }
                result.append(ALPHABET[newPosition]);
            } else {
                result.append(c);
            }
        }
        return result.toString();
    }

    /**
     * метод дешифровки
     * @param encryptedText зашифрованный текст
     * @param shift         сдвиг(ключ)
     * @return расшифрованный текст
     */
    public String decrypt(String encryptedText, int shift) {
        Objects.requireNonNull(encryptedText, "Текст не может быть пустым");
        if (!validator.isValidKey(shift, ALPHABET)) {
            throw new IllegalStateException("Ошибка!, недопустимый ключ " + shift);
        }

        if (shift == 0) {
            return encryptedText;
        }

        StringBuilder result = new StringBuilder(encryptedText.length());
        Map<Character, Integer> charToIndex = new HashMap<>();
        for (int i = 0; i < ALPHABET.length; i++) {
            charToIndex.put(ALPHABET[i], i);
        }

        for (int i = 0; i < encryptedText.length(); i++) {
            char c = encryptedText.charAt(i);

            if (!charToIndex.containsKey(c)) {
                result.append(c); //пропускаем символы, которых нет в алфавите
                continue;
            }

            int currentIndex = charToIndex.get(c);
            int newPosition = (currentIndex - shift) % ALPHABET.length;
            if (newPosition < 0) {
                newPosition += ALPHABET.length;
            }
            result.append(ALPHABET[newPosition]);
        }
        return result.toString();
    }

}
