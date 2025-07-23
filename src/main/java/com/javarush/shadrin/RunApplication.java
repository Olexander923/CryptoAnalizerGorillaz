package com.javarush.shadrin;

import com.javarush.shadrin.menu.BruteForceMenu;
import com.javarush.shadrin.menu.DecryptMenu;
import com.javarush.shadrin.menu.EncryptMenu;
import com.javarush.shadrin.menu.StatisticalAnalyzerMenu;

import javax.crypto.BadPaddingException;
import java.io.IOException;
import java.util.Scanner;

/**
 * старт пограммы, отображение главного меню, переход в конкретные меню работы.
 */
public class RunApplication {
    public static void main(String[] args) {

        Scanner mainScanner = new Scanner(System.in);

        try {
            String command;
            do {
                System.out.println("Добро пожаловать в криптоанализатор!");
                System.out.println("Выберите режим работы: ");
                System.out.println("1.Шифрование");
                System.out.println("2.Дешифровка");
                System.out.println("3.Brute force");
                System.out.println("4.Статистический анализ");
                System.out.println("5.Выход");
                System.out.print("\nВыберите режим работы: ");
                command = mainScanner.nextLine();

                switch (command) {
                    case "1":
                        EncryptMenu.startEncryptMenu(mainScanner);
                        break;

                    case "2":
                        DecryptMenu.startDecryptMenu(mainScanner);
                        break;

                    case "3":
                        BruteForceMenu.startBruteForceMenu(mainScanner);
                        break;

                    case "4":
                        StatisticalAnalyzerMenu.startStatisticalAnalyzerMenu(mainScanner);
                        break;

                    case "5":
                        System.out.println("Exit");
                        break;
                    default:
                        System.out.println("Ошибка ввода данных!");
                }
            }
            while (!command.equals("5"));

        } finally {
            mainScanner.close();
        }
    }
}
