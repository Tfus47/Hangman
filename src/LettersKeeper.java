import java.util.HashSet;
import java.util.Set;

public class LettersKeeper {
    private final Set<Character> inputLetterKeeper = new HashSet<>();

    public boolean isLetterAlreadyBeen(char letter){
        return inputLetterKeeper.contains(letter);
    }

    public void setInputLetterKeeper(char letter){
        inputLetterKeeper.add(letter);
    }

    public void showInputLettersKeeper(){
        System.out.println(inputLetterKeeper);
    }
}
