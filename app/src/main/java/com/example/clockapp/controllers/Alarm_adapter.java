package com.example.clockapp.controllers;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.example.clockapp.R;
import com.example.clockapp.databinding.AlarmrecleitemBinding;
import com.example.clockapp.modules.Alarm;

import org.chromium.base.task.TaskTraits;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import java.util.ArrayList;
import java.util.Comparator;

public class Alarm_adapter extends RecyclerView.Adapter<Alarm_adapter.Viewholder> {
    ArrayList<Alarm>Alarms;
    String[] days=new String[]{"Su","Mo","Tu","We","Th","Fr","Sa"};
    TextView txTextView;

    public Alarm_adapter(ArrayList<Alarm> alarms, TextView txTextView) {
        Alarms = alarms;
        this.txTextView = txTextView;
        if(alarms.isEmpty())
            txTextView.setVisibility(View.VISIBLE);
        else
            txTextView.setVisibility(View.GONE);
    }

    public static class  Viewholder extends RecyclerView.ViewHolder{
       AlarmrecleitemBinding binding;
        public Viewholder( AlarmrecleitemBinding binding) {
            super(binding.getRoot());
            this.binding=binding;
        }
    }
    @NonNull
    @Override
    public Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new Viewholder(AlarmrecleitemBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false));

    }

    @Override

    public void onBindViewHolder(@NonNull Viewholder holder, int position) {
        AlarmrecleitemBinding binding=holder.binding;
        Alarm alarm=Alarms.get(position);
        binding.alarmname.setText(alarm.getTitle());
        DateFormat dateFormat=new SimpleDateFormat("hh:mm a , dd EEE");
        binding.digtalclock.setText(dateFormat.format(alarm.getTime().getTime()));
        handlereapet(binding,alarm,position);
        handleactivity(binding,alarm,position);
        binding.delete.setOnClickListener(view -> {
            DB_Asenctask db_asenctask=new DB_Asenctask((Activity) binding.alarmname.getContext(),alarm,3);
            db_asenctask.executeWithTaskTraits(TaskTraits.BEST_EFFORT);
            Alarms.remove(position);
            if(Alarms.isEmpty())
                txTextView.setVisibility(View.VISIBLE);
            notifyItemRemoved(position);
        });

    }

    private void handlereapet(AlarmrecleitemBinding binding, Alarm alarm,int postion) {
        ArrayAdapter<String> adapter=new ArrayAdapter<String>(binding.bellimage.getContext(),
                android.R.layout.simple_list_item_1,days){
            @NonNull
            @Override
            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                TextView textView = (TextView) super.getView(position, convertView, parent);
                textView.setTextSize(12);
                if(alarm.getRepeat().charAt(position)=='1')
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

        binding.dayslist.setOnItemClickListener((parent, view, position1, id) -> {
            char a='1';
            if(alarm.getRepeat().charAt(position1)=='1')
                a='0';
            String repeat=alarm.getRepeat();
            alarm.setRepeat(repeat.substring(0, position1)+a+repeat.substring(position1 +1));
            Alarms.sort((Comparator<Alarm>) Alarm::compare);
            update_alarm(alarm,binding.alarmname.getContext());
            notifyItemChanged(postion);
        });
    }

    private void handleactivity(AlarmrecleitemBinding binding, Alarm alarm,int position) {
        if (!alarm.isActive())
        {
            binding.AlarmreclerCard.setAlpha(.25f);
            binding.bellimage.setImageResource(R.drawable.ic_bell_silent_line);
        }
        else
        {
            binding.AlarmreclerCard.setAlpha(1);
            binding.bellimage.setImageResource(R.drawable.bell);
        }
        binding.bellimage.setOnClickListener(view -> {
            alarm.setActive(!alarm.isActive());
            Alarms.set(position,alarm);
            if (!alarm.isActive())
            {
                binding.AlarmreclerCard.setAlpha(.25f);
                binding.bellimage.setImageResource(R.drawable.ic_bell_silent_line);
            }
            else
            {
                binding.AlarmreclerCard.setAlpha(1);
                binding.bellimage.setImageResource(R.drawable.bell);
            }
            Alarms.sort(Alarm::compare);
            update_alarm(alarm,binding.alarmname.getContext());
            notifyItemChanged(position);
        });
    }

    private void update_alarm(Alarm alarm, Context  context) {
        DB_Asenctask db_asenctask=new DB_Asenctask((Activity) context,alarm,1);
        db_asenctask.executeWithTaskTraits(TaskTraits.BEST_EFFORT);
    }

    @Override
    public int getItemCount() {
        return Alarms.size();
    }
}
