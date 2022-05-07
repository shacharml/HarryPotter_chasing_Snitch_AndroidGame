package com.example.harrypotter_chasing_snitch_androidgame.activities;

import static com.example.harrypotter_chasing_snitch_androidgame.activities.MenuActivity.SENSOR_MODE;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.harrypotter_chasing_snitch_androidgame.R;
import com.example.harrypotter_chasing_snitch_androidgame.logic.Data;
import com.example.harrypotter_chasing_snitch_androidgame.logic.GameManager;
import com.example.harrypotter_chasing_snitch_androidgame.logic.Location;
import com.example.harrypotter_chasing_snitch_androidgame.logic.PlayerCharacter;
import com.example.harrypotter_chasing_snitch_androidgame.logic.sortScore;
import com.example.harrypotter_chasing_snitch_androidgame.utilities.GameUtils;
import com.example.harrypotter_chasing_snitch_androidgame.utilities.MSP;
import com.google.gson.Gson;

import java.util.Collections;
import java.util.Timer;
import java.util.TimerTask;


public class GameActivity extends AppCompatActivity {

    private final int LIVES = 3;
    private final int COL = 5;
    private final int ROW = 7;
    private final int NULL_ICON = 0;
    private final int DELAY = 1000;
    //DIRECTIONS
    private final int UP = 0;
    private final int DOWN = 1;
    private final int RIGHT = 2;
    private final int LEFT = 3;
    private final int START_DIRECTION = -1;
    // ---- TIMER ------
    private Timer timer;
    private TIMER_STATUS timerStatus = TIMER_STATUS.OFF;
    //------ Players -------
    private PlayerCharacter snitch; //the runner
    private PlayerCharacter harryPotter; //the hunter
    private PlayerCharacter magicWand; //the magic wand - Coin
    private GameManager manager;
    private ImageButton game_BTN_arrowUp;
    private ImageButton game_BTN_arrowDown;
    private ImageButton game_BTN_arrowLeft;
    private ImageButton game_BTN_arrowRight;
    private ImageView[] game_hearts;
    private ImageView[][] game_board;
    private TextView game_LBL_score;
    //----------- Sensor Game -------------
    private SensorManager sensorManager;
    private Sensor sensor;
    private SensorEventListener listenerSensor;
    private String modeGame;
    private boolean isCollect = true;
    //Data Manager
    private Data data;
    private Location location;
    private double lat, lng;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        modeGame = getIntent().getExtras().getString("modeGame");
        //init the game players and manager
        initGame();
        //find all views
        findViews();

