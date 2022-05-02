package com.example.harrypotter_chasing_snitch_androidgame.activities;

import android.os.Bundle;

import com.example.harrypotter_chasing_snitch_androidgame.GameManager;
import com.example.harrypotter_chasing_snitch_androidgame.PlayerCharacter;
import com.example.harrypotter_chasing_snitch_androidgame.R;
import com.example.harrypotter_chasing_snitch_androidgame.Services;

import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;


public class GameActivity extends AppCompatActivity {

    private final int LIVES = 3;
    private final int COL = 3;
    private final int ROW = 5;
    private final int NULL_ICON = 0;
    // ---- TIMER ------
    private Timer timer ;
    private final int DELAY = 1000;
    private enum TIMER_STATUS {
        OFF,
        RUNNING,
        PAUSE
    }
    private TIMER_STATUS timerStatus = TIMER_STATUS.OFF;
    //DIRECTIONS
    private final int UP = 0;
    private final int DOWN = 1;
    private final int RIGHT = 2;
    private final int LEFT = 3;
    private final int STAR_DIRECTION = -1;

    //------ Players -------
    private PlayerCharacter snitch; //the runner
    private PlayerCharacter harryPotter; //the hunter
    private GameManager manager;

    private ImageButton game_BTN_arrowUp;
    private ImageButton game_BTN_arrowDown;
    private ImageButton game_BTN_arrowLeft;
    private ImageButton game_BTN_arrowRight;
    private ImageView[] game_hearts;
    private ImageView[][] game_board;
    private TextView game_LBL_score;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        //init the game players and manager
        initGame();
        //find all views
        findViews();
        //all buttons setOnClickListeners
        initButtons();
        // start the timer
        actionStartTimer();
    }

    private void actionStartTimer() {
        if (timerStatus == TIMER_STATUS.RUNNING) {
            stopTimer();
            timerStatus = TIMER_STATUS.OFF;
        } else {
            startTimer();
        }
    }

    private void startTimer() {
        timerStatus = TIMER_STATUS.RUNNING;
        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                tick();
            }
        }, 0, DELAY);
    }

    private void tick() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                /*
                 * Every tick needs to do
                 * 1)  check which direction the alien moves (player)
                 * 2) to random the direction of the zombie(hunter)
                 * 3) score++
                 */
                characterMovement(snitch, snitch.getDirection());
                randomDirection();
                manager.addScoreEveryTick();
                game_LBL_score.setText("" + manager.getScore());
            }
        });
    }

    private void randomDirection() {
        //TODO to check if random direction cant move to there random again
        characterMovement(harryPotter, (int)(Math.random()*4));
    }

    private void characterMovement(PlayerCharacter c, int currentDirection) {
        switch (currentDirection){
            case UP:
                if(c.getRow() > 0){
                    changeBoard(c, c.getRow()-1,c.getCol());
                    updateUI();
                }
                break;
            case DOWN:
                if(c.getRow() < (ROW-1)){
                    changeBoard(c, c.getRow()+1,c.getCol());
                    updateUI();
                }
                break;
            case RIGHT:
                if(c.getCol() < (COL-1)){
                    changeBoard(c, c.getRow(),c.getCol()+1);
                    updateUI();
                }
                break;
            case LEFT:
                if(c.getCol() > 0){
                    changeBoard(c, c.getRow(),c.getCol()-1);
                    updateUI();
                }
                break;
            default:
                break;
        }
    }

    private void updateUI() {
        isCatchesHim();
        game_LBL_score.setText("" + manager.getScore());
        for (int i = 0; i < game_hearts.length; i++)
            game_hearts[i].setVisibility(manager.getLive() > i ? View.VISIBLE : View.INVISIBLE);
    }

    private void isCatchesHim() {
        if (snitch.getRow() == harryPotter.getRow() && snitch.getCol() == harryPotter.getCol()) {
            new Services(this).doVibrate();
            snitch.setDirection(STAR_DIRECTION);
            manager.reduceLives();
            //TODO check if need to initial score every game of after all life reduces
            //manager.initialScore();
            updateBoardUI();
        }
    }

    private void updateBoardUI() {
        if (manager.isDead()) {
            Toast.makeText(this, "Your Dead", Toast.LENGTH_SHORT).show();
            finishGame();
            return;
        } else
            game_board[snitch.getRow()][snitch.getCol()].setImageResource(NULL_ICON);
        startNewGame();
    }

    private void startNewGame() {
        snitch.setRow(4); snitch.setCol(0);
        game_board[snitch.getRow()][snitch.getCol()].setImageResource(R.drawable.ic_snitch);
        harryPotter.setRow(0); harryPotter.setCol(2);
        game_board[harryPotter.getRow()][harryPotter.getCol()].setImageResource(R.drawable.ic_harry_potter);
    }

    private void finishGame() {
        finish();
    }

    private void changeBoard(PlayerCharacter c, int rowNew, int colNew) {
        game_board[c.getRow()][c.getCol()].setImageResource(NULL_ICON);
        c.setRow(rowNew);
        c.setCol(colNew);
        game_board[rowNew][colNew].setImageResource(c.getIcon());
    }

    private void stopTimer() {
        timer.cancel();
    }

    private void initButtons() {
        game_BTN_arrowUp.setOnClickListener(e -> snitch.setDirection(UP));
        game_BTN_arrowDown.setOnClickListener(e -> snitch.setDirection(DOWN));
        game_BTN_arrowRight.setOnClickListener(e -> snitch.setDirection(RIGHT));
        game_BTN_arrowLeft.setOnClickListener(e -> snitch.setDirection(LEFT));
    }

    private void findViews() {
        game_board = new ImageView[][]{
                {findViewById(R.id.game_IMG_img1), findViewById(R.id.game_IMG_img2), findViewById(R.id.game_IMG_img3)},
                {findViewById(R.id.game_IMG_img4), findViewById(R.id.game_IMG_img5), findViewById(R.id.game_IMG_img6)},
                {findViewById(R.id.game_IMG_img7), findViewById(R.id.game_IMG_img8), findViewById(R.id.game_IMG_img9)},
                {findViewById(R.id.game_IMG_img10), findViewById(R.id.game_IMG_img11), findViewById(R.id.game_IMG_img12)},
                {findViewById(R.id.game_IMG_img13), findViewById(R.id.game_IMG_img14), findViewById(R.id.game_IMG_img15)}
        };
        game_hearts = new ImageView[]{
                findViewById(R.id.game_IMG_heart1),
                findViewById(R.id.game_IMG_heart2),
                findViewById(R.id.game_IMG_heart3)
        };

        game_BTN_arrowUp = findViewById(R.id.game_BTN_arrowUp);
        game_BTN_arrowDown = findViewById(R.id.game_BTN_arrowDown);
        game_BTN_arrowLeft = findViewById(R.id.game_BTN_arrowLeft);
        game_BTN_arrowRight = findViewById(R.id.game_BTN_arrowRight);
        game_LBL_score = findViewById(R.id.game_LBL_score);

        game_LBL_score = findViewById(R.id.game_LBL_score);
    }

    private void initGame() {
        manager = new GameManager(0,LIVES);
        snitch = new PlayerCharacter(4,0,STAR_DIRECTION,R.drawable.ic_snitch);
        harryPotter = new PlayerCharacter(0,2,STAR_DIRECTION,R.drawable.ic_harry_potter);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (timerStatus == TIMER_STATUS.RUNNING) {
            stopTimer();
            timerStatus = TIMER_STATUS.PAUSE;
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (timerStatus == TIMER_STATUS.PAUSE)
            startTimer();
        else
            manager.initialScore();
    }
}