package com.example.grupo8.spaceinvaders;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.provider.Settings;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

/**
 * Created by sergiopaniegoblanco on 08/11/2016.
 */

public class SettingsActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);

        Button themebtn = (Button) findViewById(R.id.themeselect);
        Button musicbtn = (Button) findViewById(R.id.music);
        registerForContextMenu(themebtn);
        Typeface font = Typeface.createFromAsset(getAssets(), "Star_Jedi_Rounded.ttf");
        themebtn.setTypeface(font);
        musicbtn.setTypeface(font);

        SharedPreferences prefs = this.getSharedPreferences("SpaceInvaders", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        RelativeLayout theme = (RelativeLayout) findViewById(R.id.settings);
        if(0!=prefs.getInt("theme", 0))
            theme.setBackground( getResources().getDrawable(R.drawable.spaceinvadersback));
        if(0!=prefs.getInt("music", 0))
            musicbtn.setText("Music : 0FF");
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        SharedPreferences prefs = this.getSharedPreferences("SpaceInvaders", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        RelativeLayout theme = (RelativeLayout) findViewById(R.id.settings);
        switch (item.getItemId()) {
            case R.id.starwars:
                editor.putInt("theme", 0);
                theme.setBackground(getResources().getDrawable(R.drawable.galaxy));
                editor.apply();
                return true;
            case R.id.spaceinvaders:
                editor.putInt("theme", 1);
                theme.setBackground( getResources().getDrawable(R.drawable.spaceinvadersback));
                editor.apply();
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }
    public void music(View view){
        Button musicbtn = (Button) findViewById(R.id.music);
        SharedPreferences prefs = this.getSharedPreferences("SpaceInvaders", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        if(0==prefs.getInt("music", 0)){
            musicbtn.setText("Music : 0FF");
            editor.putInt("music", 1);
        }else{
            musicbtn.setText("Music : 0N");
            editor.putInt("music", 0);
        }
        editor.apply();
    }
}
