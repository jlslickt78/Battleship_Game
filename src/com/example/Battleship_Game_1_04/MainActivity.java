package com.example.Battleship_Game_1_04;

import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.*;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.BinaryHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;

public class MainActivity extends BaseActivity {
    EditText username, password;
    String base64EncodedCredentials = "";
    Button loginBtn, showUsersBtn;
    String loginUrl = "http://battlegameserver.com/api/v1/login.json";
    String getUsersUrl = "http://battlegameserver.com/api/v1/all_users.json";
    ProgressBar progressBar;
    TextView availableLabel;
    List<String> userList = new ArrayList<String>();
    ListView userListView;
    ArrayAdapter adapter = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        loginBtn = (Button) findViewById(R.id.loginButton);
        username = (EditText) findViewById(R.id.username);
        password = (EditText) findViewById(R.id.password);

        showUsersBtn = (Button) findViewById(R.id.showUsersBtn);

        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        availableLabel = (TextView) findViewById(R.id.availableLabel);

        userListView = (ListView) findViewById(R.id.listView);

    }

    public void loginToServer(View view) {
        // Check text of button and login or logout
        if (loginBtn.getText().toString().equals("Logout")) {
            // Hide buttons if login is shown, etc.
            loginBtn.setText("Login");
            showUsersBtn.setEnabled(false);
            showUsersBtn.setVisibility(View.INVISIBLE);
            availableLabel.setVisibility(View.INVISIBLE);
            userListView.setVisibility(View.INVISIBLE);

            invalidateOptionsMenu();

        } else {

            loginBtn.setText("Logout");

            // get login info from textboxes
            // set basic auth and pass logon info
            // get url, JsonHttpResponseHandler
            // onProgress, onSuccess, onError
            AsyncHttpClient client = new AsyncHttpClient();
            loginUsername = username.getText().toString();
            loginPassword = password.getText().toString();
            client.setBasicAuth(loginUsername, loginPassword);
            client.get(loginUrl, new JsonHttpResponseHandler() {

                @Override
                public void onProgress(int position, int length) {
                    Log.i("LOGIN", "pos: " + position + " len: " + length);
                    progressBar.setProgress(position);
                }

                // On successful login, create new gamer
                @Override
                public void onSuccess(int statusCode, Header[] header, JSONObject user) {
                    try {
                        Log.i("JSON", user.getString("first_name") + " " + user.getString("last_name") + " " + user.getString("available") + "\n");
                        // Put into a gamer object
                        // String _first_name, String _last_name, String _email, Integer _online,
                        // Integer _available, Integer _gaming, String _avatar
                        currentUser = new Gamer(
                                user.getString("first_name"),
                                user.getString("last_name"),
                                user.getString("email"),
                                user.getBoolean("online"),
                                user.getBoolean("available"),
                                user.getBoolean("gaming"),
                                user.getString("avatar_name"),
                                user.getString("avatar_image"),
                                user.getInt("level"),
                                user.getInt("coins"),
                                user.getInt("battles_won"),
                                user.getInt("battles_lost"),
                                user.getInt("battles_tied"),
                                user.getInt("experience_points")
                        );
                    } catch (Exception e) {
                        e.printStackTrace();
                        toastIt(e.getLocalizedMessage());
                    }

                    showUsersBtn.setEnabled(true);
                    showUsersBtn.setVisibility(View.VISIBLE);
                    availableLabel.setVisibility(View.VISIBLE);
                    userListView.setVisibility(View.VISIBLE);

                    invalidateOptionsMenu();

                    // Download the Avatar image and place in the ImageView
                    DownloadAvatarImage(currentUser.getAvatarPath());

                }
            });
        }
    }

    // Get user on click
    public void getUsersOnClick(View v) {
        progressBar.setVisibility(View.VISIBLE);
        ServerRequest sr = new ServerRequest(getUsersUrl, ServerCommands.GET_USERS);
        GetJSONAsync task = new GetJSONAsync();
        task.execute(new ServerRequest[]{sr});
    }

    private class GetJSONAsync extends AsyncTask<ServerRequest, Integer, ServerRequest> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected ServerRequest doInBackground(ServerRequest... params) {
            String usersUrl = params[0].getUrl();
            HttpURLConnection urlConnection = null;
            URL url = null;
            InputStream in = null;
            String jsonData = "";
            int progress = 0;

            // Get the data from the URL
            try {
                url = new URL(usersUrl);
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestProperty("Authorization", "Basic " + base64EncodedCredentials);
                urlConnection.connect();
            } catch (IOException ioe) {
                ioe.printStackTrace();
            }
            publishProgress(progress += 25);
            // Convert response into a string
            try {
                if (urlConnection != null) {
                    in = new BufferedInputStream(urlConnection.getInputStream());
                }
                BufferedReader reader = new BufferedReader(
                        new InputStreamReader(in, "UTF8"), 8);
                StringBuilder sb = new StringBuilder();
                String line = null;
                while ((line = reader.readLine()) != null) {
                    sb.append(line).append("\n");
                }
                in.close();
                jsonData = sb.toString();
            } catch (Exception e) {
                e.printStackTrace();
                in = new BufferedInputStream(urlConnection.getErrorStream());
                BufferedReader reader = null;
                try {
                    reader = new BufferedReader(
                            new InputStreamReader(in, "UTF8"), 8);
                    StringBuilder sb = new StringBuilder();
                    String line = null;
                    while ((line = reader.readLine()) != null) {
                        sb.append(line).append("\n");
                    }
                    in.close();
                    params[0].setErrorString(sb.toString());
                } catch (Exception e1) {
                    e1.printStackTrace();
                }

            } finally {
                urlConnection.disconnect();
            }

            publishProgress(progress += 50);

            Log.i("JSON", jsonData);
            params[0].setJsonDataResult(jsonData);
            return params[0];   // Return same ServerRequest but with data filled in.
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            progressBar.setProgress(values[0]);
        }

        @Override
        protected void onPostExecute(ServerRequest sr) {
            super.onPostExecute(sr);
            String jsonData = sr.getJsonDataResult();

            switch (sr.getCommand()) {

                case GET_USERS:
                    // Parse the jsonData into a JSONObject
                    try {
                        JSONArray allUsers = new JSONArray(jsonData);
                        float numUsers = allUsers.length();
                        for (int i = 0; i < numUsers; i++) {
                            JSONObject user = (JSONObject) allUsers.get(i);
                            Log.i("JSON", user.getString("first_name") + " " + user.getString("last_name") + " " + user.getString("available") + "\n");
                            // TODO: Put data into a ListAdapter to display!!


                            if (user.getString("available") == "true") {
                                Log.i("if statement works", "works");
                                userList.add(user.getString("avatar_name") + " " + user.getString("available"));
                            }
                        }
                        adapter = new ArrayAdapter(getApplicationContext(),
                                android.R.layout.simple_list_item_1, userList);
                        userListView.setAdapter(adapter);

                    } catch (Exception e) {
                        e.printStackTrace();
                        toastIt(e.getLocalizedMessage());
                    }

                    progressBar.setVisibility(progressBar.getRootView().INVISIBLE);
                    break;

                case LOGIN:
                    if (sr.getErrorString() != null) {
                        toastIt("Login: " + sr.getErrorString());
                    } else {
                        // Put info into a user profile object
                        try {
                            JSONObject user = new JSONObject(jsonData);
                            Log.i("JSON", user.getString("first_name") + " " + user.getString("last_name") + "\n");
                            // Put into a gamer object
                            // String _first_name, String _last_name, String _email, Integer _online,
                            // Integer _available, Integer _gaming, String _avatar
                            currentUser = new Gamer(
                                    user.getString("first_name"),
                                    user.getString("last_name"),
                                    user.getString("email"),
                                    user.getBoolean("online"),
                                    user.getBoolean("available"),
                                    user.getBoolean("gaming"),
                                    user.getString("avatar_name"),
                                    user.getString("avatar_image"),
                                    user.getInt("level"),
                                    user.getInt("coins"),
                                    user.getInt("battles_won"),
                                    user.getInt("battles_lost"),
                                    user.getInt("battles_tied"),
                                    user.getInt("experience_points")
                            );
                        } catch (Exception e) {
                            e.printStackTrace();
                            toastIt(e.getLocalizedMessage());
                        }

                        showUsersBtn.setEnabled(true);
                        showUsersBtn.setVisibility(View.VISIBLE);
                        availableLabel.setVisibility(View.VISIBLE);
                        invalidateOptionsMenu();

                        // Download the Avatar image and place in the ImageView
                        DownloadAvatarImage(currentUser.getAvatarPath());
                    }
                    break;

                default:
                    break;
            }
        }
    }

    public void DownloadAvatarImage(String image) {
        AsyncHttpClient client = new AsyncHttpClient();
        String[] allowedTypes = new String[]{"image/png", "image/jpeg", "image/jpg"};

        client.get("http://battlegameserver.com/" + image, new BinaryHttpResponseHandler(allowedTypes) {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] imageData) {
                // Successfully got a response
                Drawable d = new BitmapDrawable(getApplicationContext().getResources(), BitmapFactory.decodeByteArray(imageData, 0, imageData.length));
                ImageView imageView = (ImageView) findViewById(R.id.avatar);
                imageView.setImageDrawable(d);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] errorMsg, Throwable error) {
                // Response failed :(
                try {
                    toastIt(new String(errorMsg, "UTF-8"));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

    }

}
