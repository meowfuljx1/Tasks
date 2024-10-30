package passwordGenerator;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

public class Main {
    private static final SecureRandom sr = new SecureRandom();
    // список значений для пароля
    private static final List<Character> values = new ArrayList<>();
    private static final String letters = "abcdefghijklmnopqrstuvwxyz";
    private static final String symbols = "_!$-:";

    public static void main(String[] args) {
        StringBuilder password = generatePassword();
        System.out.println(password);
    }

    private static StringBuilder generatePassword() {
        int length = getInput();
        // sb для хранения пароля
        StringBuilder sb = new StringBuilder(length);
        // выбираем length значений
        generateValue(length, 0);
        fillSB(sb);
        return sb;
    }

    private static int getInput() {
        try (Scanner sc = new Scanner(System.in)) {
            while (true) {
                System.out.println("Выберите длину пароля от 8 до 12 символов");
                String input = sc.nextLine();
                if (
                        !input.isEmpty() &&
                                input.matches("[0-9]+") &&
                                (Integer.parseInt(input) >= 8 && Integer.parseInt(input) <= 12)
                )
                    return Integer.parseInt(input);
            }
        }
    }

    private static void generateValue(int length, int x) {
        if (length == 0) return; // base case

        /* x в диапазоне от 0 до 3 (вкл) гарантирует,
           что в пароле будет хотя бы по одному символу всех категорий
           (строчная и заглавная буквы, число и спец символ).

           x > 3 случайно выбирает категорию символа.
        */
        if (x > 3)
            x = sr.nextInt(4);

        // в list добавляется случайный символ категории x
        switch (x) {
            // строчные буквы
            case 0:
                values.add(letters.charAt(sr.nextInt(letters.length())));
                break;
            // заглавные буквы
            case 1:
                values.add(Character.toUpperCase(letters.charAt(sr.nextInt(letters.length()))));
                break;
            // спец символы
            case 2:
                values.add(symbols.charAt(sr.nextInt(symbols.length())));
                break;
            // цифры
            case 3:
                values.add((char) (sr.nextInt(10) + '0'));
                break;
        }
        // рекурсивный вызов для получения length случайных значений
        generateValue(length - 1, x + 1);
    }

    // метод перемешивает и добавляет в sb значения
    private static void fillSB (StringBuilder sb){
        Collections.shuffle(values);
        values.forEach(sb::append);
    }
}