package com.example.Battleship_Game_1_04;

import android.app.*;
import android.graphics.*;
import android.os.*;
import android.util.*;
import android.view.*;
import android.widget.*;
import com.loopj.android.http.*;
import org.apache.http.*;
import org.json.*;

import java.util.*;

/**
 * Created by Jeff Tartt on 2/27/2015.
 */
public class Game extends BaseActivity {
    // Create a new GameCell
    // initialize gameBoard and a custom grid activity
    // declare gameStarted publicly
    public static GameCell[][] gameGrid = new GameCell[11][11];
    public static GameCell[][] defendingGameGrid = new GameCell[11][11];
    View gameBoard = null;
    Activity customGrid = this;
    public static boolean gameStarted = false;

    // available ships
    String getShipsUrl = "http://battlegameserver.com/api/v1/available_ships.json";
    Spinner shipSpinner;
    String[] shipsArray;
    Map<String, Integer> shipsMap = new HashMap<String, Integer>();
    ArrayAdapter<String> shipSpinnerAdapter = null;

    // available directions
    String getDirectionsUrl = "http://battlegameserver.com/api/v1/available_directions.json";
    Spinner directionsSpinner;
    String[] directionsArray;
    Map<String, Integer> directionsMap = new HashMap<String, Integer>();
    ArrayAdapter<String> directionsSpinnerAdapter = null;

    // Row and column spinners
    Spinner rowSpinner, columnSpinner;

    String selectedShip, selectedRow, selectedColumn, selectedDirection, shipName, shipNumberString, directionNumberString, directionName;
    public int setShipRow, setShipColumn, setShipDirection, setShipNumber;

    // attack
    int attackRow, attackColumn;
    String attackRowString, attackColumnString;

    // comp response
    public static String userHit, compRow, compCol, compHit, userShipSunk, compShipSunk, numComputerShipsSunk, numShipsSunk, winner;
    public static int compAttackRow, compAttackCol;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initializeApp();
        //set gameboard for Game.class
        setContentView(R.layout.gameboard);
        gameBoard = findViewById(R.id.boardview);

        shipSpinner = (Spinner) findViewById(R.id.shipSpinner);
        directionsSpinner = (Spinner) findViewById(R.id.directionsSpinner);

        rowSpinner = (Spinner) findViewById(R.id.rowSpinner);
        columnSpinner = (Spinner) findViewById(R.id.columnSpinner);

        gameStarted = false;
    }

    private void initializeApp() {
        // Initialize the gameGrid
        for (int y = 0; y < 11; y++) {
            for (int x = 0; x < 11; x++) {
                gameGrid[x][y] = new GameCell();
                defendingGameGrid[x][y] = new GameCell();
            }
        }
//      gameGrid[1][3].setHas_ship(true); // For debugging
//      defendingGameGrid[1][3].setHas_ship(true); // For debugging
//        gameGrid[1][2].setHit(true);

        // API Call to create a game.
        ChallengeComputer();
        GetDirections();
        GetShips();
    }

    // Get ships
    public void GetShips() {
        AsyncHttpClient client = new AsyncHttpClient();
        client.setBasicAuth(loginUsername, loginPassword);
        client.get(getShipsUrl, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject ships) {
                // Successfully got a response so parse JSON object
                try {
                    // Fill in the spinner with ship info
                    Iterator iter = ships.keys();
                    while (iter.hasNext()) {
                        String key = (String) iter.next();
                        Integer value = ships.getInt(key);
                        shipsMap.put(key + "(" + value + ")", value);
                    }
                    int size = shipsMap.keySet().size();
                    shipsArray = new String[size];
                    shipsArray = shipsMap.keySet().toArray(new String[0]);

                    shipSpinnerAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, shipsArray);
                    shipSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    shipSpinner.setAdapter(shipSpinnerAdapter);

                } catch (Exception e) {
                    e.printStackTrace();
                    toastIt(e.getLocalizedMessage());
                }
            }

