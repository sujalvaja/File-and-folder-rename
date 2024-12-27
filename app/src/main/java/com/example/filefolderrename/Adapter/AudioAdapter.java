package com.example.filefolderrename.Adapter;

import static androidx.core.view.ViewCompat.setTransitionName;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.filefolderrename.Activity.MainActivity;
import com.example.filefolderrename.Fragments.AudiosFragment;
import com.example.filefolderrename.models.PictureFacer;
import com.example.filefolderrename.R;
import com.example.filefolderrename.holders.AudioHolder;

import java.io.File;
import java.util.ArrayList;


public class AudioAdapter extends RecyclerView.Adapter<AudioHolder> {
    private final ArrayList<PictureFacer> pictureList;
   private final ArrayList<PictureFacer> selectedItem;
    AudiosFragment AudiosFragment;
    Context mContext;

    public AudioAdapter(ArrayList<PictureFacer> pictureList, ArrayList<PictureFacer> selectedItem, Context mContext, AudiosFragment AudiosFragment) {
        this.pictureList = pictureList;
        this.selectedItem = selectedItem;
        this.mContext = mContext;
        this.AudiosFragment = AudiosFragment;
    }
    @NonNull
    @Override
    public AudioHolder onCreateViewHolder(@NonNull ViewGroup container, int position) {
        LayoutInflater inflater = LayoutInflater.from(container.getContext());
        View cell = inflater.inflate(R.layout.item_audio_holder, container, false);
        return new AudioHolder(cell);
    }

    @Override
    public void onBindViewHolder(@NonNull final AudioHolder holder, @SuppressLint("RecyclerView") final int position) {
        final PictureFacer imagedata = pictureList.get(position);
        Log.d("TAG13","---"+ imagedata);

        holder.title.setText(imagedata.getPicturName());
        holder.picture.setBackgroundResource(R.drawable.music_icn);
        setTransitionName(holder.picture, position + "_image");

        holder.item_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog dialog = new Dialog(v.getContext());
                dialog.setContentView(R.layout.rename_dialog);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

                EditText editText = dialog.findViewById(R.id.new_name);
                Button btn_cancel = dialog.findViewById(R.id.btncancel);
                Button all_apps = dialog.findViewById(R.id.btnok);
                String path = imagedata.getPicturePath();
                final File file = new File(path);
                String DocumentName = file.getName();
                DocumentName = DocumentName.substring(0, DocumentName.lastIndexOf("."));
                String[] nameCom = DocumentName.split("\\.");
                editText.setText(nameCom[0]);

                editText.requestFocus();

                all_apps.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String onlypath = file.getParentFile().getPath();
                        String ext = file.getPath();
                        ext = ext.substring(ext.lastIndexOf("."));
                        String s = editText.getText().toString().trim();
                        String newPath = onlypath + "/" + s + ext;
                        if (s.equals(nameCom[0])) {
                            Toast.makeText(mContext, "please enter different name", Toast.LENGTH_SHORT).show();
                        } else if (s.isEmpty()) {
                            Toast.makeText(mContext, "please enter name", Toast.LENGTH_SHORT).show();
                        } else {
                            File newFile = new File(newPath);
                            File Rename = new File(String.valueOf(newFile));
                            if (rename(file, Rename)) {
                                ContentResolver resolver = mContext.getApplicationContext().getContentResolver();
                                resolver.delete(MediaStore.Files.getContentUri("external"),
                                        MediaStore.MediaColumns.DATA + "=?", new String[]
                                                {file.getAbsolutePath()});
                                Intent i = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                                i.setData(Uri.fromFile(newFile));
                                mContext.getApplicationContext().sendBroadcast(i);
                                overridePendingTransition(0, 0);
                                notifyDataSetChanged();
                                Toast.makeText(mContext, "Audio Reanamed", Toast.LENGTH_SHORT).show();
                                AudiosFragment.onClick(position,editText.getText().toString());
                            } else {

                                Toast.makeText(mContext, "Process Failed", Toast.LENGTH_SHORT).show();
                                Intent iv = new Intent(mContext, MainActivity.class);
                                overridePendingTransition(0, 0);
                                mContext.startActivity(iv);
                                mContext.getApplicationContext();
                            }
                        }
                    dialog.dismiss();
                    }

                });


                btn_cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                dialog.show();
            }

        });
    }

    private void overridePendingTransition(int i, int i1) {
    }
    @Override
    public int getItemCount() {
        return pictureList.size();
    }

    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    private boolean rename(File from, File to) {
        return from.getParentFile().exists() && from.exists() && from.renameTo(to);
    }
}




