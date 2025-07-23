package com.javarush.shadrin.menu;

import com.javarush.shadrin.Cipher;
import com.javarush.shadrin.FileManager;
import com.javarush.shadrin.StatisticalAnalyzer;
import com.javarush.shadrin.Validator;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;
import static com.javarush.shadrin.Cipher.ALPHABET;

public class StatisticalAnalyzerMenu {
    private static FileManager fileManager;
    private static Cipher cipher;
    private static Validator validator;
    private static StatisticalAnalyzer statisticalAnalyzer;

    static {
        validator = new Validator();
        cipher = new Cipher(validator);
        fileManager = new FileManager(validator);
        statisticalAnalyzer = new StatisticalAnalyzer();
    }

    /**
     * метод для запуска меню в консоли, содержит основную логику и
     * все проверки существование файла, ввод и п.
     * @param scanner передается аргументом из консоли
     */
    public static void startStatisticalAnalyzerMenu(Scanner scanner) {
        System.out.println("\n=== Меню статистического анализа ===");
        final String outputPath = "src/main/resources/raw/decrypt_by_statistical_analyzer.txt";
        String command = "";
        do {
            try {
                // ввод данных
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

                System.out.println("2. Укажите паттерн для поиска (известный фрагмент текста):");
                String pattern = scanner.nextLine().trim();

                if (pattern.isEmpty()) {
                    System.out.println("Ошибка: паттерн не может быть пустым");
                    continue;
                }
                System.out.println("Идет анализ...");

                // Поиск по всем возможным сдвигам
                boolean found = false;
                for (int shift = 0; shift < Cipher.ALPHABET.length; shift++) {
                    String decrypted = cipher.decrypt(content, shift);
                    //System.out.println("[DEBUG] Shift " + shift + ": " + decrypted.substring(0, Math.min(50, decrypted.length())));

                    List<Integer> matches = statisticalAnalyzer.findMostLikelyShiftByKMP(decrypted, pattern);

                    if (!matches.isEmpty()) {
                        System.out.println("\nУспешная расшифровка!");
                        System.out.println("Найденный ключ: " + shift);
                        System.out.println("Позиция паттерна: " + matches);
                        System.out.println("Результат:\n" + decrypted);

                        fileManager.writeFile(decrypted, outputPath);
                        System.out.println("Сохранено в: " + Paths.get(outputPath).toAbsolutePath());

                        found = true;
                        break;
                    }
                }

                if (!found) {
                    System.out.println("Не удалось найти указанный паттерн ни при одном сдвиге");
                }

                System.out.println("\n3. Введите 'exit' или  '3' для выхода," +
                        " любой другой ввод - продолжить");
                command = scanner.nextLine();
                if (command.equalsIgnoreCase("exit") || command.equalsIgnoreCase("3")) {
                    return; //выход в главное меню
                }

            } catch (FileNotFoundException e) {
                System.err.println("Ошибка: файл не найден - " + e.getMessage());
            } catch (IOException e) {
                System.err.println("Ошибка ввода-вывода: " + e.getMessage());
            } catch (Exception e) {
                System.err.println("Ошибка обработки: " + e.getMessage());
            }


        } while (!command.equalsIgnoreCase("exit") && !command.equalsIgnoreCase("3"));
    }
}

