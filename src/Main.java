import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class Main {
    private static final List<String> words = new ArrayList<>();
    private static final int ATTEMPTS_MAX = 6;
    private static final String NEW_GAME = "1";
    private static final String EXIT_GAME = "2";

    public static void main(String[] args) {
        getWordsFromDictionary("src/words.txt");
        System.out.println("Игра 'Висельница' ");
        while (true) {
            String input = setMenu();
            if (input.equals(NEW_GAME)) {

                String hiddenWord = getHiddenWord();
                gameLoop(hiddenWord);
            }
            if (input.equals(EXIT_GAME)) {
                break;
            }
        }
    }

    private static String setMenu(){
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
        String filePath = getInputPlayer();
        getWordsFromDictionary(filePath);
    }

    private static String getHiddenWord() {
        Random random = new Random();
        int randomIndex = random.nextInt(words.size());
        return words.get(randomIndex);
    }

    private static String getInputPlayer() {
        Scanner scanner = new Scanner(System.in);
        return scanner.nextLine();
    }

    private static char getValidInput(){
        while(true){
            System.out.print("Введите букву : ");
            String input = getInputPlayer();
            if (isEmptyInput(input)){
                System.out.println("ввод не может быть пустым");
                continue;
            }
            if (isValidLength(input)){
                System.out.println("нужно ввести только одну букву");
                continue;
            }
            if (!isCyrillic(input)){
                System.out.println("нужно ввести только кириллицу ");
                continue;
            }
            if (isInputLowerCase(input)){
                System.out.println("доступен только нижний регистор");
                continue;
            }
            return input.charAt(0);
        }
    }

    private static boolean isEmptyInput(String inputPlayer) {
       return inputPlayer.isEmpty();
    }

    private static boolean isCyrillic(String inputPlayer) {
        return (Character.UnicodeBlock.of(inputPlayer.charAt(0)) == Character.UnicodeBlock.CYRILLIC);
    }

    private static boolean isValidLength(String inputPlayer) {
        return inputPlayer.length() != 1;
    }

    private static boolean isInputLowerCase(String inputPlayer) {

        return Character.isUpperCase(inputPlayer.charAt(0));
    }

    private static void gameLoop(String hiddenWord) {
        int attemptsCount = 0;
        Set<Character> inputLettersKeeper = new LinkedHashSet<>();
        while (true) {
            System.out.print("\n");

            showStatsGame(inputLettersKeeper, attemptsCount, hiddenWord);

            char letter = getValidInput();

            if (isLetterInKeeper(letter, inputLettersKeeper)){
                System.out.println("уже была");
                continue;
            }
            inputLettersKeeper.add(letter);
            if (!isLetterInWord(hiddenWord, letter)){
                System.out.println("такой буквы нету");
                attemptsCount++;
            }
            if (isLose(attemptsCount)){
                showPictureHangman(getPictureHangman(), attemptsCount);
                System.out.println("\nВы проиграли! \nЗагаданное слово было :"
                        + hiddenWord+"\n \n" );
                break;
            }
            if (isWin(hiddenWord, inputLettersKeeper)){
                System.out.println("\nПоздравляю вы выиграли! \nЗагаданное слово было :"
                        + hiddenWord+"\n \n" );
                break;
            }
        }
    }

    private static boolean isWin(String hiddenWord, Set<Character> letterKeeper){
       char[] mask = new char[hiddenWord.length()];
        for (int i = 0; i < hiddenWord.length(); i++) {
            if (letterKeeper.contains(hiddenWord.charAt(i))){
                mask[i] = hiddenWord.charAt(i);
            }
        }
        return hiddenWord.equals(new String(mask));
    }

    private static boolean isLose(int attemptsCount){
        return attemptsCount == ATTEMPTS_MAX;
    }

    private static void showMask(String hiddenWord, Set<Character> letterKeeper){
        for (int i = 0; i < hiddenWord.length(); i++) {
            if (letterKeeper.contains(hiddenWord.charAt(i))){
                System.out.print(hiddenWord.charAt(i));
            }else {
                System.out.print("_");
            }
        }
        System.out.println();
    }


    private static boolean isLetterInKeeper(char letter , Set<Character> letterKeeper){
        return letterKeeper.contains(letter);
    }

    private static boolean isLetterInWord(String hiddenWord, char letter) {
        return hiddenWord.indexOf(letter) != -1;
    }

    private static void showStatsGame(Set<Character> inputLettersKeeper, int attemptsCount, String hiddenWord) {
        showPictureHangman(getPictureHangman(), attemptsCount);
        System.out.println("Использованные буквы : " + inputLettersKeeper);
        System.out.printf("Число ошибок %d из %d допустимых \n", attemptsCount, ATTEMPTS_MAX);
        System.out.print("загаданное слово ");
        showMask(hiddenWord, inputLettersKeeper);
    }

    private static List<String> getPictureHangman() {
        return List.of("""
                _________
                ||/     \s
                ||
                ||
                ||
                ||
                ||
                ||\\______""", """
                _________
                ||/    | \s
                ||     |
                ||    \s
                ||
                ||
                ||
                ||\\______""", """
                _________
                ||/    | \s
                ||     |
                ||     O    \s
                ||     |
                ||     |
                ||
                ||\\______
                """, """
                _________
                ||/    | \s
                ||     |
                ||     O    \s
                ||    /|
                ||     |
                ||   \s
                ||\\______""", """
                _________
                ||/    | \s
                ||     |
                ||     O    \s
                ||    /|\\
                ||     |
                ||   \s
                ||\\______""", """
                _________
                ||/    | \s
                ||     |
                ||     O    \s
                ||    /|\\
                ||     |
                ||    /
                ||\\______""", """
                _________
                ||/    | \s
                ||     |
                ||     O    \s
                ||    /|\\
                ||     |
                ||    / \\
                ||\\______"""
        );
    }

    private static void showPictureHangman(List<String> pictureHangman, int numberPicture) {
        System.out.println(pictureHangman.get(numberPicture));
    }

}


