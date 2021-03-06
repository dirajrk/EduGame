package rk.diraj.edugame;

// CP3406 Assignment 2 by Diraj Ravikumar (13255244)

import android.media.MediaPlayer;
import android.app.Activity;
import java.util.Random;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import android.content.SharedPreferences;
import android.widget.Toast;

public class PlayActivity extends Activity implements OnClickListener {
    // Behind the scenes on how the game works
    MediaPlayer mediaPlayer;

    private int level = 0, answer = 0, operator = 0, operand1 = 0, operand2 = 0;

    private final int ADD_OPERATOR = 0, SUBTRACT_OPERATOR = 1, MULTIPLY_OPERATOR = 2, DIVIDE_OPERATOR = 3;

    private String[] operators = {"+", "-", "x", "/"};

    private int[][] levelMin = {{1, 11, 21},{1, 5, 10},{2, 5, 10},{2, 3, 5}};
    private int[][] levelMax = {{100, 250, 500},{100, 200, 300},{50, 100, 150},{100, 500, 1000}};

    private Random random;

    private TextView question, answerTxt, scoreTxt;
    private ImageView response;
    private Button btn1, btn2, btn3, btn4, btn5, btn6, btn7, btn8, btn9, btn0, enterBtn, clearBtn;

    private SharedPreferences gamePrefs;
    // User selected difficulty is stored here
    public static final String GAME_PREFS = "ArithmeticFile";

    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_play);

        // Song to play
        mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.play_theme);
        // Start the song
        mediaPlayer.start();

        // Settings where user can change difficulty level
        gamePrefs = getSharedPreferences(GAME_PREFS, 0);

        question = (TextView)findViewById(R.id.question);
        answerTxt = (TextView)findViewById(R.id.answer);
        response = (ImageView)findViewById(R.id.response);
        scoreTxt = (TextView)findViewById(R.id.score);

        // Shows correct or incorrect image on top left along with a toast
        response.setVisibility(View.INVISIBLE);

        // Buttons where users enter their answer
        btn1 = (Button)findViewById(R.id.btn1);
        btn2 = (Button)findViewById(R.id.btn2);
        btn3 = (Button)findViewById(R.id.btn3);
        btn4 = (Button)findViewById(R.id.btn4);
        btn5 = (Button)findViewById(R.id.btn5);
        btn6 = (Button)findViewById(R.id.btn6);
        btn7 = (Button)findViewById(R.id.btn7);
        btn8 = (Button)findViewById(R.id.btn8);
        btn9 = (Button)findViewById(R.id.btn9);
        btn0 = (Button)findViewById(R.id.btn0);
        enterBtn = (Button)findViewById(R.id.enter);
        clearBtn = (Button)findViewById(R.id.clear);

        btn1.setOnClickListener(this);
        btn2.setOnClickListener(this);
        btn3.setOnClickListener(this);
        btn4.setOnClickListener(this);
        btn5.setOnClickListener(this);
        btn6.setOnClickListener(this);
        btn7.setOnClickListener(this);
        btn8.setOnClickListener(this);
        btn9.setOnClickListener(this);
        btn0.setOnClickListener(this);
        enterBtn.setOnClickListener(this);
        clearBtn.setOnClickListener(this);

        if(savedInstanceState!=null){
            //restore state
            level=savedInstanceState.getInt("level");
            int exScore = savedInstanceState.getInt("score");
            scoreTxt.setText("Score: "+exScore);
        }
        else{
            Bundle extras = getIntent().getExtras();
            if(extras !=null)
            {
                int passedLevel = extras.getInt("level", -1);
                if(passedLevel>=0) level = passedLevel;
            }
        }

        random = new Random();
        chooseQuestion();
    }
    private void chooseQuestion(){
        // Questions are randomly generated based on the numbers stored in levelMin and levelMax

        answerTxt.setText("= ?");

        operator = random.nextInt(operators.length);

        operand1 = getOperand();
        operand2 = getOperand();

        if(operator == SUBTRACT_OPERATOR){
            while(operand2>operand1){
                operand1 = getOperand();
                operand2 = getOperand();
            }
        }

        else if(operator==DIVIDE_OPERATOR){
            while((((double)operand1/(double)operand2)%1 > 0) || (operand1==operand2)) {
                operand1 = getOperand();
                operand2 = getOperand();
            }
        }
        switch(operator) {
            case ADD_OPERATOR:
                answer = operand1+operand2;
                break;
            case SUBTRACT_OPERATOR:
                answer = operand1-operand2;
                break;
            case MULTIPLY_OPERATOR:
                answer = operand1*operand2;
                break;
            case DIVIDE_OPERATOR:
                answer = operand1/operand2;
                break;
            default:
                break;
        }
        question.setText(operand1+" "+operators[operator]+" "+operand2);
    }

    private int getOperand(){
        // Return operand number
        return random.nextInt(levelMax[operator][level] - levelMin[operator][level] + 1) + levelMin[operator][level];
    }

    @Override
    public void onClick(View view) {

        if(view.getId()==R.id.enter){
            // Enter button and answer gets matched with the question
            String answerContent = answerTxt.getText().toString();

            if(!answerContent.endsWith("?")) {
                int enteredAnswer = Integer.parseInt(answerContent.substring(2));

                int exScore = getScore();

                if(enteredAnswer==answer){
                    // Correct answer increments 1 point and shows the tick image and a toast is prompted
                    scoreTxt.setText("Score: "+(exScore+1));
                    response.setImageResource(R.drawable.tick);
                    response.setVisibility(View.VISIBLE);
                    Toast.makeText(getApplicationContext(), "Correct", Toast.LENGTH_LONG).show();
                }else{
                    // Incorrect answer deducts 1 point and shows the cross image and a toast is prompted
                    scoreTxt.setText("Score: "+(exScore-1));
                    response.setImageResource(R.drawable.cross);
                    response.setVisibility(View.VISIBLE);
                    Toast.makeText(getApplicationContext(), "Incorrect", Toast.LENGTH_LONG).show();
                }
                chooseQuestion();
            }
        }
        else if(view.getId()==R.id.clear){
            // Answer gets replaced with "= ?" if any incorrect number buttons are pressed and clear button is pressed to erase your mistakes
            answerTxt.setText("= ?");

        }
        else {
            // Sets the image on top left to a invisible and resets to a normal question
            response.setVisibility(View.INVISIBLE);

            int enteredNum = Integer.parseInt(view.getTag().toString());

            if(answerTxt.getText().toString().endsWith("?"))
                answerTxt.setText("= " + enteredNum);
            else
                answerTxt.append("" + enteredNum);

            setHighScore();
        }
    }
    private int getScore(){
        // Retrieves score as the user enters the answer and plays the game
        String scoreStr = scoreTxt.getText().toString();
        return Integer.parseInt(scoreStr.substring(scoreStr.lastIndexOf(" ")+1));
    }

    private void setHighScore(){
        //Setting the high score and storing it
        int exScore = getScore();
        if(exScore>0){
            //We have a valid score
            SharedPreferences.Editor scoreEdit = gamePrefs.edit();
            DateFormat dateForm = new SimpleDateFormat("dd MMMM yyyy");
            String dateOutput = dateForm.format(new Date());
            String scores = gamePrefs.getString("highScores", "");

            if(scores.length()>0){
                // If there is an existing score, gets replaced or appended
                List<Score> scoreStrings = new ArrayList<Score>();
                String[] exScores = scores.split("\\|");
                for(String eSc : exScores){
                    String[] parts = eSc.split(" - ");
                    scoreStrings.add(new Score(parts[0], Integer.parseInt(parts[1])));
                }
                Score newScore = new Score(dateOutput, exScore);
                scoreStrings.add(newScore);

                Collections.sort(scoreStrings);

                StringBuilder scoreBuild = new StringBuilder("");
                for(int s=0; s<scoreStrings.size(); s++){
                    if(s>=10) break;//Only stores top 10 high scores
                    if(s>0) scoreBuild.append("|");//Pipe separates the score strings instead of combining them into a mess
                    scoreBuild.append(scoreStrings.get(s).getScoreText());
                }
                //Write to score prefs
                scoreEdit.putString("highScores", scoreBuild.toString());
                scoreEdit.commit();
            }
            else{
                // If no existing scores
                // Gets appended with new high score
                scoreEdit.putString("highScores", ""+dateOutput+" - "+exScore);
                scoreEdit.commit();
            }
        }
    }

    @Override
    public void onPause() {
        // Pauses the song
        super.onPause();
        if(mediaPlayer.isPlaying())
            mediaPlayer.pause();
    }

    @Override
    public void onResume() {
        // Resumes the song
        super.onResume();
        if (!mediaPlayer.isPlaying())
            mediaPlayer.start();
    }

    protected void onDestroy(){
        // Save the current score
        setHighScore();
        super.onDestroy();
        mediaPlayer.stop();
        mediaPlayer.release();
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        // Update on Score Activity if its a high score
        int exScore = getScore();
        savedInstanceState.putInt("score", exScore);
        savedInstanceState.putInt("level", level);
        super.onSaveInstanceState(savedInstanceState);
    }

}
