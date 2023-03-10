package com.example.clockapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.TimePickerDialog;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.clockapp.controllers.DB_Asenctask;
import com.example.clockapp.controllers.RoomDB;
import com.example.clockapp.databinding.ActivityAddalarmBinding;
import com.example.clockapp.modules.Alarm;

import org.chromium.base.task.TaskTraits;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Addalarm extends AppCompatActivity {

    ActivityAddalarmBinding binding;
    Calendar time=Calendar.getInstance();
    SimpleDateFormat HM=new SimpleDateFormat("hh: mm a");
    StringBuilder repeat=new StringBuilder("0000000");
    String[] days=new String[]{"Su","Mo","Tu","We","Th","Fr","Sa"};
    RoomDB db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityAddalarmBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        time.add(Calendar.MINUTE, 15);
        time.set(Calendar.SECOND, 0);
        time.set(Calendar.MILLISECOND, 0);
        binding.clock.setText(HM.format(time.getTime()));
        db=RoomDB.getInstance(this);
        inialize_dayslist();
        binding.timepicker.setOnClickListener(view -> {
           timepicking();
        });
       binding.addalarmbutt.setOnClickListener(view -> Addnewalarm());
    }

    private void inialize_dayslist() {
        ArrayAdapter<String> adapter=new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1,days){
            @NonNull
            @Override
            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                TextView textView = (TextView) super.getView(position, convertView, parent);
                textView.setTextSize(12);
                if(repeat.charAt(position)=='1')
                {
                    textView.setTypeface(Typeface.DEFAULT_BOLD);
                    textView.setAlpha(1.0f);
                }
                else {
                    textView.setTypeface(Typeface.DEFAULT);
                    textView.setAlpha(.75f);
                }
                return  textView;
            }
        };
        binding.dayslist.setAdapter(adapter);
        binding.dayslist.setOnItemClickListener((parent, view, position, id) -> {
            if(repeat.charAt(position)=='0')
                    repeat.setCharAt(position,'1');
            else
                repeat.setCharAt(position, '0');
            TextView textView = (TextView) view;
            if(repeat.charAt(position)=='1')
            {
                textView.setTypeface(Typeface.DEFAULT_BOLD);
                textView.setAlpha(1.0f);
            }
            else {
                textView.setTypeface(Typeface.DEFAULT);
                textView.setAlpha(.75f);
            }
        });
    }

    private void Addnewalarm() {
        String title=binding.alarmname.getText().toString().trim();
        int tone=binding.seekBarTone.getProgress();
        if (title.isEmpty())
        {
         binding.alarmname.setError("must enter name to Alarm");
         return;
        }
        Alarm alarm=new Alarm();
        alarm.setTime(time);
        alarm.setRepeat(repeat.toString());
        alarm.setTitle(title);
        alarm.setTone(tone);
        alarm.setActive(true);
        binding.loadingaddalarm.setVisibility(View.VISIBLE);
        binding.addalarmbutt.setVisibility(View.GONE);
        DB_Asenctask db_asenctask=new DB_Asenctask(this,alarm,2);
        db_asenctask.executeWithTaskTraits(TaskTraits.BEST_EFFORT);
    }
    public  void doneloading()
    {
        setResult(RESULT_OK);
        finish();
    }
    private void timepicking() {
        TimePickerDialog timePickerDialog=new TimePickerDialog(Addalarm.this, (TimePickerDialog.OnTimeSetListener)
                (timePicker, hour,minute) -> {
                time.set(Calendar.HOUR, hour);
                time.set(Calendar.MINUTE, minute);
                if(Calendar.getInstance().after(time))
                    time.add(Calendar.DATE, 1);
                binding.clock.setText(HM.format(time.getTime()));
        }, time.get(Calendar.HOUR),time.get(Calendar.MINUTE) , false);
        timePickerDialog.setTitle("Pick up time ");
        timePickerDialog.show();
    }
}