package com.example.filefolderrename.holders;

import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;


import com.example.filefolderrename.R;

public class AudioHolder extends RecyclerView.ViewHolder {
    public ImageView picture;
    public ImageView videos_icon;
    public ImageView checkbox;
    public TextView title;
    public TextView date;
    public TextView size;
    public CardView item_card;

    public AudioHolder(@NonNull View itemView) {
        super(itemView);

        checkbox = itemView.findViewById(R.id.checkbox);
        picture = itemView.findViewById(R.id.imageview);
        videos_icon = itemView.findViewById(R.id.videos_icon);
        title = itemView.findViewById(R.id.title);
        date = itemView.findViewById(R.id.date);
        size = itemView.findViewById(R.id.size);
        item_card = itemView.findViewById(R.id.item_card);
    }
}