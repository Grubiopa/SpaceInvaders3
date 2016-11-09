package com.example.grupo8.spaceinvaders;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

/**
 * Created by gabri on 08/11/2016.
 */

public class StartActivity extends Activity {
    private TextView tx;
    private TextView tx2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        tx= (TextView) findViewById(R.id.textView);
        Typeface font = Typeface.createFromAsset(getAssets(), "Star_Jedi_Rounded.ttf");
        tx.setTypeface(font);
        tx2 = (TextView) findViewById(R.id.textView2);
        tx2.setTypeface(font);
    }
    public void play(View v){
        Intent intent = new Intent(this, GameActivity.class);
        startActivity(intent);
    }
    public void settings(View v){
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }
}
