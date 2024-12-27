package com.example.filefolderrename.holders;

import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.filefolderrename.R;

public class DocumentHolder extends RecyclerView.ViewHolder {
    public ImageView picture;
    public ImageView videos_icon;
    public ImageView ImageView;
    public TextView title;
    public TextView date;
    public TextView size;
    public CardView item_card;

    public DocumentHolder(@NonNull View itemView) {
        super(itemView);

        ImageView = itemView.findViewById(R.id.checkbox);
        picture = itemView.findViewById(R.id.imageview1);
        videos_icon = itemView.findViewById(R.id.videos_icon1);
        title = itemView.findViewById(R.id.title1);
        date = itemView.findViewById(R.id.date1);
        size = itemView.findViewById(R.id.size1);
        item_card = itemView.findViewById(R.id.item_card1);
    }
}