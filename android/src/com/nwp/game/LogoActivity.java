package com.nwp.game;

import android.content.Intent;
import android.os.Build;
import android.os.CountDownTimer;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class LogoActivity extends AppCompatActivity implements View.OnClickListener{

    CountDownTimer timer;

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_FULLSCREEN);
        setContentView(R.layout.activity_logo);

        timer = new CountDownTimer(2 * 1000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
                toMenu();
            }
        }.start();
    }
    private void toMenu(){
        Intent intent = new Intent(this, StartActivity.class);
        startActivity(intent);
        super.finish();
    }

    @Override
    public void onClick(View v) {
        timer.cancel();
        Intent intent = new Intent(this, StartActivity.class);
        startActivity(intent);
        super.finish();
    }
}