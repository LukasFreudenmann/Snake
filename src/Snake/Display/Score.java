package Snake.Display;

public class Score {

    private int score;

    /**
     * add one point to the score
     */
    public void addScore(){
        score++;
    }

    /**
     * adds points to score
     * @param number Amount of points
     */
    public void addScore(int number){
        score += number;
    }

    // getter
    public int getScore() {
        return score;
    }
}