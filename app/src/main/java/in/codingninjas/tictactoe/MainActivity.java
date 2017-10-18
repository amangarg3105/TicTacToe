package in.codingninjas.tictactoe;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.Random;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    public final static int NO_PLAYER = 0;
    public final static int PLAYER_1 = 1;
    public final static int PLAYER_2 = 2;
    public final static int INCOMPLETE = 0;
    public final static int  DRAW = 3;
    public final static int PLAYER_1_WINS = 1;
    public final static int PLAYER_2_WINS = 2;
    MyButton[][] buttons;
    LinearLayout mainLayout;
    public static int n = 3;
    LinearLayout rowLayouts[];
    boolean player1Turn = true;
    boolean gameOver = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mainLayout = (LinearLayout) findViewById(R.id.mainLayout);
        setUpBoard();
    }

    void resetBoard() {
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                buttons[i][j].player = NO_PLAYER;
                buttons[i][j].setText("");
            }
        }
        player1Turn = true;
        gameOver = false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if(id == R.id.newGame){
           resetBoard();
        }else if(id == R.id.settings){
         //   Math.random()
            Intent i = new Intent(MainActivity.this,AIActivity.class);
            startActivity(i);
            finish();

        }
        return true;
    }

    public void setUpBoard(){
        buttons = new MyButton[n][n];
        rowLayouts = new LinearLayout[n];
        mainLayout.removeAllViews();
        Random r = new Random();
        for(int i = 0; i < n; i++){
            rowLayouts[i] = new LinearLayout(this);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,0, 1f);
            params.setMargins(5,5,5,5);
            rowLayouts[i].setLayoutParams(params);
            rowLayouts[i].setOrientation(LinearLayout.HORIZONTAL);
            mainLayout.addView(rowLayouts[i]);
        }

        for(int i = 0; i < n; i++){
            for(int j = 0; j < n; j++){
                buttons[i][j] = new MyButton(this);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, 1f);
                params.setMargins(5,5,5,5);
                buttons[i][j].setLayoutParams(params);
                buttons[i][j].setOnClickListener(this);
                buttons[i][j].setTextSize(50);
                buttons[i][j].setTextColor(ContextCompat.getColor(this, R.color.colorPrimary));
                rowLayouts[i].addView(buttons[i][j]);

            }
        }

    }
    @Override
    public void onClick(View v) {

        if(gameOver){
            return;
        }

        MyButton button = (MyButton) v;

        if(button.player != NO_PLAYER){
            Toast.makeText(this, "Invalid Move ", Toast.LENGTH_SHORT).show();
            return;
        }
        if(player1Turn){
            button.player = PLAYER_1;

            button.setText("O");
        }else{
            button.player = PLAYER_2;
            button.setText("X");
        }

        int status = checkGameStatus();
        if(status == DRAW){
            Toast.makeText(this, "Draw ", Toast.LENGTH_SHORT).show();
            gameOver = true;
        }else if(status == PLAYER_1_WINS){
            Toast.makeText(this, " O WINS ", Toast.LENGTH_SHORT).show();
            gameOver = true;

        }else if(status == PLAYER_2_WINS){
            Toast.makeText(this, "X Wins ", Toast.LENGTH_SHORT).show();
            gameOver = true;
        }

        player1Turn = !player1Turn;

    }

    private int checkGameStatus() {

        // To check for winning condition in Rows
        for (int i = 0; i < n; i++) {
          boolean flag = true;
          for(int j = 0; j < n; j++)  {
              if(buttons[i][j].getPlayer() == NO_PLAYER ||
                      buttons[i][0].getPlayer() != buttons[i][j].getPlayer()){
                  flag = false;
                  break;
              }
          }
            if (flag) {
                if (buttons[i][0].getPlayer() == PLAYER_1) {
                    return PLAYER_1_WINS;
                } else {
                    return PLAYER_2_WINS;
                }
            }
        }

        // To check for winning condition in Columns
        for (int j = 0; j < n; j++) {
            boolean flag = true;
            for (int i = 0; i < n; i++) {
                if (buttons[i][j].getPlayer() == NO_PLAYER || buttons[0][j].getPlayer() != buttons[i][j].getPlayer()) {
                    flag = false;
                    break;
                }
            }
            if (flag) {
                if (buttons[0][j].getPlayer() == PLAYER_1) {
                    return PLAYER_1_WINS;
                } else {
                    return PLAYER_2_WINS;
                }
            }

        }

        // To check for winning condition in Diagonal 1
        boolean flag = true;
        for (int i = 0; i < n; i++) {
            if (buttons[i][i].getPlayer() == NO_PLAYER || buttons[0][0].getPlayer() != buttons[i][i].getPlayer()) {
                flag = false;
                break;
            }
        }
        if (flag) {
            if (buttons[0][0].getPlayer() == PLAYER_1) {
                return PLAYER_1_WINS;
            } else {
                return PLAYER_2_WINS;
            }
        }

        // To check for winning condition in Diagonal 2
        flag = true;
        for (int i = n - 1; i >= 0; i--) {
            int col = n - 1 - i;
            if (buttons[i][col].getPlayer() == NO_PLAYER ||
                    buttons[n - 1][0].getPlayer() != buttons[i][col].getPlayer()) {
                flag = false;
                break;
            }
        }
        if (flag) {
            if (buttons[n - 1][0].getPlayer() == PLAYER_1) {
                return PLAYER_1_WINS;
            } else {
                return PLAYER_2_WINS;
            }
        }

        // To check if game is incomplete
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (buttons[i][j].getPlayer() == NO_PLAYER) {
                    return INCOMPLETE;
                }
            }
        }
        return DRAW;



    }
}
