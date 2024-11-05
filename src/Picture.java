import java.util.List;

public class Picture {

    public void showPicture(int pictureNumber){
        System.out.println(getPictureHangman().get(pictureNumber));
    }

    private List<String> getPictureHangman() {
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
}
