package com.example.buddycopwow;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.buddycopwow.police.PoliceLogin;

public class LoadingScreen extends AppCompatActivity {

    ImageView logo_image;
    TextView tagLine;
    View first, second, third, fourth, fifth, sixth;

    //animations
    Animation topAnimation, bottomAnimation, middleAnimation;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading_screen);

        topAnimation = AnimationUtils.loadAnimation(this, R.anim.top_animantion);
        bottomAnimation = AnimationUtils.loadAnimation(this, R.anim.bottom_animantion);
        middleAnimation = AnimationUtils.loadAnimation(this, R.anim.middel_animantion);

        //Hooks
        first = findViewById(R.id.first_line);
        second = findViewById(R.id.second_line);
        third = findViewById(R.id.third_line);
        fourth = findViewById(R.id.fourth_line);
        fifth = findViewById(R.id.fifth_line);
        sixth = findViewById(R.id.sixth_line);

        logo_image = findViewById(R.id.logo_image);
        tagLine = findViewById(R.id.tagLine);

        //Setting Animations
        first.setAnimation(topAnimation);
        second.setAnimation(topAnimation);
        third.setAnimation(topAnimation);
        fourth.setAnimation(topAnimation);
        fifth.setAnimation(topAnimation);
        sixth.setAnimation(topAnimation);
        //Setting Animations
        logo_image.setAnimation(middleAnimation);
        tagLine.setAnimation(bottomAnimation);
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(LoadingScreen.this, PoliceLogin.class);
                startActivity(intent);
                finish();
            }
        }, 3000);
    }

}
