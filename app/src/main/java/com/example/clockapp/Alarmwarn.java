package com.example.clockapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityManager;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.view.WindowManager;

import com.example.clockapp.controllers.AlarmReciver;
import com.example.clockapp.databinding.ActivityAlarmwarnBinding;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

public class Alarmwarn extends AppCompatActivity {

    ActivityAlarmwarnBinding binding;
    public  static Alarmwarn instance;
    MediaPlayer mediaPlayer;
    int id;
    String title;
    Calendar time;
    float volume;
    SimpleDateFormat HM=new SimpleDateFormat("HH: mm a");
    private void onfinish() {
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.stop();
        }
        mediaPlayer.release();
        ActivityManager mngr = (ActivityManager) getSystemService( ACTIVITY_SERVICE );
        List<ActivityManager.AppTask> taskList = mngr.getAppTasks();
       if(taskList.size()==1)
              finishAndRemoveTask();
       else
           finish();
       instance=null;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityAlarmwarnBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        showonlockscreen();
        instance=this;
        getdatafromintent();
        start_sound();
        binding.clock.setText(HM.format(time.getTime()));
        binding.title.setText(title);
        binding.dismiss.setOnClickListener(view -> {
            onfinish();
        });
        binding.nap.setOnClickListener(view -> {
            takenap();
            onfinish();
        });
        Handler handler=new Handler();
        handler.postDelayed(() -> runOnUiThread(() -> {
            if(instance!=null)
                 onfinish();
        }), 60*1000);
    }

    private void start_sound() {
       mediaPlayer=MediaPlayer.create(this, R.raw.alarmwake);
       mediaPlayer.setLooping(true);
       mediaPlayer.setVolume(volume*100, volume*100);
       mediaPlayer.start();

    }

    private void takenap() {
        AlarmManager alarmManager=(AlarmManager) this.getSystemService(Context.ALARM_SERVICE);
        Intent intent=new Intent(this, AlarmReciver.class);
        intent.putExtra("id",id);
        PendingIntent pendingIntent=PendingIntent.getBroadcast(this,id,intent,PendingIntent.FLAG_UPDATE_CURRENT);
        Calendar nap=Calendar.getInstance();
        nap.add(Calendar.MINUTE,10);
        alarmManager.set(AlarmManager.RTC_WAKEUP,nap.getTimeInMillis(),pendingIntent);
    }

    private void showonlockscreen() {
        final Window win = getWindow();
        win.addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);
        win.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                | WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON
                | WindowManager.LayoutParams.FLAG_ALLOW_LOCK_WHILE_SCREEN_ON);
    }

    private void getdatafromintent() {
        id =getIntent().getIntExtra("id", -1);
        title=getIntent().getStringExtra("title");
        time=Calendar.getInstance();
        volume=getIntent().getFloatExtra("volume", 1.0f);
    }
}