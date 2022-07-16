package com.example.clockapp.controllers;

import android.graphics.Rect;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

/**
 *
 */
public class Spaceitemdecoration extends RecyclerView.ItemDecoration {
    private  final  int horzintal_space,verical_space;
    public Spaceitemdecoration(int horzintal_space, int verical_space){
        this.horzintal_space=horzintal_space;
        this.verical_space=verical_space;
    }

    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
            outRect.right=horzintal_space;
            outRect.left=horzintal_space;
            outRect.top=verical_space;
            outRect.bottom=verical_space;
    }
}
