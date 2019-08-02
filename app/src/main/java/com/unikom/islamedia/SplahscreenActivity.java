package com.unikom.islamedia;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;


public class SplahscreenActivity extends AppCompatActivity {

    private final static int SPALASH_TIME = 8000;
    TextView tv1;
    TextView tv2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setTheme(R.style.SplashScreen);

        setContentView(R.layout.activity_splahscreen);


        tv1 = findViewById(R.id.judul_app);
        tv2 = findViewById(R.id.motive_app);

        Animation myanim = AnimationUtils.loadAnimation(this, R.anim.mytransition);

        tv1.setAnimation(myanim);
        tv2.setAnimation(myanim);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent i = new Intent(SplahscreenActivity.this, WalkthroughtActivity.class);
                startActivity(i);
                finish();
            }
        }, SPALASH_TIME);
    }


}
