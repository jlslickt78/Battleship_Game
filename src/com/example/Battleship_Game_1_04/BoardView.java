package com.example.Battleship_Game_1_04;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * Created by Jeff Tartt on 2/27/2015.
 */
public class BoardView extends ImageView {
    int border = 10; // Width of the outer border
    int fontSize = 20;

    int attackBoardX = border;
    int attackBoardY = 50;

    int defenceBoardX;
    int defenceBoardY;

    Point[][] grid = new Point[11][11];
    Point[][] defendingGrid = new Point[11][11];
    String[] letters = {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J"};
    String[] numbers = {" 1", " 2", " 3", " 4", " 5", " 6", " 7", " 8", " 9", "10"};

    private static Paint paint;
    Point origin = new Point(this.getLeft(), this.getTop());

    public BoardView(Context context, AttributeSet attrs) {
        super(context, attrs);

        paint = new Paint();
        paint.setStrokeWidth(10);
        //paint.setColor(Color.BLUE);
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        paint.setTextSize(fontSize);
    }

    // all calculations from drawing should be in this method
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        this.getLeft();

        // Get screen measurements
        int screenHeight = this.getMeasuredHeight();
        int screenWidth = this.getMeasuredWidth();

        // set board heights
        int middle = screenHeight / 3 + 50;

        // attack board size
        int attackBoardWidth = screenWidth - border;
        int attackBoardHeight = middle;

        // attackBoardX = 10, attackBoardY = 50
        // drawRect( from left, from top, width, height)
        // draw attack board
        // start 10 from left and 50 from top
        // width = the screenWidth - 10
        // height = screenHeight / 3
        paint.setColor(Color.BLUE);
        // position (x, y, width, height)
        canvas.drawRect(attackBoardX, attackBoardY, attackBoardWidth, attackBoardHeight, paint);

        // Calculate cell width based on screen width
        int cellWidth = (screenWidth / 11) - 1;
        int cellHeight = (middle - border) / 12; // 10 cells + 1 for letters and 1 for Attack/Defend Boards

        paint.setColor(Color.GREEN);
        paint.setStrokeWidth(2);

        // If gameStarted draw text and cells, or lines
//        if (Game.gameStarted) {

        // set cell dimensions
        Game.gameGrid[0][0].setCellHeight(cellHeight);
        Game.gameGrid[0][0].setCellWidth(cellWidth);
        Game.gameGrid[0][0].setViewOrigin(origin);

        // Attack grid
        for (int attackY = 0; attackY < 11; attackY++) {
            for (int attackX = 0; attackX < 11; attackX++) { // TODO: add the points to the gameGrid
                Game.gameGrid[attackX][attackY].setTopleft(new Point(attackX * cellWidth + border, attackY * cellHeight + attackBoardY));
                Game.gameGrid[attackX][attackY].setBottomright(new Point((attackX + 1) * cellWidth + border, (attackY + 1) * cellHeight + attackBoardX));
            }
        }

        // Draw attack board lines
        // drawLine (float startX, float startY, float stopX, float stopY, Paint paint)
        for (int i = 0; i < 11; i++) {
            canvas.drawLine(i * cellWidth + border, attackBoardY, i * cellWidth + border, middle, paint); // Vertical Lines
            canvas.drawLine(border, i * cellHeight + attackBoardY, screenWidth - border, i * cellHeight + attackBoardY, paint); // Horizontal Lines
        }

        // draw board title
        paint.setStrokeWidth(2);
        paint.setColor(Color.WHITE);
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        canvas.drawText("Attacking Board", 40, 40, paint);

        float w = paint.measureText(numbers[0], 0, numbers[0].length());
        float center = (cellWidth / 2) - (w / 2);

        // draw letters and numbers for columns and rows
        for (int attackNumbersX = 0; attackNumbersX < 10; attackNumbersX++) {
            canvas.drawText(
                    numbers[attackNumbersX],
                    Game.gameGrid[attackNumbersX][0].getTopleft().x + center + cellWidth,
                    Game.gameGrid[attackNumbersX][0].getTopleft().y + fontSize + border,
                    paint
            );
        }

        for (int attackLettersY = 0; attackLettersY < 10; attackLettersY++) {
            canvas.drawText(
                    letters[attackLettersY],
                    Game.gameGrid[0][attackLettersY].getTopleft().x + center,
                    Game.gameGrid[0][attackLettersY + 1].getTopleft().y + cellHeight - border,
                    paint
            );
        }

        // set font color white
        paint.setColor(Color.WHITE);

        // Draw the contents of the attack grid
        for (int y = 0; y < 11; y++) {
            for (int x = 0; x < 11; x++) {
                if (Game.gameGrid[x][y].getHas_ship()) {
                    drawCell("S", x, y, center, canvas);
                }
                if (Game.gameGrid[x][y].getWaiting()) {
                    drawCell("W", x, y, center, canvas);
                }

                if (Game.gameGrid[x][y].getMiss()) {
                    drawCell("M", x, y, center, canvas);
                }

                if (Game.gameGrid[x][y].getHit()) {
                    drawCell("H", x, y, center, canvas);
                }
            }
        }


        // set position and size of defence board;
        defenceBoardX = border;
        defenceBoardY = middle + 50;
        int defenceBoardWidth = attackBoardWidth;
        int defenceBoardHeight = middle + middle;

        // draw defence board
        // start 10 from left and 50 from bottom of attack board
        // width = the screenWidth - 10
        // height = screenHeight / 3
        paint.setColor(Color.RED);
        canvas.drawRect(defenceBoardX, defenceBoardY, defenceBoardWidth, defenceBoardHeight, paint);

        paint.setColor(Color.GREEN);
        paint.setStrokeWidth(2);

        // set defence cell dimensions
        Game.defendingGameGrid[0][0].setCellHeight(cellHeight);
        Game.defendingGameGrid[0][0].setCellWidth(cellWidth);
        Game.defendingGameGrid[0][0].setViewOrigin(origin);

        // Defence grid
        for (int defenceY = 0; defenceY < 11; defenceY++) {
            for (int defenceX = 0; defenceX < 11; defenceX++) { // TODO: add the points to the gameGrid
                Game.defendingGameGrid[defenceX][defenceY].setTopleft(new Point(defenceX * cellWidth + border, defenceY * cellHeight + defenceBoardY));
                Game.defendingGameGrid[defenceX][defenceY].setBottomright(new Point((defenceX + 1) * cellWidth + border, (defenceY + 1) * cellHeight + defenceBoardX));
            }
        }

        // Draw defence board lines
        // drawLine (float startX, float startY, float stopX, float stopY, paint)
        for (int j = 0; j < 11; j++) {
            canvas.drawLine(j * cellWidth + border, middle + 50, j * cellWidth + border, middle + middle, paint); // Vertical Lines
            canvas.drawLine(border, j * cellHeight + defenceBoardY, screenWidth - border, j * cellHeight + defenceBoardY, paint); // Horizontal Lines
        }

        // draw board title
        paint.setStrokeWidth(2);
        paint.setColor(Color.WHITE);
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        // drawText("TEXT", x, y, paint)
        canvas.drawText("Defending Board", 40, middle + 40, paint);

        // draw letters and numbers for columns and rows
        for (int x = 0; x < 10; x++) {
            canvas.drawText(numbers[x], Game.defendingGameGrid[x][0].getTopleft().x + center + cellWidth, Game.defendingGameGrid[x][0].getTopleft().y + fontSize + border, paint);
        }

        for (int y = 0; y < 10; y++) {
            canvas.drawText(letters[y], Game.defendingGameGrid[0][y].getTopleft().x + center, Game.defendingGameGrid[0][y + 1].getTopleft().y + cellHeight - border, paint);
        }

        paint.setColor(Color.WHITE);

        // Draw the contents of the grid
        for (int y = 0; y < 11; y++) {
            for (int x = 0; x < 11; x++) {
                if (Game.defendingGameGrid[x][y].getHas_ship()) {
                    drawDefendingCell("S", x, y, center, canvas);
                }
                if (Game.defendingGameGrid[x][y].getWaiting()) {
                    drawDefendingCell("W", x, y, center, canvas);
                }

                if (Game.defendingGameGrid[x][y].getMiss()) {
                    drawDefendingCell("M", x, y, center, canvas);
                }

                if (Game.defendingGameGrid[x][y].getHit()) {
                    drawDefendingCell("H", x, y, center, canvas);
                }
            }
        }
    }

    // Draw cell
    void drawCell(String contents, int x, int y, float center, Canvas canvas) {
        canvas.drawText(contents, Game.gameGrid[x][y].getTopleft().x + center, Game.gameGrid[x][y].getTopleft().y + center, paint);
    }
    void drawDefendingCell(String contents, int x, int y, float center, Canvas canvas) {
        canvas.drawText(contents, Game.defendingGameGrid[x][y].getTopleft().x + center, Game.defendingGameGrid[x][y].getTopleft().y + center, paint);
    }

}

