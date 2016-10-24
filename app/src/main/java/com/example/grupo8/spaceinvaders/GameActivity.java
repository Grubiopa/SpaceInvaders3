package com.example.grupo8.spaceinvaders;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Handler;
import android.support.v4.view.MotionEventCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

public class GameActivity extends Activity {
    private TextView tx;
    private String scored;
    private int score;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        h1.postDelayed(run, 0);
        h3.postDelayed(run3, 0);
        tx = (TextView) findViewById(R.id.score);
        Typeface font = Typeface.createFromAsset(getAssets(), "Star_Jedi_Rounded.ttf");
        tx.setTypeface(font);
        /*Typeface font = Typeface.createFromAsset(getAssets(), "fonts/munro_narrow.ttf");
        tx.setTypeface(font);*/
        scored=Integer.toString(score);
        tx.setText("PTS: "+scored);


    }

    Handler h1 = new Handler();
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
                caza.setX(caza.getX()+25);
            }else{
                caza.setX(caza.getX()-25);
            }
            if((caza.getX()+caza.getWidth() >= width - 25 )|| (caza.getX() <=25)){
                sentido=!sentido;
                caza.setY(caza.getY()+25);
                if(caza.getY()>=height/2+100){
                    caza.setY(300);
                }
                if(sentido){
                    caza.setX(caza.getX()+25);
                }else{
                    caza.setX(caza.getX()-25);
                }
            }
            h1.postDelayed(this, 10);

        }
    };
    Runnable run2 = new Runnable() {

        @Override
        public void run() {
            ImageView misil = (ImageView) findViewById(R.id.Misil);
            ImageView caza = (ImageView) findViewById(R.id.caza1);
            ImageButton nave = (ImageButton) findViewById(R.id.imageButton);
            DisplayMetrics metrics = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(metrics);
            int width = metrics.widthPixels; // ancho absoluto en pixels
            int height = metrics.heightPixels; // alto absoluto en pixels


            misil.setY(misil.getY()-50);

            if((misil.getY()+misil.getHeight() <15 ))
            {
                misil.setVisibility(View.INVISIBLE);
                h2.removeCallbacks(run2);
                nave.setEnabled(true);
                if(caza.getVisibility()==View.INVISIBLE){
                    caza.setX(width/2-200);
                    caza.setVisibility(View.VISIBLE);
                }
            }
            h2.postDelayed(this,0);
            if(caza.getVisibility()==View.VISIBLE){
                float centreX=caza.getX() + caza.getWidth()  / 2;
                float centreY=caza.getY() + caza.getHeight() /2;
                int x= ((int) centreX);
                int y= ((int) centreY);
                if ((Math.abs(x - misil.getX()) < caza.getWidth()  / 2)&&(Math.abs(y - misil.getY()) < caza.getHeight()/2)) {
                    caza.setVisibility(View.INVISIBLE);
                    score+=100;
                    scored=Integer.toString(score);
                    tx.setText("PTS: "+scored);
                }
            }


        }
    };
    Runnable run3 = new Runnable() {

        @Override
        public void run() {
            ImageView misilrojo = (ImageView) findViewById(R.id.laserrojo);
            ImageView caza = (ImageView) findViewById(R.id.caza1);
            DisplayMetrics metrics = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(metrics);
            int width = metrics.widthPixels; // ancho absoluto en pixels
            int height = metrics.heightPixels; // alto absoluto en pixels

            if (misilrojo.getY()<=height){
                misilrojo.setY(misilrojo.getY()+50);
            }else{
                if(caza.getVisibility()==View.INVISIBLE){
                    misilrojo.setVisibility(View.INVISIBLE);
                }else{
                    misilrojo.setX(caza.getX()+ caza.getWidth()/2);
                    misilrojo.setY(caza.getY()+250);
                    misilrojo.setVisibility(View.VISIBLE);
                }

            }

            h3.postDelayed(this, 30);
        }
    };
    public void disparo(View v){
        ImageView misil = (ImageView) findViewById(R.id.Misil);
        ImageButton nave = (ImageButton) findViewById(R.id.imageButton);
        misil.setX(nave.getX()+ nave.getWidth()/2);
        misil.setY(nave.getY()-200);
        misil.setVisibility(View.VISIBLE);
        nave.setEnabled(false);
        h2.postDelayed(run2, 40);
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
                    boton.setX(boton.getX()+50);
                }else{
                    boton.setX(boton.getX()-50);
                }
                return true;

            default:
                return super.onTouchEvent(event);
        }
    }
}