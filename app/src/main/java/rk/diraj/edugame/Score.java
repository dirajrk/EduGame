package rk.diraj.edugame;

// CP3406 Assignment 2 by Diraj Ravikumar (13255244)

public class Score implements Comparable<Score> {

    private String scoreDate;
    public int scoreNum;

    public Score(String date, int num){
        scoreDate=date;
        scoreNum=num;
    }

    public int compareTo(Score sc){
        return sc.scoreNum>scoreNum? 1 : sc.scoreNum<scoreNum? -1 : 0;
    }

    public String getScoreText() {
        return scoreDate+" - "+scoreNum;
    }
}