package com.example.grupo8.spaceinvaders;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.os.Handler;
import android.support.v4.view.MotionEventCompat;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import java.util.Random;


public class GameActivity extends Activity {

    //DECLARACION DE VARIABLEs

    private TextView tx;
    private TextView txt_lives;
    private String scored;
    private String str_lives;
    private int score;
    private int width;
    private int height;
    private int lives=3;

    private Handler han_MovimientoCaza = new Handler();
    private Handler han_MisilVerde = new Handler();
    private Handler han_MisilRojo = new Handler();
    private boolean sentidoEnemigo = true;

    private ImageButton jugador;
    private ImageView misilRojo;
    private ImageView misilVerde;
    private int densidad;
    private MediaPlayer mp;

    private ImageView enemigos[] = new ImageView[20];
    private int enemiesInitialHeight =300;
    private boolean enemigomuerto = false;

    private ImageView escudo1;
    private ImageView escudo2;
    private ImageView escudo3;
    int counter1=0;
    int counter2=0;
    int counter3=0;

    @Override

    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        SharedPreferences prefs = this.getSharedPreferences("SpaceInvaders", Context.MODE_PRIVATE);
        if(0!=prefs.getInt("theme", 0)){
            setContentView(R.layout.activity_game2);
        }else{
            setContentView(R.layout.activity_game);
        }

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
        txt_lives = (TextView) findViewById(R.id.lives);
        Typeface font = Typeface.createFromAsset(getAssets(), "Star_Jedi_Rounded.ttf");
        tx.setTypeface(font);
        scored=Integer.toString(score);
        tx.setText("PTS: "+scored);
        txt_lives.setTypeface(font);
        str_lives=Integer.toString(lives);
        txt_lives.setText("Lives: "+str_lives);

        //INICIACION DE VARIABLES QUE USAN IMAGENES
        jugador = (ImageButton) findViewById(R.id.naveJugador);
        misilRojo = (ImageView) findViewById(R.id.misilRojo);
        misilVerde = (ImageView) findViewById(R.id.misilVerde);
        escudo1 = (ImageView) findViewById(R.id.escudo1);
        escudo2 = (ImageView) findViewById(R.id.escudo2);
        escudo3 = (ImageView) findViewById(R.id.escudo3);

        if(0==prefs.getInt("music", 0)){
            mp= MediaPlayer.create(this, R.raw.music);
            mp.start();
        }