        if (modeGame.equalsIgnoreCase(SENSOR_MODE)) {
            initSensors();
        } else {
            //all buttons setOnClickListeners
            initButtons();
        }
        // start the timer
        actionStartTimer();
    }

    /**
     * The sensors game function
     */
    private void initSensors() {
        //set the visibility of the buttons
        game_BTN_arrowUp.setVisibility(View.INVISIBLE);
        game_BTN_arrowDown.setVisibility(View.INVISIBLE);
        game_BTN_arrowLeft.setVisibility(View.INVISIBLE);
        game_BTN_arrowRight.setVisibility(View.INVISIBLE);
        //init the sensors
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        accelerometerListener();
    }

    private void accelerometerListener() {
        listenerSensor = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent sensorEvent) {
                float x = sensorEvent.values[0];
                float y = sensorEvent.values[1];

                // The player direction - sensors instead of click buttons
                if (x < -6) {
                    snitch.setDirection(RIGHT);
                } else if (x > 6) {
                    snitch.setDirection(LEFT);
                } else if (y < -2) {
                    snitch.setDirection(UP);
                } else if (y > 6) {
                    snitch.setDirection(DOWN);
                }
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int i) {

            }
        };
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
                 * 1) check which direction the alien moves (player)
                 * 2) to random the direction of the zombie(hunter)
                 * 3) score++
                 * 4) every 10 second add a coin on screen - if collect +10 to score
                 */
                characterMovement(snitch, snitch.getDirection());
                randomDirection();
                manager.addScoreEveryTick();

                //every 5 second the magic wand disappear else is collected
                if (manager.getScore() % 5 == 0 && !isCollect && manager.getScore() > 10) {
                    game_board[magicWand.getRow()][magicWand.getCol()].setImageResource(NULL_ICON);
                    isCollect = true;
                }
                // after 10 second
                if (manager.getScore() % 10 == 0 ) {
                    showMagicWandRandomly();
                    isCollect = false;
                }
                game_LBL_score.setText("" + manager.getScore());
            }
        });
    }

    private void showMagicWandRandomly() {
        game_board[magicWand.getRow()][magicWand.getCol()].setImageResource(NULL_ICON);
        magicWand.setRow((int) (Math.random() * ROW));
        magicWand.setCol((int) (Math.random() * COL));

        if ((magicWand.getRow() == snitch.getRow() && magicWand.getCol() == snitch.getCol()) ||
                (magicWand.getRow() == harryPotter.getRow() && magicWand.getCol() == harryPotter.getCol()))
            showMagicWandRandomly();
        else
            game_board[magicWand.getRow()][magicWand.getCol()].setImageResource(magicWand.getIcon());
    }

    private void randomDirection() {
        characterMovement(harryPotter, (int) (Math.random() * 4));
    }

    private void characterMovement(PlayerCharacter c, int currentDirection) {
        switch (currentDirection) {
            case UP:
                if (c.getRow() > 0) {
                    changeBoard(c, c.getRow() - 1, c.getCol());
                    updateUI();
                }
                break;
            case DOWN:
                if (c.getRow() < (ROW - 1)) {
                    changeBoard(c, c.getRow() + 1, c.getCol());
                    updateUI();
                }
                break;
            case RIGHT:
                if (c.getCol() < (COL - 1)) {
                    changeBoard(c, c.getRow(), c.getCol() + 1);
                    updateUI();
                }
                break;
            case LEFT:
                if (c.getCol() > 0) {
                    changeBoard(c, c.getRow(), c.getCol() - 1);
                    updateUI();
                }
                break;
            default:
                break;
        }
    }

    private void updateUI() {
        isCatchesHim();
        isCollectMagicWand();
        game_LBL_score.setText("" + manager.getScore());
        for (int i = 0; i < game_hearts.length; i++)
            game_hearts[i].setVisibility(manager.getLive() > i ? View.VISIBLE : View.INVISIBLE);
    }

    private void isCollectMagicWand() {
        if (magicWand.getRow() == snitch.getRow() && magicWand.getCol() == snitch.getCol() && !isCollect) {
            GameUtils.getInstance().doVibrate(500);
            GameUtils.getInstance().playSound(R.raw.sound_magic_wand);
            manager.collectMagicWand();
            game_board[magicWand.getRow()][magicWand.getCol()].setImageResource(snitch.getIcon());
            isCollect = true;
        } else if (magicWand.getRow() == harryPotter.getRow() && magicWand.getCol() == harryPotter.getCol() && !isCollect) {
            GameUtils.getInstance().ToastLONG("Harry take your magic wand :(", this);
            GameUtils.getInstance().playSound(R.raw.sound_magic_wand);
            game_board[magicWand.getRow()][magicWand.getCol()].setImageResource(harryPotter.getIcon());
            isCollect = true;
        }
    }

    private void isCatchesHim() {
        if (snitch.getRow() == harryPotter.getRow() && snitch.getCol() == harryPotter.getCol()) {
            GameUtils.getInstance().playSound(R.raw.sound_crush);
            GameUtils.getInstance().doVibrate(500);
            snitch.setDirection(START_DIRECTION);
            manager.reduceLives();
            //todo check if need to initial score every game of after all life reduces
            //manager.initialScore();
            updateBoardUI();
        }
    }

    private void updateBoardUI() {
        if (manager.isDead()) {
            GameUtils.getInstance().ToastLONG("Your Dead", this);
            finishGame();
            return;
        } else {
            game_board[snitch.getRow()][snitch.getCol()].setImageResource(NULL_ICON);
            game_board[harryPotter.getRow()][harryPotter.getCol()].setImageResource(NULL_ICON);
            startNewGame();
        }
    }

    private void startNewGame() {
        snitch.setRow(6);
        snitch.setCol(0);
        game_board[snitch.getRow()][snitch.getCol()].setImageResource(R.drawable.ic_snitch);
        harryPotter.setRow(0);
        harryPotter.setCol(4);
        game_board[harryPotter.getRow()][harryPotter.getCol()].setImageResource(R.drawable.ic_harry_potter);
        isCollect = false;
    }

    private void finishGame() {
        //----- Get current Location use permission and check -----
        currentLocation();
        //need to forward the data to next activity
        manager.saveRecord(lat, lng);
        saveRecords();
        Intent intent = new Intent(GameActivity.this, TopTenActivity.class);
        Bundle bundle = new Bundle();
        bundle.putBoolean("isFromGame", true);
        bundle.putString("gameMode", modeGame);
        bundle.putString("userName", manager.getUserName());
        intent.putExtras(bundle);
        startActivity(intent);
        finish();
    }

    private void currentLocation() {
        try {
            if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 101);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        // * Start of Location Service
        location = new Location(GameActivity.this);
        if (location.canGetLocation()) {
            lat = location.getLatitude();
            lng = location.getLongitude();
        } else {
            location.showSettingsAlert();
        }
        // * End of Location Service
    }

    private void saveRecords() {
        String jason = MSP.getMe().getString("MY_RECORDS", "");
        data = new Gson().fromJson(jason, Data.class);
        data.getRecords().add(manager.getRecord());
        Collections.sort(data.getRecords(), new sortScore());
        data.cleanUp();
        String js = new Gson().toJson(data);
        MSP.getMe().putString("MY_RECORDS", js);
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

    /**
     * The Buttons game function
     */
    private void initButtons() {
        game_BTN_arrowUp.setOnClickListener(e -> snitch.setDirection(UP));
        game_BTN_arrowDown.setOnClickListener(e -> snitch.setDirection(DOWN));
        game_BTN_arrowRight.setOnClickListener(e -> snitch.setDirection(RIGHT));
        game_BTN_arrowLeft.setOnClickListener(e -> snitch.setDirection(LEFT));
    }

    private void findViews() {
        game_board = new ImageView[][]{
                {findViewById(R.id.game_IMG_img1), findViewById(R.id.game_IMG_img2), findViewById(R.id.game_IMG_img3), findViewById(R.id.game_IMG_img4), findViewById(R.id.game_IMG_img5)},
                {findViewById(R.id.game_IMG_img6), findViewById(R.id.game_IMG_img7), findViewById(R.id.game_IMG_img8), findViewById(R.id.game_IMG_img9), findViewById(R.id.game_IMG_img10)},
                {findViewById(R.id.game_IMG_img11), findViewById(R.id.game_IMG_img12), findViewById(R.id.game_IMG_img13), findViewById(R.id.game_IMG_img14), findViewById(R.id.game_IMG_img15)},
                {findViewById(R.id.game_IMG_img16), findViewById(R.id.game_IMG_img17), findViewById(R.id.game_IMG_img18), findViewById(R.id.game_IMG_img19), findViewById(R.id.game_IMG_img20)},
                {findViewById(R.id.game_IMG_img21), findViewById(R.id.game_IMG_img22), findViewById(R.id.game_IMG_img23), findViewById(R.id.game_IMG_img24), findViewById(R.id.game_IMG_img25)},
                {findViewById(R.id.game_IMG_img26), findViewById(R.id.game_IMG_img27), findViewById(R.id.game_IMG_img28), findViewById(R.id.game_IMG_img29), findViewById(R.id.game_IMG_img30)},
                {findViewById(R.id.game_IMG_img31), findViewById(R.id.game_IMG_img32), findViewById(R.id.game_IMG_img33), findViewById(R.id.game_IMG_img34), findViewById(R.id.game_IMG_img35)}
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
        manager = new GameManager(0, LIVES);
        manager.setUserName(getIntent().getExtras().getString("userName"));
        Log.d("Game", "1 " + getIntent().getExtras().getString("userName"));
        Log.d("Game", "2 " + manager.getUserName());
        snitch = new PlayerCharacter(6, 0, START_DIRECTION, R.drawable.ic_snitch);
        harryPotter = new PlayerCharacter(0, 4, START_DIRECTION, R.drawable.ic_harry_potter);
        magicWand = new PlayerCharacter(0, 0, START_DIRECTION, R.drawable.ic_magic_wand);
        isCollect = false;
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

    @Override
    protected void onResume() {
        super.onResume();
        if (modeGame.equalsIgnoreCase(SENSOR_MODE))
            sensorManager.registerListener(listenerSensor, sensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (modeGame.equalsIgnoreCase(SENSOR_MODE))
            sensorManager.unregisterListener(listenerSensor);
    }

    private enum TIMER_STATUS {
        OFF,
        RUNNING,
        PAUSE
    }
}