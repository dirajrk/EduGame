package rk.diraj.edugame;

// CP3406 Assignment 2 by Diraj Ravikumar (13255244)

public class Score implements Comparable<Score> {

    private String scoreDate;
    public int scoreNum;

    public Score(String date, int num){
        // Scores are stored with their score number and the date the score was achieved
        scoreDate=date;
        scoreNum=num;
    }

    public int compareTo(Score sc){
        // Compared for any conflicting scores
        return sc.scoreNum>scoreNum? 1 : sc.scoreNum<scoreNum? -1 : 0;
    }

    public String getScoreText() {
        // Score is stored in the format of DD/MM/YYYY - 24
        return scoreDate+" - "+scoreNum;
    }
}