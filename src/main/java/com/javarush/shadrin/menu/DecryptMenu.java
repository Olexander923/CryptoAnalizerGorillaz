package com.javarush.shadrin.menu;

import com.javarush.shadrin.Cipher;
import com.javarush.shadrin.FileManager;
import com.javarush.shadrin.Validator;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Scanner;

/**
 * Меню дешифровки
 */
public class DecryptMenu {
    private static FileManager fileManager;
    private static Cipher cipher;
    private static Validator validator;

    static {
        validator = new Validator();
        fileManager = new FileManager(validator);
        cipher = new Cipher(validator);
    }

    /**
     * метод для запуска меню в консоли, содержит основную логику и
     * все проверки существование файла, ввод и п.
     * @param scanner передается аргументом из консоли
     */
    public static void startDecryptMenu(Scanner scanner) {
        System.out.println("\n=== Меню дешифровки ===");
        String command = "";

        do {
            try {
                // ввод данных
                System.out.println("1. Введите зашифрованный текст или путь к файлу без отступов и нажмите Enter дважды." +
                        "(для выхода из меню введите 'exit или '3'):");
                StringBuilder inputBuilder = new StringBuilder();
                String line = scanner.nextLine();

                if (line.equalsIgnoreCase("exit") || line.equalsIgnoreCase("3")) break; // Выход по 3

                while (!line.isEmpty()) {
                    inputBuilder.append(line).append("\n");
                    line = scanner.nextLine();
                }

                String input = inputBuilder.toString().trim();
                if (input.isEmpty()) {
                    System.out.println("Ошибка: ввод не может быть пустым");
                    continue;
                }

                // определяем файл или текст на входе
                String content;
                if (Files.exists(Paths.get(input.split("\n")[0]))) {
                    content = fileManager.readFile(input.split("\n")[0]);
                    System.out.println("Файл прочитан: " + Paths.get(input.split("\n")[0]).toAbsolutePath());
                } else {
                    content = input;
                }

                System.out.println("2. Введите ключ дешифровки (0-" + (Cipher.ALPHABET.length - 1) + ")");
                String keyInput = scanner.nextLine();

                try {
                    int key = Integer.parseInt(keyInput);
                    if (!validator.isValidKey(key, Cipher.ALPHABET)) {
                        System.out.println("Ключ должен быть от 0 до " + (Cipher.ALPHABET.length - 1));
                        continue;
                    }
                    //дешифровка
                    String decryptText = cipher.decrypt(content, key);
                    System.out.println("\nРезультат дешифровки:\n" + decryptText);
                    String outputPath = "src/main/resources/raw/decrypted.txt";
                    fileManager.writeFile(decryptText, outputPath);
                    System.out.println("Результат успешно записан в 'decrypted.txt'," +
                            " расположенный по пути: " + outputPath);

                    System.out.println("\n3. Введите 'exit' или  '3' для выхода," +
                            " любой другой ввод - продолжить");
                    command = scanner.nextLine();
                    if (command.equalsIgnoreCase("exit") || command.equalsIgnoreCase("3")) {
                        return; //выход в главное меню
                    } else {
                        continue; //в противном случае продолжаем
                    }

                } catch (NumberFormatException e) {
                    System.out.println("Ошибка: ключ должен быть целым числом");
                }
            } catch (IOException e) {
                    System.err.println("Ошибка записи: " + e.getMessage());
            }

            System.out.println("Введите 'exit' или '3' для выхода, любой другой ввод - продолжить:");
            command = scanner.nextLine();
            } while (!command.equalsIgnoreCase("exit") && !command.equalsIgnoreCase("3"));

        }

}
