package com.example.clockapp;

import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.util.Log;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.clockapp.databinding.ActivitySplashScreenBinding;

public class SplashScreen extends AppCompatActivity {
ActivitySplashScreenBinding biding;
CardView cardView;
int index=0;
String cahrsquence=" Clock Alarm System";
long delay=100;
Handler handler=new Handler();
Runnable runnable=new Runnable() {
    @Override
    public void run() {
      biding.splashtitle.setText(cahrsquence.substring(0, index++));
      if(index<=cahrsquence.length())
          handler.postDelayed(runnable, delay);
    }
};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        biding=ActivitySplashScreenBinding.inflate(getLayoutInflater());
        setContentView(biding.getRoot());

        cardView=findViewById(R.id.splashimage);
        ObjectAnimator objectAnimator=ObjectAnimator.ofPropertyValuesHolder(
                cardView,
                PropertyValuesHolder.ofFloat("scaleX",0.5f,1f),
                PropertyValuesHolder.ofFloat("scaleY",0.5f,1f)
        );
        objectAnimator.setDuration(500);
        objectAnimator.setRepeatCount(ValueAnimator.INFINITE);
        objectAnimator.setRepeatMode(ValueAnimator.REVERSE);
        objectAnimator.start();
        handler.removeCallbacks(runnable);
        handler.postDelayed(runnable,delay);
        handler.postDelayed(() -> runOnUiThread(() -> {
            Intent i=new Intent(getApplicationContext(),MainActivity.class);
            startActivity(i);
           finish();
        }), delay*(cahrsquence.length()+5));
    }


}