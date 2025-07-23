package com.javarush.shadrin;

import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * реализация валидации ключа шифрования и файлов
 */
public class Validator {
    /**
     * проверяет валидность ключа шифрования
     * @param key для проверки
     */
    public boolean isValidKey(int key, char[] alphabet) {
        if (alphabet == null || alphabet.length == 0) {
            throw new IllegalStateException("Алфавит не можт быть пустым");
        }
        return key >= 0 && key < alphabet.length;

    }

    /**
     * проверяет сущестование файла по пути
     * @param filePath путь к фалу
     * @return exists(true), если файл существует
     */
    public boolean isFileExist(String filePath) {
        if (filePath == null || filePath.isBlank()) {
            throw new IllegalStateException("Путь к файлу не может быть пустым");
        }

        Path path = FileSystems.getDefault().getPath(filePath);
        boolean exists = Files.exists(path) && Files.isRegularFile(path) && Files.isReadable(path);
        System.out.println("Файл " + path + (exists ? "доступен" : "не доступен"));
        return exists;
    }
}
