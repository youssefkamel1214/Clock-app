package com.example.clockapp.controllers;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import com.example.clockapp.Alarmwarn;
import com.example.clockapp.modules.Alarm;

import java.text.SimpleDateFormat;

public class AlarmReciver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving
        // an Intent broadcast.
        Log.d("fromBroadcast","---------------------------------");
        Log.d("fromBroadcast","aaa---------------------------------");
        Log.d("fromBroadcast","---------------------------------ssssfaawff");
        int id=intent.getIntExtra("id",-1);
        RoomDB roomDB=RoomDB.getInstance(context);
        Alarm alarm =roomDB.maindao().get_Alarm(id);
        //Log.d("fromBroadcast",new SimpleDateFormat("HH:mm a , dd EEE").format(alarm.getTime().getTime()));
        if (alarm.getRepeat().indexOf("1")!=-1)
              updateAlarm(alarm,context);
        if(Alarmwarn.instance==null) {
            Intent Alarmintent = new Intent(context, Alarmwarn.class);
            Alarmintent.putExtra("id", id);
            Alarmintent.putExtra("title",alarm.getTitle());
            Alarmintent.putExtra("volume",((float) alarm.getTone()/100));
            Alarmintent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(Alarmintent);
        }
    }
    private void updateAlarm(Alarm alarm,Context context){
        AlarmManager alarmManager=(AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent=new Intent(context, AlarmReciver.class);
        intent.putExtra("id",alarm.getId());
        PendingIntent pendingIntent=PendingIntent.getBroadcast(context,alarm.getId(),intent,PendingIntent.FLAG_UPDATE_CURRENT);
        alarmManager.set(AlarmManager.RTC_WAKEUP,alarm.getTime().getTimeInMillis(),pendingIntent);

    }
}