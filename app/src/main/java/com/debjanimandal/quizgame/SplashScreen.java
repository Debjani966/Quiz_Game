package com.debjanimandal.quizgame;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

public class SplashScreen extends AppCompatActivity {

    TextView textsplash;
    ImageView imagesplash;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        textsplash=findViewById(R.id.textViewSplash);
        imagesplash=findViewById(R.id.imageViewSplash);

        Animation animation= AnimationUtils.loadAnimation(getApplicationContext(),R.anim.splash);
        textsplash.setAnimation(animation);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent=new Intent(SplashScreen.this, Login_Page.class);
                startActivity(intent);
                finish();
            }
        },5000);
    }
}