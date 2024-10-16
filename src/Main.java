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
            String input = menu();
            if (input.equals(NEW_GAME)) {
                String hiddenWord = getHiddenWord();
                startGame(hiddenWord);
            }
            if (input.equals(EXIT_GAME)) {
                break;
            }
        }
    }

    private static String menu (){
        showMenu();
        String input = getInputPlayer();
        showInputCase(input);
        return input;
    }

    private static void  showMenu(){
        System.out.printf("%s.Новая игра\n%s.Выход\n ", NEW_GAME, EXIT_GAME);
    }

    private static void showInputCase(String input){
        if (!((input.equals(NEW_GAME)) || (input.equals(EXIT_GAME)))) {
            System.out.printf("Принимается только %s или %s \n", NEW_GAME, EXIT_GAME);
        }
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
        String input = "";
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

    private static String setModifierMask(char input, String hiddenWord, String mask) {
        char[] maskWordCharArray = mask.toCharArray();
        for (int indexWord = 0; indexWord < hiddenWord.length(); indexWord++) {
            if (hiddenWord.charAt(indexWord) == input) {
                maskWordCharArray[indexWord] = input;
            }
        }
        mask = new String(maskWordCharArray);
        return mask;
    }

    private static String getMask(String hiddenWord) {
        return "_".repeat(hiddenWord.length());
    }

    private static void startGame(String hiddenWord) {
        int attemptsCount = 0;
        attemptsCount = getGameResult(hiddenWord, attemptsCount);
        showGameResult(attemptsCount, hiddenWord);
    }

    private static int getGameResult(String hiddenWord, int attemptsCount) {

        boolean status = true;
        Set<Character> LettersKeeper = new LinkedHashSet<>();
        String mask = getMask(hiddenWord);
        showPictureHangman(getPictureHangman(), attemptsCount);
        while (status) {
            System.out.print("\n");
            char input = getUniqueLetter(LettersKeeper);
            mask = getModifierMask(hiddenWord, input, mask);
            attemptsCount = getAttemptsCount(attemptsCount, hiddenWord, input);
            LettersKeeper.add(input);
            showStatsGame(LettersKeeper, attemptsCount, mask);
            status = checkStatusGame(status, attemptsCount, mask);
        }
        return attemptsCount;
    }

    private static String getModifierMask(String hiddenWord, char input, String mask) {
        if (hiddenWord.contains(String.valueOf(input))) {
            mask = setModifierMask(input, hiddenWord, mask);
        }
        return mask;
    }

    private static int getAttemptsCount(int attemptsCount, String hiddenWord, char input) {
        if (!hiddenWord.contains(String.valueOf(input))) {
            System.out.println("вы ошиблись");
            attemptsCount++;
        }
        return attemptsCount;
    }

    private static boolean checkStatusGame(boolean status, int attemptsCount, String mask) {
        if (attemptsCount == ATTEMPTS_MAX) {
            status = false;
        }
        if (!mask.contains("_")) {
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

    private static void showStatsGame(Set<Character> inputLettersKeeper, int attemptsCount, String mask) {
        showPictureHangman(getPictureHangman(), attemptsCount);
        System.out.println("Использованные буквы : " + inputLettersKeeper);
        System.out.printf("Число ошибок %d из %d допустимых \n", attemptsCount, ATTEMPTS_MAX);
        System.out.println("Загаданное слово : " + mask);
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


