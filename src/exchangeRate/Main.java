package exchangeRate;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {
    private static final Map<String, Map<String, Double>> MAP = new HashMap<>();

    static { // инициализация курса валют
        MAP.put("RUB", Map.of("USD", 0.010345, "EUR", 0.009541, "BYN", 0.034246, "CNY", 0.07407));
        MAP.put("USD", Map.of("RUB", 96.67, "EUR", 0.92445, "BYN", 3.3, "CNY", 7.13));
        MAP.put("EUR", Map.of("RUB", 104.81, "USD", 1.08, "BYN", 3.57, "CNY", 7.71));
        MAP.put("BYN", Map.of("RUB", 29.2, "USD", 0.303352, "EUR", 0.280285, "CYN", 2.15));
        MAP.put("CNY", Map.of("RUB", 13.5, "USD", 0.140321, "EUR", 0.12972, "BYN", 0.4645));
    }

    public static void main(String[] args) {
        exchangeRate();
    }

    private static void exchangeRate() {
        String input = getInput(); // результат метода запроса - строка ввода
        // получение суммы и преобразование в double для конвертации в другую валюту
        double amount = Double.parseDouble(input.substring(0, input.length() - 4));
        // строка исходной валюты (для обращения к мапе)
        String srcCurrency = input.substring(input.length() - 3);

        MAP.get(srcCurrency).forEach((currency, rate) -> {
            double converted = amount * rate; // конвертация суммы в валюту
            // если результат = целое число, то вывод без дробной части (например, 10 USD -> 33 BYN)
            if (converted % 1 == 0) {
                System.out.println(input + " -> " + (int) converted + " " + currency);
            } else { // иначе округляем до 2ух чисел после запятой
                converted = Math.round(converted * 100) / 100.0;
                System.out.println(input + " -> " + converted + " " + currency);
            }
        });
    }

    private static String getInput() {
        System.out.println("Введите сумму с указанием валюты для конвертирования в другие.\n" +
                "Доступные валюты:" +
                MAP.keySet().toString().replace("[", " ").replace("]", ".")
        );

        try (Scanner sc = new Scanner(System.in)) {
            while (true) {
                String input = sc.nextLine();
                // паттерн: число (опционально дробное) + пробел + 3 заглавных символа латинского алфавита
                Pattern pattern = Pattern.compile("(\\d+(\\.\\d+)?) ([A-Z]{3})");
                Matcher matcher = pattern.matcher(input);
                // в случае соответствия паттерну и при наличии ключа в мапе, метод возвращает массив String
                if (matcher.matches() && MAP.containsKey(matcher.group(3)))
                    return input;
                else
                    System.out.println("Неверный ввод!");
            }
        }
    }
}
