package com.javarush.shadrin;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;


/**
 * реализация валидации чтения, записи и входных файлов/текстов
 */
public class FileManager {
    private final Validator validator;
    private static final List<Path> DEFAULT_SEARCH_DIRS = List.of(
            Paths.get("src/main/resources/raw"),
            Paths.get("resources/raw"),
            Paths.get(System.getProperty("user.dir"))
    );

    public FileManager(Validator validator) {
        this.validator = validator;
    }


    /**
     * метод для чтения входных данных или файлов
     * @param filePath путь к файлу
     * @throws IOException если неверно указан путь или файл не найден
     */
    public String readFile(String filePath) throws IOException {

        if (!validator.isFileExist(filePath)) {
            throw new IOException("Ошибка!, файл не найден " + filePath);
        }

        StringBuilder content = new StringBuilder();
        System.out.println("Выполняется чтение...");
        try (BufferedReader fileReader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = fileReader.readLine()) != null) {
                content.append(line).append("\n");
            }
        } catch (IOException e) {
            throw new IOException("Файл не найден " + filePath);
        }
        return content.toString();
    }

    /**
     * метод для запаси результатов в файл,также проверяет существует ли дирректория
     */
    public void writeFile(String content, String path) {
        //проверка на директорию, создаем если ее нет
        File file = new File(path);
        if (file.getParent() != null) {
            file.getParentFile().mkdirs();
        }

        try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(path))) {

            bufferedWriter.write(content);
            bufferedWriter.newLine();
        } catch (IOException e) {
            System.err.println("Ошибка при записи в файл: " + path);
            e.printStackTrace();
        }
    }


}
