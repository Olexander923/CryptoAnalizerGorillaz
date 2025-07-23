package com.javarush.shadrin.menu;

import com.javarush.shadrin.BruteForce;
import com.javarush.shadrin.Cipher;
import com.javarush.shadrin.FileManager;
import com.javarush.shadrin.Validator;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Scanner;

/**
 * Меню взлома брут-форсом
 */
public class BruteForceMenu {
    private static FileManager fileManager;
    private static Cipher cipher;
    private static Validator validator;

    static {
        validator = new Validator();
        cipher = new Cipher(validator);
        fileManager = new FileManager(validator);
    }

    /**
     * метод для запуска меню в консоли, содержит все проверки существование файла, ввод и п.
     * @param scanner передается аргументом из консоли
     */
    public static void startBruteForceMenu(Scanner scanner) {
        System.out.println("\n=== Brute Force ===");
        final String referencePath = "src/main/resources/raw/reference_text.txt";
        final String outputPath = "src/main/resources/raw/decrypt_by_brute_force.txt";
        String command = "";

        do {
            try {
                BruteForce bruteForce = new BruteForce(referencePath);


                System.out.println("1. Введите зашифрованный текст или путь к файлу без отступов и нажмите Enter дважды. " +
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

                System.out.println("Подбор ключа...");

                // автоматический подбор с частотным анализом
                int foundKey = bruteForce.findBestKey(content, true);
                String decryptedText = bruteForce.decryptWithBestKey(content, true);

                // альтернативный подбор по пробелам
                if (decryptedText.chars().filter(c -> c == ' ').count() < content.length() / 10) {
                    System.out.println("Пробуем альтернативный метод...");
                    foundKey = bruteForce.findBestKey(content, false);
                    decryptedText = bruteForce.decryptWithBestKey(content, false);
                }

                System.out.println("\nНайденный ключ: " + foundKey);
                System.out.println("Результат расшифровки:\n" + decryptedText);

                // Сохранение результата
                fileManager.writeFile(decryptedText, outputPath);
                System.out.println("Сохранено в файл: " + Paths.get(outputPath).toAbsolutePath());

                System.out.println("\n3. Введите 'exit' или  '3' для выхода," +
                        " любой другой ввод - продолжить");
                command = scanner.nextLine();
                if (command.equalsIgnoreCase("exit") || command.equalsIgnoreCase("3")) {
                    return; //выход в главное меню
                } else {
                    continue; //в противном случае продолжаем
                }

            } catch (FileNotFoundException e) {
                System.err.println("Файл не найден: " + e.getMessage());
            } catch (IOException e) {
                System.err.println("Ошибка ввода-вывода: " + e.getMessage());
            } catch (Exception e) {
                System.err.println("Ошибка при обработке: " + e.getMessage());
            }

            System.out.println("Введите 'exit' или '3' для выхода, любой другой ввод - продолжить:");
            command = scanner.nextLine();

        } while (!command.equalsIgnoreCase("exit") && !command.equalsIgnoreCase("3"));

    }
}
