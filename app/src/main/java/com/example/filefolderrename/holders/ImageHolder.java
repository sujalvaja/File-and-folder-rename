package com.example.filefolderrename.holders;

import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.filefolderrename.R;

public class ImageHolder extends RecyclerView.ViewHolder {
    public ImageView picture;
    public ImageView videos_icon;
    public CheckBox checkbox;
    public RelativeLayout item_relative;
    public TextView title;

    public ImageHolder(@NonNull View itemView) {
        super(itemView);
        checkbox = itemView.findViewById(R.id.checkbox);
        picture = itemView.findViewById(R.id.imageview);
        videos_icon = itemView.findViewById(R.id.videos_icon);
        title = itemView.findViewById(R.id.title);
        item_relative = itemView.findViewById(R.id.item_relative);
    }
}