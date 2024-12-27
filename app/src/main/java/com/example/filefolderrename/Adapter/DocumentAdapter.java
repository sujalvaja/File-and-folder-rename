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

import com.example.filefolderrename.Activity.MainActivity;
import com.example.filefolderrename.Fragments.DocumentFragment;
import com.example.filefolderrename.R;
import com.example.filefolderrename.holders.DocumentHolder;
import com.example.filefolderrename.models.Doc;

import java.io.File;
import java.util.ArrayList;
import java.util.Objects;

public class DocumentAdapter extends RecyclerView.Adapter<DocumentHolder> {
    private final ArrayList<Doc> pictureList;
    com.example.filefolderrename.Fragments.DocumentFragment DocumentFragment;
    Context context;

    public DocumentAdapter(ArrayList<Doc> pictureList, Context context, DocumentFragment DocumentFragment) {
        this.pictureList = pictureList;
        this.context = context;
        this.DocumentFragment = DocumentFragment;
    }


    public DocumentHolder onCreateViewHolder(ViewGroup container, int position) {
        LayoutInflater inflater = LayoutInflater.from(container.getContext());
        View cell = inflater.inflate(R.layout.item_document_holder, container, false);
        return new DocumentHolder(cell);
    }

    @Override
    public void onBindViewHolder(@NonNull final DocumentHolder holder, final int position) {

        final Doc imagedata = pictureList.get(position);

      /* // Glide.with(holder.picture.getContext())
                .load(imagedata.getPath())
                .apply(new RequestOptions().centerCrop())
                .into(holder.picture);*/
        // holder.picture.setBackgroundResource(R.drawable.ad_icn);
        Log.d("TAG13", "ss" + imagedata.getName());
        if (imagedata.getName().endsWith(".zip")) {
            holder.picture.setBackgroundResource(R.drawable.zip_ic);
        } else if (imagedata.getName().endsWith(".doc")) {
            holder.picture.setBackgroundResource(R.drawable.doc_ic);
        } else if (imagedata.getName().endsWith(".pdf")) {
            holder.picture.setBackgroundResource(R.drawable.pdf_ic);
        } else if (imagedata.getName().endsWith(".ppt")) {
            holder.picture.setBackgroundResource(R.drawable.ppt_ic);
        } else if (imagedata.getName().endsWith(".txt")) {
            holder.picture.setBackgroundResource(R.drawable.ictxt);
        }
        setTransitionName(holder.picture, String.valueOf(position) + "_document");
        holder.title.setText(imagedata.getName().trim().toString());

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
                String path = imagedata.getPath();
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
                            Toast.makeText(context, "please enter different name", Toast.LENGTH_SHORT).show();
                        } else if (s.isEmpty()) {
                            Toast.makeText(context, "please enter name", Toast.LENGTH_SHORT).show();
                        } else {
                            File newFile = new File(newPath);
                            File Rename = new File(String.valueOf(newFile));
                            Toast.makeText(context, "file" + Rename, Toast.LENGTH_SHORT).show();
                            Log.d("TAG13", "----" + Rename);
                            if (rename(file, Rename)) {
                                ContentResolver resolver = context.getContentResolver();
                                resolver.delete(MediaStore.Files.getContentUri("external"), MediaStore.MediaColumns.DATA + "=?", new String[]
                                        {file.getAbsolutePath()});
                                Intent i = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                                i.setData(Uri.fromFile(newFile));
                                context.sendBroadcast(i);
                                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                Toast.makeText(context, "Document Reanamed", Toast.LENGTH_SHORT).show();
                                DocumentFragment.onClick(position, editText.getText().toString());

                            } else {
                                Toast.makeText(context, "Process Failed", Toast.LENGTH_SHORT).show();
                                Intent iv = new Intent(context, MainActivity.class);
                                context.startActivity(iv);
                                context.getApplicationContext();
                            }
                        }
                        dialog.dismiss();
                    }

                });
                setTransitionName(holder.picture, position + "_document");
                holder.title.setVisibility(View.VISIBLE);
                holder.title.setText(imagedata.getName()/*.getPdfList()*/);
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

    private boolean isEmpty(EditText editText) {
        return editText.getText().toString().trim().length() == 0;
    }
}