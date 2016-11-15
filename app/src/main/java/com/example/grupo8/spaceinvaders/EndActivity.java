package com.example.grupo8.spaceinvaders;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import org.w3c.dom.Text;

/**
 * Created by gabri on 15/11/2016.
 */

public class EndActivity extends Activity {
    private TextView tx;
    private TextView scored;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_end);
        String score = (String)getIntent().getExtras().getSerializable("points");
        tx= (TextView) findViewById(R.id.txt_gameOver);
        Typeface font = Typeface.createFromAsset(getAssets(), "Star_Jedi_Rounded.ttf");
        tx.setTypeface(font);
        scored = (TextView) findViewById(R.id.txt_points);
        scored.setTypeface(font);
        scored.setText("Puntuacion: " + score );
    }

    public void restart(View v){
        Intent intent = new Intent(this, GameActivity.class);
        startActivity(intent);
    }

    public void menu(View v){
        Intent intent = new Intent(this, StartActivity.class);
        startActivity(intent);
    }
}
