package gallows;

import java.util.HashSet;
import java.util.Random;
import java.util.Scanner;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {
    private static String word; // загаданное слово
    private static StringBuilder userWord;
    private static final Set<String> CHECKED = new HashSet<>();
    private static GameState gameState = GameState.INGAME; // состояние игры
    private static int lives = 6;

    private enum GameState {
        INGAME,
        VICTORY,
        DEAD
    }

    public static void main(String[] args) {
        startGame();
    }

    private static void startGame() {
        // загадывается слово
        String[] words = {"apple", "car", "docker", "котик"};
        Random random = new Random();
        word = words[random.nextInt(words.length)];
        userWord = new StringBuilder("_".repeat(word.length()));

        System.out.println("Сыграем в Виселицу? \n" + "Загаданное слово: " + userWord);
        try(Scanner scanner = new Scanner(System.in)){
            while(gameState == GameState.INGAME) {
                String letter = scanner.nextLine();
                checkLetter(letter);
                checkGameState();
                System.out.println("Слово: " + userWord);
            }
        }
        System.out.println(gameState == GameState.VICTORY ? "Ты выиграл!" : "Ты проиграл!");
    }

    // метод валидации ввода и проверки наличия буквы в слове
    private static void checkLetter(String letter) {
        if (letter.length() == 1 && letter.matches("[a-zA-Zа-яА-Я]")) {
            // если буква уже была, нет смысла обрабатывать
            if (CHECKED.contains(letter)) {
                System.out.println("Уже было!");
                return;
            } else { // если не была
                // добавляем в Set, обработав случай ввода верхнего регистра и продолжаем обработку
                letter = letter.toLowerCase();
                CHECKED.add(letter);
            }
        } else {
            System.out.println("Ошибка ввода! Введите одну букву!");
            return;
        }

        // добавляем флаг поддержки русскоязычных символов
        Pattern pattern = Pattern.compile(letter, Pattern.UNICODE_CHARACTER_CLASS);
        Matcher matcher = pattern.matcher(word);
        boolean found = matcher.find();

        // Если введена буква, добавим в SB все вхождения.
        // В случае отсутствия буквы - отнимаем жизнь и дорисовываем виселицу.
        if (found) {
            do {
                int index = matcher.start();
                userWord.setCharAt(index, letter.charAt(0));
            } while (matcher.find());
        } else {
            print();
        }
    }

    private static void checkGameState() {
        if (word.contentEquals(userWord)) gameState = GameState.VICTORY;
        else if (lives == 0) gameState = GameState.DEAD;
    }

    private static void print() {
        String image = switch (--lives) {
            case 5 -> """
                      _______
                       |    |
                       O    |
                            |
                            |
                    ________|
                    """;
            case 4 -> """
                      _______
                       |    |
                       O    |
                       |    |
                            |
                    ________|
                    """;
            case 3 -> """
                      _______
                       |    |
                       O    |
                      /|    |
                            |
                    ________|
                    """;
            case 2 -> """
                      _______
                       |    |
                       O    |
                      /|\\   |
                            |
                    ________|
                    """;
            case 1 -> """
                      _______
                       |    |
                       O    |
                      /|\\   |
                      /     |
                    ________|
                    """;
            default -> """
                      _______
                       |    |
                       O    |
                      /|\\   |
                      / \\   |
                    ________|
                    """;
        };
        System.out.println("Такой буквы нет!\n" + image);
    }
}
