package com.example.filefolderrename.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;


import com.example.filefolderrename.ClsGlobal.Storage;
import com.example.filefolderrename.R;

import java.io.File;
import java.util.List;

/**
 * Created by sromku on June, 2017.
 */
public class FilesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<File> mFiles;
    private OnFileItemListener mListener;
    private Storage mStorage;

    public FilesAdapter(Context context) {
        mStorage = new Storage(context);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.file_line_view, parent, false);
        return new FileViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final File file = mFiles.get(position);
        FileViewHolder fileViewHolder = (FileViewHolder) holder;
        fileViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.onClick(file);
            }
        });
        fileViewHolder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                mListener.onLongClick(file);
                return true;
            }
        });
        fileViewHolder.mName.setText(file.getName());

        if (file.isDirectory()) {
            fileViewHolder.mSize.setVisibility(View.GONE);
        } else {
            fileViewHolder.mSize.setVisibility(View.VISIBLE);
            fileViewHolder.mSize.setText(mStorage.getReadableSize(file));
        }

    }

    @Override
    public int getItemCount() {
        return mFiles != null ? mFiles.size() : 0;
    }

    public void setFiles(List<File> files) {
        mFiles = files;
    }

    public void setListener(OnFileItemListener listener) {
        mListener = listener;
    }

    static class FileViewHolder extends RecyclerView.ViewHolder {

        TextView mName;
        TextView mSize;
        ImageView mIcon;

        FileViewHolder(View v) {
            super(v);
            mName = (TextView) v.findViewById(R.id.name);
            mSize = (TextView) v.findViewById(R.id.size);
            mIcon = (ImageView) v.findViewById(R.id.icon);
        }
    }

    public interface OnFileItemListener {
        void onClick(File file);

        void onLongClick(File file);
    }
}
