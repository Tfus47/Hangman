import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class WordsList {
    private final Random random = new Random();
    private final List<String> words = new ArrayList<>();
    private final InputPlayer inputPlayer;

    public WordsList(String filePath, InputPlayer inputPlayer){
        this.inputPlayer = inputPlayer;
        setWordsList(filePath);
    }

    public String getRandomWord(){
        int index = words.size();
        return words.get(random.nextInt(index));
    }

    private void setWordsList(String filePath) {
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

    private void setDifferentPath() {
        System.out.println("О, кажется отсутствует текстовый файл со словарем");
        System.out.print("Укажите вручную путь к текстовому файлу: ");
        String filePath = inputPlayer.getInputPlayer();
        setWordsList(filePath);
    }

}
