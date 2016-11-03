package com.example.grupo8.spaceinvaders;

import android.app.Activity;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.support.v4.view.MotionEventCompat;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import static com.example.grupo8.spaceinvaders.R.drawable.caza;

public class GameActivity extends Activity {
    //DECLARACION DE VARIABLEs
    private TextView tx;
    private String scored;
    private int score;
    private int width;
    private int height;

    private Handler han_MovimientoCaza = new Handler();
    private Handler han_MisilVerde = new Handler();
    private Handler han_MisilRojo = new Handler();
    private boolean sentidoEnemigo = true;

    private ImageView enemigo;
    private ImageButton jugador;
    private ImageView misilRojo;
    private ImageView misilVerde;
    private int densidad;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        //MEDIDAS DEL DISPOSITIVO
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        densidad = metrics.densityDpi;
        width = metrics.widthPixels; // ancho absoluto en pixels
        height = metrics.heightPixels; // alto absoluto en pixels

        //INICIACION DE HEBRAS CON MOVIMIENTO CONTINUO
        han_MovimientoCaza.postDelayed(run_MovimientoCaza, 0);
        han_MisilRojo.postDelayed(run_MisilRojo, 0);

        //TXT PUNTACION
        tx = (TextView) findViewById(R.id.score);
        Typeface font = Typeface.createFromAsset(getAssets(), "Star_Jedi_Rounded.ttf");
        tx.setTypeface(font);
        /*Typeface font = Typeface.createFromAsset(getAssets(), "fonts/munro_narrow.ttf");
        tx.setTypeface(font);*/
        scored=Integer.toString(score);
        tx.setText("PTS: "+scored);

        //INICIACION DE VARIABLES QUE USAN IMAGENES
        enemigo = (ImageView) findViewById(R.id.caza1);
        jugador = (ImageButton) findViewById(R.id.naveJugador);
        misilRojo = (ImageView) findViewById(R.id.misilRojo);
        misilVerde = (ImageView) findViewById(R.id.misilVerde);
    }


    Runnable run_MovimientoCaza = new Runnable() {

        @Override
        public void run() {

            //DIRECCION DEL ENEMIGO
            if(sentidoEnemigo){
                enemigo.setX(enemigo.getX()+25);
            }else{
                enemigo.setX(enemigo.getX()-25);
            }

            //Sentido y altura del enemigo
            if((enemigo.getX()+ enemigo.getWidth() >= width - 25 )|| (enemigo.getX() <=25)){
                sentidoEnemigo =!sentidoEnemigo;
                enemigo.setY(enemigo.getY()+25);
            }

            //Sube para arriba
            if(enemigo.getY()>=height/2+100){
                enemigo.setY(300);
            }
            han_MovimientoCaza.postDelayed(this, 10);

        }
    };

    Runnable run_MisilVerde = new Runnable() {

        @Override
        public void run() {
            //Sube el misil
            misilVerde.setY(misilVerde.getY()-50);
            //crearEnemigos();
            //Misil fuera de pantalla
            if((misilVerde.getY()+ misilVerde.getHeight() <15 ))
            {
                //Misil desaparece y activa al jugador
                misilVerde.setVisibility(View.INVISIBLE);
                han_MisilVerde.removeCallbacks(run_MisilVerde);
                jugador.setEnabled(true);

                //Enemigo reaparece
                if(enemigo.getVisibility()==View.INVISIBLE){
                    enemigo.setX(width/2-200);
                    enemigo.setVisibility(View.VISIBLE);
                }
            }

            han_MisilVerde.postDelayed(this,0);

            //MUERTE DEL ENEMIGO
            if(enemigo.getVisibility()==View.VISIBLE){
                float centreX=enemigo.getX() + enemigo.getWidth()  / 2;
                float centreY=enemigo.getY() + enemigo.getHeight() /2;
                int x= ((int) centreX);
                int y= ((int) centreY);
                if ((Math.abs(x - misilVerde.getX()) < enemigo.getWidth()  / 2)&&(Math.abs(y - misilVerde.getY()) < enemigo.getHeight()/2)) {
                    enemigo.setVisibility(View.INVISIBLE);
                    enemigo.setY(300);
                    score+=100;
                    scored=Integer.toString(score);
                    tx.setText("PTS: "+scored);
                }
            }
        }
    };

    Runnable run_MisilRojo = new Runnable() {

        @Override
        public void run() {
            //MOVIMIENTO MISIL ROJO
            if (misilRojo.getY()<=height){
                misilRojo.setY(misilRojo.getY()+50);
            }else{
                if(enemigo.getVisibility()==View.INVISIBLE){
                    misilRojo.setVisibility(View.INVISIBLE);
                }else{
                    misilRojo.setX(enemigo.getX()+ enemigo.getWidth()/2);
                    misilRojo.setY(enemigo.getY()+(enemigo.getHeight()/2));
                    misilRojo.setVisibility(View.VISIBLE);
                }
            }
            han_MisilRojo.postDelayed(this, 30);
            float centreX=jugador.getX() + jugador.getWidth()  / 2;
            float centreY=jugador.getY() + jugador.getHeight() /2;
            int x= ((int) centreX);
            int y= ((int) centreY);
            if ((Math.abs(x - misilRojo.getX()) < jugador.getWidth()  / 2)&&(Math.abs(y - misilRojo.getY()) < jugador.getHeight()/2)) {
                score = 0;
                scored=Integer.toString(score);
                tx.setText("PTS: "+scored);
            }
        }
    };

    public void disparo(View v){
        misilVerde.setX(jugador.getX()+ jugador.getWidth()/2);
        misilVerde.setY(jugador.getY()-200);
        misilVerde.setVisibility(View.VISIBLE);
        jugador.setEnabled(false);
        han_MisilVerde.postDelayed(run_MisilVerde, 40);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = MotionEventCompat.getActionMasked(event);

        switch (action) {
            case (MotionEvent.ACTION_DOWN):
                Float x = event.getX();
                if (x > width/2){
                    jugador.setX(jugador.getX()+50);
                    if(jugador.getX()>width)jugador.setX(0);
                }else{
                    jugador.setX(jugador.getX()-50);
                    if(jugador.getX()<0)jugador.setX(width);
                }
                return true;

            default:
                return super.onTouchEvent(event);
        }
    }
    /*private void crearEnemigos(){
        ImageView enemigo2 = new ImageView(this);
        enemigo2.setImageResource(R.drawable.caza);
        enemigo2.setX(50);
        enemigo2.setY(300);
        RelativeLayout rl = (RelativeLayout) findViewById(R.id.activity_game);
        rl.addView(enemigo2);

        enemigo2.getLayoutParams().height=25*(width*160)/densidad;
        enemigo2.getLayoutParams().width=60*densidad;

    }*/
}