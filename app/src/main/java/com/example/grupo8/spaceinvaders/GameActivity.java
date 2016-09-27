package com.example.grupo8.spaceinvaders;

import android.os.Handler;
import android.support.v4.view.MotionEventCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;

public class GameActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        h2.postDelayed(run, 0);

    }

    Handler h2 = new Handler();
    Handler h3 = new Handler();

    boolean sentido = true;
    Runnable run = new Runnable() {

        @Override
        public void run() {
            ImageView caza = (ImageView) findViewById(R.id.caza1);
            DisplayMetrics metrics = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(metrics);
            int width = metrics.widthPixels; // ancho absoluto en pixels
            int height = metrics.heightPixels; // alto absoluto en pixels

            if(sentido){
                caza.setX(caza.getX()+10);
            }else{
                caza.setX(caza.getX()-10);
            }
            if((caza.getX()+caza.getWidth() >= width - 10 )|| (caza.getX() <=10))
                sentido=!sentido;

            h2.postDelayed(this, 50);
        }
    };
    Runnable run2 = new Runnable() {

        @Override
        public void run() {
            ImageView misil = (ImageView) findViewById(R.id.Misil);
            ImageButton nave = (ImageButton) findViewById(R.id.imageButton);
            DisplayMetrics metrics = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(metrics);
            int width = metrics.widthPixels; // ancho absoluto en pixels
            int height = metrics.heightPixels; // alto absoluto en pixels


            misil.setY(misil.getY()-10);

            if((misil.getY()+misil.getHeight() <15 ))
            {
                misil.setVisibility(View.INVISIBLE);
                h3.removeCallbacks(run2);
                nave.setEnabled(true);
            }

            h3.postDelayed(this, 50);
        }
    };
    public void disparo(View v){
        ImageView misil = (ImageView) findViewById(R.id.Misil);
        ImageButton nave = (ImageButton) findViewById(R.id.imageButton);
        misil.setX(nave.getX()+ nave.getWidth()/2);
        misil.setY(nave.getY());
        misil.setVisibility(View.VISIBLE);
        nave.setEnabled(false);
        h3.postDelayed(run2, 0);
    }



    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = MotionEventCompat.getActionMasked(event);

        switch (action) {
            case (MotionEvent.ACTION_DOWN):

                DisplayMetrics metrics = new DisplayMetrics();
                getWindowManager().getDefaultDisplay().getMetrics(metrics);
                int width = metrics.widthPixels; // ancho absoluto en pixels
                int height = metrics.heightPixels; // alto absoluto en pixels

                Float x = event.getX();
                ImageButton boton = (ImageButton) findViewById(R.id.imageButton);
                if (x > width/2){
                    boton.setX(boton.getX()+10);
                }else{
                    boton.setX(boton.getX()-10);
                }
                return true;

            default:
                return super.onTouchEvent(event);
        }
    }
}