//            @Override
//            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
//                // Response failed :(
//                try {
//                    toastIt(new String(responseBody, "UTF-8"));
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
        });
    }

    // Get directions
    public void GetDirections() {
        AsyncHttpClient client = new AsyncHttpClient();
        client.setBasicAuth(loginUsername, loginPassword);
        client.get(getDirectionsUrl, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject directions) {
                // Successfully got a response so parse JSON object
                try {
                    // Fill in the spinner with ship info
                    Iterator iter = directions.keys();
                    while (iter.hasNext()) {
                        String key = (String) iter.next();
                        Integer value = directions.getInt(key);
                        directionsMap.put(key + "(" + value + ")", value);
                        //directionNumberString = value;

                    }
                    int size = directionsMap.keySet().size();
                    directionsArray = new String[size];
                    directionsArray = directionsMap.keySet().toArray(new String[0]);

                    directionsSpinnerAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, directionsArray);
                    directionsSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    directionsSpinner.setAdapter(directionsSpinnerAdapter);

                } catch (Exception e) {
                    e.printStackTrace();
                    toastIt(e.getLocalizedMessage());
                }
            }

        });
    }

    // challenge computer success
    // "error":"illegal ship placement"
    public static void challengeComputerSuccess(JSONObject game) {
        try {
            gameID = game.getString("game_id");
            Log.i("Game id", gameID);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // challenge computer
    public void ChallengeComputer() {
        new BattleshipAPI(loginUsername, loginPassword).challengeComputer();
    }

    // add ship success
    public static void addShipSuccess(JSONObject ship) {
        try {
            gameID = ship.getString("game_id");
            status = ship.getString("status");

            Log.i("Game ID", gameID);
            Log.i("Status", status);


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void addShip() {
        new BattleshipAPI(loginUsername, loginPassword).addShip(gameID, shipName, selectedRow, selectedColumn, directionNumberString);
    }

    public void onClickAddShip(View view) {
        // Get the ship, row, column, and direction information.
        // call the server,
        // remove the ship from the spinner,
        // update the grid
        selectedShip = shipSpinner.getSelectedItem().toString();
        drawShip(selectedShip);


        shipsMap.remove(selectedShip);
        int size = shipsMap.keySet().size();
        shipsArray = new String[size];
        shipsArray = shipsMap.keySet().toArray(new String[0]);

        shipSpinnerAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, shipsArray);
        shipSpinner.setAdapter(shipSpinnerAdapter);

        shipName = selectedShip.substring(0, selectedShip.length() - 3);
        shipNumberString = selectedShip.substring(selectedShip.length() - 2, selectedShip.length() - 1);
        setShipNumber = Integer.parseInt(shipNumberString);
        // toastIt(shipName);
        selectedDirection = directionsSpinner.getSelectedItem().toString();
        directionName = selectedDirection.substring(0, selectedDirection.length() - 3);
        directionNumberString = selectedDirection.substring(selectedDirection.length() - 2, selectedDirection.length() - 1);

        // get rows and columns from spinners
        setShipRow = 0;
        selectedRow = rowSpinner.getSelectedItem().toString();

        selectedColumn = columnSpinner.getSelectedItem().toString();
        setShipColumn = Integer.parseInt(selectedColumn);

        setShipDirection = Integer.parseInt(directionNumberString);
        shipNumberString = selectedShip.substring(selectedShip.length() - 2, selectedShip.length() - 1);
        setShipNumber = Integer.parseInt(shipNumberString);

        Log.i("ship name: ", shipName);
        Log.i("ship number: ", shipNumberString);
        Log.i("selected row: ", selectedRow);
        Log.i("selected column: ", selectedColumn);
        Log.i("directionName", directionName);
        Log.i("directionNumberString", directionNumberString);
        Log.i("set ship row", Integer.toString(setShipRow));

        if (selectedRow.equals("A")) {
            setShipRow = 1;
        } else if (selectedRow.equals("B")) {
            setShipRow = 2;
        } else if (selectedRow.equals("C")) {
            setShipRow = 3;
        } else if (selectedRow.equals("D")) {
            setShipRow = 4;
        } else if (selectedRow.equals("E")) {
            setShipRow = 5;
        } else if (selectedRow.equals("F")) {
            setShipRow = 6;
        } else if (selectedRow.equals("G")) {
            setShipRow = 7;
        } else if (selectedRow.equals("H")) {
            setShipRow = 8;
        } else if (selectedRow.equals("I")) {
            setShipRow = 9;
        } else if (selectedRow.equals("J")) {
            setShipRow = 10;
        }

        // if direction = north
        if (setShipDirection == 0) {
            // if sub or cruiser
            if (setShipNumber == 3) {
                defendingGameGrid[setShipColumn][setShipRow].setHas_ship(true);
                defendingGameGrid[setShipColumn][setShipRow - 1].setHas_ship(true);
                defendingGameGrid[setShipColumn][setShipRow - 2].setHas_ship(true);

                // if carrier
            } else if (setShipNumber == 5) {
                defendingGameGrid[setShipColumn][setShipRow].setHas_ship(true);
                defendingGameGrid[setShipColumn][setShipRow - 1].setHas_ship(true);
                defendingGameGrid[setShipColumn][setShipRow - 2].setHas_ship(true);
                defendingGameGrid[setShipColumn][setShipRow - 3].setHas_ship(true);
                defendingGameGrid[setShipColumn][setShipRow - 4].setHas_ship(true);

                // if destroyer
            } else if (setShipNumber == 2) {
                defendingGameGrid[setShipColumn][setShipRow].setHas_ship(true);
                defendingGameGrid[setShipColumn][setShipRow - 1].setHas_ship(true);

                // if battleship
            } else if (setShipNumber == 4) {
                defendingGameGrid[setShipColumn][setShipRow].setHas_ship(true);
                defendingGameGrid[setShipColumn][setShipRow - 1].setHas_ship(true);
                defendingGameGrid[setShipColumn][setShipRow - 2].setHas_ship(true);
                defendingGameGrid[setShipColumn][setShipRow - 3].setHas_ship(true);
            }
            // if direction south
        } else if (setShipDirection == 4) {
            // if sub or cruiser
            if (setShipNumber == 3) {
                defendingGameGrid[setShipColumn][setShipRow].setHas_ship(true);
                defendingGameGrid[setShipColumn][setShipRow + 1].setHas_ship(true);
                defendingGameGrid[setShipColumn][setShipRow + 2].setHas_ship(true);

                // if carrier
            } else if (setShipNumber == 5) {
                defendingGameGrid[setShipColumn][setShipRow].setHas_ship(true);
                defendingGameGrid[setShipColumn][setShipRow + 1].setHas_ship(true);
                defendingGameGrid[setShipColumn][setShipRow + 2].setHas_ship(true);
                defendingGameGrid[setShipColumn][setShipRow + 3].setHas_ship(true);
                defendingGameGrid[setShipColumn][setShipRow + 4].setHas_ship(true);

                // if destroyer
            } else if (setShipNumber == 2) {
                defendingGameGrid[setShipColumn][setShipRow].setHas_ship(true);
                defendingGameGrid[setShipColumn][setShipRow + 1].setHas_ship(true);

                // if battleship
            } else if (setShipNumber == 4) {
                defendingGameGrid[setShipColumn][setShipRow].setHas_ship(true);
                defendingGameGrid[setShipColumn][setShipRow + 1].setHas_ship(true);
                defendingGameGrid[setShipColumn][setShipRow + 2].setHas_ship(true);
                defendingGameGrid[setShipColumn][setShipRow + 3].setHas_ship(true);

            }
            // if east
        } else if (setShipDirection == 2) {
            // if sub or cruiser
            if (setShipNumber == 3) {
                defendingGameGrid[setShipColumn][setShipRow].setHas_ship(true);
                defendingGameGrid[setShipColumn + 1][setShipRow].setHas_ship(true);
                defendingGameGrid[setShipColumn + 2][setShipRow].setHas_ship(true);

                // if carrier
            } else if (setShipNumber == 5) {
                defendingGameGrid[setShipColumn][setShipRow].setHas_ship(true);
                defendingGameGrid[setShipColumn + 1][setShipRow].setHas_ship(true);
                defendingGameGrid[setShipColumn + 2][setShipRow].setHas_ship(true);
                defendingGameGrid[setShipColumn + 3][setShipRow].setHas_ship(true);
                defendingGameGrid[setShipColumn + 4][setShipRow].setHas_ship(true);

                // if destroyer
            } else if (setShipNumber == 2) {
                defendingGameGrid[setShipColumn][setShipRow].setHas_ship(true);
                defendingGameGrid[setShipColumn + 1][setShipRow].setHas_ship(true);

                // if battleship
            } else if (setShipNumber == 4) {
                defendingGameGrid[setShipColumn][setShipRow].setHas_ship(true);
                defendingGameGrid[setShipColumn + 1][setShipRow].setHas_ship(true);
                defendingGameGrid[setShipColumn + 2][setShipRow].setHas_ship(true);
                defendingGameGrid[setShipColumn + 3][setShipRow].setHas_ship(true);

            }

            // if west
        } else if (setShipDirection == 6) {
            // if sub or cruiser
            if (setShipNumber == 3) {
                defendingGameGrid[setShipColumn][setShipRow].setHas_ship(true);
                defendingGameGrid[setShipColumn - 1][setShipRow].setHas_ship(true);
                defendingGameGrid[setShipColumn - 2][setShipRow].setHas_ship(true);

                // if carrier
            } else if (setShipNumber == 5) {
                defendingGameGrid[setShipColumn][setShipRow].setHas_ship(true);
                defendingGameGrid[setShipColumn - 1][setShipRow].setHas_ship(true);
                defendingGameGrid[setShipColumn - 2][setShipRow].setHas_ship(true);
                defendingGameGrid[setShipColumn - 3][setShipRow].setHas_ship(true);
                defendingGameGrid[setShipColumn - 4][setShipRow].setHas_ship(true);

                // if destroyer
            } else if (setShipNumber == 2) {
                defendingGameGrid[setShipColumn][setShipRow].setHas_ship(true);
                defendingGameGrid[setShipColumn - 1][setShipRow].setHas_ship(true);

                // if battleship
            } else if (setShipNumber == 4) {
                defendingGameGrid[setShipColumn][setShipRow].setHas_ship(true);
                defendingGameGrid[setShipColumn - 1][setShipRow].setHas_ship(true);
                defendingGameGrid[setShipColumn - 2][setShipRow].setHas_ship(true);
                defendingGameGrid[setShipColumn - 3][setShipRow].setHas_ship(true);

            }
        }

        addShip();

        if (size == 0) {
            // if game started draw gameBoard(BoardView.class)
            gameStarted = true;

            // invalidate forces view to draw
            gameBoard.invalidate();
        }

    }

    public static void attackOpponentSuccess(JSONObject attackSequence) {
        try {

            userHit = attackSequence.getString("hit");
            compRow = attackSequence.getString("comp_row");
            compCol = attackSequence.getString("comp_col");
            compHit = attackSequence.getString("comp_hit");
            userShipSunk = attackSequence.getString("user_ship_sunk");
            compShipSunk = attackSequence.getString("comp_ship_sunk");
            numComputerShipsSunk = attackSequence.getString("num_computer_ships_sunk");
            numShipsSunk = attackSequence.getString("num_your_ships_sunk");
            winner = attackSequence.getString("winner");
            Log.i("userHit", userHit);
            Log.i("comp col", compCol);

            compAttackCol = Integer.parseInt(compCol);
            Log.i("compAttackCol", Integer.toString(compAttackCol));

            if ("a".equals(compRow)) {
                compAttackRow = 1;
            } else if ("b".equals(compRow)) {
                compAttackRow = 2;
            } else if ("c".equals(compRow)) {
                compAttackRow = 3;
            } else if ("d".equals(compRow)) {
                compAttackRow = 4;
            } else if ("e".equals(compRow)) {
                compAttackRow = 5;
            } else if ("f".equals(compRow)) {
                compAttackRow = 6;
            } else if ("g".equals(compRow)) {
                compAttackRow = 7;
            } else if ("h".equals(compRow)) {
                compAttackRow = 8;
            } else if ("i".equals(compRow)) {
                compAttackRow = 9;
            } else if ("j".equals(compRow)) {
                compAttackRow = 10;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void attackOpponent() {
        new BattleshipAPI(loginUsername, loginPassword).attackOpponent(gameID, attackRowString, attackColumnString);
    }

    public void onClickAttackShip(View view) {
        if (attackRow == 1) {
            attackRowString = "A";
        } else if (attackRow == 2) {
            attackRowString = "B";
        } else if (attackRow == 3) {
            attackRowString = "C";
        } else if (attackRow == 4) {
            attackRowString = "D";
        } else if (attackRow == 5) {
            attackRowString = "E";
        } else if (attackRow == 6) {
            attackRowString = "F";
        } else if (attackRow == 7) {
            attackRowString = "G";
        } else if (attackRow == 8) {
            attackRowString = "H";
        } else if (attackRow == 9) {
            attackRowString = "I";
        } else if (attackRow == 10) {
            attackRowString = "J";
        }

        attackColumnString = Integer.toString(attackColumn);

        if (userHit == "true") {
            gameGrid[attackColumn][attackRow].setHit(true);
            gameGrid[attackColumn][attackRow].setWaiting(false);

        } else if (userHit == "false") {
            gameGrid[attackColumn][attackRow].setMiss(true);
            gameGrid[attackColumn][attackRow].setWaiting(false);

        }

        if (compHit == "true") {
            defendingGameGrid[compAttackCol + 1][compAttackRow].setHit(true);
            defendingGameGrid[compAttackCol + 1][compAttackRow].setHas_ship(false);

        } else if (compHit == "false") {
            defendingGameGrid[compAttackCol + 1][compAttackRow].setMiss(true);
            defendingGameGrid[compAttackCol + 1][compAttackRow].setHas_ship(false);
        }

        Log.i("compAttackCol", Integer.toString(compAttackCol));
        Log.i("compAttackRow", Integer.toString(compAttackRow));

        attackOpponent();
        gameBoard.invalidate();
    }

    public void drawShip(String ship) {

    }

    // Get coordinates for touch event
    public static float[] getRelativeCoords(Activity activity, MotionEvent e) {
        // MapView
        View contentView = activity.getWindow().findViewById(Window.ID_ANDROID_CONTENT);
        return new float[]{
                e.getRawX() - contentView.getLeft(),
                e.getRawY() - contentView.getTop()
        };
    }

    // touch event
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int eventAction = event.getAction();
        switch (eventAction) {
            case MotionEvent.ACTION_DOWN:
                // finger touches the screen
                break;
            case MotionEvent.ACTION_MOVE:
                // finger moves on the screen
                break;
            case MotionEvent.ACTION_UP:
                // finger leaves the screen
                Log.i("TOUCH", "x:" + event.getX() + " y:" + event.getY());
                float[] xy = getRelativeCoords(customGrid, event);
                // Point indicies = findXYIndexes( event.getX(), event.getY() );
                Point indicies = findXYIndexes(xy[0], xy[1]);
                Log.i("TOUCH", "ix: " + indicies.x + " iy: " + indicies.y);

                attackRow = indicies.y;
                attackColumn = indicies.x;
                Log.i("attack row", Integer.toString(attackRow));
                Log.i("attack column", Integer.toString(attackColumn));

                gameGrid[indicies.x][indicies.y].setWaiting(true);
                getWindow().getDecorView().getRootView().invalidate();
                gameBoard.invalidate();
                break;
        }
        // tell the system that we handled the event and no further processing is required
        return true;
    }

    Point findXYIndexes(float x, float y) {
        // Given X, Y, find the grid indexes that contain that point
        int height = gameGrid[0][0].getCellHeight();
        int width = gameGrid[0][0].getCellWidth();
        int xo = Game.gameGrid[0][0].getViewOrigin().x;
        int yo = Game.gameGrid[0][0].getViewOrigin().y;
        return new Point((int) ((x - xo) / width),
                (int) ((y - gridTop - yo) / height));
    }
}
