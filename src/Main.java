import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class Main {

    private static final Scanner scanner = new Scanner(System.in);
    private static final List<String> words = new ArrayList<>();
    private static String hiddenWord;
    private static String maskWord;
    private static final int ATTEMPTS_MAX = 6;
    private static final String NEW_GAME = "1";
    private static final String EXIT_GAME = "2";

    public static void main(String[] args) {

        getWordsFromDictionary("src/dictionary.txt");
        System.out.println("Игра 'Висельница' ");
        while (true) {
            hiddenWord = getHiddenWord();
            maskWord = "_".repeat(hiddenWord.length());
            String input = checkInputForStart();
            if (input.equals(NEW_GAME)) {
                startGame();
            }
            if (input.equals(EXIT_GAME)) {
                break;
            }
        }

    }

    private static String checkInputForStart() {
        System.out.printf("%s.Новая игра\n%s.Выход\n ", NEW_GAME, EXIT_GAME);
        String input = scanner.nextLine().toUpperCase();
        if (!((input.equals(NEW_GAME)) || (input.equals(EXIT_GAME)))) {
            System.out.printf("Принимается только %s или %s \n", NEW_GAME, EXIT_GAME);
        }
        return input;
    }

    private static void getWordsFromDictionary(String filePath) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] lineWords = line.split("\\s+");
                Collections.addAll(words, lineWords);
            }
        } catch (IOException e) {
            notSearchDictionary();
        }
    }

    private static void notSearchDictionary() {
        System.out.println("О, кажется отсутствует текстовый файл со словарем");
        System.out.print("Вставьте сюда путь к текстовому файлу: ");
        String filePath = scanner.nextLine();
        getWordsFromDictionary(filePath);
    }

    private static String getHiddenWord() {
        Random random = new Random();
        Collections.shuffle(words, random);
        return words.getFirst();
    }

    private static char checkCorrectInput() {
        while (true) {
            System.out.print("Введите букву : ");
            String inputPlayer = scanner.nextLine();

            if (inputPlayer.isEmpty()) {
                System.out.println("Требуется ввести букву, а не оставлять ответ пустым!");
            } else {
                if (!(Character.UnicodeBlock.of(inputPlayer.charAt(0)) == Character.UnicodeBlock.CYRILLIC)) {
                    System.out.println("В данной игре можно использовать только кириллицу!");
                } else if (inputPlayer.length() > 1) {
                    System.out.println("Требуется ввод только одной буквы!");
                } else if (!Character.isUpperCase(inputPlayer.charAt(0))) {
                    return inputPlayer.charAt(0);
                } else {
                    System.out.println("Только маленькие буквы");
                }
            }
        }
    }

    private static void checkInputAndRandomWord(char input) {
        char[] maskWordCharArray = maskWord.toCharArray();
        for (int indexWord = 0; indexWord < hiddenWord.length(); indexWord++) {
            if (hiddenWord.charAt(indexWord) == input) {
                maskWordCharArray[indexWord] = input;
            }
        }
        maskWord = new String(maskWordCharArray);
    }

    private static void startGame() {
        Set<Character> wordsKeeper = new LinkedHashSet<>();
        int attemptsCount = 0;
        boolean status = true;
        System.out.println(artHangman.values()[0].getArtHangman());

        while (status) {

            System.out.print("\n");
            char input = checkCorrectInput();

            if (!wordsKeeper.contains(input)) {
                if (hiddenWord.contains(String.valueOf(input))) {
                    checkInputAndRandomWord(input);
                } else {
                    attemptsCount++;
                    System.out.println("Вы ошиблись(");
                }
                wordsKeeper.add(input);
            } else {
                System.out.println("Эта буква уже была, введите другую");
            }
            getStatsGame(wordsKeeper, attemptsCount);
            if (attemptsCount == ATTEMPTS_MAX) {
                status = false;
            }
            if (!maskWord.contains("_")) {
                status = false;
            }
        }
        getResultGame(attemptsCount);
    }

    private static void getResultGame(int attemptsCount) {
        if (attemptsCount != ATTEMPTS_MAX) {
            System.out.println("Поздравляю вы выиграли!");
        } else {
            System.out.println("Вы проиграли, загаданное слово было :" + hiddenWord);
        }
    }

    private static void getStatsGame(Set<Character> wordsKeeper, int attemptsCount) {
        String artHangman = Main.artHangman.values()[attemptsCount].getArtHangman();
        System.out.println(artHangman);
        System.out.println("Использованные буквы : " + wordsKeeper);
        System.out.printf("Число ошибок %d из %d допустимых \n", attemptsCount, ATTEMPTS_MAX);
        System.out.println("Загаданное слово : " + maskWord);
    }

    private enum artHangman {
        ZERO("""
                _________
                ||/     \s
                ||
                ||
                ||
                ||
                ||
                ||\\______"""), 
        ONE("""
                _________
                ||/    | \s
                ||     |
                ||    \s
                ||
                ||
                ||
                ||\\______"""), 
        TWO("""
                _________
                ||/    | \s
                ||     |
                ||     O    \s
                ||
                ||
                ||
                ||\\______"""), 
        THREE("""
                _________
                ||/    | \s
                ||     |
                ||     O    \s
                ||     |
                ||     |
                ||
                ||\\______
                """), 
        FOUR("""
                _________
                ||/    | \s
                ||     |
                ||     O    \s
                ||    /|\\
                ||     |
                ||   \s
                ||\\______"""), 
        FIVE("""
                _________
                ||/    | \s
                ||     |
                ||     O    \s
                ||    /|\\
                ||     |
                ||    /
                ||\\______"""), 
        SIX("""
                _________
                ||/    | \s
                ||     |
                ||     O    \s
                ||    /|\\
                ||     |
                ||    / \\
                ||\\______""");


        private final String artHangman;

        artHangman(String artHangman) {
            this.artHangman = artHangman;
        }

        public String getArtHangman() {
            return artHangman;
        }

    }

}


