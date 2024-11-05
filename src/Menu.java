public class Menu {

    private final InputPlayer inputPlayer;
    private static final String NEW_GAME = "1";
    private static final String EXIT_GAME = "2";

    public Menu(InputPlayer inputPlayer) {
        this.inputPlayer = inputPlayer;
    }

    public void setMenu() {
        System.out.println(" Игра 'Висельница' ");
        while (true) {
            System.out.printf("%s.Новая игра\n%s.Выход\n ", NEW_GAME, EXIT_GAME);
            String input = inputPlayer.getInputPlayer();
            switch (input) {
                case NEW_GAME:
                    gameStat();
                    break;
                case EXIT_GAME:
                    return;
                default:
                    System.out.printf("Можно ввести только (%s) или (%s)\n\n", NEW_GAME, EXIT_GAME);

            }
        }
    }


    private void gameStat() {
        WordsList wordsList = new WordsList("src/resources/words.txt", inputPlayer);
        HiddenWord hiddenWord = new HiddenWord(wordsList);
        LettersKeeper lettersKeeper = new LettersKeeper();
        Picture picture = new Picture();
        GameLogic gameLogic = new GameLogic(inputPlayer, hiddenWord, lettersKeeper, picture);
        gameLogic.gameLoop();
    }

}
