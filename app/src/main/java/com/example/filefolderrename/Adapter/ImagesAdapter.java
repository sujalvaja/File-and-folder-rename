package com.example.filefolderrename.Adapter;

import static androidx.core.view.ViewCompat.setTransitionName;

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
import com.example.filefolderrename.Fragments.ImageFragment;
import com.example.filefolderrename.R;
import com.example.filefolderrename.holders.ImageHolder;
import com.example.filefolderrename.models.PictureFacer;

import java.io.File;
import java.util.ArrayList;
import java.util.Objects;

public class ImagesAdapter extends RecyclerView.Adapter<ImageHolder> {
    private ArrayList<PictureFacer> pictureList;
    private ArrayList<PictureFacer> selectedItem;
    ImageFragment imageFragment;
    Context mContext;

    public ImagesAdapter(ArrayList<PictureFacer> pictureList, ArrayList<PictureFacer> selectedItem, Context mContext, ImageFragment imageFragment) {
        this.pictureList = pictureList;
        this.selectedItem = selectedItem;
        this.mContext = mContext;
        this.imageFragment = imageFragment;
    }


    @NonNull
    @Override
    public ImageHolder onCreateViewHolder(@NonNull ViewGroup container, int position) {
        LayoutInflater inflater = LayoutInflater.from(container.getContext());
        View cell = inflater.inflate(R.layout.item_image_holder, container, false);
        return new ImageHolder(cell);
    }

    @Override
    public void onBindViewHolder(@NonNull final ImageHolder holder, final int position) {
        final PictureFacer imagedata = pictureList.get(position);
        if (!selectedItem.isEmpty()) {
            for (int i = 0; i < selectedItem.size(); i++) {
                if (selectedItem.get(i).getPicturePath().equalsIgnoreCase(imagedata.getPicturePath())) {
                    holder.checkbox.setChecked(true);
                }
            }
        }

        holder.title.setText(imagedata.getPicturName());
        Glide.with(mContext)
                .load(imagedata.getPicturePath())
                .apply(new RequestOptions().centerCrop())
                .into(holder.picture);
        setTransitionName(holder.picture, position + "_image");
        holder.picture.setOnClickListener(v -> {

                    Dialog dialog = new Dialog(v.getContext());
                    dialog.setContentView(R.layout.rename_dialog);
                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

                    EditText editText = dialog.findViewById(R.id.new_name);
                    Button btn_cancel = dialog.findViewById(R.id.btncancel);
                    Button all_apps = dialog.findViewById(R.id.btnok);
                    String path = imagedata.getPicturePath();
                    Log.d("TAG13", "ss" + path);
                    File file = new File(path);
                    String ImageName = file.getName();
                    ImageName = ImageName.substring(0, ImageName.lastIndexOf(""));
                    String[] namecom = ImageName.split("\\.");
                    editText.setText(namecom[0]);
                    editText.requestFocus();

                    all_apps.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            String onlypath = Objects.requireNonNull(file.getParentFile()).getAbsolutePath();
                            String ext = file.getAbsolutePath();
                            ext = ext.substring(ext.lastIndexOf("."));
                            String s = String.valueOf(editText.getText().toString().trim());
                            String newPath = onlypath + "/" + s + ext;
                            if (s.equals(namecom[0])) {
                                Toast.makeText(mContext, "please enter different name", Toast.LENGTH_SHORT).show();
                            } else if (s.isEmpty()) {
                                Toast.makeText(mContext, "please enter name", Toast.LENGTH_SHORT).show();
                            } else {
                                File newFile = new File(newPath);
                                File Rename = new File(String.valueOf(newFile));
                                Toast.makeText(mContext, "file" + Rename, Toast.LENGTH_SHORT).show();
                                Log.d("TAG13", "----" + Rename);
                                if (rename(file, Rename)) {
                                    ContentResolver resolver = mContext.getContentResolver();
                                    resolver.delete(MediaStore.Files.getContentUri("external"), MediaStore.MediaColumns.DATA + "=?", new String[]
                                            {file.getAbsolutePath()});
                                    Intent i = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                                    i.setData(Uri.fromFile(newFile));
                                    mContext.sendBroadcast(i);
                                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    Toast.makeText(mContext, "Image Reanamed", Toast.LENGTH_SHORT).show();
                                  imageFragment.onClick(position,editText.getText().toString());

                                } else {
                                    Toast.makeText(mContext, "Process Failed", Toast.LENGTH_SHORT).show();
                                    Intent iv = new Intent(mContext, MainActivity.class);
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
        });

    }

    private LayoutInflater getLayoutInflater() {
        return null;
    }

    @Override
    public int getItemCount() {
        return pictureList.size();
    }
    public long getItemId(int position) {
        return position;
    }

    public int getItemViewType(int position) {
        return position;
    }

    private boolean rename(File from, File to) {
        return from.getParentFile().exists() && from.exists() && from.renameTo(to);
    }

}