public class HiddenWord {
    private final String hiddenWord;
    private final String mask;


    public HiddenWord (WordsList wordsList){
        this.hiddenWord = setHiddenWord(wordsList);
        this.mask = setMask();

    }

    public String getMask(){
        return mask;
    }

    public String getHiddenWord(){
        return hiddenWord;
    }

    public boolean isMaskFullOpen(String mask){
        return mask.equals(hiddenWord);
    }

    public String openMask(String mask, char input){
        char[] maskArray = mask.toCharArray();
        for (int i = 0; i < hiddenWord.length(); i++) {
            if (input == hiddenWord.charAt(i)){
                maskArray[i] = input;
            }
        }
        return new String(maskArray);
    }

    public boolean isLetterInHiddenWord(char letter){
        return hiddenWord.indexOf(letter) != -1;
    }

    private String setMask(){
        return "_".repeat(hiddenWord.length());
    }

    private String setHiddenWord(WordsList wordsList){
        return wordsList.getRandomWord();
    }


}
