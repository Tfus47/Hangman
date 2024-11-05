public class GameLogic {
    private final HiddenWord hiddenWord;
    private final InputPlayer inputPlayer;
    private final Picture picture;
    private final LettersKeeper lettersKeeper;
    private static final int ATTEMPTS_MAX = 6;


    public GameLogic(InputPlayer inputPlayer, HiddenWord hiddenWord,
                     LettersKeeper lettersKeeper, Picture picture) {
        this.inputPlayer = inputPlayer;
        this.hiddenWord = hiddenWord;
        this.lettersKeeper = lettersKeeper;
        this.picture = picture;
    }


    public void gameLoop() {
        int attemptsCount = 0;
        String mask = hiddenWord.getMask();
        String word = hiddenWord.getHiddenWord();

        while (true) {
            System.out.println();

            showStatus(attemptsCount, mask);

            char letter = inputPlayer.getValidInput();

            if (lettersKeeper.isLetterAlreadyBeen(letter)) {
                System.out.println("Введенная буква уже была");
                continue;
            }

            lettersKeeper.setInputLetterKeeper(letter);

            if (!hiddenWord.isLetterInHiddenWord(letter)) {
                System.out.println("Такой буквы нету");
                attemptsCount++;
            }

            mask = hiddenWord.openMask(mask, letter);

            if (hiddenWord.isMaskFullOpen(mask)) {
                System.out.println("\nПоздравляю вы выиграли! \nЗагаданное слово было :"
                        + word + "\n \n");
                break;
            }
            if (attemptsCount == ATTEMPTS_MAX) {
                picture.showPicture(attemptsCount);
                System.out.println("\nВы проиграли! \nЗагаданное слово было :"
                        + word + "\n \n");
                break;
            }
        }
    }

    private void showStatus(int attemptsCount, String mask){
        picture.showPicture(attemptsCount);
        System.out.print("Использованные буквы : " );
        lettersKeeper.showInputLettersKeeper();
        System.out.printf("Число ошибок %d из %d допустимых \n", attemptsCount, ATTEMPTS_MAX);
        System.out.println("загаданное слово : "+ mask);

    }

}
