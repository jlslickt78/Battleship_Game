package com.example.Battleship_Game_1_04;


import android.util.Log;
import com.loopj.android.http.*;
import org.apache.http.*;
import org.json.*;

/**
 * Created by Jeff Tartt on 2/17/2015.
 */
public class BattleshipAPI extends BaseActivity {
    final String loginUrl = "http://battlegameserver.com/api/v1/login.json";
    String userName, userPassword;

    public BattleshipAPI(String loginUsername, String loginPassword) {
        super();
    }

    public void BattleShipAPI(String _userName, String _password) {
        userName = _userName;
        userPassword = _password;
    }

    public void challengeComputer() {
        AsyncHttpClient client = new AsyncHttpClient();
        client.setBasicAuth(loginUsername, loginPassword);
        String challengeUrl = "http://battlegameserver.com/api/v1/challenge_computer.json";
        client.get(challengeUrl, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject game) {
                Game.challengeComputerSuccess(game);
            }

//        @Override
//        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error){
//            apiFailure(statusCode, headers, responseBody, error);
//        }
        });
    }

    // add_ship = /api/v1/game/7/add_ship/carrier/b/8/0.json
    public void addShip(String game_id, String ship, String row, String column, String direction) {
        AsyncHttpClient client = new AsyncHttpClient();
        client.setBasicAuth(loginUsername, loginPassword);
        String add_ship_url = "http://battlegameserver.com/api/v1/game/" + game_id + "/add_ship/" + ship + "/" + row + "/" + column + "/" + direction + ".json";
        Log.i("Add ship url", add_ship_url);

        client.get(add_ship_url, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject ship) {
                Game.addShipSuccess(ship);
            }
        });
    }

    public void attackOpponent(String game_id, String row, String column) {
        AsyncHttpClient client = new AsyncHttpClient();
        client.setBasicAuth(loginUsername, loginPassword);
        String attack_opponent_url = "http://battlegameserver.com/api/v1/game/" + game_id + "/attack/" + row + "/" + column + ".json";
        Log.i("attack ", attack_opponent_url);

        client.get(attack_opponent_url, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject attackSequence) {
                Game.attackOpponentSuccess(attackSequence);
            }

//            @Override
//            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
//                error.printStackTrace();
//            }
        });

    }

    public void apiFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
        // Response failed :(String decodedResponse = ;
        try {
            toastIt(new String(responseBody, "UTF-8"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
