package com.example.clockapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.room.RoomDatabase;

import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.clockapp.controllers.Alarm_adapter;
import com.example.clockapp.controllers.DB_Asenctask;
import com.example.clockapp.controllers.RoomDB;
import com.example.clockapp.controllers.Spaceitemdecoration;
import com.example.clockapp.databinding.ActivityMainBinding;
import com.example.clockapp.modules.Alarm;

import org.chromium.base.task.TaskTraits;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;

public class MainActivity extends AppCompatActivity {
    private static final int ACTION_MANAGE_OVERLAY_PERMISSION_REQUEST_CODE =205 ;
    ActivityMainBinding binding;
    Alarm_adapter alarm_adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        checkpopup();
        binding.rclreralarm.addItemDecoration(new Spaceitemdecoration(5, 10));
        binding.rclreralarm.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        DB_Asenctask db_asenctask=new DB_Asenctask(this,null,0);
        db_asenctask.executeWithTaskTraits(TaskTraits.BEST_EFFORT);
        binding.addalarmpagebutton.setOnClickListener(view -> {
               Intent intent=new Intent(this,Addalarm.class);
            startActivityForResult(intent,1);
        });

    }

    private void checkpopup() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!Settings.canDrawOverlays(this)) {
                Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                        Uri.parse("package:" + this.getPackageName()));
                startActivityForResult(intent, ACTION_MANAGE_OVERLAY_PERMISSION_REQUEST_CODE);
            }
        }
    }

    public void inailzedb_getalarms(ArrayList<Alarm> alarms) {

        alarm_adapter=new Alarm_adapter(alarms,binding.empty);
        binding.rclreralarm.setAdapter(alarm_adapter);
        binding.loading.setVisibility(View.GONE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && requestCode != ACTION_MANAGE_OVERLAY_PERMISSION_REQUEST_CODE) {
            DB_Asenctask db_asenctask = new DB_Asenctask(this, null, 0);
            db_asenctask.executeWithTaskTraits(TaskTraits.BEST_EFFORT);
            binding.empty.setVisibility(View.GONE);
        } else if (requestCode == ACTION_MANAGE_OVERLAY_PERMISSION_REQUEST_CODE) {
            if (!Settings.canDrawOverlays(this)) {
                Toast.makeText(this, "sorry application must have this premmision",Toast.LENGTH_LONG);
                finish();
            }
        }
    }
}