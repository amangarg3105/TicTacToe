package in.codingninjas.tictactoe;

import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class AIActivity extends AppCompatActivity implements View.OnClickListener {
    public final static int NO_PLAYER = 0;
    public final static int PLAYER_1 = 1;
    public final static int PLAYER_2 = 2;
    public final static int INCOMPLETE = 0;
    public final static int DRAW = 3;
    public final static int PLAYER_1_WINS = 1;
    public final static int PLAYER_2_WINS = 2;
    MyButton[][] buttons;
    LinearLayout mainLayout;
    MyButton b;
    public static int n = 3;
    LinearLayout rowLayouts[];
    boolean computerTurn = true;
    boolean gameOver = false;
    int[][] PseudoMatrix;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i("First Log","Inside On Create");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ai);
        mainLayout = (LinearLayout) findViewById(R.id.layout);
        PseudoMatrix = new int[n][n];
        setUpBoard();
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
                buttons[i][j].x = i;
                buttons[i][j].y = j;
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, 1f);
                params.setMargins(5,5,5,5);
                buttons[i][j].setLayoutParams(params);
                buttons[i][j].setOnClickListener(this);
                buttons[i][j].setTextSize(50);
                buttons[i][j].setTextColor(ContextCompat.getColor(this, R.color.colorPrimary));
                rowLayouts[i].addView(buttons[i][j]);

            }
        }

        int point1 = r.nextInt(n);
        int point2 = r.nextInt(n);
         PseudoMatrix[point1][point2] = PLAYER_1;
        buttons[point1][point2].player = PLAYER_1;
        buttons[point1][point2].setText("O");
        computerTurn = false;
    }

    @Override
    public void onClick(View v) {
        if(gameOver) {
            return;
        }
        MyButton button = (MyButton) v;
        if (!computerTurn) {
            button.player = PLAYER_2;
            PseudoMatrix[button.x][button.y] = PLAYER_2;
            button.setText("X");
            computerTurn = true;
        }
        int status = checkGameStatus();
        if (status == DRAW) {
            Toast.makeText(this, "Draw ", Toast.LENGTH_SHORT).show();
            gameOver = true;
            return;
        } else if (status == PLAYER_1_WINS) {
            Toast.makeText(this, " O WINS ", Toast.LENGTH_SHORT).show();
            gameOver = true;
            return;

        } else if (status == PLAYER_2_WINS) {
            Toast.makeText(this, "X Wins ", Toast.LENGTH_SHORT).show();
            gameOver = true;
            return;
        }

        if(computerTurn) {
            callMinimax(0, 1,true);
            Points p = returnBestMove();
            PseudoMatrix[p.x][p.y] = PLAYER_1;
            buttons[p.x][p.y].player = PLAYER_1;
            buttons[p.x][p.y].setText("O");
        }
        int status2 = checkGameStatus();
        if (status2 == DRAW) {
            Toast.makeText(this, "Draw ", Toast.LENGTH_SHORT).show();
            gameOver = true;
            return;
        } else if (status2 == PLAYER_1_WINS) {
            Toast.makeText(this, " O WINS ", Toast.LENGTH_SHORT).show();
            gameOver = true;
            return;

        } else if (status2 == PLAYER_2_WINS) {
            Toast.makeText(this, "X Wins ", Toast.LENGTH_SHORT).show();
            gameOver = true;
            return;
        }
              computerTurn = !computerTurn;
    }

    private Points returnBestMove() {
        int MAX = -100000;
        int best = -1;

        for (int i = 0; i < rootsChildrenScores.size(); ++i) {
            if (MAX < rootsChildrenScores.get(i).score) {
                MAX = rootsChildrenScores.get(i).score;
                best = i;
            }
        }

        return rootsChildrenScores.get(best).point;
    }

    List<PointsAndScores> rootsChildrenScores;

    public void callMinimax(int depth, int turn,boolean firstMove) {
        rootsChildrenScores = new ArrayList<>();
        minimax(depth, turn,firstMove);
    }
   /* Points computersMove;*/
    private int minimax(int depth, int turn,boolean firstMove) {// Making Decision Tree
        Log.i("Moves","Turn " + turn + "Move " +firstMove);
        int status = checkGameStatus();
        if (status == PLAYER_1_WINS) {
            return +1;
        }
        if (status == PLAYER_2_WINS) {
            return -1;
        }
        List<Points> buttonsAvailable = getButtons();
        if (buttonsAvailable.isEmpty()) {
            return 0;
        }
        if(turn == 1 && firstMove) {
            Log.i("Winning", "Obviously");
            for(int i = 0; i < buttonsAvailable.size(); i++) {
                Points p = buttonsAvailable.get(i);
                placeAMove(p,1);
                if (checkGameStatus() == PLAYER_1_WINS) {
                    Log.i("Winner", "Obviously");
                    rootsChildrenScores.add(new PointsAndScores(100, p));
                    PseudoMatrix[p.x][p.y] = 0;
                    return +1;
                }
                PseudoMatrix[p.x][p.y] = 0;
            }
        }
        List<Integer> scores = new ArrayList<>();
        for (int i = 0; i < buttonsAvailable.size(); i++) {
            Points p = buttonsAvailable.get(i);
            if (turn == 1) {
                placeAMove(p, 1);
                int currentScore = minimax(depth + 1, 2,false);
                scores.add(currentScore);
                if (depth == 0) {
                    rootsChildrenScores.add(new PointsAndScores(currentScore, p));
                }
            } else if (turn == 2) {
                placeAMove(p, 2);
                scores.add(minimax(depth + 1, 1,false));
            }
            PseudoMatrix[p.x][p.y] = 0; // Resetting this point
        }
        return turn == 1 ? returnMax(scores) : returnMin(scores);
      /*  int min = Integer.MAX_VALUE, max = Integer.MIN_VALUE;

        for (int i = 0; i < pointsAvailable.size(); ++i) {
            Points point = pointsAvailable.get(i);
            if (turn == 1) {
                placeAMove(point, 1);
                int currentScore = minimax(depth + 1, 2);
                max = Math.max(currentScore, max);

                if(depth == 0)System.out.println("Score for position "+(i+1)+" = "+currentScore);
                if(currentScore >= 0){ if(depth == 0) computersMove = point;}
                if(currentScore == 1){PseudoMatrix[point.x][point.y] = 0; break;}
                if(i == pointsAvailable.size()-1 && max < 0){if(depth == 0)computersMove = point;}
            } else if (turn == 2) {
                placeAMove(point, 2);
                int currentScore = minimax(depth + 1, 1);
                min = Math.min(currentScore, min);
                if(min == -1){PseudoMatrix[point.x][point.y] = 0; break;}
            }
            PseudoMatrix[point.x][point.y] = 0; //Reset this point
        }
        return turn == 1?max:min;*/

}

    private int returnMin(List<Integer> list) {
        int min = Integer.MAX_VALUE;
        int index = -1;
        for (int i = 0; i < list.size(); ++i) {
            if (list.get(i) < min) {
                min = list.get(i);
                index = i;
            }
        }
        return list.get(index);
    }

    private int returnMax(List<Integer> list) {
        int max = Integer.MIN_VALUE;
        int index = -1;
        for (int i = 0; i < list.size(); ++i) {
            if (list.get(i) > max) {
                max = list.get(i);
                index = i;
            }
        }
        return list.get(index);
    }

    private void placeAMove(Points p, int i) {
        PseudoMatrix[p.x][p.y] = i;
    }

    private List<Points> getButtons() {
        List<Points> available = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (PseudoMatrix[i][j] == 0) {
                    available.add(new Points(i, j));
                }
            }
        }
        return available;
    }

    private int checkGameStatus() {
        // To check for winning condition in Rows
        for (int i = 0; i < n; i++) {
            boolean flag = true;
            for(int j = 0; j < n; j++)  {
                if(PseudoMatrix[i][j] == NO_PLAYER ||
                        PseudoMatrix[i][0] != PseudoMatrix[i][j]){
                    flag = false;
                    break;
                }
            }
            if (flag) {
                if (PseudoMatrix[i][0] == PLAYER_1) {
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
                if (PseudoMatrix[i][j] == NO_PLAYER || PseudoMatrix[0][j] != PseudoMatrix[i][j]) {
                    flag = false;
                    break;
                }
            }
            if (flag) {
                if (PseudoMatrix[0][j] == PLAYER_1) {
                    return PLAYER_1_WINS;
                } else {
                    return PLAYER_2_WINS;
                }
            }

        }

        // To check for winning condition in Diagonal 1
        boolean flag = true;
        for (int i = 0; i < n; i++) {
            if (PseudoMatrix[i][i] == NO_PLAYER || PseudoMatrix[0][0] != PseudoMatrix[i][i]) {
                flag = false;
                break;
            }
        }
        if (flag) {
            if (PseudoMatrix[0][0] == PLAYER_1) {
                return PLAYER_1_WINS;
            } else {
                return PLAYER_2_WINS;
            }
        }

        // To check for winning condition in Diagonal 2
        flag = true;
        for (int i = n - 1; i >= 0; i--) {
            int col = n - 1 - i;
            if (PseudoMatrix[i][col] == NO_PLAYER ||
                    PseudoMatrix[n - 1][0] != PseudoMatrix[i][col]) {
                flag = false;
                break;
            }
        }
        if (flag) {
            if (PseudoMatrix[n - 1][0] == PLAYER_1) {
                return PLAYER_1_WINS;
            } else {
                return PLAYER_2_WINS;
            }
        }

        // To check if game is incomplete
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (PseudoMatrix[i][j] == NO_PLAYER) {
                    return INCOMPLETE;
                }
            }
        }
        return DRAW;
    }
}

class Points {
    int x;
    int y;

    public Points(int x, int y) {
        this.x = x;
        this.y = y;
    }
}

class PointsAndScores {

    int score;
    Points point;

    PointsAndScores(int score, Points point) {
        this.score = score;
        this.point = point;
    }
}
