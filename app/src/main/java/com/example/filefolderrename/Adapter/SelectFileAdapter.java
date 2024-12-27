package com.example.filefolderrename.Adapter;

import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.ArrayList;

public class SelectFileAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    ArrayList<File> files = new ArrayList<>();


    @NonNull
    @NotNull
    @Override
    public SelectFileViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull RecyclerView.ViewHolder holder, int position) {

    }


    @Override
    public int getItemCount() {
        return 0;
    }

    public class SelectFileViewHolder extends RecyclerView.ViewHolder {
        public SelectFileViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
        }
    }

}
