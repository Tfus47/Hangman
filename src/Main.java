import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class Main {
    private static final Scanner scanner = new Scanner(System.in);
    private static final List<String> words = new ArrayList<>();
    private static final int ATTEMPTS_MAX = 6;
    private static final String NEW_GAME = "1";
    private static final String EXIT_GAME = "2";

    public static void main(String[] args) {
        getWordsFromDictionary("src/words.txt");
        System.out.println("Игра 'Висельница' ");
        while (true) {
            String input = checkInputForStart();
            if (input.equals(NEW_GAME)) {
                String hiddenWord = getHiddenWord();
                startGame(hiddenWord);
            }
            if (input.equals(EXIT_GAME)) {
                break;
            }
        }
    }

    // много действий - разделить
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
            setDifferentPath();
        }
    }

    private static void setDifferentPath() {
        System.out.println("О, кажется отсутствует текстовый файл со словарем");
        System.out.print("Вставьте сюда путь к текстовому файлу: ");
        String filePath = scanner.nextLine();
        getWordsFromDictionary(filePath);
    }

    private static String getHiddenWord() {
        Random random = new Random();
        int randomIndex = random.nextInt(words.size());
        return words.get(randomIndex);
    }

    private static String getInputPlayer() {
        return scanner.nextLine();
    }

    private static char getCorrectLetter() {
        boolean check = true;
        String input = new String();
        while (check) {
            System.out.print("Введите букву : ");
            input = getInputPlayer();
            check = checkIsEmpty(input, check);
        }
        return input.charAt(0);
    }

    private static char getUniqueLetter(Set<Character> LettersKeeper) {
        char input = 0;
        boolean correct = true;
        while (correct) {
            input = getCorrectLetter();
            if (LettersKeeper.contains(input)) {
                System.out.println("буква уже была");
            } else {
                correct = false;
            }
        }
        return input;
    }

    private static boolean checkIsEmpty(String inputPlayer, boolean check) {
        if (inputPlayer.isEmpty()) {
            System.out.println("Требуется ввести букву, а не оставлять ответ пустым!");
            check = true;
        } else {
            check = checkCyrillic(check, inputPlayer);
        }
        return check;
    }

    private static boolean checkCyrillic(boolean correctinput, String inputPlayer) {
        if (!(Character.UnicodeBlock.of(inputPlayer.charAt(0)) == Character.UnicodeBlock.CYRILLIC)) {
            System.out.println("В данной игре можно использовать только кириллицу!");
        } else {
            correctinput = checkLengthInput(correctinput, inputPlayer);
        }

        return correctinput;
    }

    private static boolean checkLengthInput(boolean check, String inputPlayer) {
        if (inputPlayer.length() > 1) {
            System.out.println("Требуется ввод только одной буквы!");
        } else {
            check = checkUpperCase(check, inputPlayer);
        }
        return check;
    }

    private static boolean checkUpperCase(boolean check, String inputPlayer) {
        if (Character.isUpperCase(inputPlayer.charAt(0))) {
            System.out.println("Только маленькие буквы");
        } else {
            check = false;
        }
        return check;
    }

    private static String setModifierMask(char input, String hiddenWord, String maskWord) {
        char[] maskWordCharArray = maskWord.toCharArray();
        for (int indexWord = 0; indexWord < hiddenWord.length(); indexWord++) {
            if (hiddenWord.charAt(indexWord) == input) {
                maskWordCharArray[indexWord] = input;
            }
        }
        maskWord = new String(maskWordCharArray);
        return maskWord;
    }

    private static String getMask(String hiddenWord) {
        return "_".repeat(hiddenWord.length());
    }

    private static void startGame(String hiddenWord) {
        int attemptsCount = 0;
        attemptsCount = gameLoop(hiddenWord, attemptsCount);
        showGameResult(attemptsCount, hiddenWord);
    }

    // переименовать
    private static int gameLoop(String hiddenWord, int attemptsCount) {

        boolean status = true;
        Set<Character> LettersKeeper = new LinkedHashSet<>();
        String maskWord = getMask(hiddenWord);
        showPictureHangman(getPictureHangman(), attemptsCount);
        while (status) {
            System.out.print("\n");
            char input = getUniqueLetter(LettersKeeper);
            maskWord = getModifierMask(hiddenWord, input, maskWord);
            attemptsCount = getAttemptsCount(attemptsCount, hiddenWord, input);
            LettersKeeper.add(input);
            showStatsGame(LettersKeeper, attemptsCount, maskWord);
            status = checkStatusGame(status, attemptsCount, maskWord);
        }
        return attemptsCount;
    }

    private static String getModifierMask(String hiddenWord, char input, String maskWord) {
        if (hiddenWord.contains(String.valueOf(input))) {
            maskWord = setModifierMask(input, hiddenWord, maskWord);
        }
        return maskWord;
    }

    private static int getAttemptsCount(int attemptsCount, String hiddenWord, char input) {
        if (!hiddenWord.contains(String.valueOf(input))) {
            System.out.println("вы ошиблись");
            attemptsCount++;
        }
        return attemptsCount;
    }

    private static boolean checkStatusGame(boolean status, int attemptsCount, String maskWord) {
        if (attemptsCount == ATTEMPTS_MAX) {
            status = false;
        }
        if (!maskWord.contains("_")) {
            status = false;
        }
        return status;
    }

    private static void showGameResult(int attemptsCount, String hiddenWord) {
        if (attemptsCount != ATTEMPTS_MAX) {
            System.out.println("Поздравляю вы выиграли!");
        } else {
            System.out.println("Вы проиграли, загаданное слово было :" + hiddenWord);
        }
    }

    private static void showStatsGame(Set<Character> inputLettersKeeper, int attemptsCount, String maskWord) {
        showPictureHangman(getPictureHangman(), attemptsCount);
        System.out.println("Использованные буквы : " + inputLettersKeeper);
        System.out.printf("Число ошибок %d из %d допустимых \n", attemptsCount, ATTEMPTS_MAX);
        System.out.println("Загаданное слово : " + maskWord);
    }

    private static List<String> getPictureHangman() {
        List<String> pictureHangman = new ArrayList<>();
        pictureHangman.add("""
                _________
                ||/     \s
                ||
                ||
                ||
                ||
                ||
                ||\\______""");
        pictureHangman.add("""
                _________
                ||/    | \s
                ||     |
                ||    \s
                ||
                ||
                ||
                ||\\______""");
        pictureHangman.add("""
                _________
                ||/    | \s
                ||     |
                ||     O    \s
                ||
                ||
                ||
                ||\\______""");
        pictureHangman.add("""
                _________
                ||/    | \s
                ||     |
                ||     O    \s
                ||     |
                ||     |
                ||
                ||\\______
                """);
        pictureHangman.add("""
                _________
                ||/    | \s
                ||     |
                ||     O    \s
                ||    /|\\
                ||     |
                ||   \s
                ||\\______""");
        pictureHangman.add("""
                _________
                ||/    | \s
                ||     |
                ||     O    \s
                ||    /|\\
                ||     |
                ||    /
                ||\\______""");
        pictureHangman.add("""
                _________
                ||/    | \s
                ||     |
                ||     O    \s
                ||    /|\\
                ||     |
                ||    / \\
                ||\\______""");

        return pictureHangman;
    }

    private static void showPictureHangman(List<String> pictureHangman, int attemptsCount) {
        System.out.println(pictureHangman.get(attemptsCount));
    }

}


