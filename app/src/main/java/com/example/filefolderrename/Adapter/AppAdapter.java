package com.example.filefolderrename.Adapter;

import android.app.Dialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
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
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.filefolderrename.Activity.MainActivity;
import com.example.filefolderrename.R;
import com.example.filefolderrename.CommonFunction;
import com.example.filefolderrename.holders.AppHolder;
import com.example.filefolderrename.Fragments.AppFragment;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static androidx.core.view.ViewCompat.setTransitionName;

public class AppAdapter extends RecyclerView.Adapter<AppHolder> {

    private AppFragment pictureContx;
    private static ArrayList<File> datalist = new ArrayList<>();
    Context context;
    CommonFunction commonFunction;
    List<ApplicationInfo> selectedItem;

 public AppAdapter(ArrayList<File> datalist, List<ApplicationInfo> selectedItem, Context context, AppFragment pictureContx) {

        this.datalist = datalist;
this.pictureContx =pictureContx;
        this.context = context;
        this.selectedItem = selectedItem;
        commonFunction = new CommonFunction(); }

    @NonNull
    @Override
    public AppHolder onCreateViewHolder(@NonNull ViewGroup container, int position) {
        LayoutInflater inflater = LayoutInflater.from(container.getContext());
        View cell = inflater.inflate(R.layout.item_app_cardview, container, false);
        return new AppHolder(cell);
    }

    @Override
    public void onBindViewHolder(@NonNull final AppHolder holder, final int position) {

        final File imagedata = datalist.get(position);
        holder.title.setText(imagedata.getName());
        holder.picture.setBackgroundResource(R.drawable.apk_ic);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog dialog = new Dialog(v.getContext());
                dialog.setContentView(R.layout.rename_dialog);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

                EditText editText = dialog.findViewById(R.id.new_name);
                Button btn_cancel = dialog.findViewById(R.id.btncancel);
                Button all_apps = dialog.findViewById(R.id.btnok);
                String path = imagedata.getPath();
                final File file = new File(path);
                String DocumentName = file.getName();
                DocumentName = DocumentName.substring(0, DocumentName.lastIndexOf(""));
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
                            Toast.makeText(context, "please enter different name", Toast.LENGTH_SHORT).show();
                        } else if (s.isEmpty()) {
                            Toast.makeText(context, "please enter name", Toast.LENGTH_SHORT).show();
                        } else {
                            File newFile = new File(newPath);
                            File Rename = new File(String.valueOf(newFile));
                            if (rename(file, Rename)) {
                                ContentResolver resolver = context.getApplicationContext().getContentResolver();
                                resolver.delete(MediaStore.Files.getContentUri("external"),
                                        MediaStore.MediaColumns.DATA + "=?", new String[]
                                                {file.getAbsolutePath()});
                                Intent i = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                                i.setData(Uri.fromFile(newFile));
                                context.getApplicationContext().sendBroadcast(i);

                                notifyDataSetChanged();
                                Toast.makeText(context, "Apk Reanamed", Toast.LENGTH_SHORT).show();
                                pictureContx.onClick(position,editText.getText().toString());
                            } else {

                                Toast.makeText(context, "Process Failed", Toast.LENGTH_SHORT).show();
                                Intent iv = new Intent(context, MainActivity.class);
                                overridePendingTransition(0, 0);
                                context.startActivity(iv);
                                context.getApplicationContext();
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
        return datalist.size();
    }

    @Override
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
