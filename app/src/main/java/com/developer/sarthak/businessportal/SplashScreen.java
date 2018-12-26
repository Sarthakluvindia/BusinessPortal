package com.developer.sarthak.businessportal;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ImageView;

public class SplashScreen extends AppCompatActivity {
ImageView im;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        im=(ImageView)findViewById(R.id.icon_splash);
        Animation anim= AnimationUtils.loadAnimation(SplashScreen.this,R.anim.splashscreen);
        im.startAnimation(anim);
        final Intent in=new Intent(SplashScreen.this,Login.class);
        Thread timer=new Thread(){
            public void run(){
                try {
                    sleep(2000);
                }catch (Exception e){

                }
                finally {
                    startActivity(in);
                    finish();
                }
            }
        };
        timer.start();
    }
}
