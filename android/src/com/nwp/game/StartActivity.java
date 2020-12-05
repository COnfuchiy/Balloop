package com.nwp.game;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

public class StartActivity extends AppCompatActivity implements View.OnClickListener {

    private Animation animationFadeIn, animationFadeOut;
    private TextView text;
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_FULLSCREEN);
        setContentView(R.layout.activity_start);
        text = findViewById(R.id.textView);

        // подключаем файл анимации
        animationFadeIn = AnimationUtils.loadAnimation(this, android.R.anim.fade_in);
        animationFadeOut = AnimationUtils.loadAnimation(this, android.R.anim.fade_out);
        animationFadeIn.setAnimationListener(animationFadeInListener);
        animationFadeOut.setAnimationListener(animationFadeOutListener);

        // при запуске начинаем с анимации исчезновения
        text.startAnimation(animationFadeIn);
    }
    @Override
    protected void onPause() {
        super.onPause();
        text.clearAnimation();
    }

    Animation.AnimationListener animationFadeOutListener = new Animation.AnimationListener() {

        @Override
        public void onAnimationEnd(Animation animation) {
            text.startAnimation(animationFadeIn);
            text.setTextColor(Color.RED);
        }

        @Override
        public void onAnimationRepeat(Animation animation) {
            // TODO Auto-generated method stub
        }

        @Override
        public void onAnimationStart(Animation animation) {
            // TODO Auto-generated method stub
        }
    };

    Animation.AnimationListener animationFadeInListener = new Animation.AnimationListener() {
        @Override
        public void onAnimationEnd(Animation animation) {
            text.startAnimation(animationFadeOut);
            text.setTextColor(Color.BLUE);
        }

        @Override
        public void onAnimationRepeat(Animation animation) {
            // TODO Auto-generated method stub
        }

        @Override
        public void onAnimationStart(Animation animation) {
            // TODO Auto-generated method stub
        }
    };

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(this, MenuActivity.class);
        startActivity(intent);
    }
}