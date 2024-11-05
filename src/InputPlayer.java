import java.util.Scanner;

public class InputPlayer {
    private static Scanner scanner = new Scanner(System.in);


    public String getInputPlayer() {
        Scanner scanner = new Scanner(System.in);
        return scanner.nextLine();
    }

    public char getValidInput() {
        while (true) {
            System.out.print("Введите букву : ");
            String input = getInputPlayer();
            if (isEmptyInput(input)) {
                System.out.println("ввод не может быть пустым");
                continue;
            }
            if (isValidLength(input)) {
                System.out.println("нужно ввести только одну букву");
                continue;
            }
            if (!isCyrillic(input)) {
                System.out.println("нужно ввести только кириллицу ");
                continue;
            }
            if (isInputLowerCase(input)) {
                System.out.println("доступен только нижний регистор");
                continue;
            }
            return input.charAt(0);
        }

    }

    private boolean isEmptyInput(String inputPlayer) {
        return inputPlayer.isEmpty();
    }

    private boolean isCyrillic(String inputPlayer) {
        return (Character.UnicodeBlock.of(inputPlayer.charAt(0)) == Character.UnicodeBlock.CYRILLIC);
    }

    private boolean isValidLength(String inputPlayer) {
        return inputPlayer.length() != 1;
    }

    private boolean isInputLowerCase(String inputPlayer) {

        return Character.isUpperCase(inputPlayer.charAt(0));
    }


}