        crearEnemigos();
    }

    Runnable run_MovimientoCaza = new Runnable() {

        @Override
        public void run() {
            //DIRECCION DEL ENEMIGO
            if(sentidoEnemigo){
                for (int i=0;i<20;i++){
                    enemigos[i].setX(enemigos[i].getX()+5);
                }
            }else{
                for (int i=0;i<20;i++){
                    enemigos[i].setX(enemigos[i].getX()-5);
                }
            }

            //Sentido y altura del enemigo
            calcularSentido();

            //Sube para arriba
            if(enemigos[0].getY()>=height/2+100) {
                for (int i = 0; i < 20; i++) {
                    enemigos[i].setY(enemiesInitialHeight);
                    if (i % 5 == 4) {
                        enemiesInitialHeight += 60;
                    }
                }
                enemiesInitialHeight = 300;
            }
            han_MovimientoCaza.postDelayed(this, 10);
        }
    };

    Runnable run_MisilVerde = new Runnable() {
        @Override
        public void run() {
            //Sube el misil
            misilVerde.setY(misilVerde.getY()-50);
            //Misil fuera de pantalla
            if((misilVerde.getY()+ misilVerde.getHeight() <15 ))
            {
                //Misil desaparece y activa al jugador
                misilVerde.setVisibility(View.INVISIBLE);
                han_MisilVerde.removeCallbacks(run_MisilVerde);
                jugador.setEnabled(true);
                enemigomuerto=false;

                //Enemigos reaparecen
                int j = 0;
                while(j<20 && enemigos[j].getVisibility()==View.INVISIBLE){
                    j++;
                }

                if(j==20){
                    reaperecerEnemigos();
                }
            }

            han_MisilVerde.postDelayed(this,0);

            //MUERTE DEL ENEMIGO
            int i = 0;
            while (i<20 && !enemigomuerto){
                if (enemigos[i].getVisibility()==View.VISIBLE){
                    float centreX=enemigos[i].getX() + enemigos[i].getLayoutParams().width  / 2;
                    float centreY=enemigos[i].getY() + enemigos[i].getLayoutParams().width /2;
                    int x= ((int) centreX);
                    int y= ((int) centreY);
                    if ((Math.abs(x - misilVerde.getX()) < enemigos[i].getLayoutParams().width / 2)&&
                            (Math.abs(y - misilVerde.getY()) <enemigos[i].getLayoutParams().height/2)) {
                        enemigos[i].setVisibility(View.INVISIBLE);
                        score+=100;
                        if(score==300){
                            lives++;
                        }
                        if(score==500){
                            lives+=2;
                        }

                        if(score==1000){
                            lives+=5;
                        }
                        scored=Integer.toString(score);
                        tx.setText("PTS: "+scored);
                        enemigomuerto = true;
                        misilVerde.setVisibility(View.INVISIBLE);
                    }
                }
                i++;
            }
            destruccionEscudos(misilRojo.getX(),misilRojo.getY());
        }
    };

    Runnable run_MisilRojo = new Runnable() {

        @Override
        public void run() {
            //MOVIMIENTO MISIL ROJO
            if (misilRojo.getY()<=height){
                misilRojo.setY(misilRojo.getY()+50);
            }else{
                Random rnd = new Random();
                int enemigoSelccionado= rnd.nextInt(20);
                if(enemigos[enemigoSelccionado].getVisibility()==View.INVISIBLE){
                    misilRojo.setVisibility(View.INVISIBLE);
                }else{
                    misilRojo.setX(enemigos[enemigoSelccionado].getX()+ enemigos[enemigoSelccionado].getLayoutParams().width/2);
                    misilRojo.setY(enemigos[enemigoSelccionado].getY()+(enemigos[enemigoSelccionado].getLayoutParams().height/2));
                    misilRojo.setVisibility(View.VISIBLE);
                }
            }

            float centreX=jugador.getX() + jugador.getWidth()  / 2;
            float centreY=jugador.getY() + jugador.getHeight() /2;
            int x= ((int) centreX);
            int y= ((int) centreY);
            if ((Math.abs(x - misilRojo.getX()) < jugador.getWidth()  / 2)&&(Math.abs(y - misilRojo.getY())
                    < jugador.getHeight()/2)) {
                if (misilRojo.getVisibility()==View.VISIBLE) {
                    lives--;
                    str_lives = Integer.toString(lives);
                    txt_lives.setText("Lives: " + str_lives);
                    if(lives == 0){
                        end();
                    }
                    misilRojo.setVisibility(View.INVISIBLE);
                }
            }
            destruccionEscudos(misilRojo.getX(),misilRojo.getY());
            han_MisilRojo.postDelayed(this, 30);
        }
    };

    private void destruccionEscudos(float misilX, float misilY){
        float centreX1=escudo1.getX() + escudo1.getWidth()  / 2;
        float centreY1=escudo1.getY() + escudo1.getHeight() /2;
        float centreX2=escudo2.getX() + escudo2.getWidth()  / 2;
        float centreY2=escudo2.getY() + escudo2.getHeight() /2;
        float centreX3=escudo3.getX() + escudo3.getWidth()  / 2;
        float centreY3=escudo3.getY() + escudo3.getHeight() /2;

        if ((Math.abs(centreX1 - misilX) < escudo1.getWidth()  / 2)&&(Math.abs(centreY1 - misilY)
                < escudo1.getHeight()/2)) {
            if(counter1==0){
                escudo1.setImageResource(R.drawable.escudo2);
                misilRojo.setX(0);
                misilRojo.setY(0);
                counter1++;
            }else if(counter1==1){
                escudo1.setImageResource(R.drawable.escudo3);
                misilRojo.setX(0);
                misilRojo.setY(0);
                counter1++;
            }else {
                RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) escudo1.getLayoutParams();
                params.width = 0;
                params.height=0;
                escudo1.setY(0);
                escudo1.setX(0);
                escudo1.setLayoutParams(params);
                escudo1.setVisibility(View.GONE);
                misilRojo.setX(0);
                misilRojo.setY(0);
            }
        }
        if ((Math.abs(centreX2 - misilX) < escudo2.getWidth()  / 2)&&(Math.abs(centreY2 - misilY)
                < escudo2.getHeight()/2)) {
            if(counter2==0){
                escudo2.setImageResource(R.drawable.escudo2);
                misilRojo.setX(0);
                misilRojo.setY(0);
                counter2++;
            }else if(counter2==1){
                escudo2.setImageResource(R.drawable.escudo3);
                misilRojo.setX(0);
                misilRojo.setY(0);
                counter2++;
            }else {
                RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) escudo2.getLayoutParams();
                params.width = 0;
                params.height=0;
                escudo2.setY(0);
                escudo2.setX(0);
                escudo2.setLayoutParams(params);
                escudo2.setVisibility(View.GONE);
                misilRojo.setX(0);
                misilRojo.setY(0);
            }
        }

        if ((Math.abs(centreX3 - misilX) < escudo3.getWidth()  / 2)&&(Math.abs(centreY3 - misilY)
                < escudo3.getHeight()/2)) {
            if(counter3==0){
                escudo3.setImageResource(R.drawable.escudo2);
                misilRojo.setX(0);
                misilRojo.setY(0);
                counter3++;
            }else if(counter3==1){
                escudo3.setImageResource(R.drawable.escudo3);
                misilRojo.setX(0);
                misilRojo.setY(0);
                counter3++;
            }else {
                RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) escudo3.getLayoutParams();
                params.width = 0;
                params.height=0;
                escudo3.setY(0);
                escudo3.setX(0);
                escudo3.setLayoutParams(params);
                escudo3.setVisibility(View.GONE);
                misilRojo.setX(0);
                misilRojo.setY(0);
            }
        }
    }

    public void disparo(View v){
        misilVerde.setX(jugador.getX()+ jugador.getWidth()/2);
        misilVerde.setY(jugador.getY()+jugador.getHeight()/2 -50);
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

    private void crearEnemigos(){
        SharedPreferences prefs = this.getSharedPreferences("SpaceInvaders", Context.MODE_PRIVATE);
        int nextHeight= enemiesInitialHeight;
        int nextWidth=100;
        int opcion=0;
        RelativeLayout rl = (RelativeLayout) findViewById(R.id.activity_game);

        if(0!=prefs.getInt("theme", 0)){
            opcion = 0;
        }else{
            opcion = 1;
        }

        for (int i=0; i<20;i++){
            enemigos[i]= new ImageView(this);
            enemigos[i].setX(nextWidth);
            enemigos[i].setY(nextHeight);
            if (opcion==0){
                enemigos[i].setImageResource(R.drawable.spaceinvaders);
            }else{
                enemigos[i].setImageResource(R.drawable.caza);
            }
            rl.addView(enemigos[i]);
            enemigos[i].getLayoutParams().height=50*width/densidad;
            enemigos[i].getLayoutParams().width=50*height/densidad;
            if (i%5==4){
                nextHeight+=100;
                nextWidth = 100;
            }else{
                nextWidth +=180;
            }
        }
    }

    @Override
    public void onBackPressed(){
        SharedPreferences prefs = this.getSharedPreferences("SimplySnake", Context.MODE_PRIVATE);
        if(0==prefs.getInt("music", 0)){
            mp.stop();
        }
        super.onBackPressed();
    }

    @Override
    protected void onUserLeaveHint(){
        SharedPreferences prefs = this.getSharedPreferences("SimplySnake", Context.MODE_PRIVATE);
        if(0==prefs.getInt("MusiC", 0) &&  mp!=null){
            mp.stop();
        }
        super.onUserLeaveHint();
    }

    public void end(){
        Intent intent = new Intent(this, EndActivity.class);
        intent.putExtra("points",scored);
        startActivity(intent);
    }


    private void reaperecerEnemigos(){
        int nextHeight= enemiesInitialHeight;
        int nextWidth=100;
        for(int i = 0;i<20;i++){
            enemigos[i].setVisibility(View.VISIBLE);
            enemigos[i].setX(nextWidth);
            enemigos[i].setY(nextHeight);
            if (i%5==4){
                nextHeight+=100;
                nextWidth = 100;
            }else{
                nextWidth +=180;
            }
        }
    }

    private void calcularSentido(){
        if(sentidoEnemigo){
            if (enemigos[4].getVisibility()==View.VISIBLE
                    ||enemigos[9].getVisibility()==View.VISIBLE
                    ||enemigos[14].getVisibility()==View.VISIBLE
                    ||enemigos[19].getVisibility()==View.VISIBLE){
                if (enemigos[4].getX()+ enemigos[4].getLayoutParams().width >= width - 25 )
                {
                    cambio();
                }
            }else if (enemigos[3].getVisibility()==View.VISIBLE
                    ||enemigos[8].getVisibility()==View.VISIBLE
                    ||enemigos[13].getVisibility()==View.VISIBLE
                    ||enemigos[18].getVisibility()==View.VISIBLE){
                if (enemigos[3].getX()+ enemigos[3].getLayoutParams().width >= width - 25 )
                {
                    cambio();
                }

            } else if (enemigos[2].getVisibility()==View.VISIBLE
                    ||enemigos[7].getVisibility()==View.VISIBLE
                    ||enemigos[12].getVisibility()==View.VISIBLE
                    ||enemigos[17].getVisibility()==View.VISIBLE){
                if (enemigos[2].getX()+ enemigos[2].getLayoutParams().width >= width - 25 )
                {
                    cambio();
                }
            } else if (enemigos[1].getVisibility()==View.VISIBLE
                    ||enemigos[6].getVisibility()==View.VISIBLE
                    ||enemigos[11].getVisibility()==View.VISIBLE
                    ||enemigos[16].getVisibility()==View.VISIBLE){
                if (enemigos[1].getX()+ enemigos[1].getLayoutParams().width >= width - 25 )
                {
                    cambio();
                }
            }else{
                if (enemigos[0].getX()+ enemigos[0].getLayoutParams().width >= width - 25 )
                {
                    cambio();
                }
            }
        }else{
            if (enemigos[0].getVisibility()==View.VISIBLE
                    ||enemigos[5].getVisibility()==View.VISIBLE
                    ||enemigos[10].getVisibility()==View.VISIBLE
                    ||enemigos[15].getVisibility()==View.VISIBLE){
                if (enemigos[0].getX() <=25 )
                {
                    cambio();
                }

            }else if (enemigos[1].getVisibility()==View.VISIBLE
                    ||enemigos[6].getVisibility()==View.VISIBLE
                    ||enemigos[11].getVisibility()==View.VISIBLE
                    ||enemigos[16].getVisibility()==View.VISIBLE){
                if (enemigos[1].getX() <=25 )
                {
                    cambio();
                }
            } else if (enemigos[2].getVisibility()==View.VISIBLE
                    ||enemigos[7].getVisibility()==View.VISIBLE
                    ||enemigos[12].getVisibility()==View.VISIBLE
                    ||enemigos[17].getVisibility()==View.VISIBLE){
                if (enemigos[2].getX() <=25)
                {
                    cambio();
                }

            } else if (enemigos[3].getVisibility()==View.VISIBLE
                    ||enemigos[8].getVisibility()==View.VISIBLE
                    ||enemigos[13].getVisibility()==View.VISIBLE
                    ||enemigos[18].getVisibility()==View.VISIBLE){
                if (enemigos[3].getX() <=25 )
                {
                    cambio();
                }
            }else{
                if (enemigos[4].getX() <=25 )
                {
                    cambio();
                }
            }
        }
    }

    private void cambio(){

        sentidoEnemigo = !sentidoEnemigo;
        for(int i = 0;i<20;i++){
            enemigos[i].setY(enemigos[i].getY()+25);
        }
    }
}