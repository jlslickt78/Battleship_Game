package com.example.Battleship_Game_1_04;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.io.InputStream;
import java.net.URL;

/**
 * Created by Jeff Tartt on 2/13/2015.
 */
public class BaseActivity extends Activity {

    public static Gamer currentUser = null;
    public static AttackInfo newAttack = null;
    public final int gridTop = 50;
    public static String loginUsername = "";
    public static String loginPassword = "";
    public static String gameID, status;

    public enum ServerCommands {
        LOGIN, GET_USERS, GET_AVATAR
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Restore preferences
//    SharedPreferences settings = getSharedPreferences( PREFS_NAME, 0 );
    }

    @Override
    protected void onStop() {
        super.onStop();
        savePreferences();
    }

    public static Drawable LoadImageFromWeb(String name, String url) {
        try {
            InputStream is = (InputStream) new URL(url).getContent();
            return Drawable.createFromStream(is, name);
        } catch (Exception e) {
            return null;
        }
    }

    public void savePreferences() {
        // We need an Editor object to make preference changes.
        // All objects are from android.context.Context
        // SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        // SharedPreferences.Editor editor = settings.edit();
        // editor.putString("doctorEmail", DOCTOR_EMAIL );

        // Commit the edits!
        // editor.commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.mastermenu, menu);
        MenuItem preferenceItem = menu.findItem(R.id.switchToPreferences);
        MenuItem gameItem = menu.findItem(R.id.switchToGame);

        if (currentUser == null) {
            preferenceItem.setVisible(false);
            gameItem.setVisible(false);
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.switchToPreferences:
                startActivity(new Intent(this, Preferences.class));
                break;
            case R.id.switchToGame:
                startActivity(new Intent(this, Game.class));
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }

//  public  File copyFileToExternal(String fileName) {
//    File file = null;
//    String newPath = Environment.getExternalStorageDirectory() + EXT_FOLDERNAME;
//    try {
//      File f = new File(newPath);
//      f.mkdirs();
//      FileInputStream fin = openFileInput(fileName);
//      FileOutputStream fos = new FileOutputStream(newPath + fileName);
//      byte[] buffer = new byte[1024];
//      int len1 = 0;
//      while ((len1 = fin.read(buffer)) != -1) {
//        fos.write(buffer, 0, len1);
//      }
//      fin.close();
//      fos.close();
//      file = new File(newPath + fileName);
//      if (file.exists())
//        return file;
//    } catch (Exception e) {
//      toastIt( "HELP!" );
//    }
//    return null;
//  }


    public void toastIt(String msg) {
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
    }
}
